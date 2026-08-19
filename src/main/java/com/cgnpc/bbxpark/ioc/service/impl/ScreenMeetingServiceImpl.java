package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceInfo;
import com.cgnpc.bbxpark.acl.iot.service.IotCapacityService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.MeetingAttendantTaskStatusEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.enums.WorkOrderTypeEnum;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.DeviceScreenRecord;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupRelModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupRelParam;
import com.cgnpc.bbxpark.device.mapper.*;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.MeetingPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.MeetingRoomParam;
import com.cgnpc.bbxpark.ioc.service.IScreenMeetingService;
import com.cgnpc.bbxpark.meeting.domain.*;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountDetailPageParam;
import com.cgnpc.bbxpark.meeting.mapper.*;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantService;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderDeviceRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:12
 */
@Service
public class ScreenMeetingServiceImpl implements IScreenMeetingService {
    @Autowired
    private ThirdMeetingRecordRepository meetingRecordRepository;
    @Autowired
    private MeetingReserveRepository meetingReserveRepository;
    @Autowired
    private MeetingAttendantTaskRepository meetingAttendantTaskRepository;
    @Autowired
    private MeetingAttendantEvaluateRepository meetingAttendantEvaluateRepository;
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private MeetingRoomDeviceRelRepository meetingRoomDeviceRelRepository;
    @Autowired
    private MeetingRoomServiceRepository meetingRoomServiceRepository;
    @Autowired
    private MeetingServiceRepository meetingServiceRepository;
    @Autowired
    private IocDeviceRepository iocDeviceRepository;
    @Autowired
    private IMeetingAttendantService meetingAttendantService;
    @Autowired
    private MeetingReserveSignRepository meetingReserveSignRepository;
    @Autowired
    private ThirdMeetingRecordRepository thirdMeetingRecordRepository;
    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private ParkSpaceRepository parkSpaceRepository;
    @Autowired
    private DeviceEcStatisticsRepository deviceEcStatisticsRepository;
    @Autowired
    private DeviceScreenRecordRepository deviceScreenRecordRepository;
    @Autowired
    private AlarmDeviceRepository alarmDeviceRepository;
    @Autowired
    private AlarmInfoRepository alarmInfoRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkOrderDeviceRepository workOrderDeviceRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private DeviceGroupRelRepository deviceGroupRelRepository;
    @Autowired
    private IotCapacityService iotCapacityService;
    @Value("${bbx.device.group.light:LIGHT}")
    private String lightDeviceGroup;

    @Override
    public SpecialMeetingOverview getSpecialMeetingOverview() {
        SpecialMeetingOverview model = meetingRecordRepository.getSpecialMeetingOverview(WebFrameworkUtils.getHeaderTenantId());
        model.setSpecialRoomCount(74);
        model.setTotalRoomCount(97);
        return model;
    }

    @Override
    public List<MeetingRoomUse> getMeetingRoomUsageRanking(String sortOrder) {
        return meetingRecordRepository.getMeetingRoomUsageRanking(WebFrameworkUtils.getHeaderTenantId(), sortOrder);
    }

    @Override
    public MeetingBehaviorInsight getMeetingBehaviorInsight() {
        //预定义区间
        List<String> rangeLabels = Arrays.asList("<=1小时", "1-2小时", "2-3小时", ">3小时");
        Map<String, Long> map = meetingRecordRepository.getMeetingBehaviorInsight(WebFrameworkUtils.getHeaderTenantId());
        List<MeetingBehaviorInsight.DurationRange> ranges = new ArrayList<>();
        for (int i = 1; i <= rangeLabels.size(); i++) {
            MeetingBehaviorInsight.DurationRange range = new MeetingBehaviorInsight.DurationRange();
            range.setRange(rangeLabels.get(i - 1));
            range.setCount(map.get("r" + i).intValue());
            ranges.add(range);
        }
        int total = ranges.stream().mapToInt(MeetingBehaviorInsight.DurationRange::getCount).sum();
        MeetingBehaviorInsight model = new MeetingBehaviorInsight();
        model.setRanges(ranges);
        model.setWarningThreshold((int) (total * 0.3));
        return model;
    }

    @Override
    public DepartmentActivity getDepartmentActivity() {
        List<DepartmentActivity.DepartmentMeeting> list = meetingRecordRepository.getDepartmentActivity(WebFrameworkUtils.getHeaderTenantId());
        int total = list.stream().mapToInt(DepartmentActivity.DepartmentMeeting::getCount).sum();
        return DepartmentActivity.builder().activityThreshold((int)(total * 0.7)).departmentMeetings(list).build();
    }

    @Override
    public PilotMeetingOverview getPilotMeetingOverview() {
        PilotMeetingOverview model = new PilotMeetingOverview();
        model.setPilotRoomCount(23);
        model.setTotalRoomCount(97);
        //会议数量(有效&&未取消&&已结束)
        model.setTotalMeetingCount(meetingReserveRepository.selectCount(Wrappers.<MeetingReserve>lambdaQuery().eq(MeetingReserve::getInValidFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey()).eq(MeetingReserve::getStatus, 30).eq(MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId())));
        //会服提供次数
        model.setTotalServiceCount(meetingAttendantTaskRepository.selectCount(Wrappers.<MeetingAttendantTask>lambdaQuery()
                .eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue()).eq(MeetingAttendantTask::getTenantId, WebFrameworkUtils.getHeaderTenantId())));
        //会服整体满意度
        model.setAvgServiceScore(meetingAttendantEvaluateRepository.getAvgScore(WebFrameworkUtils.getHeaderTenantId()));
        return model;
    }

    @Override
    public List<ServiceStaffWorkload> getServiceWorkloadMatrix(String month) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Date date = df.parse(month);
            //获取会服人员数据
            List<MeetingAttendantModel> attendantModels = meetingAttendantService.findList();
            if (CollectionUtils.isEmpty(attendantModels)) {
                return Collections.emptyList();
            }
            List<String> userIds = attendantModels.stream().map(MeetingAttendantModel::getUserId).collect(Collectors.toList());
            //查询会服任务
            List<MeetingAttendantTask> tasks = meetingAttendantTaskRepository.selectList(Wrappers.<MeetingAttendantTask>lambdaQuery()
                    .eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
                    .between(MeetingAttendantTask::getHandleTime,DateUtil.beginOfMonth(date), DateUtil.endOfMonth(date))
                    .isNotNull(MeetingAttendantTask::getHandleUid).eq(MeetingAttendantTask::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
            Map<String, Long> attendantCountMap = tasks.stream().collect(Collectors.groupingBy(MeetingAttendantTask::getHandleUid, Collectors.counting()));
            //查询会服评价数据
            List<ServiceStaffWorkload> evaluates = meetingAttendantEvaluateRepository.findAvgScoreByAttendant(WebFrameworkUtils.getHeaderTenantId(), DateUtil.beginOfMonth(date), DateUtil.endOfMonth(date), userIds);
            Map<String, Double> attendantScoreMap = evaluates.stream().collect(Collectors.toMap(ServiceStaffWorkload::getStaffId, ServiceStaffWorkload::getAvgScore, (v1, v2) -> v2));
            return attendantModels.stream().map(a -> {
                ServiceStaffWorkload model = new ServiceStaffWorkload();
                model.setStaffId(a.getUserId());
                model.setStaffName(a.getUserName());
                model.setServiceCount(attendantCountMap.getOrDefault(a.getUserId(), 0L));
                model.setAvgScore(attendantScoreMap.getOrDefault(a.getUserId(), 0.0));
                return model;
            }).collect(Collectors.toList());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<DailyRoomHealth> getMeetingRoomHealth() {
        //获取会议室的空间id集合
        List<MeetingRoom> rooms = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery()
                .eq(MeetingRoom::getDeleted, Delete.NORMAL.getKey()).eq(MeetingRoom::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<Long> roomSpaceIds = rooms.stream().filter(r -> r.getSpaceId() != null).map(MeetingRoom::getSpaceId).collect(Collectors.toList());
        //获取会议室的设备id集合
        List<MeetingRoomDeviceRel> deviceRels = meetingRoomDeviceRelRepository.selectList(Wrappers.<MeetingRoomDeviceRel>lambdaQuery()
                .in(MeetingRoomDeviceRel::getRoomId, rooms.stream().map(MeetingRoom::getId).collect(Collectors.toList()))
                .eq(MeetingRoomDeviceRel::getDeleted, Delete.NORMAL.getKey()).eq(MeetingRoomDeviceRel::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<Long> roomDeviceIds = deviceRels.stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
        //获取智能化设备id集合
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery()
                .ne(IocDevice::getIotDevicePlatform, 0)
                .eq(IocDevice::getDeleted, Delete.NORMAL.getKey()).eq(IocDevice::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<Long> deviceIds = devices.stream().map(IocDevice::getId).collect(Collectors.toList());

        //获取一周内的会议数据 (有效&&未取消)
//        List<MeetingReserve> meetings = meetingReserveRepository.selectList(Wrappers.<MeetingReserve>lambdaQuery()
//                .between(MeetingReserve::getRealStartTime,startTime,endTime)
//                .eq(MeetingReserve::getInValidFlag, Status.disabled.getKey()).eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
//                .eq(MeetingReserve::getDeleted, Delete.NORMAL.getKey()).eq(MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<WeekRoomHealth> meetingHealths = meetingReserveRepository.getMeetingRoomHealth(WebFrameworkUtils.getHeaderTenantId());
        //获取一周内会议室设备的告警次数
//        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getDeviceId, roomDeviceIds)
//                .between(AlarmDevice::getCreateTime,startTime,endTime).eq(AlarmDevice::getDeleted, Delete.NORMAL.getKey())
//                .eq(AlarmDevice::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
//        List<Long> alarmIds = alarmDevices.stream().map(AlarmDevice::getAlarmId).collect(Collectors.toList());
//        List<AlarmInfo> alarms = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().in(AlarmInfo::getId,alarmIds)
//                .eq(AlarmInfo::getDeleted, Delete.NORMAL.getKey()).eq(AlarmInfo::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
        List<WeekRoomHealth> alarmHealths = alarmInfoRepository.getMeetingRoomHealth(WebFrameworkUtils.getHeaderTenantId(), roomDeviceIds);
        //获取会议室及设备的工单(报事报修、告警)数量
//        List<WorkOrder> workOrders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery()
//                .in(WorkOrder::getType,Arrays.asList(WorkOrderTypeEnum.REPAIR.getCode(),WorkOrderTypeEnum.DEVICEALARM.getCode()))
//                .between(WorkOrder::getCreateTime,startTime,endTime)
//                .eq(WorkOrder::getDeleted, Delete.NORMAL.getKey()).eq(WorkOrder::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
//        List<Long> workOrderIds = workOrders.stream().map(WorkOrder::getId).collect(Collectors.toList());
//        List<WorkOrderDevice> workOrderDevices = workOrderDeviceRepository.selectList(Wrappers.<WorkOrderDevice>lambdaQuery()
//                .in(WorkOrderDevice::getWorkOrderId,workOrderIds).and(wrapper->wrapper.in(WorkOrderDevice::getDeviceId,roomDeviceIds).or().in(WorkOrderDevice::getSpaceId,roomSpaceIds))
//                .eq(WorkOrderDevice::getDeleted, Delete.NORMAL.getKey()).eq(WorkOrderDevice::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
//        List<Long> validWorkOrderIds = workOrderDevices.stream().map(WorkOrderDevice::getDeviceId).collect(Collectors.toList());
//        workOrders = workOrders.stream().filter(w->validWorkOrderIds.contains(w.getId())).collect(Collectors.toList());
        List<WeekRoomHealth> workOrderHealths = workOrderRepository.getMeetingRoomHealth(WebFrameworkUtils.getHeaderTenantId(), Arrays.asList(WorkOrderTypeEnum.REPAIR.getCode(), WorkOrderTypeEnum.DEVICEALARM.getCode()), roomDeviceIds, roomSpaceIds);
        //获取会议室智能化设备数量
        roomDeviceIds.retainAll(deviceIds);
        //服务次数
//        List<MeetingAttendantTask> list = meetingAttendantTaskRepository.selectList(Wrappers.<MeetingAttendantTask>lambdaQuery()
//                .eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
//                .between(MeetingAttendantTask::getHandleTime,startTime,endTime).eq(MeetingAttendantTask::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<WeekRoomHealth> serviceHealths = meetingAttendantTaskRepository.getMeetingRoomHealth(WebFrameworkUtils.getHeaderTenantId(), MeetingAttendantTaskStatusEnum.COMPLETE.getValue());
        return combineDailyStats(meetingHealths, workOrderHealths, alarmHealths, serviceHealths, roomDeviceIds.size());
    }

    @Override
    public List<DailySignAnalysis> getEmployeeAttendance() {
        List<DailySignAnalysis> list = meetingReserveSignRepository.getEmployeeAttendance(WebFrameworkUtils.getHeaderTenantId());
        list.forEach(d -> d.setAbnormalSignRate(100 - d.getNormalSignRate()));
        return list;
    }

    @Override
    public EnergySavingOverview getEnergySavingOverview() {
        EnergySavingOverview model = deviceEcStatisticsRepository.getEnergySavingOverview(WebFrameworkUtils.getHeaderTenantId());
        model.setExecutionCount(deviceScreenRecordRepository.selectCount(Wrappers.<DeviceScreenRecord>lambdaQuery().eq(DeviceScreenRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId())));
        model.setPilotRoomCount(23);
        model.setTotalRoomCount(97);
        model.setPilotRoomRatio(BigDecimal.valueOf(23 * 100.0 / 97).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return model;
    }

    @Override
    public List<DailyEnergyTrend> getDailyEnergyTrend() {
        return deviceEcStatisticsRepository.getDailyEnergyTrend(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<ExecutionTrend> getExecutionTrend() {
        return deviceScreenRecordRepository.getExecutionTrend(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<RoomEnergyRank> getRoomEnergyRanking(String sortOrder) {
        return deviceEcStatisticsRepository.getRoomEnergyRanking(WebFrameworkUtils.getHeaderTenantId(), sortOrder);
    }

    @Override
    public MeetingGuaranteeOverview getMeetingGuaranteeOverview() {
        return MeetingGuaranteeOverview.builder().preCheckItemCount(3233).meetingRoomCount(45).meetingRoomRatio(54.78)
                .preCheckExecuteCount(45345).executeCompleteRatio(74.78).build();
    }

    @Override
    public List<AbnormalCheckItem> getAbnormalCheckItems() {
        List<AbnormalCheckItem> list = new ArrayList<>();
        list.add(new AbnormalCheckItem("视频会议","W1416",new Date(),"工单处理中"));
        list.add(new AbnormalCheckItem("投影正常打开","W1416",new Date(),"已恢复"));
        return list;
    }

    @Override
    public MeetingServiceOverview getMeetingServiceOverview() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        MeetingServiceOverview model = new MeetingServiceOverview();
        //会服项目数量
        model.setServiceItemCount(meetingServiceRepository.selectCount(Wrappers.<MeetingService>lambdaQuery().eq(MeetingService::getDeleted, Delete.NORMAL.getKey()).eq(MeetingService::getTenantId, tenantId)));
        //会议室
        List<MeetingRoom> rooms = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery()
                .eq(MeetingRoom::getDeleted, Delete.NORMAL.getKey()).eq(MeetingRoom::getTenantId, tenantId).select(MeetingRoom::getId));
        double meetingRoomRatio = 0.0;
        if (CollectionUtils.isEmpty(rooms)) {
            model.setMeetingRoomCount(0);
        } else {
            List<Long> roomIds = rooms.stream().map(MeetingRoom::getId).collect(Collectors.toList());
            //有会服的会议室
            List<MeetingRoomService> roomServices = meetingRoomServiceRepository.selectList(Wrappers.<MeetingRoomService>lambdaQuery()
                    .in(MeetingRoomService::getRoomId, roomIds).eq(MeetingRoomService::getDeleted, Delete.NORMAL.getKey())
                    .eq(MeetingRoomService::getTenantId, tenantId).select(MeetingRoomService::getRoomId));
            Set<Long> roomServiceIds = roomServices.stream().map(MeetingRoomService::getRoomId).collect(Collectors.toSet());
            meetingRoomRatio = roomServiceIds.size() * 100.0 / roomIds.size();
            model.setMeetingRoomCount(roomServiceIds.size());
        }
        model.setMeetingRoomRatio(BigDecimal.valueOf(meetingRoomRatio).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //会服完成提供次数
        Integer completeCount = meetingAttendantTaskRepository.selectCount(Wrappers.<MeetingAttendantTask>lambdaQuery()
                .eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue()).eq(MeetingAttendantTask::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        //会服总次数
        Integer totalCount = meetingAttendantTaskRepository.selectCount(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        double serviceCompleteRatio = totalCount == 0 ? 0.0 : completeCount * 100.0 / totalCount;
        model.setServiceProvideCount(completeCount);
        model.setServiceCompleteRatio(BigDecimal.valueOf(serviceCompleteRatio).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return model;
    }

    @Override
    public List<MeetingEvaluation> getMeetingEvaluationList() {
        MeetingAttendantTaskCountDetailPageParam param = new MeetingAttendantTaskCountDetailPageParam();
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        IPage<MeetingAttendantEvaluateDetailModel> page = meetingAttendantEvaluateRepository.pageByUserId(new Page<>(1, 20), param);
        return page.getRecords().stream().map(e -> {
            MeetingEvaluation evaluation = new MeetingEvaluation();
            evaluation.setId(e.getId());
            evaluation.setMeetingName(e.getReserveName());
            evaluation.setStaffId(e.getHandleUid());
            evaluation.setStaffName(e.getHandleUname());
            evaluation.setScore(e.getScore());
            evaluation.setEvaluateTime(e.getCreateTime());
            return evaluation;
        }).collect(Collectors.toList());
    }

    @Override
    public MeetingEvaluationDetail getMeetingEvaluationDetail(String evaluationId) {
        //查询会服评价信息
        MeetingAttendantEvaluate evaluate = meetingAttendantEvaluateRepository.selectById(evaluationId);
        AssertUtils.notNull(evaluate, SystemResultCode.RESULT_DATA_NONE.message());
        //查询会服任务信息
        List<MeetingAttendantTask> tasks = meetingAttendantTaskRepository.selectList(Wrappers.<MeetingAttendantTask>lambdaQuery()
                .eq(MeetingAttendantTask::getReserveId, evaluate.getReserveId())
                .eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
                .isNotNull(MeetingAttendantTask::getHandleUid));
        MeetingEvaluationDetail model = new MeetingEvaluationDetail();
        model.setScore(evaluate.getScore());
        model.setContent(evaluate.getContent());
        model.setEvaluateTime(evaluate.getCreateTime());
        if (CollectionUtils.isNotEmpty(tasks)) {
            model.setMeetingName(tasks.get(0).getReserveName());
            model.setRoomName(tasks.get(0).getRoomName());
            model.setStaffId(tasks.get(0).getHandleStaffid());
            model.setStaffName(tasks.get(0).getHandleUname());
        }
        return model;
    }

    @Override
    public IPage<MeetingModel> list(MeetingPageParam param) {
        if ("special".equals(param.getType())) {
            //集团会议数据
            return findSpecialMeeting(param);
        } else {
            return findPilotMeeting(param);
        }
    }

    @Override
    public List<MeetingRoomModel> roomList(MeetingRoomParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        if (StringUtils.isNotEmpty(param.getSslcCode())) {
            ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode, param.getSslcCode()).eq(ParkSpace::getTenantId, tenantId));
            param.setSpaceId(parkSpace.getId());
        }
        //先查询所有的会议室信息
        List<MeetingRoom> list = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery()
                .eq(MeetingRoom::getDeleted, Delete.NORMAL.getKey()).eq(MeetingRoom::getTenantId, tenantId));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        //会议室空间信息
        List<Long> spaceIds = list.stream().map(MeetingRoom::getSpaceId).collect(Collectors.toList());
        if (param.getSpaceId() != null) {
            //根据空间id筛选出符合条件的下级空间id
            spaceIds = findDescendantSpaceIds(spaceIds, param.getSpaceId());
        }
        List<Long> finalSpaceIds = spaceIds;
        if(CollectionUtils.isEmpty(finalSpaceIds)){
            return Collections.emptyList();
        }

        List<ParkSpace> spaces = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().in(ParkSpace::getId, finalSpaceIds));
        Map<Long, String> spaceMap = spaces.stream().filter(s->StringUtils.isNotEmpty(s.getSslcCode())).collect(Collectors.toMap(ParkSpace::getId, ParkSpace::getSslcCode, (v1, v2) -> v2));
        //获取智能化设备id集合
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery().ne(IocDevice::getIotDevicePlatform, 0)
                .eq(IocDevice::getDeleted, Delete.NORMAL.getKey()).eq(IocDevice::getTenantId, tenantId));
        Set<Long> deviceIds = devices.stream().map(IocDevice::getId).collect(Collectors.toSet());
        Map<String,Long> deviceDnMap = devices.stream().collect(Collectors.toMap(IocDevice::getIotDeviceDn,IocDevice::getId,(v1,v2)->v2));

        //获取会议室的设备id集合
        List<MeetingRoomDeviceRel> deviceRels = meetingRoomDeviceRelRepository.selectList(Wrappers.<MeetingRoomDeviceRel>lambdaQuery()
                .in(MeetingRoomDeviceRel::getRoomId, list.stream().filter(r->finalSpaceIds.contains(r.getSpaceId())).map(MeetingRoom::getId).collect(Collectors.toList()))
                .in(MeetingRoomDeviceRel::getDeviceId,deviceIds)
                .eq(MeetingRoomDeviceRel::getDeleted, Delete.NORMAL.getKey()).eq(MeetingRoomDeviceRel::getTenantId, tenantId));
        //每个会议室的智能化设备数量
        Map<Long,Long> deviceCountMap = deviceRels.stream().filter(d->deviceIds.contains(d.getDeviceId())).collect(Collectors.groupingBy(MeetingRoomDeviceRel::getRoomId,Collectors.counting()));
        //会议室绑定的全部智能化设备id集合
        List<Long> roomDeviceIds = deviceRels.stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
        //roomDeviceIds.retainAll(deviceIds);
        //获取全部灯光设备id
        Set<Long> allLightDeviceIds = getLightDeviceIds();
        //会议室绑定的灯光设备id
        roomDeviceIds.retainAll(allLightDeviceIds);
        //批量获取灯光设备的属性
        List<String> roomDeviceDns = devices.stream().filter(d->roomDeviceIds.contains(d.getId())).map(IocDevice::getIotDeviceDn).collect(Collectors.toList());
        List<IotDeviceInfo> iotDeviceInfos = iotCapacityService.queryDeviceProperties(roomDeviceDns,tenantId);
        //开灯的设备id
        Set<Long> openLightDeviceIds = new HashSet<>();
        iotDeviceInfos.stream().filter(d->deviceDnMap.containsKey(d.getDeviceId())).forEach(d->{
            Map<String,Object> map = d.getPropertys();
            if("1".equals(map.get("switch1")) || "1".equals(map.get("switch2")) || "1".equals(map.get("switch3"))){
                openLightDeviceIds.add(deviceDnMap.get(d.getDeviceId()));
            }
        });
        //开灯的会议室
        Set<Long> openLightRoomIds = deviceRels.stream().filter(d->openLightDeviceIds.contains(d.getDeviceId())).map(MeetingRoomDeviceRel::getRoomId).collect(Collectors.toSet());

        //会议室开关状态(获取会议室绑定的指定分组的设备的开关属性状态)
        return list.stream().filter(r -> finalSpaceIds.contains(r.getSpaceId())).map(r -> {
            MeetingRoomModel model = BeanUtils.convertTo(r, MeetingRoomModel::new);
            model.setSslcCode(spaceMap.getOrDefault(r.getSpaceId(), ""));
            model.setLightStatus(openLightRoomIds.contains(r.getId()) ? 1 : 0);
            model.setDeviceCount(deviceCountMap.getOrDefault(r.getId(),0L));
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SimpleMeetingRoomModel> simpleRoomList() {
        List<MeetingRoom> list = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery()
                .eq(MeetingRoom::getDeleted, Delete.NORMAL.getKey()).eq(MeetingRoom::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        return list.stream().map(r -> BeanUtils.convertTo(r, SimpleMeetingRoomModel::new)).collect(Collectors.toList());
    }

    @Override
    public List<SpaceViewModel> getSpaceView(String sslcCode) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<String> sslcCodeList;
        if(ObjectUtil.isEmpty(sslcCode)){
            sslcCodeList = Arrays.asList("东塔", "西塔");
        }else{
            sslcCodeList = Collections.singletonList(sslcCode);
        }
        List<ParkSpace> parkSpaces = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().in(ParkSpace::getSslcCode, sslcCodeList).eq(ParkSpace::getTenantId, tenantId));
        if (CollectionUtils.isEmpty(parkSpaces)) {
            return Collections.emptyList();
        }
        //查询下级空间
        List<ParkSpace> children = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().in(ParkSpace::getParentSpaceId, parkSpaces.stream().map(ParkSpace::getId).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(children)) {
            return Collections.emptyList();
        }
        //查询会议室的空间
        List<MeetingRoom> rooms = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery()
                .eq(MeetingRoom::getDeleted, Delete.NORMAL.getKey()).eq(MeetingRoom::getTenantId, tenantId));
        List<Long> spaceIds = rooms.stream().map(MeetingRoom::getSpaceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(spaceIds)) {
            return Collections.emptyList();
        }
        //获取会议室相关的空间路径
        Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIds, tenantId);
        Set<Long> spaceIdSet = spaceMap.values().stream().map(ParkSpaceFullModel::getIdFullPath)
                .filter(Objects::nonNull).filter(p -> !p.isEmpty()).flatMap(p -> Arrays.stream(p.split("-")))
                .map(Long::valueOf).collect(Collectors.toSet());
        //组装数据
        return children.stream().filter(s -> spaceIdSet.contains(s.getId())).map(s -> {
            SpaceViewModel spaceModel = new SpaceViewModel();
            spaceModel.setId(s.getId());
            spaceModel.setName(s.getSpaceName());
            spaceModel.setSslcCode(s.getSslcCode());
            return spaceModel;
        }).collect(Collectors.toList());
    }

    /**
     * 分页获取集团会议数据
     *
     * @param param 参数
     * @return 数据
     */
    private IPage<MeetingModel> findSpecialMeeting(MeetingPageParam param) {
        IPage<ThirdMeetingRecord> page = thirdMeetingRecordRepository.selectPage(param.getPage(), Wrappers.<ThirdMeetingRecord>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getMeetingName()), ThirdMeetingRecord::getConferName, param.getMeetingName())
                .eq(ThirdMeetingRecord::getDeleted, Delete.NORMAL.getKey()).eq(ThirdMeetingRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<MeetingModel> list = page.getRecords().stream().map(m -> {
            MeetingModel model = new MeetingModel();
            model.setId(m.getId() + "");
            model.setMeetingName(m.getConferName());
            model.setRoomName(m.getRoomName().substring(m.getRoomName().indexOf("-") + 1));
            model.setRoomLocation(m.getRoomName().substring(0, m.getRoomName().indexOf("-")));
            model.setActualStartTime(m.getStartTime());
            model.setActualEndTime(m.getEndTime());
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page, list);
    }

    /**
     * 分页获取会议数据
     *
     * @param param 参数
     * @return 数据
     */
    private IPage<MeetingModel> findPilotMeeting(MeetingPageParam param) {
        IPage<MeetingReserve> page = meetingReserveRepository.selectPage(param.getPage(), Wrappers.<MeetingReserve>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getMeetingName()), MeetingReserve::getReserveName, param.getMeetingName())
                .eq(param.getRoomId() != null, MeetingReserve::getRoomId, param.getRoomId())
                .eq(MeetingReserve::getDeleted, Delete.NORMAL.getKey()).eq(MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        //获取会议室id及会议室对应的空间数据
        List<Long> roomIds = page.getRecords().stream().map(MeetingReserve::getRoomId).collect(Collectors.toList());
        List<MeetingRoom> rooms = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery().in(MeetingRoom::getId, roomIds));
        Map<Long, Long> roomSpaceMap = rooms.stream().collect(Collectors.toMap(MeetingRoom::getId, MeetingRoom::getSpaceId, (v1, v2) -> v2));
        List<Long> spaceIds = rooms.stream().map(MeetingRoom::getSpaceId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIds, WebFrameworkUtils.getHeaderTenantId());
        //组装
        List<MeetingModel> list = page.getRecords().stream().map(m -> {
            MeetingModel model = new MeetingModel();
            model.setId(m.getId() + "");
            model.setMeetingName(m.getReserveName());
            model.setRoomName(m.getRoomName());
            model.setRoomLocation(spaceMap.getOrDefault(roomSpaceMap.getOrDefault(m.getRoomId(), 0L), new ParkSpaceFullModel()).getFullPath());
            model.setActualStartTime(m.getStartTime());
            model.setActualEndTime(m.getEndTime());
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page, list);
    }

    private List<DailyRoomHealth> combineDailyStats(List<WeekRoomHealth> meetings, List<WeekRoomHealth> workOrders, List<WeekRoomHealth> alarms, List<WeekRoomHealth> services, Integer totalDeviceCount) {
        // 生成本周周一至周日的日期列表
        Date now = new Date();
        Date monday = DateUtil.beginOfWeek(now); // 周一 00:00:00
        Date sunday = DateUtil.endOfWeek(now);   // 周日 23:59:59，但我们需要日期部分，所以用beginOfDay
        // 生成从周一到周日的日期（只取日期部分）
        List<Date> weekDays = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDays.add(DateUtil.offsetDay(monday, i)); // i=0周一, i=6周日
        }

        // 将各统计列表转换为Map<日期, 计数>
        Map<Date, Integer> meetingMap = convertToMap(meetings);
        Map<Date, Integer> workOrderMap = convertToMap(workOrders);
        Map<Date, Integer> alarmMap = convertToMap(alarms);
        Map<Date, Integer> serviceMap = convertToMap(services);

        // 定义星期几的名称
        String[] weekdays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        List<DailyRoomHealth> result = new ArrayList<>();
        for (int i = 0; i < weekDays.size(); i++) {
            Date day = weekDays.get(i);
            result.add(DailyRoomHealth.builder()
                    .weekday(weekdays[i]).deviceCount(totalDeviceCount).selfCheckCount(0)
                    .meetingCount(meetingMap.getOrDefault(day, 0))
                    .workOrderCount(workOrderMap.getOrDefault(day, 0))
                    .alarmCount(alarmMap.getOrDefault(day, 0))
                    .serviceCount(serviceMap.getOrDefault(day, 0)).build());
        }
        return result;
    }

    private Map<Date, Integer> convertToMap(List<WeekRoomHealth> stats) {
        if (stats == null) return new HashMap<>();
        return stats.stream().collect(Collectors.toMap(WeekRoomHealth::getDate, WeekRoomHealth::getCount, (v1, v2) -> v2));
    }

    /**
     * 根据传入的空间id筛选出符合条件下级空间id集合
     * @param spaceIds
     * @param spaceId
     * @return
     */
    private List<Long> findDescendantSpaceIds(List<Long> spaceIds, Long spaceId) {
        Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIds, WebFrameworkUtils.getHeaderTenantId());
        return spaceMap.entrySet().stream().filter(entry -> {
            String idFullPath = entry.getValue().getIdFullPath();
            if (StringUtils.isEmpty(idFullPath)) {
                return false;
            }
            String[] ids = idFullPath.split("-");
            return IntStream.range(0, ids.length).filter(i -> ids[i].equals(String.valueOf(spaceId)))
                    .anyMatch(i -> i < ids.length - 1);
        }).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * 获取灯光面板设备id集合
     * @return id集合
     */
    private Set<Long> getLightDeviceIds(){
        //查询灯光面板设备
        DeviceGroupRelParam relParam = new DeviceGroupRelParam();
        relParam.setGroupCode(lightDeviceGroup);
        relParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<DeviceGroupRelModel> relModels = deviceGroupRelRepository.findDevices(relParam);
        if(CollectionUtil.isEmpty(relModels)){
            return Collections.EMPTY_SET;
        }
        return relModels.stream().map(DeviceGroupRelModel::getDeviceId).collect(Collectors.toSet());
    }
}

