
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.MeetingReserveStartTypeEnum;
import com.cgnpc.bbxpark.common.enums.MeetingReserveStatusEnum;
import com.cgnpc.bbxpark.common.enums.MeetingSignTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserveSign;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignTypeStatisticsModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveSignRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantTaskService;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveSignService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 会议签到服务实现
 * @author huangyongtao
 * @date 2024/8/26 11:08
 */
@Service("meetingReserveSignService")
public class MeetingReserveSignServiceImpl extends ServiceImpl<MeetingReserveSignRepository, MeetingReserveSign> implements IMeetingReserveSignService {
    @Autowired
    private MeetingReserveRepository meetingReserveRepository;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IMeetingAttendantTaskService meetingAttendantTaskService;


    @Override
    public IPage<MeetingSignModel> page(MeetingSignPageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        LambdaQueryWrapper<MeetingReserveSign> query = new LambdaQueryWrapper<>();
        query.eq(ObjectUtil.isNotEmpty(tenantId), MeetingReserveSign::getTenantId, tenantId);
        query.eq(MeetingReserveSign::getReserveId, param.getReserveId());
        query.eq(param.getType() != null,MeetingReserveSign::getType,param.getType());
        if(!StringUtils.isEmpty(param.getSignUname())){
            query.and(lambdaQueryWrapper->lambdaQueryWrapper.like(MeetingReserveSign::getSignUname,param.getSignUname()).or().like(MeetingReserveSign::getSignStaffid,param.getSignUname()));
        }
        query.orderByDesc(MeetingReserveSign::getSignTime);
        IPage<MeetingReserveSign> signPage = page(new Page<>(param.getCurrent(), param.getSize()), query);
        List<MeetingSignModel> list = BeanUtils.convertListTo(signPage.getRecords(), MeetingSignModel::new);
        return ConvertUtil.pageConvert(signPage.getCurrent(), signPage.getTotal(), signPage.getSize(), list);
    }

    @Override
    public List<MeetingSignModel> findByReserveId(Long reserveId) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<MeetingReserveSign> signList = list(Wrappers.<MeetingReserveSign>lambdaQuery().eq(tenantId != null,MeetingReserveSign::getTenantId,tenantId).eq(MeetingReserveSign::getReserveId,reserveId));
        return BeanUtils.convertListTo(signList,MeetingSignModel::new);
    }

    @Override
    public MeetingSignTypeStatisticsModel typeStatistics(Long reserveId) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        MeetingSignTypeStatisticsModel model = new MeetingSignTypeStatisticsModel();
        //参会人数
        model.setTotal(count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId,reserveId).eq(tenantId != null, MeetingReserveSign::getTenantId, tenantId)));
        //正常签到数量
        model.setNormalCount(count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId,reserveId).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.NORMAL.getCode()).eq(tenantId != null, MeetingReserveSign::getTenantId, tenantId)));
        //补签数量
        model.setRepairCount(count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId,reserveId).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.REPAIR.getCode()).eq(tenantId != null, MeetingReserveSign::getTenantId, tenantId)));
        //代签到数量
        model.setBehalfCount(count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId,reserveId).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.BEHALF.getCode()).eq(tenantId != null, MeetingReserveSign::getTenantId, tenantId)));
        //代补签数量
        model.setBehalfRepairCount(count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId,reserveId).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.BEHALF_REPAIR.getCode()).eq(tenantId != null, MeetingReserveSign::getTenantId, tenantId)));
        //未签到数量
        model.setNotCount(count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId,reserveId).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.NOT.getCode()).eq(tenantId != null, MeetingReserveSign::getTenantId, tenantId)));
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean sign(MeetingSignParam param) {
        Date localTime = new Date();
        MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
        signCheck(reserve);
        //会议状态检查(未结束||允许补签)
        boolean flag = !MeetingReserveStatusEnum.END.getCode().equals(reserve.getStatus()) || Status.enabled.getKey() == reserve.getReplenishSignFlag();
        AssertUtils.isTrue(flag, "不允许补签");

        List<MeetingReserveSign> signs = this.getBaseMapper().selectList(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, param.getId()).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.NOT.getCode()));
        Map<String, Long> signIdMap = CollectionUtil.isEmpty(signs) ? new HashMap<>() : signs.stream().collect(Collectors.toMap(MeetingReserveSign::getSignUid, MeetingReserveSign::getId, (k1,k2)->k1));
        int type = MeetingReserveStatusEnum.END.getCode().equals(reserve.getStatus()) ? MeetingSignTypeEnum.REPAIR.getCode():MeetingSignTypeEnum.NORMAL.getCode();
        if(signIdMap.containsKey(WebFrameworkUtils.getHeaderUserId())){
          return signUpdate(reserve,Collections.singletonList(WebFrameworkUtils.getHeaderUserId()),false,type,localTime,  signIdMap);
        }
        //重复签到检查
        int count = this.count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, param.getId()).eq(MeetingReserveSign::getSignUid, WebFrameworkUtils.getHeaderUserId()));
        AssertUtils.isFalse(count>0, "已签到");
        return signSave(reserve, Collections.singletonList(WebFrameworkUtils.getHeaderUserId()),false,type,localTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean signByRepair(MeetingSignParam param) {
        AssertUtils.isFalse(CollectionUtils.isEmpty(param.getUserIdList()), "请选择代补签人员");
        Date localTime = new Date();
        MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
        AssertUtils.notNull(reserve, SystemResultCode.RESULT_DATA_NONE.message());
        //会议是否取消检查
        AssertUtils.isTrue((int)Status.disabled.getKey() == reserve.getCancelFlag(),"会议已取消，无法签到");

        List<MeetingReserveSign> signs = this.getBaseMapper().selectList(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, param.getId()).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.NOT.getCode()));
        Map<String, Long> signIdMap = CollectionUtil.isEmpty(signs) ? new HashMap<>() : signs.stream().collect(Collectors.toMap(MeetingReserveSign::getSignUid, MeetingReserveSign::getId, (k1,k2)->k1));
        signUpdate(reserve, param.getUserIdList().stream().filter(signIdMap::containsKey).collect(Collectors.toList()), true,MeetingSignTypeEnum.BEHALF_REPAIR.getCode(),localTime,  signIdMap);
        //查询已签到
        List<String> signIdList = findByReserveId(param.getId()).stream().map(MeetingSignModel::getSignUid).collect(Collectors.toList());
        List<String> newSignIdList = CollectionUtils.isEmpty(signIdList) ? param.getUserIdList() : (List<String>) CollectionUtils.subtract(param.getUserIdList(),signIdList);
        return signSave(reserve,newSignIdList,true,MeetingSignTypeEnum.BEHALF_REPAIR.getCode(),localTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean signByBehalf(MeetingSignParam param) {
        AssertUtils.isFalse(CollectionUtils.isEmpty(param.getThirdUserIdList()), "请选择代签到人员");
        Date localTime = new Date();
        MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
        signCheck(reserve);
        //会议状态检查(未结束)
        AssertUtils.isFalse(MeetingReserveStatusEnum.END.getCode().equals(reserve.getStatus()), "会议已结束，无法代签到");
        //会议规则检查(允许代签)
        AssertUtils.isTrue(Objects.equals(Status.enabled.getKey(), reserve.getBehalfSignFlag()), "规则校验失败，无法代签到");
        //根据钉钉用户id查询用户id
       /* UserLoginParam loginParam = new UserLoginParam();
        loginParam.setThirdUserIdList(param.getThirdUserIdList());
        List<UserLoginModel> list = dingDingRepository.getUser(loginParam);*/
        if(!CollectionUtils.isEmpty(param.getThirdUserIdList())){
            List<MeetingReserveSign> signs = this.getBaseMapper().selectList(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, param.getId()).eq(MeetingReserveSign::getType, MeetingSignTypeEnum.NOT.getCode()));
            Map<String, Long> signIdMap = CollectionUtil.isEmpty(signs) ? new HashMap<>() : signs.stream().collect(Collectors.toMap(MeetingReserveSign::getSignUid, MeetingReserveSign::getId, (k1,k2)->k1));
            signUpdate(reserve, param.getThirdUserIdList().stream().filter(signIdMap::containsKey).collect(Collectors.toList()), true,MeetingSignTypeEnum.BEHALF.getCode(),localTime,  signIdMap);

            // List<String> userIdList = list.stream().map(UserLoginModel::getId).collect(Collectors.toList());
            List<String> userIdList = param.getThirdUserIdList();
            //查询已签到
            List<String> signIdList = findByReserveId(param.getId()).stream().map(MeetingSignModel::getSignUid).collect(Collectors.toList());
            List<String> newSignIdList = CollectionUtils.isEmpty(signIdList) ?userIdList : (List<String>) CollectionUtils.subtract(userIdList,signIdList);
            return signSave(reserve,newSignIdList,true,MeetingSignTypeEnum.BEHALF.getCode(),localTime);
        }
        return false;
    }

    /**
     * 会议签到检查
     * @param reserve 会议信息
     */
    private void signCheck( MeetingReserve reserve){
        AssertUtils.notNull(reserve, SystemResultCode.RESULT_DATA_NONE.message());
        //会议是否取消检查
        AssertUtils.isTrue(Status.disabled.getKey().equals(reserve.getCancelFlag()),"会议已取消，无法签到");
        //签到时间检查(待开始前三十分钟)
        Date localTime = new Date();
        AssertUtils.isFalse(localTime.before(reserve.getStartTime()) && DateUtil.between(localTime, reserve.getStartTime(), DateUnit.MINUTE) > 30, "会议开始前30分钟内，方可进行签到！");
    }

    /**
     * 会议签到保存
     * @param reserve 会议信息
     * @param userIdList 用户id集合
     * @param behalfFlag 是否代签
     * @param type 签到类型
     * @param signTime 签到时间
     * @return 是否成功
     */
    private boolean signSave(MeetingReserve reserve,List<String> userIdList,boolean behalfFlag,int type,Date signTime){
        UserInfoModel user = Objects.requireNonNull(userApiService.detail(WebFrameworkUtils.getHeaderUserId()));
        boolean endFlag = MeetingReserveStatusEnum.END.getCode().equals(reserve.getStatus());
        //是否允许代签
        List<MeetingReserveSign> signList = userIdList.stream().map(userId->{
            UserInfoModel signUser = !behalfFlag ? user : Objects.requireNonNull(userApiService.detail(userId));
            MeetingReserveSign sign = new MeetingReserveSign();
            sign.setReserveId(reserve.getId());
            sign.setType(type);
            sign.setSignTime(signTime);
            sign.setSignUid(signUser.getId());
            sign.setSignUname(signUser.getUserName());
            sign.setSignStaffid(signUser.getStaffid());
            sign.setSignDepartment(signUser.getDepartmentName());
            sign.setOperateUid(user.getId());
            sign.setOperateUname(user.getUserName());
            sign.setOperateStaffid(user.getStaffid());
            if(MeetingSignTypeEnum.NOT.getCode().equals(type)){
                sign.setInvited(Status.enabled.getKey());
                sign.setSignTime(null);
                sign.setTenantId(reserve.getTenantId());
            }
            return sign;
        }).collect(Collectors.toList());
        if(!saveBatch(signList)){
            return false;
        }
        if(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()) && !MeetingSignTypeEnum.BEHALF_REPAIR.getCode().equals(type) && !MeetingSignTypeEnum.NOT.getCode().equals(type)){
            //移动端签到(签到/补签/代签)需更改待开始会议的状态、实际开始时间、实际开始类型
            reserve.setStatus(MeetingReserveStatusEnum.GOING.getCode());
            reserve.setRealStartTime(signTime);
            reserve.setRealStartType(MeetingReserveStartTypeEnum.SIGN.getCode());
            meetingReserveRepository.updateById(reserve);
            meetingAttendantTaskService.handleTaskStartReserve(reserve.getId(), reserve.getRoomId());
        }
        return true;
    }

    /***
     * @Description 移动端 - 参会人批量新增
     * @author huangyongtao
     * @date 2025/1/3 9:40
     * @param reserve
     * @param userIdList
     * @param loginId
     */
    @Override
    public void signSaveByReserve(MeetingReserve reserve,List<String> userIdList, String loginId){
        UserInfoModel user = Objects.requireNonNull(userApiService.detail(loginId));
        List<MeetingReserveSign> signList = userIdList.stream().map(userId->{
            UserInfoModel signUser = Objects.requireNonNull(userApiService.detail(userId));
            MeetingReserveSign sign = new MeetingReserveSign();
            sign.setReserveId(reserve.getId());
            sign.setType(MeetingSignTypeEnum.NOT.getCode());
            sign.setSignUid(signUser.getId());
            sign.setSignUname(signUser.getUserName());
            sign.setSignStaffid(signUser.getStaffid());
            sign.setSignDepartment(signUser.getDepartmentName());
            sign.setOperateUid(user.getId());
            sign.setOperateUname(user.getUserName());
            sign.setOperateStaffid(user.getStaffid());
            sign.setInvited(Status.enabled.getKey());
            sign.setTenantId(reserve.getTenantId());
            return sign;
        }).collect(Collectors.toList());
        this.saveBatch(signList);
    }

    @Override
    public boolean removeByReserveId(Long reserveId) {
        return remove(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, reserveId));
    }

    private boolean signUpdate(MeetingReserve reserve,List<String> userIdList,boolean behalfFlag,int type,Date signTime,  Map<String, Long> signIdMap){
        if(CollectionUtil.isEmpty(userIdList)){
            return true;
        }
        UserInfoModel user = Objects.requireNonNull(userApiService.detail(WebFrameworkUtils.getHeaderUserId()));
        //是否允许代签
        List<MeetingReserveSign> signList = userIdList.stream().map(userId->{
            UserInfoModel signUser = !behalfFlag ? user : Objects.requireNonNull(userApiService.detail(userId));
            MeetingReserveSign sign = new MeetingReserveSign();
            sign.setId(signIdMap.get(userId));
            sign.setReserveId(reserve.getId());
            sign.setType(type);
            sign.setSignTime(signTime);
            sign.setSignUid(signUser.getId());
            sign.setSignUname(signUser.getUserName());
            sign.setSignStaffid(signUser.getStaffid());
            sign.setSignDepartment(signUser.getDepartmentName());
            sign.setOperateUid(user.getId());
            sign.setOperateUname(user.getUserName());
            sign.setOperateStaffid(user.getStaffid());
            return sign;
        }).collect(Collectors.toList());
        if(!updateBatchById(signList)){
            return false;
        }
        return true;
    }




    /**
     * md5的基本使用
     * 生成32位的密文
     */
    public static void main(String[] args) {
        // 密钥，使用前请初始化为16、24或32个字符（128位、192位或256位）
        String key = "1234567890abcdef";
        // 明文
        String text = "Hello World!";
        // AES加密
        AES aes = SecureUtil.aes(key.getBytes());
        String encryptHex = aes.encryptHex(text); // 加密为16进制表示
        System.out.println("加密后的数据为：" + encryptHex);
        // AES解密
        encryptHex = "336cec09590af4aeacd05235b36e0c0e";
        String decryptStr = aes.decryptStr(encryptHex);
        System.out.println("解密后的数据为：" + decryptStr);

        Long diff = DateUtil.between(DateUtils.format("2024-08-30 23:10:00", "yyyy-MM-dd HH:mm:ss"), DateUtils.format("2024-08-30 23:11:59", "yyyy-MM-dd HH:mm:ss"), DateUnit.MINUTE);
        System.out.println("时间差值：" + diff);

        //获取cpu核心线程数
        int core = Runtime.getRuntime().availableProcessors();
        System.out.println("核心线程数：" + core);
    }

}
