package com.cgnpc.qrtz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cgnpc.cud.cache.redis.RedisUtil;
import com.cgnpc.cud.core.common.util.CollectionUtils;
import com.cgnpc.cud.core.common.util.SpringUtils;
import com.cgnpc.cud.utils.UUIDUtil;
import com.cgnpc.cud.workbench.common.dto.proc.req.ReqEmailDto;
import com.cgnpc.cud.workbench.manage.domain.WbProcessInfoEntity;
import com.cgnpc.cud.workbench.manage.mapper.WbProcessInfoMapper;
import com.cgnpc.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.cgnpc.dingtalk.model.WorkNotice;
import com.cgnpc.dingtalk.service.AsyncWarningMsgService;
import com.cgnpc.qrtz.service.ILogTimeoutNotifyService;
import com.cgnpc.qrtz.enums.NotifyStatusEnum;
import com.cgnpc.cud.workbench.manage.enums.SendTypeEnum;
import com.cgnpc.qrtz.model.IpmAssAll;
import com.cgnpc.qrtz.mapper.IpmAssAllMapper;
import com.cgnpc.qrtz.model.LogTimeoutNotify;
import com.cgnpc.cud.workbench.manage.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.qrtz.service.IIpmAssAllService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 超时节点视图 服务实现类
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-20
 */
@Slf4j
@Service
public class IpmAssAllServiceImpl extends ServiceImpl<IpmAssAllMapper, IpmAssAll> implements IIpmAssAllService {

    @Value("${cud.dtalk.notify.email: false}")
    private Boolean enableEmailNotify;
    @Value("${cud.dtalk.notify.dingTalk: false}")
    private Boolean enableDingTalkNotify;
    @Value("${cud.dtalk.messageUrl: http://cuddemo4-t/#/Home}")
    private String messageUrl;
    @Value("${cud.dtalk.notify.text: 您有一个任务已超时 {expire} :\n工作主题：{title}\n流程名称：{procName} \n当前环节：{actName}}")
    private String text;

    @Resource
    private ILogTimeoutNotifyService logTimeoutNotifyService;

    @Resource
    private ProcessService processService;

    @Resource
    private WbProcessInfoMapper wbProcessInfoMapper;

    @Resource
    private WbProcessInfoService wbProcessInfoService;

    @Resource
    private IpmAssAllMapper ipmAssAllMapper;

    @Resource
    private AsyncWarningMsgService asyncWarningMsgService;

    private String lockKey = IIpmAssAllService.class.getName();


    /**
      * @title: 提醒节点处理人处理节点任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 14:12
      * @description:
      * @param:
      * @return
      */
    @Override
    public void notifyTodo(){
        Object lock = RedisUtil.get(lockKey);
        if(Objects.nonNull(lock)){
            this.releaseLock();
            throw new RuntimeException("任务正在执行中，请稍后重试...");
        }
        RedisUtil.setIfAbsent(lockKey,String.valueOf(System.currentTimeMillis()));
        //获取所有超时（或预警）节点任务
        List<IpmAssAll>  ipmAssAlls = ipmAssAllMapper.selectAllParamOnDm();
        //根据视图查出超时提醒节点集合
        List<IpmAssAll> overTimeList = ipmAssAlls.stream()
                .filter(ipmAssAll -> ipmAssAll.getOverDateFlag1().compareTo(BigDecimal.ONE.intValue())==0)
                .collect(Collectors.toList());

        log.debug("超时节点集合：{}", JSON.toJSONString(overTimeList));

        //根据视图查出预警提醒节点集合
        List<IpmAssAll> expireTimeList = ipmAssAlls.stream()
                .filter(ipmAssAll -> ipmAssAll.getExpireDateFlag1().compareTo(BigDecimal.ONE.intValue())==0)
                .collect(Collectors.toList());
        log.debug("预警节点集合：{}", JSON.toJSONString(expireTimeList));

        //超时提醒日志
        List<LogTimeoutNotify> timeoutNotifies = logTimeoutNotifyService.findLastest(null);
        log.debug("已执行过超时提醒的日志信息:{}",JSON.toJSONString(timeoutNotifies));
        Map<String,LogTimeoutNotify> notifyMap = timeoutNotifies.stream()
                .collect(Collectors.toMap(LogTimeoutNotify::getActId, notify->notify));

        overTimeList = getNeedNotifyList(overTimeList,notifyMap,NotifyStatusEnum.OVERTIME.getKey());
        expireTimeList = getNeedNotifyList(expireTimeList,notifyMap,NotifyStatusEnum.EXPIRE.getKey());

        //超时提醒邮件列表
        List<IpmAssAll> overTimeEmailList = overTimeList.stream().filter(overTime->
                    Objects.nonNull(overTime.getOverTimeNotifyWay())&&
                overTime.getOverTimeNotifyWay()
                .contains(NotifyStatusEnum.EMAIL.getValue().toString())).collect(Collectors.toList());

        //超时提醒钉钉列表
        List<IpmAssAll> overTimeDingList = overTimeList.stream().filter(overTime->
                Objects.nonNull(overTime.getOverTimeNotifyWay())&&
                overTime.getOverTimeNotifyWay()
                .contains(NotifyStatusEnum.DING_TALK.getValue().toString())).collect(Collectors.toList());

        //预警提醒邮件列表
        List<IpmAssAll> expireEmailList = expireTimeList.stream().filter(expire->
                Objects.nonNull(expire.getExpireTimeNotifyWay())&&
                expire.getExpireTimeNotifyWay()
                .contains(NotifyStatusEnum.EMAIL.getValue().toString())).collect(Collectors.toList());

        //预警提醒钉钉列表
        List<IpmAssAll> expireDingList = expireTimeList.stream().filter(expire->
                Objects.nonNull(expire.getExpireTimeNotifyWay())&&
                expire.getExpireTimeNotifyWay()
                .contains(NotifyStatusEnum.DING_TALK.getValue().toString())).collect(Collectors.toList());

        //发送邮件超时提醒
        sendEmailNotify(overTimeEmailList,SendTypeEnum.OVERTIME.getValue());
        sendEmailNotify(expireEmailList,SendTypeEnum.EXPIRE.getValue());
        //发送钉钉超时提醒
        sendDingTalkNotify(overTimeDingList,SendTypeEnum.OVERTIME.getValue());
        sendDingTalkNotify(expireDingList,SendTypeEnum.EXPIRE.getValue());
        RedisUtil.expire(lockKey,10L);
    }

    @Override
    public void releaseLock(){
        RedisUtil.expire(lockKey,10L);
    }

    /**
      * @title: 发送邮件提醒
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/22 17:37
      * @description:
      * @param:  ipmAssAll 需要发送提醒的消息集合
      * @param:  sendType 提醒类型 超时提醒：6 预警提醒：7
      * @return
      */
    public void sendEmailNotify(List<IpmAssAll> ipmAssAll,Integer sendType){
        if(enableEmailNotify && CollectionUtils.isNotEmpty(ipmAssAll)) {
            List<String> instanceIds = ipmAssAll.stream().map(IpmAssAll::getProcInstId).distinct()
                    .collect(Collectors.toList());
            List<WbProcessInfoEntity> wbProcessInfoEntities = findByInstanceIds(instanceIds);
            Map<String, WbProcessInfoEntity> processInfoMap = wbProcessInfoEntities.stream()
                    .collect(Collectors.toMap(WbProcessInfoEntity::getProcInstId, process -> process));
            for(IpmAssAll notify:ipmAssAll){
                String actId = notify.getActId();
                String assigneeUser = notify.getAssigneeUser();
                String[] sendTos = assigneeUser.split("[,;|]");
                String instanceId = notify.getProcInstId();
                WbProcessInfoEntity processInfo = processInfoMap.get(instanceId);
                String startUser = processInfo.getStartUser();
                ReqEmailDto emailDto = new ReqEmailDto();
                //环节ID
                emailDto.setActId(actId);
                //流程实例ID
                emailDto.setProcInstId(instanceId);
                //发起人工号
                emailDto.setUserId(startUser);//startUser
                log.info("开始发送超时提醒邮件:actId:{},sendTos:{},sendType:{}",actId,assigneeUser,sendType);
                String status = NotifyStatusEnum.FAILURE.getKey();
                try{
                    processService.sendActEmail(actId,null, sendTos, sendType, processInfo, emailDto);
                    status = NotifyStatusEnum.SUCCESS.getKey();
                }catch (Exception e){
                    log.error("发送超时提醒钉钉消息失败:{}",e.getMessage(),e);
                }finally {
                    logNotify(notify,processInfo,NotifyStatusEnum.EMAIL.getValue(),assigneeUser,status);
                }
            }
        }
    }

    public List<WbProcessInfoEntity> findByInstanceIds(List<String> instanceIds){
        LambdaQueryWrapper<WbProcessInfoEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(WbProcessInfoEntity::getProcInstId,instanceIds);
        return wbProcessInfoMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * @title: 发送钉消息提醒
     * @author: P636016 XIAOJINHUI
     * @date: 2023/9/22 17:37
     * @description:
     * @param:  ipmAssAll 需要发送提醒的消息集合
     * @param:  sendType 提醒类型 超时提醒：6 预警提醒：7
     * @return
     */
    public void sendDingTalkNotify(List<IpmAssAll> ipmAssAll,Integer sendType){
        if(enableDingTalkNotify && CollectionUtils.isNotEmpty(ipmAssAll)){
            List<String> instanceIds = ipmAssAll.stream().map(IpmAssAll::getProcInstId).distinct()
                    .collect(Collectors.toList());
            List<WbProcessInfoEntity> wbProcessInfoEntities = findByInstanceIds(instanceIds);
            Map<String, WbProcessInfoEntity> processInfoMap = wbProcessInfoEntities.stream()
                    .collect(Collectors.toMap(WbProcessInfoEntity::getProcInstId, process -> process));

            for(IpmAssAll notify:ipmAssAll){
                String actId = notify.getActId();
                String assigneeUser = notify.getAssigneeUser();

                String instanceId = notify.getProcInstId();
                WbProcessInfoEntity processInfo = processInfoMap.get(instanceId);
                String startUser = processInfo.getStartUser();
                ReqEmailDto emailDto = new ReqEmailDto();
                String actName = processInfo.getActName();
                String procName = processInfo.getProcName();
                String procTitle = processInfo.getProcTitle();
                //环节ID
                emailDto.setActId(actId);
                //流程实例ID
                emailDto.setProcInstId(instanceId);
                //发起人工号
                emailDto.setUserId(startUser);

                String notifyType = "";
                if(sendType.equals(SendTypeEnum.OVERTIME.getValue())){
                    notifyType =SendTypeEnum.OVERTIME.getDesc();
                }else if(sendType.equals(SendTypeEnum.EXPIRE.getValue())){
                    notifyType = SendTypeEnum.EXPIRE.getDesc();
                }
                String message = text;

                Date startTime = notify.getStartTime();
                int timeFix=0;
                Calendar calendar = Calendar.getInstance();
                if (startTime == null){
                    startTime = new Date();
                }
                calendar.setTime(startTime);
                if(sendType.compareTo(SendTypeEnum.OVERTIME.getValue())==0){
                    String overtime= notify.getOverTime();
                    timeFix = Integer.valueOf(overtime);
                    calendar.add(Calendar.MINUTE,timeFix);
                }else if(sendType.compareTo(SendTypeEnum.EXPIRE.getValue())==0){
                    String expireTime= notify.getExpireTime();
                    timeFix = Integer.valueOf(expireTime);
                    calendar.add(Calendar.MINUTE,timeFix);
                }
                startTime = calendar.getTime();

                Date now = new Date();
                String diff = dateDiff(startTime,now);

                message = message.replace("{expire}",diff);
                message = message.replace("{title}",notify.getProcTitle());
                message = message.replace("{procName}",processInfo.getProcName()+ " " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                message = message.replace("{actName}",processInfo.getActName());

                log.info("开始发送超时提醒钉钉消息:actId:{},sendTos:{},sendType:{},message:{}"
                        ,actId,assigneeUser,sendType,JSON.toJSONString(message));
                String status = NotifyStatusEnum.FAILURE.getKey();

                try {
                    status = NotifyStatusEnum.SUCCESS.getKey();
                    DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO = new DingTalkWorkNoticeInputVO();
                    dingTalkWorkNoticeInputVO.setWorkNotice(WorkNotice.builder()
                            .title(notify.getProcTitle())
                            .content(StrUtil.format("工作主题:{} 环节名称：{} 超时类型：{}{}", procTitle,actName,notifyType,message)).build());
                    dingTalkWorkNoticeInputVO.setUserIdList(Arrays.asList(assigneeUser));
                    dingTalkWorkNoticeInputVO.getWorkNotice().setUrl(messageUrl);
                    dingTalkWorkNoticeInputVO.getWorkNotice().setMsgType("link");
                    asyncWarningMsgService.sendWorkNotice(dingTalkWorkNoticeInputVO);

                }catch (Exception e){
                    log.error("发送超时提醒钉钉消息失败:{}",e.getMessage(),e);
                }finally {
                    logNotify(notify,processInfo,NotifyStatusEnum.DING_TALK.getValue(),assigneeUser,status);
                }



            }
        }
    }

    /**
      * @title: 记录超时提醒推送日志
      * @author: p636016 XIAOJINHUI
      * @date: 2023/9/28 11:04
      * @description:
      * @param:
      * @return
      */
    public void logNotify(IpmAssAll ipmAssAll, WbProcessInfoEntity processInfo, String sendType, String receiver,
                          String status){
        LogTimeoutNotify notify = new LogTimeoutNotify();
        BeanUtils.copyProperties(processInfo,notify);
        BeanUtils.copyProperties(ipmAssAll,notify);
        notify.setNotificationId(UUIDUtil.uuid());
        notify.setNotifyDate(new Date());
        notify.setStatus(status);
        notify.setNotifyWay(sendType);
        notify.setReceiver(receiver);
        logTimeoutNotifyService.save(notify);
    }


    /**
      * @title: 获取需要提醒的列表
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/22 17:14
      * @description:
      * @param: notifyList 视图中需要提醒的节点数据
      * @param: notifyMap 视图中需要提醒的节点数据Map
      * @param: delayType 超时提醒/预警提醒类型
      * @return
      */
    public List<IpmAssAll> getNeedNotifyList(List<IpmAssAll> notifyList,Map<String,LogTimeoutNotify> notifyMap,String delayType){
        List<IpmAssAll> needNotifyList = new ArrayList<>();
        //遍历根据视图查出预警提醒节点集合，如果当没有已发送的超时提醒 或 已预警提醒的日期加上预警间隔（下次可预警日期）小于当前日期
        //则将当前超时任务加入到预警提醒节点集合中
        notifyList.stream().forEach(expire->{
            //当前系统时间
            Date now = new Date();
            String actId = expire.getActId();
            if(notifyMap.containsKey(actId)){
                LogTimeoutNotify notify = notifyMap.get(actId);
                Date notifyDate = notify.getNotifyDate();
                //时间间隔
                BigDecimal interval= BigDecimal.ZERO;
                //类型为超时，取超时时间间隔
                if(delayType.equals(NotifyStatusEnum.OVERTIME.getKey())){
                    interval = notify.getOverTimeInterval();
                //类型为预警，取预警时间间隔
                }else if(delayType.equals(NotifyStatusEnum.EXPIRE.getKey())){
                    interval = notify.getExpireTimeInterval();
                }

                Date nextNotifyDate = getNextNotifyDate(notifyDate,interval);
                int compare = now.compareTo(nextNotifyDate);
                if(compare<0){
                    return;
                }
            }
            needNotifyList.add(expire);
        });
        return needNotifyList;
    }

    /**
      * @title: 根据上次提醒时间构建下次可提醒的时间
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/22 16:30
      * @description:
      * @param: notifyDate 上次提醒时间
      * @param: interval 提醒时间间隔
      * @return
      */
    public Date getNextNotifyDate(Date notifyDate,BigDecimal interval){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(notifyDate);
        if (interval == null) interval = BigDecimal.ZERO;
        calendar.add(Calendar.MINUTE,interval.intValue());
        return calendar.getTime();
    }

    /**
      * @title: 计算两个时间的间隔
      * @author: P636016 XIAOJINHUI
      * @date: 2023/10/8 15:17
      * @description:
      * @param:
      * @return
      */
    public String dateDiff(Date startTime, Date endTime) {

        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long diff;
        //获得两个时间的毫秒时间差异
        diff = endTime.getTime() - startTime.getTime();
        long day = diff/nd;//计算差多少天
        long hour = diff%nd/nh;//计算差多少小时
        long min = diff%nd%nh/nm;//计算差多少分钟
        long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
        StringBuilder builder = new StringBuilder();
        if(day>0){
            builder.append(day+"天");
        }
        if(hour>0){
            builder.append(hour+"小时");
        }
        if(min>0){
            builder.append(min+"分钟");
        }
        return builder.toString();
    }
}
