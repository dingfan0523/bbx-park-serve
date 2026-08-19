
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.MeetingConstant;
import com.cgnpc.bbxpark.common.enums.MeetingAttendantRoomStatusEnum;
import com.cgnpc.bbxpark.common.enums.MeetingAttendantTaskStatusEnum;
import com.cgnpc.bbxpark.common.enums.MeetingAttendantTaskTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantEvaluate;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantTask;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantCountPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountDetailPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantEvaluateRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantTaskRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantRoomService;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantService;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantTaskCountService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/***
 * @Description 会服统计服务实现
 * @author huangyongtao
 * @date 2025/2/10 10:10
 */
@Service
public class MeetingAttendantTaskCountServiceImpl extends ServiceImpl<MeetingAttendantTaskRepository, MeetingAttendantTask> implements IMeetingAttendantTaskCountService {

    @Autowired
    private MeetingAttendantEvaluateRepository meetingAttendantEvaluateRepository;

    @Autowired
    private MeetingReserveRepository meetingReserveRepository;

    @Autowired
    private MeetingAttendantRepository meetingAttendantRepository;

    @Resource
    private IMeetingAttendantRoomService meetingAttendantRoomService;

    @Autowired
    private IMeetingAttendantService meetingAttendantService;

    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executorService;

    @Override
    @SneakyThrows
    public MeetingAttendantTaskTopCountModel attendantTaskCount(MeetingAttendantTaskCountParam param) {
        MeetingAttendantTaskTopCountModel model = new MeetingAttendantTaskTopCountModel();
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        CompletableFuture<Void> handleTaskFuture = CompletableFuture.runAsync(() -> handleTask(model,  param), executorService);
        CompletableFuture<Void> handleEvaluateFuture = CompletableFuture.runAsync(() -> handleEvaluate(model,  param), executorService);
        CompletableFuture<Void> handlePersonNumFuture = CompletableFuture.runAsync(() -> handlePersonNum(model,  param), executorService);
        //同步
        CompletableFuture.allOf(handleTaskFuture, handleEvaluateFuture, handlePersonNumFuture).get();
        return model;
    }

    @Override
    public List<MeetingAttendantTaskPersonCountModel> attendantTaskPersonCount(MeetingAttendantTaskCountParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<MeetingAttendantModel> attendantModels = meetingAttendantService.findList();
        if (CollectionUtils.isEmpty(attendantModels)) {
            return new ArrayList<>();
        }
        List<MeetingAttendantTaskPersonCountModel> models = BeanUtils.convertListTo(attendantModels, MeetingAttendantTaskPersonCountModel::new);
        //会服人员-会议室
        List<MeetingAttendantRoomModel> roomModelList = meetingAttendantRoomService.listAllByUserIdIn(attendantModels.stream().map(MeetingAttendantModel::getUserId).collect(Collectors.toList()));
        Map<String, List<Long>> roomMap = roomModelList.stream().collect(Collectors.groupingBy(MeetingAttendantRoomModel::getUserId,
                Collectors.mapping(MeetingAttendantRoomModel::getRoomId, Collectors.toList())
        ));
        //服务会议集合
        List<MeetingAttendantTaskAndReserveModel> taskAndReserveModels = this.getBaseMapper().findTaskAndReserve(param);
         //提供的会服集合
        List<MeetingAttendantTaskModel> tasks = this.getBaseMapper().findTask(param);
        for(MeetingAttendantTaskPersonCountModel model : models){
            if(CollectionUtil.isNotEmpty(taskAndReserveModels)){
                Map<String, List<MeetingAttendantTaskAndReserveModel>> taskAndReserveMap = taskAndReserveModels.stream().collect(Collectors.groupingBy(MeetingAttendantTaskAndReserveModel::getHandleUid));
                if(taskAndReserveMap.containsKey(model.getUserId())){
                    //服务的会议场次
                    model.setMeetingNum((long) taskAndReserveMap.get(model.getUserId()).size());
                    //服务会议的人数
                    model.setPersonNum(taskAndReserveMap.get(model.getUserId()).stream().mapToLong(MeetingAttendantTaskAndReserveModel::getRealParticipantNumber).sum());
                }
            }
            if(CollectionUtil.isNotEmpty(tasks)){
                List<MeetingAttendantTaskModel> validTasks = tasks.stream().filter(task -> ObjectUtil.isNotEmpty(task.getHandleUid()) && MeetingAttendantTaskStatusEnum.COMPLETE.getValue().equals(task.getServiceStatus())).collect(Collectors.toList());
                List<MeetingAttendantTaskModel> expireTasks = tasks.stream().filter(task -> Status.disabled.getKey().equals(task.getServiceValid())).collect(Collectors.toList());
                Map<String, List<MeetingAttendantTaskModel>> validTaskMap = validTasks.stream().collect(Collectors.groupingBy(MeetingAttendantTaskModel::getHandleUid));
                if(validTaskMap.containsKey(model.getUserId())){
                    //提供的会服次数
                    model.setAttendantTaskNum((long) validTaskMap.get(model.getUserId()).size());
                }
                if(roomMap.containsKey(model.getUserId())){
                    //过期的会服次数
                    model.setExpireAttendantTaskNum(expireTasks.stream().filter(task -> roomMap.get(model.getUserId()).contains(task.getRoomId())).count());
                }
            }
        }
        return models;
    }

    @Override
    public List<MeetingAttendantTaskPersonCountModel> attendantTaskScoreCount(MeetingAttendantTaskCountParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<MeetingAttendantModel> attendantModels = meetingAttendantService.findList();
        if (CollectionUtils.isEmpty(attendantModels)) {
            return new ArrayList<>();
        }
        List<MeetingAttendantTaskPersonCountModel> models = BeanUtils.convertListTo(attendantModels, MeetingAttendantTaskPersonCountModel::new);
        handleSore(models, param);
        return models;
    }


    @SneakyThrows
    @Override
    public IPage<MeetingAttendantTaskPersonCountModel> attendantPage(MeetingAttendantCountPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<MeetingAttendantModel> attendantModels = meetingAttendantService.findList(param.getUserName());
        if (CollectionUtils.isEmpty(attendantModels)) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        List<MeetingAttendantTaskPersonCountModel> models = BeanUtils.convertListTo(attendantModels, MeetingAttendantTaskPersonCountModel::new);
        MeetingAttendantTaskCountParam countParam = BeanUtils.convertTo(param, MeetingAttendantTaskCountParam::new);
        countParam.setUserIdList(models.stream().map(MeetingAttendantTaskPersonCountModel::getUserId).collect(Collectors.toList()));
        CompletableFuture<Void> handleRoomNameFuture = CompletableFuture.runAsync(() -> {
            //处理会议室名称
            handleRoomName(models);
        }, executorService);
        CompletableFuture<Void> handleSoreFuture = CompletableFuture.runAsync(() -> {
            //处理评分
            handleSore(models, countParam);
        }, executorService);
        CompletableFuture<Void> handleTaskNumFuture = CompletableFuture.runAsync(() -> {
            //处理会议人数和会服次数
            handleTaskNum(models, countParam);
        }, executorService);
        //同步
        CompletableFuture.allOf(handleRoomNameFuture, handleSoreFuture, handleTaskNumFuture).get();

        // 根据入参类型进行排序
        if (MeetingConstant.AVERAGESCORE.equals(param.getSortBy())) {
            if (MeetingConstant.ASC.equals(param.getSortOrder())) {
                models.sort(Comparator.comparingDouble(MeetingAttendantTaskPersonCountModel::getAverageScoreDouble));
            } else if (MeetingConstant.DESC.equals(param.getSortOrder())) {
                models.sort(Comparator.comparingDouble(MeetingAttendantTaskPersonCountModel::getAverageScoreDouble).reversed());
            }
        } else if (MeetingConstant.PERSONNUM.equals(param.getSortBy())) {
            if (MeetingConstant.ASC.equals(param.getSortOrder())) {
                models.sort(Comparator.comparingLong(MeetingAttendantTaskPersonCountModel::getPersonNum));
            } else if (MeetingConstant.DESC.equals(param.getSortOrder())) {
                models.sort(Comparator.comparingLong(MeetingAttendantTaskPersonCountModel::getPersonNum).reversed());
            }
        } else if (MeetingConstant.ATTENDANTTASKNUM.equals(param.getSortBy())) {
            if (MeetingConstant.ASC.equals(param.getSortOrder())) {
                models.sort(Comparator.comparingLong(MeetingAttendantTaskPersonCountModel::getAttendantTaskNum));
            } else if (MeetingConstant.DESC.equals(param.getSortOrder())) {
                models.sort(Comparator.comparingLong(MeetingAttendantTaskPersonCountModel::getAttendantTaskNum).reversed());
            }
        }
        // 分页
        int startIndex = Math.toIntExact((param.getCurrent() - 1) * param.getSize());
        int endIndex = Math.toIntExact(Math.min(startIndex + param.getSize(), models.size()));
        List<MeetingAttendantTaskPersonCountModel> pagedModels = startIndex > models.size()-1 ? new ArrayList<>() : models.subList(startIndex, endIndex);
        return ConvertUtil.pageConvert(param.getCurrent(), attendantModels.size(), param.getSize(), pagedModels);
    }

    @Override
    public List<MeetingAttendantRoomModel> findRoomList(String userId) {
        return meetingAttendantRoomService.listAllByUserIdIn(Collections.singletonList(userId));
    }

    @Override
    public IPage<MeetingAttendantEvaluateDetailModel> scoreDetailPage(MeetingAttendantTaskCountDetailPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        if(!MeetingConstant.DESC.equals(param.getSortOrder()) && !MeetingConstant.ASC.equals(param.getSortOrder())){
            param.setSortOrder(null);
        }
        IPage<MeetingAttendantEvaluateDetailModel> page = meetingAttendantEvaluateRepository.pageByUserId(new Page<>(param.getCurrent(), param.getSize()), param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(page.getCurrent(), page.getSize());
        }
        return ConvertUtil.pageConvert(page,page.getRecords());
    }


    private void handleSore(List<MeetingAttendantTaskPersonCountModel> models, MeetingAttendantTaskCountParam param){
        List<MeetingAttendantTaskAndReserveModel> evaluates = meetingAttendantEvaluateRepository.findScoreByTask(param);
        if(CollectionUtil.isEmpty(evaluates)){
            return;
        }
        for(MeetingAttendantTaskPersonCountModel model : models){
            Map<String, List<MeetingAttendantTaskAndReserveModel>> evaluateMap = evaluates.stream().collect(Collectors.groupingBy(MeetingAttendantTaskAndReserveModel::getHandleUid));
            if(evaluateMap.containsKey(model.getUserId())){
                //服务的平均分
                OptionalDouble average = evaluateMap.get(model.getUserId()).stream().mapToInt(MeetingAttendantTaskAndReserveModel::getScore).average();
                average.ifPresent(avg -> {
                    model.setAverageScore(getDecimalFormat(avg));
                    model.setAverageScoreDouble(avg);
                });
            }
        }
    }

    private void handleRoomName(List<MeetingAttendantTaskPersonCountModel> models){
        //会服人员-会议室
        List<MeetingAttendantRoomModel> roomModelList = meetingAttendantRoomService.listAllByUserIdIn(models.stream().map(MeetingAttendantTaskPersonCountModel::getUserId).collect(Collectors.toList()));
        Map<String, List<String>> roomMap = roomModelList.stream().collect(Collectors.groupingBy(MeetingAttendantRoomModel::getUserId,
                Collectors.mapping(m->m.getRoomName() + statusHandle(m.getStatus()), Collectors.toList())
        ));
        //处理会议室名称
        models.forEach(item -> item.setRoomNameList(roomMap.get(item.getUserId())));
    }

    private void handleTaskNum(List<MeetingAttendantTaskPersonCountModel> models, MeetingAttendantTaskCountParam param){
        //服务会议集合
        List<MeetingAttendantTaskAndReserveModel> taskAndReserveModels = this.getBaseMapper().findTaskAndReserve(param);
        //提供的会服集合
        List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery()
                .select(MeetingAttendantTask::getId, MeetingAttendantTask::getHandleUid)
                .eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
                .isNotNull(MeetingAttendantTask::getHandleUid)
                .ge(ObjectUtil.isNotEmpty(param.getStartTime()), MeetingAttendantTask::getHandleTime, param.getStartTime())
                .le(ObjectUtil.isNotEmpty(param.getEndTime()), MeetingAttendantTask::getHandleTime, param.getEndTime())
                .in(CollectionUtil.isNotEmpty(param.getUserIdList()), MeetingAttendantTask::getHandleUid, param.getUserIdList())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), MeetingAttendantTask::getTenantId, param.getTenantId()));

        for(MeetingAttendantTaskPersonCountModel model : models){
            if(CollectionUtil.isNotEmpty(taskAndReserveModels)){
                Map<String, List<MeetingAttendantTaskAndReserveModel>> taskAndReserveMap = taskAndReserveModels.stream().collect(Collectors.groupingBy(MeetingAttendantTaskAndReserveModel::getHandleUid));
                if(taskAndReserveMap.containsKey(model.getUserId())){
                    //服务的会议场次
                    model.setMeetingNum((long) taskAndReserveMap.get(model.getUserId()).size());
                    //服务会议的人数
                    model.setPersonNum(taskAndReserveMap.get(model.getUserId()).stream().mapToLong(MeetingAttendantTaskAndReserveModel::getRealParticipantNumber).sum());
                }
            }
            if(CollectionUtil.isNotEmpty(tasks)){
                Map<String, List<MeetingAttendantTask>> validTaskMap = tasks.stream().collect(Collectors.groupingBy(MeetingAttendantTask::getHandleUid));
                if(validTaskMap.containsKey(model.getUserId())){
                    //提供的会服次数
                    model.setAttendantTaskNum((long) validTaskMap.get(model.getUserId()).size());
                }
            }
        }
    }
    /**
     * 状态参数处理
     * @param status 状态
     * @return 状态
     */
    private String statusHandle(Integer status){
        for(MeetingAttendantRoomStatusEnum statusEnum:MeetingAttendantRoomStatusEnum.values()){
            if(statusEnum == MeetingAttendantRoomStatusEnum.NORMAL){
                continue;
            }
            if(statusEnum.getValue().equals(status)){
                return "("+statusEnum.getName()+")";
            }
        }
        return "";
    }


    private void handlePersonNum(MeetingAttendantTaskTopCountModel model, MeetingAttendantTaskCountParam param){
        //本次的人数
        Long[] personNum = new Long[1];
        //上次的人数
        Long[] lastPersonNum = new Long[1];
        //上上次的人数
        Long[] lastLastPersonNum = new Long[1];
        switch (param.getTimeType()) {
            case "day":
                handleTimeRange(param.getStartTime(), param.getEndTime(), DateField.DAY_OF_MONTH, 1,  personNum,  lastPersonNum,  lastLastPersonNum,  param.getTenantId());
                break;
            case "week":
                handleTimeRange(param.getStartTime(), param.getEndTime(), DateField.WEEK_OF_YEAR, 1,  personNum,  lastPersonNum,  lastLastPersonNum,  param.getTenantId());
                break;
            case "month":
                handleTimeRange(param.getStartTime(), param.getEndTime(), DateField.MONTH, 1,  personNum,  lastPersonNum,  lastLastPersonNum,  param.getTenantId());
                break;
            case "quarter":
                handleTimeRange(param.getStartTime(), param.getEndTime(), DateField.MONTH, 3,  personNum,  lastPersonNum,  lastLastPersonNum,  param.getTenantId());
                break;
            case "year":
                handleTimeRange(param.getStartTime(), param.getEndTime(), DateField.YEAR, 1,  personNum,  lastPersonNum,  lastLastPersonNum,  param.getTenantId());
                break;
            default:
                throw GenericException.fail("timeType参数错误");
        }
        //本次服务会议的人数
        model.setPersonNum(personNum[0]);
        //上次服务会议的人数
        model.setLastPersonNum(lastPersonNum[0]);
        //上上次服务会议的人数
        model.setLastLastPersonNum(lastLastPersonNum[0]);
        if(lastPersonNum[0] <= 0L){
            //服务会议人数环比
            model.setMeetingPersonChain("-");
        }else{
            //服务会议人数环比
            model.setMeetingPersonChain(getDecimalFormat(personNum[0]-lastPersonNum[0], lastPersonNum[0]));

        }
    }

    private Long getPersonNum(Date startTime, Date endTime, Long tenantId){
        List<MeetingReserve> meetingReserves = meetingReserveRepository.selectList(Wrappers.<MeetingReserve>lambdaQuery()
                .select(MeetingReserve::getId, MeetingReserve::getRealParticipantNumber)
                .ge(ObjectUtil.isNotEmpty(startTime), MeetingReserve::getStartTime, startTime)
                .le(ObjectUtil.isNotEmpty(endTime), MeetingReserve::getStartTime, endTime)
                .gt(MeetingReserve::getRealParticipantNumber, 0)
                .eq(ObjectUtil.isNotEmpty(tenantId), MeetingReserve::getTenantId, tenantId));
        if(CollectionUtil.isEmpty(meetingReserves)){
            return 0L;
        }
        // 计算实际总人数
        return meetingReserves.stream().mapToLong(MeetingReserve::getRealParticipantNumber).sum();
    }


    /**
     *
     * @param field 时间字段（日、周、月、年）
     * @param step  偏移步长（仅用于季度）
     */
    private void handleTimeRange(Date start, Date end, DateField field, int step, Long[] personNum, Long[] lastPersonNum, Long[] lastLastPersonNum, Long tenantId) {
        // 当前时间段的开始和结束
//        Date start = getStartOfRange(start, field);
//        Date end = getEndOfRange(end, field);
        personNum[0] = getPersonNum(start, end, tenantId);

        // 上一个时间段的开始和结束
        Date lastStart = DateUtil.offset(start, field, -step);
        Date lastEnd = DateUtil.offset(end, field, -step);
        if(DateField.MONTH.equals(field)){
            lastEnd =  DateUtil.endOfMonth(lastEnd);
        }
        lastPersonNum[0] = getPersonNum(lastStart, lastEnd, tenantId);

        // 上上一个时间段的开始和结束
        Date lastLastStart = DateUtil.offset(lastStart, field, -step);
        Date lastLastEnd = DateUtil.offset(lastEnd, field, -step);
        if(DateField.MONTH.equals(field)){
            lastLastEnd =  DateUtil.endOfMonth(lastLastEnd);
        }
        lastLastPersonNum[0] = getPersonNum(lastLastStart, lastLastEnd, tenantId);

    }
    /**
     * 获取时间段的开始时间
     */
//    private static Date getStartOfRange(Date date, DateField field) {
//        switch (field) {
//            case DAY_OF_MONTH:
//                return DateUtil.beginOfDay(date);
//            case WEEK_OF_YEAR:
//                return DateUtil.beginOfWeek(date);
//            case MONTH:
//                return DateUtil.beginOfMonth(date);
//            case YEAR:
//                return DateUtil.beginOfYear(date);
//            default:
//                throw GenericException.fail("不支持的时间类型: " + field);
//        }
//    }

    /**
     * 获取时间段的结束时间
     */
//    private static Date getEndOfRange(Date date, DateField field) {
//        switch (field) {
//            case DAY_OF_MONTH:
//                return DateUtil.endOfDay(date);
//            case WEEK_OF_YEAR:
//                return DateUtil.endOfWeek(date);
//            case MONTH:
//                return DateUtil.endOfMonth(date);
//            case YEAR:
//                return DateUtil.endOfYear(date);
//            default:
//                throw GenericException.fail("不支持的时间类型: " + field);
//        }
//    }

    private void handleEvaluate(MeetingAttendantTaskTopCountModel model, MeetingAttendantTaskCountParam param){
        List<MeetingAttendantEvaluate> evaluates = meetingAttendantEvaluateRepository.findScoreByReserve(param);
        if(CollectionUtil.isEmpty(evaluates)){
            return;
        }
        //服务的平均分
        OptionalDouble average = evaluates.stream().mapToInt(MeetingAttendantEvaluate::getScore).average();
        average.ifPresent(avg -> {
            model.setAverageScore(getDecimalFormat(avg));
            model.setAverageScoreDouble(avg);
        });

        long totalEvaluates = evaluates.size();
        long above2Count = evaluates.stream().filter(e -> e.getScore() < 2).count();
        long between2And4Count = evaluates.stream().filter(e -> e.getScore() >= 2 && e.getScore() <= 4).count();
        long above4Count = evaluates.stream().filter(e -> e.getScore() > 4).count();

        //评分低于2分的比例
        model.setScoreLowRate(getDecimalFormat(above2Count, totalEvaluates));
        //评分2-4分的比例
        model.setScoreInRate(getDecimalFormat(between2And4Count, totalEvaluates));
        //评分高于4分的比例
        model.setScoreHighRate(getDecimalFormat(above4Count, totalEvaluates));
    }

    private void handleTask(MeetingAttendantTaskTopCountModel model, MeetingAttendantTaskCountParam param){
        List<MeetingAttendantTaskModel> tasks = this.getBaseMapper().findTask(param);
        if(CollectionUtil.isEmpty(tasks)){
            return;
        }
        List<MeetingAttendantTaskModel> validTasks = tasks.stream().filter(task -> ObjectUtil.isNotEmpty(task.getHandleUid()) && MeetingAttendantTaskStatusEnum.COMPLETE.getValue().equals(task.getServiceStatus())).collect(Collectors.toList());
        List<MeetingAttendantTaskModel> expireTasks = tasks.stream().filter(task -> Status.disabled.getKey().equals(task.getServiceValid())).collect(Collectors.toList());
        long validTasksCount = ObjectUtil.isEmpty(validTasks) ? 0L : validTasks.size();
        long expireTasksCount = ObjectUtil.isEmpty(expireTasks) ? 0L : expireTasks.size();
        long totalTasksCount = validTasksCount + expireTasksCount;
        //会服完成的比例
        model.setTaskCompleteRate(getDecimalFormat(validTasksCount, totalTasksCount));
        //会服过期的比例
        model.setTaskExpireRate(getDecimalFormat(expireTasksCount, totalTasksCount));
        if(CollectionUtil.isNotEmpty(validTasks)){
            //提供的会服次数
            model.setAttendantTaskNum(validTasksCount);
            //服务的会议场次
            model.setMeetingNum(validTasks.stream().map(MeetingAttendantTaskModel::getReserveId).distinct().count());
            // 根据服务类型分组并计算每个组的数量
            Map<Integer, Long> validTaskCountByServiceType = validTasks.stream().collect(Collectors.groupingBy(MeetingAttendantTaskModel::getServiceType, Collectors.counting()));
            Map<Integer, String> validTaskPercentageByServiceType = validTaskCountByServiceType.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> getDecimalFormat(entry.getValue(), (long) validTasks.size())
                    ));
            if(validTaskPercentageByServiceType.containsKey(MeetingAttendantTaskTypeEnum.BEFORE.getValue())){
                //会前布置的比例
                model.setTaskBeforeRate(validTaskPercentageByServiceType.get(MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
            }
            if(validTaskPercentageByServiceType.containsKey(MeetingAttendantTaskTypeEnum.IN.getValue())){
                //会中呼叫的比例
                model.setTaskInRate(validTaskPercentageByServiceType.get(MeetingAttendantTaskTypeEnum.IN.getValue()));
            }
            if(validTaskPercentageByServiceType.containsKey(MeetingAttendantTaskTypeEnum.AFTER.getValue())){
                //会后清洁的比例
                model.setTaskAfterRate(validTaskPercentageByServiceType.get(MeetingAttendantTaskTypeEnum.AFTER.getValue()));
            }
        }
    }

    private String getDecimalFormat(Long value1, Long value2){
        if(value1 == 0l){
            return "0.00%";
        }
        // 计算每个组的占比并格式化为保留两位小数的字符串
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(new BigDecimal(value1).multiply(new BigDecimal(100)).divide(new BigDecimal(value2), 2, RoundingMode.HALF_UP)) + "%";
    }

    private String getDecimalFormat(Double value){
        if(value == 0l){
            return "0.00";
        }
        // 使用 BigDecimal 四舍五入保留两位小数
        BigDecimal bd = new BigDecimal(value.toString());
        bd = bd.setScale(2, RoundingMode.HALF_UP); // 四舍五入
        // 计算每个组的占比并格式化为保留两位小数的字符串
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(bd);
    }

    public static void main(String[] args) {
        Date lastStart = DateUtil.offset(DateUtil.parse("2024-4-30 00:00:00", "yyyy-MM-dd HH:mm:ss"), DateField.MONTH, -1);
        Date lastStart1 =  DateUtil.endOfMonth(lastStart);
        Date date1 = DateUtil.parse("2024-10-30 00:00:00","yyyy-MM-dd HH:mm:ss");
        Date beginQuarter =  DateUtil.beginOfQuarter(date1);
        Date endQuarter =  DateUtil.endOfQuarter(date1);
        System.out.println(DateUtil.format(lastStart, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.format(lastStart1, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.format(beginQuarter, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.format(endQuarter, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd"));
        System.out.println(beginQuarter.equals(endQuarter));
    }
}
