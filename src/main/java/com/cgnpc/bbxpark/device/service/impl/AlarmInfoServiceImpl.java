package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.*;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.*;
import com.cgnpc.bbxpark.device.dto.model.*;
import com.cgnpc.bbxpark.device.dto.param.AlarmHandleRecordParam;
import com.cgnpc.bbxpark.device.dto.param.AlarmIgnoreConfigParam;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupRelParam;
import com.cgnpc.bbxpark.device.mapper.*;
import com.cgnpc.bbxpark.device.service.*;
import com.cgnpc.bbxpark.ioc.dto.model.ScreenAlarmMessageModel;
import com.cgnpc.bbxpark.ioc.service.WebSocketService;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.domain.UserSpace;
import com.cgnpc.bbxpark.space.dto.kafka.ThingModelKaFkaMessage;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.mapper.UserSpaceRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderRoman;
import com.cgnpc.bbxpark.workorder.dto.param.WorkScheduleUserParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderDeviceRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRomanRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkScheduleUserService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import groovy.lang.Lazy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlarmInfoServiceImpl extends BaseServiceImpl<AlarmInfoRepository, AlarmInfo> implements IAlarmInfoService {
    @Autowired
    private AlarmInfoRepository alarmInfoRepository;
    @Autowired
    private IAlarmIgnoreConfigService alarmIgnoreConfigService;
    @Autowired
    private IDeviceInfoService deviceInfoService;
    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private IAlarmHandleRecordService alarmHandleRecordService;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private ITenantMemberService tenantMemberService;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private WorkOrderDeviceRepository workOrderDeviceRepository;
    @Autowired
    @Lazy
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private IMessageCommonService messageCommonService;

    @Autowired
    private IIotDeviceRelationService iotDeviceRelationService;

    @Autowired
    private WorkOrderRomanRepository workOrderRomanRepository;

    @Autowired
    private IocDeviceRepository iIocDeviceService;

    @Autowired
    private DeviceGroupRelRepository deviceGroupRelRepository;
    @Autowired
    private AlarmDeviceRepository alarmDeviceRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @Autowired
    private AlarmLevelMapRepository alarmLevelMapRepository;
    @Autowired
    private IRoleApiService roleApiService;
    @Value("${bbx.role.system:1}")
    private String systemRoleCode;
    @Value("${ioc.kafka.noticeLevel:1}")
    private String noticeLevel;
    @Value("${bbx.device.group.af:ZHAFJK}")
    private String afDeviceGroup;

    @Autowired
    private IPropertyScheduleService propertyScheduleService;

    @Autowired
    private IWorkScheduleUserService workScheduleUserService;

    @Autowired
    private IConfigInfoService configInfoService;
    @Autowired
    private WebSocketService webSocketService;

    /**
     * 当前登录用户能查询的所有设备列表
     *
     * @return
     */
    @Override
    public List<String> getDeviceIdList(String deviceName) {
        String userId = userApiService.getCurrentStaffNo();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        TenantMemberListParam param1 = new TenantMemberListParam();
        param1.setTenantId(tenantId);
        param1.setUserId(userId);
        List<TenantMemberDomain> domainList = tenantMemberService.list(param1);
        AssertUtils.notEmpty(domainList,"当前园区下用户不存在");
        TenantMemberDomain domain = domainList.get(0);

        LambdaQueryWrapper<DeviceInfo> query = new LambdaQueryWrapper<>();
        // 如果是园区管理员
        if (domain.getIdentity() != null && domain.getIdentity() == 1) {
            query.eq(DeviceInfo::getTenantId, tenantId);
        } else {
            query.apply(" space_id in (select space_id from bbx_user_space where tenant_id = {0} and user_id = {1} )", tenantId, userId);
        }
        if (StringUtils.isNotEmpty(deviceName)) {
            if (tenantId != null) {
                query.eq(DeviceInfo::getTenantId, tenantId);
            }
            query.and(w -> w.like(DeviceInfo::getName, deviceName)
                            .or(w1 -> w1.like(DeviceInfo::getDeviceAlias, deviceName)));
        }
        query.select(DeviceInfo::getDeviceId, DeviceInfo::getDeviceName, DeviceInfo::getName);

        List<DeviceInfo> deviceInfoList = deviceInfoRepository.selectList(query);
        return deviceInfoList.stream().map(DeviceInfo::getDeviceId).collect(Collectors.toList());
    }

    /**
     * 权限：
     * 如果是管理员 则也需要查询分给自己的权限 和 设备空间id为空的记录
     * 如果是普通用户 只查询 自己空间下的设备
     *
     * @param param
     * @return
     */
    @Override
    public IPage<AlarmInfoModel> pageAlarmInfoModel(AlarmInfoParam param) {
        IPage<AlarmInfo> page = new Page<>(param.getCurrent(), param.getSize());
        // todo 需要根据人员权限查询 设备下的日志
        LambdaQueryWrapper<AlarmInfo> queryWrapper = handlePublicQuery(param);
        queryWrapper.in(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_1, AlarmInfoConstant.ALARM_STATUS_2);
        IPage<AlarmInfo> pageResult = this.page(page, queryWrapper);
        List<AlarmInfoModel> list = BeanUtils.convertListTo(pageResult.getRecords(),AlarmInfoModel::new);
        buildAlarmInfoModel(list);
        return ConvertUtil.pageConvert(pageResult,list);
    }

    @Override
    public IPage<AlarmInfoModel> pageHisAlarmInfoModel(AlarmInfoParam param) {
        IPage<AlarmInfo> page = new Page<>(param.getCurrent(), param.getSize());
        // todo 需要根据人员权限查询 设备下的日志
        LambdaQueryWrapper<AlarmInfo> queryWrapper = handlePublicQuery(param);
        queryWrapper.eq(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3);
        IPage<AlarmInfo> pageResult = this.page(page, queryWrapper);
        List<AlarmInfoModel> list = BeanUtils.convertListTo(pageResult.getRecords(),AlarmInfoModel::new);
        buildAlarmInfoModel(list);
        return ConvertUtil.pageConvert(pageResult,list);
    }

    @Override
    public IPage<AlarmInfoModel> pageVideoAlarmInfoModel(AlarmInfoParam param) {
        param.setDeviceIdList(getVideoDeviceIds());
        return this.pageAlarmInfoModel(param);
    }

    @Override
    public IPage<AlarmInfoModel> pageVideoHisAlarmInfoModel(AlarmInfoParam param) {
        param.setDeviceIdList(getVideoDeviceIds());
        return this.pageHisAlarmInfoModel(param);
    }
    private List<Long> getVideoDeviceIds(){
        //查询视频设备列表
        DeviceGroupRelParam relParam = new DeviceGroupRelParam();
        relParam.setGroupCode(afDeviceGroup);
        relParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<DeviceGroupRelModel> relModels = deviceGroupRelRepository.findDevices(relParam);
        if(CollectionUtil.isEmpty(relModels)){
            return new ArrayList<>();
        }
        return relModels.stream().map(DeviceGroupRelModel::getDeviceId).collect(Collectors.toList());
    }
    @Override
    public AlarmInfoModel detail(Long id) {
        AlarmInfo info = this.getById(id);
        if (info == null) {
            return null;
        }
        AlarmInfoModel model = BeanUtils.convertTo(info, AlarmInfoModel::new);
        //大屏使用的字段
        model.setDeviceName(model.getAlarmDevice());
        List<AlarmHandleRecord> handleRecords = alarmHandleRecordService.list(new LambdaQueryWrapper<AlarmHandleRecord>()
                .eq(AlarmHandleRecord::getAlarmId, model.getId()).orderByAsc(AlarmHandleRecord::getCreateTime));
        List<AlarmHandleRecordModel> handleRecordModelList = BeanUtils.convertListTo(handleRecords, AlarmHandleRecordModel::new);

        buildUserName(handleRecordModelList);
        model.setHandleRecordModelList(handleRecordModelList);

        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(new LambdaQueryWrapper<AlarmDevice>()
                .eq(AlarmDevice::getAlarmId, model.getId()).orderByAsc(AlarmDevice::getCreateTime));
        List<Long> deviceIds = alarmDevices.stream().map(AlarmDevice::getDeviceId).collect(Collectors.toList());

        List<IotDeviceRelation> relations = iotDeviceRelationService.list(Wrappers.<IotDeviceRelation>lambdaQuery().in(IotDeviceRelation::getDeviceId, deviceIds).eq(IotDeviceRelation::getRelationType, "video"));
        if (CollectionUtils.isEmpty(relations)){
            model.setHaveVideo(Constants.ISORNOT_NO);
        }else {
            model.setHaveVideo(Constants.ISORNOT_YES);
        }
        model.setAlarmDeviceList(BeanUtils.convertListTo(alarmDevices, AlarmDeviceModel::new));
        return model;
    }

    private void buildUserName(List<AlarmHandleRecordModel> handleRecords) {
        List<String> userIdList = handleRecords.stream().filter(e -> StringUtils.isNotEmpty(e.getOperator())).map(AlarmHandleRecordModel::getOperator).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        Map<String, UserInfoModel> userMap = getUserMap(userIdList);
        if (userMap == null || userMap.isEmpty()) {
            return;
        }
        handleRecords.forEach(e -> {
            if (StringUtils.isEmpty(e.getOperator())) {
                return;
            }
            UserInfoModel userInfoModel = userMap.get(e.getOperator());
            if (userInfoModel == null) {
                return;
            }
            e.setAccount(userInfoModel.getStaffNo());
            e.setOperatorName(userInfoModel.getUserName());
        });
    }

    private Map<String, UserInfoModel> getUserMap(List<String> userIdList) {
        List<UserInfoModel> userList = userApiService.getByStaffNos(userIdList);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }
        return userList.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo,Function.identity()));
    }


    /**
     * 忽略告警
     * 如果是忽略本次 则需要结束当前告警 并且 清空已存在的告警记录
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public synchronized Boolean alarmIgnore(AlarmIgnoreConfigParam param) {
        AlarmIgnoreConfig ignoreConfig = BeanUtils.convertTo(param, AlarmIgnoreConfig::new);
        Long alarmInfoId = ignoreConfig.getAlarmInfoId();
        AssertUtils.notNull(alarmInfoId, "告警id不能为空");
        AlarmInfo alarmInfo = alarmInfoRepository.selectById(alarmInfoId);
        AssertUtils.notNull(alarmInfo, "告警信息不存在");
        AssertUtils.isEquals(AlarmInfoConstant.ALARM_SOURCE_1, alarmInfo.getAlarmStatus(), "告警状态已变更，请知悉");

        ignoreConfig.setAlarmUnique(alarmInfo.getAlarmUnique());

        String operateDesc = "";
        if (AlarmIgnoreConfigConstant.IGNORE_TYPE_1.equals(param.getIgnoreType())) {
            alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            alarmInfo.setAlarmEndType(AlarmInfoConstant.ALARM_END_TYPE_4);
            alarmInfo.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_3);
            ignoreConfig.setEnableStatus(AlarmIgnoreConfigConstant.ENABLE_STATUS_1);
            operateDesc = "确认忽略告警，告警标记为已结束";
        } else if (AlarmIgnoreConfigConstant.IGNORE_TYPE_2.equals(param.getIgnoreType())) {
            AssertUtils.notNull(ignoreConfig.getIgnoreStartTime(), "忽略开始时间不能为空");
            AssertUtils.notNull(ignoreConfig.getIgnoreEndTime(), "忽略结束时间不能为空");

            alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            alarmInfo.setAlarmEndType(AlarmInfoConstant.ALARM_END_TYPE_4);
            alarmInfo.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_3);
            ignoreConfig.setEnableStatus(AlarmIgnoreConfigConstant.ENABLE_STATUS_1);
            operateDesc = DateUtils.format(ignoreConfig.getIgnoreStartTime()) + " - " + DateUtils.format(ignoreConfig.getIgnoreEndTime()) +
                    " 确认忽略告警，告警标记为已结束";
        }
        String userId = userApiService.getCurrentStaffNo();
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        Map<String, UserInfoModel> userMap = getUserMap(userIdList);
        if (userMap != null && !userMap.isEmpty()) {
            UserInfoModel user = userMap.get(userId);
            if (user != null) {
                alarmInfo.setAlarmRecoveryDesc(user.getUserName() + " 确认忽略告警，告警标记为已结束");
            }
        }
        AlarmHandleRecord record = buildAlarmHandleRecord(alarmInfo, AlarmHandleRecordConstant.CONFIRM_LINK_2);
        record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_3);
        record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
        record.setOperateDesc(operateDesc);
        alarmInfoRepository.updateById(alarmInfo);
        alarmHandleRecordService.save(record);
        ignoreConfig.setId(null);
        alarmIgnoreConfigService.save(ignoreConfig);
        return Boolean.TRUE;
    }


    /**
     * 告警确认
     * 告警确认为系统误报后，系统将自动结束该设备的所有告警
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public synchronized Boolean alarmConfirm(AlarmHandleRecordParam param) {
        if("Y".equals(param.getWork())){
            AssertUtils.notNull(param.getScheduleId(),"物业分组id不能为空");
            AssertUtils.notNull(param.getDispatchType(),"派单方式不能为空");
        }
        AlarmHandleRecord record = BeanUtils.convertTo(param, AlarmHandleRecord::new);
        Long alarmId = record.getAlarmId();
        AlarmInfo alarmInfo = alarmInfoRepository.selectById(alarmId);
        AssertUtils.notNull(alarmInfo, "告警信息不存在");
        AssertUtils.isEquals(AlarmInfoConstant.ALARM_STATUS_1, alarmInfo.getAlarmStatus(), "告警状态已变更，请知悉");
        String userId = userApiService.getCurrentStaffNo();
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();
        record.setOperator(userId);
        record.setOperateTime(DateUtils.currentDate());
        record.setOperatorStaffid(userInfoModel.getId());
        //  真实告警
        if (AlarmInfoConstant.ALARM_CONFIRM_RESULT_1.equals(param.getAlarmConfirmResult())) {

            alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_2);
            alarmInfo.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_1);

            record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_2);
            record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_2);
            record.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_1);
            record.setLinkDesc("真实告警");
            //TODO判断是否转工单 后续需求
            if ("Y".equals(param.getWork())) {
                handleWorkOrder(param, alarmInfo, record, userInfoModel);
            }
            // 系统误报
        } else if (AlarmInfoConstant.ALARM_CONFIRM_RESULT_2.equals(param.getAlarmConfirmResult())) {
            alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            alarmInfo.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_2);
            alarmInfo.setAlarmEndTime(DateUtils.currentDate());
            alarmInfo.setAlarmEndType(AlarmInfoConstant.ALARM_END_TYPE_3);

            record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_2);
            record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            record.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_2);
            record.setLinkDesc("系统误报");
            //运维导致
        } else if (AlarmInfoConstant.ALARM_CONFIRM_RESULT_3.equals(param.getAlarmConfirmResult())) {
            alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_2);
            alarmInfo.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_3);

            record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_2);
            record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_2);
            record.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_3);
            record.setLinkDesc("运维导致");
        }
        alarmInfoRepository.updateById(alarmInfo);
        record.setId(null);
        return alarmHandleRecordService.save(record);
    }

    /**
     * 处理告警转工单逻辑
     *
     * @param param          告警处理记录参数
     * @param alarmInfo      告警信息对象
     * @param record         告警处理记录对象
     * @param userInfoModel  用户信息对象
     */
    private void handleWorkOrder(AlarmHandleRecordParam param, AlarmInfo alarmInfo, AlarmHandleRecord record, UserInfoModel userInfoModel) {
        try {
            Integer number = workOrderRepository.selectCount(Wrappers.<WorkOrder>lambdaQuery().eq(WorkOrder::getSource, WorkOrderSourceEnum.ALARM.getCode()).eq(WorkOrder::getDeleted,  Status.enabled.getKey()).eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), WorkOrder::getTenantId, WebFrameworkUtils.getHeaderTenantId()));

            AssertUtils.notNull(param.getScheduleId(), "物业分组不能为空");
            AssertUtils.notNull(param.getDispatchType(), "派单方式不能为空");
            List<PropertyScheduleUserModel> scheduleUserModels = propertyScheduleService.findUserList(param.getScheduleId());
            //manager:是否是管理员，保持0：是；1否（前端要改的地方过多，保持不动）
            PropertyScheduleUserModel leader = scheduleUserModels.stream().filter(scheduleUserModel -> scheduleUserModel.getManager().equals(0)).findFirst().orElse(null);
            // 创建工单
            WorkOrder workOrder = new WorkOrder();
            workOrder.setName("【" + AlarmLevelEnum.getDesc(alarmInfo.getAlarmLevel()) + "】" + alarmInfo.getAlarmName() + "(" + alarmInfo.getAlarmDevice() + ")" + "("+ cn.hutool.core.date.DateUtil.format(new Date(), "MMdd") + ")");
            workOrder.setCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.GJ, Long.valueOf(number)));
            workOrder.setType(WorkOrderTypeEnum.DEVICEALARM.getCode());
            workOrder.setSource(WorkOrderSourceEnum.ALARM.getCode());
            if(DispatchTypeEnum.ASSIGN.getCode().equals(param.getDispatchType())){
                workOrder.setProcessedPersonName(param.getHandleUname());
                workOrder.setProcessedPersonId(param.getHandleUid());
                workOrder.setProcessedPersonStaffid(param.getHandleStaffid());
                workOrder.setStatus(WorkOrderStatusEnum.REPORTED.getCode());
                UserInfoModel userPeron = userApiService.getSecondDeptByStaffNo(param.getHandleStaffid());
                workOrder.setDepartmentId(userPeron.getDepartmentSecondId());
                workOrder.setDepartmentName(userPeron.getDepartmentSecondName());
            }else if(DispatchTypeEnum.GROUPING.getCode().equals(param.getDispatchType())){
                workOrder.setStatus(WorkOrderStatusEnum.ALLOT.getCode());
            }else if(DispatchTypeEnum.LEADER.getCode().equals(param.getDispatchType()) && ObjectUtil.isNotEmpty(leader)){
                workOrder.setAllotUname(leader.getUserName());
                workOrder.setAllotUid(leader.getUserId());
                workOrder.setAllotUstaffid(leader.getStaffid());
                workOrder.setStatus(WorkOrderStatusEnum.ALLOT.getCode());
            }
            workOrder.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            workOrder.setCreateTime(new Date());
            workOrder.setCreatorId(WebFrameworkUtils.getHeaderUserId());
            workOrder.setCreateBy(userInfoModel.getUserName());
            workOrder.setSpaceName(alarmInfo.getSpaceAddr());
            workOrder.setSpaceId(alarmInfo.getSpaceAddrId());
            workOrder.setBusinessId(alarmInfo.getId());
            workOrder.setDispatchType(param.getDispatchType());
            if(Status.enabled.getKey().equals(param.getAuditType())){
                workOrder.setAuditUid(param.getAuditUid());
                workOrder.setAuditUname(param.getAuditUname());
                workOrder.setAuditStaffid(param.getAuditStaffid());
            }
            // 插入工单数据到数据库
            workOrderRepository.insert(workOrder);

            // 创建工单设备关联
            List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(new LambdaQueryWrapper<AlarmDevice>()
                    .eq(AlarmDevice::getAlarmId, alarmInfo.getId()));
            List<WorkOrderDevice> workOrderDevices = alarmDevices.stream().map(device -> {
                WorkOrderDevice workOrderDevice = new WorkOrderDevice();
                workOrderDevice.setWorkOrderId(workOrder.getId());
                workOrderDevice.setDeviceId(device.getDeviceId());
                workOrderDevice.setDeviceName(device.getDeviceName());
                workOrderDevice.setSpaceId(device.getSpaceId());
                workOrderDevice.setSpaceFullPath(device.getSpaceName());
                workOrderDevice.setTenantId(WebFrameworkUtils.getHeaderTenantId());
                workOrderDevice.setCreateTime(new Date());
                return workOrderDevice;
            }).collect(Collectors.toList());
            //生成工单流程
            WorkOrderRoman workOrderRoman = new WorkOrderRoman();
            workOrderRoman.setWorkOrderId(workOrder.getId());
            workOrderRoman.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            if(DispatchTypeEnum.ASSIGN.getCode().equals(param.getDispatchType())){
                workOrderRoman.setRomanStatus(WorkOrderStatusEnum.REPORTED.getCode());
                workOrderRoman.setOperatorId(param.getHandleUid());
                workOrderRoman.setOperatorName(param.getHandleUname());
                workOrderRoman.setOperatorStaffid(param.getHandleStaffid());
                workOrderRoman.setOperator("处理人");
                workOrderRoman.setOperatorValue(param.getHandleUname());
            }else if(DispatchTypeEnum.GROUPING.getCode().equals(param.getDispatchType())){
                workOrderRoman.setRomanStatus(WorkOrderStatusEnum.ALLOT.getCode());
                workOrderRoman.setOperator("分配人");
            }else if(DispatchTypeEnum.LEADER.getCode().equals(param.getDispatchType()) && ObjectUtil.isNotEmpty(leader)){
                workOrderRoman.setRomanStatus(WorkOrderStatusEnum.ALLOT.getCode());
                workOrderRoman.setOperatorId(leader.getUserId());
                workOrderRoman.setOperatorName(leader.getUserName());
                workOrderRoman.setOperatorStaffid(leader.getStaffid());
                workOrderRoman.setOperator("分配人");
                workOrderRoman.setOperatorValue(leader.getUserName());
            }
            workOrderRoman.setCreateTime(new Date());

            workOrderRomanRepository.insert(workOrderRoman);
            // 插入工单设备关联数据到数据库
            for (WorkOrderDevice workOrderDevice : workOrderDevices) {
                workOrderDeviceRepository.insert(workOrderDevice);
            }
            Map<Long, String> scheduleNameMap = propertyScheduleService.getScheduleNameMap(Collections.singletonList(param.getScheduleId()));
            List<WorkScheduleUserParam> workScheduleUsers = scheduleUserModels.stream().map(scheduleUser -> {
                WorkScheduleUserParam workScheduleUser = new WorkScheduleUserParam();
                BeanUtils.copyProperties(scheduleUser, workScheduleUser);
                workScheduleUser.setId(null);
                workScheduleUser.setWorkId(workOrder.getId());
                workScheduleUser.setScheduleId(param.getScheduleId());
                workScheduleUser.setScheduleName(scheduleNameMap.get(param.getScheduleId()));
                workScheduleUser.setTenantId(WebFrameworkUtils.getHeaderTenantId());
                return workScheduleUser;
            }).collect(Collectors.toList());
            //工单分组人员
            if(CollectionUtils.isNotEmpty(workScheduleUsers)){
                workScheduleUserService.addBatch(workScheduleUsers);
            }
            if(ObjectUtil.isEmpty(param.getHandleStaffid()) || ObjectUtil.isEmpty(param.getHandleUname())){
                record.setLinkDesc("真实告警-转工单");
            }else{
                record.setLinkDesc("真实告警-转工单(处理人:" + param.getHandleUname()+")");
            }
            alarmInfo.setWrokOrder(Constants.ISORNOT_YES);
        } catch (Exception e) {
            log.error("处理告警转工单失败", e);
            throw GenericException.fail("处理告警转工单失败！！");
        }
    }

    /**
     * 设备停用
     *
     * @param param
     * @return
     */
    @Override
    public Boolean stopDevice(AlarmHandleRecordParam param) {
        AssertUtils.notNull(param.getDeviceId(), "设备id不能为空");

        LambdaQueryWrapper<AlarmInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlarmInfo::getDeviceDn, param.getDeviceId());
        queryWrapper.ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3);
        queryWrapper.last(" limit 1");
        AlarmInfo alarmInfo = alarmInfoRepository.selectOne(queryWrapper);
        if (alarmInfo != null) {
            alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            alarmInfo.setAlarmEndType(AlarmInfoConstant.ALARM_END_TYPE_2);
            alarmInfo.setAlarmRecoveryDesc("设备停用，系统自动处理告警");
            alarmInfoRepository.updateById(alarmInfo);
            AlarmHandleRecord record = buildAlarmHandleRecord(alarmInfo, AlarmHandleRecordConstant.CONFIRM_LINK_4);
            record.setAlarmConfirmResult(null);
            record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            record.setOperateDesc("设备停用，系统自动处理告警");
            alarmHandleRecordService.save(record);
        }
        return Boolean.TRUE;
    }

    @Override
    public AlarmIgnoreConfigModel getLastIgnore(AlarmIgnoreConfigParam param) {
        AssertUtils.notNull(param.getAlarmUnique(), "告警唯一标识不能为空");
        LambdaQueryWrapper<AlarmIgnoreConfig> query = new LambdaQueryWrapper<>();
        query.eq(AlarmIgnoreConfig::getAlarmUnique, param.getAlarmUnique());
        query.orderByDesc(AlarmIgnoreConfig::getCreateTime);
        query.last(" limit 1");
        AlarmIgnoreConfig config = alarmIgnoreConfigService.getOne(query);
        if (config != null) {
            config.setId(null);
            return BeanUtils.convertTo(config, AlarmIgnoreConfigModel::new);
        }
        return null;
    }


    /**
     * 告警级别调整
     *
     * @param param 告警处理记录参数
     * @return 是否成功
     */
    @Transactional
    @Override
    public synchronized Boolean adjustAlarmLevel(AlarmHandleRecordParam param) {
        Long alarmId = param.getAlarmId();
        AlarmInfo alarmInfo = alarmInfoRepository.selectById(alarmId);
        AssertUtils.notNull(alarmInfo, "告警信息不存在");
        AssertUtils.isNotEquals(AlarmInfoConstant.ALARM_STATUS_3, alarmInfo.getAlarmStatus(), "告警状态已变更，请知悉");

        if (Integer.parseInt(param.getAlarmLevel()) < Integer.parseInt(alarmInfo.getAlarmLevel())){

            Map<String, String> variables = new HashMap<>();
            //todo升级告警通知固定人员消息
            variables.put("alarmName", alarmInfo.getAlarmName());
            variables.put("spaceAddr", alarmInfo.getSpaceAddr());
            variables.put("alarmDevice", alarmInfo.getAlarmDevice());
            variables.put("oldAlarmLevel", AlarmLevelEnum.getName(alarmInfo.getAlarmLevel()));
            variables.put("newAlarmLevel", AlarmLevelEnum.getName(param.getAlarmLevel()));
            messageCommonService.sendMessage(MessageConstant.ALARM_UPGRADE, alarmInfo.getTenantId(), alarmInfo.getId(), Collections.emptySet(), variables);
        }
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();
        AlarmHandleRecord record = new AlarmHandleRecord();
        record.setAlarmId(alarmId);
        record.setOperator(userInfoModel.getId());
        record.setOperateTime(DateUtils.currentDate());
        record.setOperatorStaffid(userInfoModel.getId());
        record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_3);
        record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_2);
        record.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_4);
        record.setOperateDesc(param.getOperateDesc());

        if (alarmInfo.getAlarmConfirmResult() == null){
            alarmInfo.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_4);
        }
        //TODO判断是否转工单 后续需求
        if (AlarmInfoConstant.ALARM_STATUS_1.equals(alarmInfo.getAlarmStatus()) && "Y".equals(param.getWork())) {
            handleWorkOrder(param, alarmInfo, record, userInfoModel);
        }
        alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_2);
        record.setLinkDesc("手动级别调整(" + AlarmLevelEnum.getName(alarmInfo.getAlarmLevel()) + "-" +AlarmLevelEnum.getName(param.getAlarmLevel()) + ")");
        // 更新告警级别
        alarmInfo.setAlarmLevel(param.getAlarmLevel());
        alarmInfoRepository.updateById(alarmInfo);
        alarmHandleRecordService.save(record);
        return Boolean.TRUE;
    }

    /**
     * 手动结束告警
     * @param param
     * @return
     */
    @Transactional
    @Override
    public synchronized Boolean endAlarm(AlarmHandleRecordParam param) {
        Long alarmId = param.getAlarmId();
        AlarmInfo alarmInfo = alarmInfoRepository.selectById(alarmId);
        AssertUtils.notNull(alarmInfo, "告警信息不存在");
        AssertUtils.isNotEquals(AlarmInfoConstant.ALARM_STATUS_3, alarmInfo.getAlarmStatus(), "告警状态已变更，请知悉");
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();

        AlarmHandleRecord record = new AlarmHandleRecord();
        record.setAlarmId(alarmId);
        record.setOperator(userInfoModel.getId());
        record.setOperateTime(DateUtils.currentDate());
        record.setOperatorStaffid(userInfoModel.getStaffNo());
        record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_5);
        record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
        record.setOperateDesc(param.getOperateDesc());
        record.setLinkDesc("手动结束");
        // 更新告警状态
        alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
        alarmInfo.setAlarmEndType(AlarmInfoConstant.ALARM_END_TYPE_5);
        alarmInfo.setAlarmEndTime(DateUtils.currentDate());
        alarmInfoRepository.updateById(alarmInfo);
        alarmHandleRecordService.save(record);

        return Boolean.TRUE;
    }

    /**
     * 批量忽略告警
     * @param param 忽略告警入参
     * @return 是否成功
     */
    @Override
    public Boolean ignoreAlarmS(AlarmHandleRecordParam param) {
        AssertUtils.notEmpty(param.getIds(), "告警id不能为空");
        // 获取当前操作用户ID
        String userId = WebFrameworkUtils.getHeaderUserId();
        // 获取当前操作用户的详细信息
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();

        // 批量更新告警状态
        LambdaQueryWrapper<AlarmInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AlarmInfo::getId, param.getIds());
        queryWrapper.ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3); // 确保告警状态不是已结束

        AlarmInfo updateAlarmInfo = new AlarmInfo();
        updateAlarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3); // 设置告警状态为已结束
        updateAlarmInfo.setAlarmEndTime(DateUtils.currentDate()); // 设置告警状态为已结束
        updateAlarmInfo.setAlarmEndType(AlarmInfoConstant.ALARM_END_TYPE_4); // 设置告警结束类型

        int updateCount = alarmInfoRepository.update(updateAlarmInfo, queryWrapper);
        if (updateCount == 0) {
            log.warn("没有找到可忽略的告警记录");
            return Boolean.TRUE;
        }

        // 批量生成告警处理记录
        List<AlarmHandleRecord> handleRecords = new ArrayList<>();
        for (Long alarmId : param.getIds()) {
            AlarmHandleRecord record = new AlarmHandleRecord();
            record.setAlarmId(alarmId);
            record.setOperator(userId);
            record.setOperateTime(DateUtils.currentDate());
            record.setOperatorStaffid(userInfoModel.getStaffNo());
            record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_5);
            record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            record.setOperateDesc(param.getOperateDesc());
            record.setLinkDesc("忽略告警");
            handleRecords.add(record);
        }

        // 批量保存告警处理记录
        boolean saveBatchResult = alarmHandleRecordService.saveBatch(handleRecords);
        if (!saveBatchResult) {
            log.error("批量保存告警处理记录失败");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public List<AlarmInfo> findAlarmInfoList(List<Long> deviceIds) {
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().select(AlarmDevice::getId, AlarmDevice::getAlarmId).in(AlarmDevice::getDeviceId, deviceIds));
        if(CollectionUtil.isEmpty(alarmDevices)){
            return Collections.emptyList();
        }
        List<Long> alarmIds = alarmDevices.stream().map(AlarmDevice::getAlarmId).collect(Collectors.toList());
        LambdaQueryWrapper<AlarmInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AlarmInfo::getId, alarmIds);
        queryWrapper.in(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_1, AlarmInfoConstant.ALARM_STATUS_2);
        return this.list(queryWrapper);
    }

    /**
     * 批量设备下线
     *
     * @param param 设备处理记录参数
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean offlineDevices(AlarmHandleRecordParam param) {
        AssertUtils.notEmpty(param.getDeviceIds(), "设备ID不能为空");

        // 获取当前操作用户ID
        String userId = WebFrameworkUtils.getHeaderUserId();
        AssertUtils.notNull(userId,"当前操作用户ID为空！");

        // 获取当前操作用户的详细信息
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();
        AssertUtils.notNull(userInfoModel,"当前操作用户信息为空！");
        //判断是否有空间权限
        List<Long> deviceIds = param.getDeviceIds();
        List<IocDevice> iocDevices = new ArrayList<>();
        if (!buildRole(userInfoModel)){
            LambdaQueryWrapper<UserSpace> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(UserSpace::getUserId, userId);
            List<UserSpace> userSpaces = userSpaceRepository.selectList(queryWrapper);

            List<Long> spaceIds = userSpaces.stream().map(UserSpace::getSpaceId).collect(Collectors.toList());
            AssertUtils.isNotEmpty(spaceIds,"当前用户没有空间权限！");

            //获取所有设备信息
            iocDevices = iIocDeviceService.selectList(Wrappers.<IocDevice>lambdaQuery().in(IocDevice::getId, deviceIds));
            //判断是否有设备空间权限
            for (IocDevice iocDevice : iocDevices) {
                AssertUtils.isFalse(!spaceIds.contains(iocDevice.getSpaceId()),"您暂时没有" +iocDevice.getDeviceName() +"设备的权限，不能操作此设备的下线");
            }
        }
        // 批量更新设备在线状态为下线
//        iocDevices = CollectionUtils.isNotEmpty(iocDevices) ? iocDevices : iIocDeviceService.selectList(Wrappers.<IocDevice>lambdaQuery().in(IocDevice::getId, deviceIds));
//        iocDevices.forEach(d->d.setOnlineStatus(Status.disabled.getKey()));
//        boolean updateFlag = iIocDeviceService.(iocDevices);
        LambdaUpdateWrapper<IocDevice> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.in(IocDevice::getId, deviceIds);
        updateWrapper.set(IocDevice::getOnlineStatus , Status.disabled.getKey());
        int updateCount = iIocDeviceService.update(null, updateWrapper);
        if (updateCount == 0) {
            log.warn("没有找到可下线的设备记录");
            return Boolean.TRUE;
        }

       //判断设备绑定的告警是否应该结束
        handAlarmsByDevices(deviceIds , userInfoModel , param.getOperateDesc() , AlarmInfoConstant.ALARM_END_TYPE_2);

        return Boolean.TRUE;
    }

    /**
     * 根据设备ID列表处理未结束的告警
     *
     * @param deviceIds 设备ID列表
     * @param user 用户信息模型，用于记录操作者信息
     * @param operateDesc 操作备注，用于记录操作备注
     * @param endType 告警结束类型，表示告警结束的原因
     * @return 处理结果，true表示成功处理
     */
    @Override
    public Boolean handAlarmsByDevices(List<Long> deviceIds , UserInfoModel user ,String operateDesc , Integer endType) {
        // 查询与这些设备相关的未结束告警
        List<AlarmInfo> unfinishedAlarms = alarmInfoRepository.selectUnfinishedAlarmsByDeviceIds(deviceIds);
        if (unfinishedAlarms.isEmpty()) {
            log.info("没有找到与设备相关的未结束告警");
            return Boolean.TRUE;
        }
        if (user == null ){
            user = userApiService.getCurrentUserInfo();
        }
        List<Long> alarmIds = unfinishedAlarms.stream().map(AlarmInfo::getId).collect(Collectors.toList());
        // 检测告警下的设备是否都处于下线状态
        for (Long alarmInfoId : alarmIds) {
            LambdaQueryWrapper<AlarmDevice> alarmDeviceQueryWrapper = Wrappers.lambdaQuery();
            alarmDeviceQueryWrapper.eq(AlarmDevice::getAlarmId , alarmInfoId);
            List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(alarmDeviceQueryWrapper);
            List<Long> alarmDeviceIds = alarmDevices.stream().map(AlarmDevice::getDeviceId).collect(Collectors.toList());
            List<IocDevice> devices =  iIocDeviceService.selectList(Wrappers.<IocDevice>lambdaQuery().in(IocDevice::getId, alarmDeviceIds));
            boolean endAlarm = true ;
            for (IocDevice device : devices) {
                if (device.getOnlineStatus() == 1 && device.getEnableStatus() ==1 && device.getDeleted() == 1 ){
                    endAlarm = false ;
                    break;
                }
            }
            if (endAlarm){
                // 所有设备都处于下线状态，更新告警状态为已结束
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.setId(alarmInfoId);
                alarmInfo.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
                alarmInfo.setAlarmEndTime(DateUtils.currentDate());
                alarmInfo.setAlarmEndType(endType); // 结束告警原因
                alarmInfoRepository.updateById(alarmInfo);

                // 生成告警处理记录
                AlarmHandleRecord record = new AlarmHandleRecord();
                record.setAlarmId(alarmInfoId);
                record.setOperator(user.getId());
                record.setOperateTime(DateUtils.currentDate());
                record.setOperatorStaffid(user.getStaffNo());
                record.setOperateDesc(operateDesc);
                record.setLink(AlarmHandleRecordConstant.CONFIRM_LINK_5);
                record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
                record.setLinkDesc("设备下线");
                alarmHandleRecordService.save(record);
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 判断登陆人是否是超级管理员或园区管理员
     * @param user 用户信息
     * @return
     */
    private boolean buildRole(UserInfoModel user) {
        if(roleApiService.hasRole(userApiService.getCurrentStaffNo(),systemRoleCode)){
            //如果是超级管理员
            return true;
        }
        TenantMemberListParam param1 = new TenantMemberListParam();
        param1.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        param1.setUserId(user.getId());
        List<TenantMemberDomain> domainList = tenantMemberService.list(param1);
        AssertUtils.notEmpty(domainList,"当前园区下用户不存在");

        TenantMemberDomain domain = domainList.get(0);
        // 如果是园区管理员
        return domain.getIdentity() != null && domain.getIdentity() == 1;
    }

    /**
     * 根据告警处理记录参数获取告警设备列表
     *
     * 此方法首先确保传入的告警ID不为空，然后根据告警ID查询数据库中的告警设备信息，
     * 最后将查询到的告警设备实体列表转换为告警设备模型列表返回
     *
     * @param param 告警处理记录参数，包含告警ID等信息
     * @return 告警设备模型列表
     */
    @Override
    public List<AlarmDeviceModel> getAlarmDevices(AlarmHandleRecordParam param) {
        // 确保告警ID不为空
        AssertUtils.notNull(param.getAlarmId(), "告警ID不能为空");

        // 创建Lambda查询条件
        LambdaQueryWrapper<AlarmDevice> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AlarmDevice::getAlarmId , param.getAlarmId());

        // 根据查询条件获取告警设备实体列表
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(queryWrapper);

        // 将告警设备实体列表转换为告警设备模型列表并返回
        return BeanUtils.convertListTo(alarmDevices, AlarmDeviceModel::new);
    }

    /**
     * 获取空间用户信息列表
     * 根据告警ID获取告警信息，并根据告警信息中的空间地址ID和租户ID查询用户空间表，
     * 获取该空间下所有用户的ID，并进一步调用用户信息服务获取用户详细信息列表
     *
     * @param param 告警处理记录参数对象，包含告警ID
     * @return 空间用户信息列表，如果无用户则返回空列表
     */
    @Override
    public List<UserInfoModel> getSpaceUsers(AlarmHandleRecordParam param) {
//        // 确保告警ID不为空
//        AssertUtils.notNull(param.getAlarmId(),"告警id不能为空");
//        // 根据告警ID查询告警信息
//        AlarmInfo alarmInfo = alarmInfoRepository.selectById(param.getAlarmId());
//        // 确保告警信息存在
//        AssertUtils.notNull(alarmInfo,"告警信息不存在");
//        //确保设备拥有空间位置
//        if (alarmInfo.getSpaceAddrId() == null){
//            return new ArrayList<>();
//        }
//        // 创建查询条件，用于查询用户空间表
//        LambdaQueryWrapper<UserSpace> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper.eq(UserSpace::getSpaceId , Long.parseLong(alarmInfo.getSpaceAddrId()))
//                .eq(UserSpace::getTenantId , alarmInfo.getTenantId());
//
//        // 查询用户空间表，并提取所有用户ID
//        List<Long> userIds = userSpaceRepository.selectList(queryWrapper)
//                .stream().map(UserSpace::getUserId).collect(Collectors.toList());
//
//        // 如果用户ID列表不为空，则调用用户信息服务获取用户信息列表
//        if (CollectionUtils.isNotEmpty(userIds)){
//            return userInfoFeignClient.list(new UserInfoListParam().setIds(userIds)).getBody().getResult();
//        }else {
//            // 如果用户ID列表为空，则返回空列表
//            return new ArrayList<>();
//        }
        //产品修改为可选择所有人员
        return Collections.emptyList();
//       return userInfoFeignClient.list(new UserInfoListParam().setStatus(Status.enabled.getKey())).getBody().getResult();
    }

    /**
     * 忽略告警时构建操作记录
     *
     * @param alarmInfo
     * @return
     */
    private AlarmHandleRecord buildAlarmHandleRecord(AlarmInfo alarmInfo, Integer link) {
        AlarmHandleRecord record = new AlarmHandleRecord();
        alarmInfo.setAlarmSource(alarmInfo.getAlarmSource());
        record.setAlarmId(alarmInfo.getId());
        record.setLink(link);
        record.setAlarmConfirmResult(AlarmInfoConstant.ALARM_CONFIRM_RESULT_3);
        record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_2);
        if (WebFrameworkUtils.getHeaderUserId() != null) {
            record.setOperator(WebFrameworkUtils.getHeaderUserId() + "");
            record.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        }
        if (alarmInfo.getTenantId() != null) {
            record.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        }
        record.setOperateTime(DateUtils.currentDate());
        return record;
    }

    private void buildAlarmInfoModel(List<AlarmInfoModel> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> deviceIds = list.stream().map(AlarmInfoModel::getDeviceDn).collect(Collectors.toList());
        List<DeviceInfo> deviceInfoList = deviceInfoService.list(new LambdaQueryWrapper<DeviceInfo>().in(DeviceInfo::getDeviceId, deviceIds));
        if (CollectionUtils.isEmpty(deviceInfoList)) {
            return;
        }
        Map<String, DeviceInfo> deviceMap = deviceInfoList.stream().collect(Collectors.toMap(DeviceInfo::getDeviceId, e -> e));
        list.forEach(e -> {
            DeviceInfo device = deviceMap.get(e.getDeviceDn());
            if (device == null) {
                return;
            }
            e.setDeviceName(StringUtils.isNotEmpty(device.getDeviceAlias()) ? device.getDeviceAlias() : device.getName());
        });
    }

    /***
     * @Description 查询字段的处理
     */
    private LambdaQueryWrapper<AlarmInfo> handlePublicQuery(AlarmInfoParam param) {
        LambdaQueryWrapper<AlarmInfo> queryWrapper = Wrappers.lambdaQuery();
        //修改为不做人员设备权限校验
//        if (StringUtils.isNotEmpty(param.getDeviceName())) {
//            List<String> deviceIdList = getDeviceIdList(param.getDeviceName());
//            if (deviceIdList.isEmpty()) {
//                deviceIdList.add("-1");
//            }
//            queryWrapper.in(AlarmInfo::getDeviceDn, deviceIdList);
////			queryWrapper.eq(StringUtils.isNotEmpty(param.getDeviceDn()), AlarmInfo::getDeviceDn, param.getDeviceDn());
//        } else {
//            List<String> deviceIdList = getDeviceIdList(null);
//            if (deviceIdList.isEmpty()) {
//                queryWrapper.eq(AlarmInfo::getDeviceDn, "-1");
//            } else {
//                queryWrapper.in(AlarmInfo::getDeviceDn, deviceIdList);
//            }
//        }

        queryWrapper.like(StringUtils.isNotEmpty(param.getAlarmName()), AlarmInfo::getAlarmName, param.getAlarmName());
        queryWrapper.like(StringUtils.isNotEmpty(param.getAlarmDevice()), AlarmInfo::getAlarmDevice, param.getAlarmDevice());
        queryWrapper.eq(param.getAlarmConfirmResult() != null, AlarmInfo::getAlarmConfirmResult, param.getAlarmConfirmResult());
        queryWrapper.eq( StringUtils.isNotEmpty(param.getAlarmLevel()), AlarmInfo::getAlarmLevel, param.getAlarmLevel());
        queryWrapper.eq(param.getAlarmStatus() != null, AlarmInfo::getAlarmStatus, param.getAlarmStatus());
        queryWrapper.eq(param.getTenantId() != null, AlarmInfo::getTenantId, param.getTenantId());
        queryWrapper.eq(param.getAlarmEndType() != null, AlarmInfo::getAlarmEndType, param.getAlarmEndType());
        queryWrapper.eq(param.getSpaceAddrId() != null, AlarmInfo::getSpaceAddrId, param.getSpaceAddrId());
        if (param.getAlarmFirstTime() != null && param.getAlarmLastTime() != null ){
            queryWrapper.between(AlarmInfo::getAlarmFirstTime, param.getAlarmFirstTime(), param.getAlarmLastTime());
        }
        if (param.getAlarmEndTimeStart() != null && param.getAlarmEndTimeEnd() != null ){
            queryWrapper.between(AlarmInfo::getAlarmEndTime, param.getAlarmEndTimeStart(), param.getAlarmEndTimeEnd());
        }

        // 添加设备ID查询条件
        if (param.getDeviceId() != null) {
            queryWrapper.inSql(AlarmInfo::getId, "SELECT alarm_id FROM bbx_alarm_device WHERE device_id = " + param.getDeviceId());
        }else if (CollectionUtils.isNotEmpty(param.getDeviceIdList())) {
            String deviceIdsStr = param.getDeviceIdList().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            queryWrapper.inSql(AlarmInfo::getId, "SELECT alarm_id FROM bbx_alarm_device WHERE device_id IN (" + deviceIdsStr + ")");
        }
        // 排序方式
        if (StringUtils.isNotEmpty(param.getSort())) {
            if ("asc".equalsIgnoreCase(param.getSort())) {
                queryWrapper.orderByAsc(AlarmInfo::getCreateTime);
            } else {
                queryWrapper.orderByDesc(AlarmInfo::getCreateTime);
            }
        } else {
            queryWrapper.orderByDesc(AlarmInfo::getCreateTime);
        }
        return queryWrapper;
    }


    /**
     * kafka 接收数据 逻辑
     *
     * @param msg
     * @param offset
     * @return
     */
    @Override
    @Transactional
    public int saveAlarmInfo(String msg, Long offset) {
        ThingModelKaFkaMessage kafkaModel = JSON.parseObject(msg, ThingModelKaFkaMessage.class);
        kafkaModel.setDeviceIds(Collections.singletonList(kafkaModel.getDeviceId()));
        if (isStopDevice(kafkaModel)) {
            log.info("=====>KAFKA收到消息|保存告警|设备【{}】未关联ioc设备或设备停用了 不处理", kafkaModel.getDeviceIds());
            log.info("设备【{}】未关联ioc设备或设备停用了 不处理", kafkaModel.getDeviceIds());
            return 0;
        }
        //取消告警忽略，ioc告警忽略修改为仅忽略本次告警 后续告警还是会处理
//        flag = isIgnore(unique, kafkaModel.getOccurred());
//        if (flag) {
//            log.info("设备【{}】忽略告警时间段内的告警信息", kafkaModel.getDeviceIds());
//            return 0;
//        }

        LambdaQueryWrapper<AlarmInfo> query = new LambdaQueryWrapper<>();
        query.ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3);
        query.eq(AlarmInfo::getAlarmRuleId, kafkaModel.getRuleId());

        List<AlarmInfo> infoList = alarmInfoRepository.selectList(query);
        Set<String> deviceIdsSet = new HashSet<>(kafkaModel.getDeviceIds());

        // 查找匹配的告警信息
        AlarmInfo existingInfo = findMatchingAlarmInfo(infoList, deviceIdsSet);

        if (existingInfo == null) {
            // 处理新告警
            log.info("=====>KAFKA收到消息|保存告警|处理新告警|kafka数据:{}",kafkaModel);
            handleNewAlarm(kafkaModel,"1");
        } else {
            // 处理已存在的告警
            log.info("=====>KAFKA收到消息|保存告警|处理已存在告警|kafka数据:{}|告警数据:{}",kafkaModel, existingInfo);
            handleExistingAlarm(existingInfo, kafkaModel);
        }
        log.info("=====>KAFKA收到消息|保存告警|操作结束");
        return 1;
    }

    /**
     * 在给定的报警信息列表中找到与指定设备ID集合匹配的报警信息
     * 此方法用于处理报警信息的匹配，通过比较报警信息中的设备ID与给定的设备ID集合来确定是否匹配
     *
     * @param infoList     报警信息列表，包含多个AlarmInfo对象
     * @param deviceIdsSet 设备ID集合，用于匹配报警信息中的设备ID
     * @return 如果找到匹配的报警信息则返回该AlarmInfo对象，否则返回null
     */
    private AlarmInfo findMatchingAlarmInfo(List<AlarmInfo> infoList, Set<String> deviceIdsSet) {
        for (AlarmInfo alarmInfo : infoList) {
            // 将报警信息中的设备ID字符串分割并转换为集合，以便与设备ID集合进行比较
            Set<String> deviceDnSet = new HashSet<>(Arrays.asList(alarmInfo.getDeviceDn().split("&")));
            // 如果分割后的设备ID集合与给定的设备ID集合相等，则返回当前的报警信息对象
            if (deviceDnSet.equals(deviceIdsSet)) {
                return alarmInfo;
            }
        }
        // 如果没有找到匹配的报警信息，返回null
        return null;
    }

    /**
     * 处理新的报警信息
     *
     * @param kafkaModel 从Kafka消息系统接收到的，包含报警信息的模型
     * @param thirdType  第三方类型，用于区分告警来源
     */
    private void handleNewAlarm(ThingModelKaFkaMessage kafkaModel , String thirdType) {
        // 构建报警信息对象
        AlarmInfo info = buildAlarmInfo(kafkaModel, 1);
        AlarmLevelMap alarmLevelMap = alarmLevelMapRepository.selectOne(new LambdaQueryWrapper<AlarmLevelMap>().eq(AlarmLevelMap::getThirdType, thirdType).eq(AlarmLevelMap::getThirdAlarmLevel,info.getAlarmLevel()));
        if (alarmLevelMap != null){
            info.setAlarmLevel(alarmLevelMap.getIocAlarmLevel());
        }else {
            log.error("未匹配到对应的告警等级"+kafkaModel);
            throw new RuntimeException("未匹配到对应的告警等级");
        }
        // 将构建的报警信息插入数据库
        alarmInfoRepository.insert(info);

        List<AlarmDevice> alarmDevices = new ArrayList<>();
        //创建告警设备子表
        for (IocDevice iocDevice : kafkaModel.getIocDeviceList()) {
            AlarmDevice alarmDevice = new AlarmDevice();
            alarmDevice.setAlarmId(info.getId());
            alarmDevice.setDeviceName(iocDevice.getDeviceName());
            alarmDevice.setDeviceId(iocDevice.getId());
            if (iocDevice.getSpaceId() != null) {
                alarmDevice.setSpaceId(iocDevice.getSpaceId());
                ParkSpaceFullModel parkSpaceFullModel = kafkaModel.getFullSpaceMap().get(iocDevice.getSpaceId());
                alarmDevice.setSpaceName(parkSpaceFullModel.getFullPath());
            }
            alarmDevice.setTenantId(iocDevice.getTenantId());
            alarmDevice.setCreateTime(new Date());
            alarmDevice.setUpdateTime(new Date());
            alarmDevices.add(alarmDevice);
        }
        if (CollectionUtils.isNotEmpty(alarmDevices)) {
            alarmDeviceRepository.beachInsert(alarmDevices);
        }

        // 判断当前报警级别是否需要发送通知
        if (isSendNoticeLevel().contains(kafkaModel.getLevel())) {
            // 如果需要，发送报警信息
            sendAlarmMessage(info);
            //紧急告警发送大屏通知
            sendScreenAlarmMessage(info,kafkaModel.getIocDeviceList().get(0).getId());
        }

        // 构建报警处理记录
        AlarmHandleRecord record = buildAlarmHandleRecord(info, AlarmHandleRecordConstant.CONFIRM_LINK_1);
        // 设置报警确认结果为null，表示尚未处理
        record.setAlarmConfirmResult(null);
        // 设置报警状态为特定的初始状态
        record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_1);
        // 保存报警处理记录到数据库
        alarmHandleRecordService.save(record);
        log.info("=====>KAFKA收到消息|保存告警|新告警保存|告警数据:{}",info);
    }

    private void sendAlarmMessage(AlarmInfo alarmInfo) {
       Map<String, String> variables = new HashMap<>();
       //告警通知固定人员消息
       variables.put("alarmName", alarmInfo.getAlarmName());
       variables.put("spaceAddr", alarmInfo.getSpaceAddr());
       variables.put("alarmDevice", alarmInfo.getAlarmDevice());
       messageCommonService.sendMessage(MessageConstant.URGENT_ALARM, alarmInfo.getTenantId(), alarmInfo.getId(), Collections.emptySet(), variables);

    }

    private void sendScreenAlarmMessage(AlarmInfo alarmInfo,Long deviceId){
        //推送大屏websocket
        ScreenAlarmMessageModel model = BeanUtils.convertTo(alarmInfo,ScreenAlarmMessageModel::new);
        //关联摄像头设备集合
        List<IotDeviceRelation> relations = iotDeviceRelationService.list(Wrappers.<IotDeviceRelation>lambdaQuery().eq(IotDeviceRelation::getDeviceId, deviceId).eq(IotDeviceRelation::getRelationType, "video"));
        if(CollectionUtils.isNotEmpty(relations)){
            List<Long> deviceIds = relations.stream().map(IotDeviceRelation::getRelationDeviceId).collect(Collectors.toList());
            List<IocDevice> devices = iIocDeviceService.selectList(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getDeviceName, IocDevice::getIotDeviceDn,IocDevice::getSpaceId).in(IocDevice::getId, deviceIds));
            if(CollectionUtil.isNotEmpty(devices)){
                Set<Long> spaceIds = devices.stream().filter(d->d.getSpaceId() != null).map(IocDevice::getSpaceId).collect(Collectors.toSet());
                Map<Long, ParkSpaceFullModel> spaceMap = CollectionUtils.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(new ArrayList<>(spaceIds),alarmInfo.getTenantId());
                List<ScreenAlarmMessageModel.RelationDevice> relationDevices = devices.stream().map(d->{
                    ScreenAlarmMessageModel.RelationDevice relationDevice = new ScreenAlarmMessageModel.RelationDevice();
                    relationDevice.setIotDeviceDn(d.getIotDeviceDn());
                    relationDevice.setRelationDeviceName(d.getDeviceName());
                    relationDevice.setSpacesName(d.getSpaceId() != null && spaceMap.containsKey(d.getSpaceId()) ? spaceMap.get(d.getSpaceId()).getFullPath() : "");
                    return relationDevice;
                }).collect(Collectors.toList());
                model.setRelationDevices(relationDevices);
            }
        }
        webSocketService.sendAlarmMessage(alarmInfo.getTenantId()+"",model);
    }

    /**
     * 处理现有报警信息
     * 当报警级别发生变化，且新的报警级别在需要发送通知的级别范围内时，发送报警信息
     * 更新报警信息的名称、描述、计数、最后发生时间、报警级别，并保存到数据库
     *
     * @param info       报警信息对象，包含当前报警的详细信息
     * @param kafkaModel Kafka消息对象，包含新接收到的报警信息
     */
    private void handleExistingAlarm(AlarmInfo info, ThingModelKaFkaMessage kafkaModel) {
        // 检查报警级别是否变化，且新级别是否需要发送通知
        if (!info.getAlarmLevel().equals(kafkaModel.getLevel()) && isSendNoticeLevel().contains(kafkaModel.getLevel())) {
            sendMessage(info);
        }

        // 更新报警名称，基于新的Kafka消息和增加的报警计数
        info.setAlarmName(kafkaModel.getAlterName());
        // 更新报警描述为Kafka消息的内容
        info.setAlarmDesc(kafkaModel.getContent());
        // 增加报警计数
        info.setAlarmCount(info.getAlarmCount() + 1);
        // 更新报警最后发生时间为Kafka消息中的发生时间
        info.setAlarmLastTime(DateUtil.parseDate(kafkaModel.getOccurred()));
        // 更新报警级别为Kafka消息中的级别
        info.setAlarmLevel(kafkaModel.getLevel());
        // 将更新后的报警信息保存到数据库
        alarmInfoRepository.updateById(info);
    }

    /**
     * 告警撤销
     *
     * @param msg
     * @param offset
     * @return
     */
    @Override
    public int restoreAlarmInfo(String msg, Long offset) {
        ThingModelKaFkaMessage kafkaModel = JSON.parseObject(msg, ThingModelKaFkaMessage.class);
        kafkaModel.setDeviceIds(Collections.singletonList(kafkaModel.getDeviceId()));
        String unique = buildAlarmUnique(kafkaModel);
        log.info("=====>KAFKA收到消息|准备执行撤销告警|unique:{}",unique);
        LambdaQueryWrapper<AlarmInfo> query = new LambdaQueryWrapper<>();
        query.eq(AlarmInfo::getAlarmUnique, unique);
        query.ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3);
        query.orderByDesc(AlarmInfo::getCreateTime);
        query.last(" limit 1");
        AlarmInfo info = alarmInfoRepository.selectOne(query);
        if (info != null) {
            info.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            info.setAlarmEndType(AlarmInfoConstant.ALARM_END_TYPE_1);
            info.setAlarmRecoveryDesc(kafkaModel.getContent());
            info.setAlarmEndTime(DateUtil.parseDate(kafkaModel.getOccurred()));
            alarmInfoRepository.updateById(info);

            AlarmHandleRecord record = buildAlarmHandleRecord(info, AlarmHandleRecordConstant.CONFIRM_LINK_3);
            record.setOperateDesc(kafkaModel.getContent());
            record.setAlarmConfirmResult(null);
            record.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
            alarmHandleRecordService.save(record);
            log.info("=====>KAFKA收到消息|执行撤销告警完成|告警信息:{}",info);
            return 1;
        }
        return 0;
    }

    /**
     * 异步发送
     * <p>
     * 如果是紧急告警、重要告警，则需要发送站内新到当前设备所属园区的 指定 运维角色
     * <p>
     * 如果 设备没有获取到 空间 则不发送
     * <p>
     * 2024/10/30 调整为调用消息服务统一发送,默认推送运维角色用户
     *
     * @param info 告警信息
     */
    public void sendMessage(AlarmInfo info) {
        Map<String, String> variables = new HashMap<>(4);
        //告警级别
        variables.put("alarmGrade", AlarmLevelEnum.getName(info.getAlarmLevel()));
        variables.put("deviceLocation", "");
        variables.put("deviceAddress", info.getSpaceAddr());
        //设备名称
        variables.put("deviceName", info.getAlarmDevice());
        //告警规则
        variables.put("alarmRuleType", info.getAlarmRuleType());
        //消息通知
        List<String> userIdList = getRoleUserId();
        messageCommonService.sendMessage(MessageConstant.DEVICE_ALARM, info.getTenantId(), info.getId(), new HashSet<>(userIdList), variables);
    }

    private List<String> getRoleUserId(){
        ConfigInfoModel configInfo = configInfoService.getByCodeDetail(Constant.OPS_ROLE_NAME_CONFIG);
        return cn.hutool.core.util.ObjectUtil.isNotEmpty(configInfo) ? roleApiService.findStaffNoByRoleCode(configInfo.getValue()) : Collections.emptyList();
    }

    /**
     * 是否停用设备
     *
     * @param kafkaModel
     * @return 只有确定设备停用才返回停用
     */
    private boolean isStopDevice(ThingModelKaFkaMessage kafkaModel) {
        //TODO 1.IOT设备未与业务平台设备进行关联，该设备产生的告警，业务平台此告警不产生告警
        // 2.业务平台中该设备已被禁用，业务平台此告警不产生
        // 3. 业务平台中该设备已被下线 业务平台此告警不产生
        // 4.改为绑定多个设备 任一设备满足以上三种情况 业务平台此告警不产生
        LambdaQueryWrapper<IocDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IocDevice::getIotDeviceDn, kafkaModel.getDeviceIds());
        queryWrapper.apply("id = (SELECT MAX(id) FROM bbx_ioc_device d2 WHERE d2.iot_device_dn = bbx_ioc_device.iot_device_dn)");
        List<IocDevice> iocDevices = iIocDeviceService.selectList(queryWrapper);
        if (iocDevices.isEmpty()) {
            return true;
        } else {
            if (iocDevices.size() < kafkaModel.getDeviceIds().size()) {
                return true;
            }
            for (IocDevice iocDevice : iocDevices) {
                if (Status.disabled.getKey().equals(iocDevice.getDeleted()) || Status.disabled.getKey().equals(iocDevice.getEnableStatus()) || Status.disabled.getKey().equals(iocDevice.getOnlineStatus())) {
                    return true;
                }
            }
        }
        kafkaModel.setIocDeviceList(iocDevices);
        kafkaModel.setTenantId(iocDevices.get(0).getTenantId());
        return false;
    }


    /**
     * 第一次告警信息入库
     *
     * @param kafkaModel
     * @return
     */
    private AlarmInfo buildAlarmInfo(ThingModelKaFkaMessage kafkaModel, Integer num) {
        AlarmInfo info = new AlarmInfo();
        info.setAlarmRuleType(kafkaModel.getAlterName());
        info.setAlarmName(kafkaModel.getAlterName());
        info.setAlarmUnique(buildAlarmUnique(kafkaModel));
        info.setAlarmLevel(kafkaModel.getLevel());

        info.setDeviceDn(buildAlarmUnique(kafkaModel));
        info.setAlarmStatus(AlarmInfoConstant.ALARM_STATUS_1);
        info.setAlarmFirstTime(DateUtil.parseDate(kafkaModel.getOccurred()));
        info.setAlarmLastTime(DateUtil.parseDate(kafkaModel.getOccurred()));
        info.setAlarmCount(num);
        info.setAlarmSource(AlarmInfoConstant.ALARM_SOURCE_1);
        info.setAlarmDesc(kafkaModel.getContent());
        info.setTenantId(kafkaModel.getTenantId());
        //告警规则id
        info.setAlarmRuleId(kafkaModel.getRuleId());
        //设备名字拼接
        info.setAlarmDevice(kafkaModel.getIocDeviceList().stream().map(IocDevice::getDeviceName).collect(Collectors.joining("、")));
        //获取告警的公共空间
        getAlarmsPace(kafkaModel, info);
        return info;
    }

    /**
     * 处理从IoC设备中提取报警位置信息，并设置告警信息中的空间地址。
     *
     * @param kafkaModel 包含Kafka消息数据的模型，用于获取报警相关信息。
     * @param info       告警信息对象，用于存储或更新报警详情。
     */
    private void getAlarmsPace(ThingModelKaFkaMessage kafkaModel, AlarmInfo info) {
        // 从IoC设备列表中提取所有空间ID。
        List<Long> spaceIds = kafkaModel.getIocDeviceList().stream()
                .map(IocDevice::getSpaceId).filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 查询并获取所有空间的完整路径信息。
        Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(spaceIds, null);

        // 如果查询到的空间信息不为空，则进一步处理。
        if (fullSpaceMap != null && !fullSpaceMap.isEmpty()) {
            kafkaModel.setFullSpaceMap(fullSpaceMap);
            // 提取所有空间的完整路径和ID路径。
            List<String> fullPaths = fullSpaceMap.values().stream()
                    .map(ParkSpaceFullModel::getFullPath)
                    .collect(Collectors.toList());
            List<String> idFullPaths = fullSpaceMap.values().stream()
                    .map(ParkSpaceFullModel::getIdFullPath)
                    .collect(Collectors.toList());

            // 查找所有路径的最长公共路径。
            String commonPath = findCommonPath(fullPaths);
            String commonIdPath = findIdCommonPath(idFullPaths);
            if (commonPath.endsWith("/")) {
                commonPath = commonPath.substring(0, commonPath.length() - 1);
            }
            if (StringUtils.isNotEmpty(commonIdPath)) {
                String[] split = commonIdPath.split("-");
                commonIdPath = split[split.length - 1];
            }
            // 设置告警信息中的空间地址和ID路径。
            info.setSpaceAddr(commonPath);
            String[] split = commonIdPath.split("/");
            info.setSpaceAddrId(split[split.length - 1]);
        }
    }


    /**
     * 查找多个路径中的最长公共路径。
     *
     * @param paths 路径列表，每个路径为一个字符串。
     * @return 返回最长的公共路径字符串，如果没有公共路径或输入列表为空，则返回空字符串。
     */
    private String findCommonPath(List<String> paths) {
        // 检查输入路径列表是否为空，如果为空则返回空字符串。
        if (paths == null || paths.isEmpty()) {
            return "";
        }

        // 将第一个路径拆分为路径段，用于后续比较。
        String[] firstPathParts = paths.get(0).split("/");
        // 初始化一个StringBuilder来构建公共路径。
        StringBuilder commonPath = new StringBuilder();

        // 遍历第一个路径的每个段。
        for (int i = 0; i < firstPathParts.length; i++) {
            String currentPart = firstPathParts[i];
            // 遍历所有路径以比较当前段。
            for (String path : paths) {
                String[] parts = path.split("/");
                // 如果当前段不匹配或超出路径长度，则返回当前构建的公共路径。
                if (i >= parts.length || !parts[i].equals(currentPart)) {
                    return commonPath.toString();
                }
            }
            // 如果所有路径在当前位置都有相同的段，则将其添加到公共路径中。
            commonPath.append(currentPart).append("/");
        }

        // 返回构建的公共路径。
        return commonPath.toString();
    }


    private String findIdCommonPath(List<String> paths) {
        // 检查输入路径列表是否为空，如果为空则返回空字符串。
        if (paths == null || paths.isEmpty()) {
            return "";
        }

        // 将第一个路径拆分为路径段，用于后续比较。
        String[] firstPathParts = paths.get(0).split("-");
        // 初始化一个StringBuilder来构建公共路径。
        StringBuilder commonPath = new StringBuilder();

        // 遍历第一个路径的每个段。
        for (int i = 0; i < firstPathParts.length; i++) {
            String currentPart = firstPathParts[i];
            // 遍历所有路径以比较当前段。
            for (String path : paths) {
                String[] parts = path.split("-");
                // 如果当前段不匹配或超出路径长度，则返回当前构建的公共路径。
                if (i >= parts.length || !parts[i].equals(currentPart)) {
                    return commonPath.toString();
                }
            }
            // 如果所有路径在当前位置都有相同的段，则将其添加到公共路径中。
            commonPath.append(currentPart).append("-");
        }

        // 返回构建的公共路径。
        return commonPath.toString();
    }

    /**
     * 构建告警唯一编码
     *
     * @param kafkaModel
     * @return
     */
    private String buildAlarmUnique(ThingModelKaFkaMessage kafkaModel) {
        //变更为所有设备DN拼接
        return String.join("&", kafkaModel.getDeviceIds());
    }

    /**
     * 告警通知等级配置
     *
     * @return
     */
    private List<String> isSendNoticeLevel() {
        return Arrays.asList(noticeLevel.split(","));
    }
}
