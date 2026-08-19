
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.AlarmInfo;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.service.IAlarmInfoService;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.device.service.ISceneControlService;
import com.cgnpc.bbxpark.meeting.domain.*;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantRoomRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomRepository;
import com.cgnpc.bbxpark.meeting.service.*;
import com.cgnpc.bbxpark.space.domain.SpaceManager;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceManagerModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ISpaceManagerService;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 * @Description 会议预约服务实现
 * @author huangyongtao
 * @date 2024/8/26 11:11
 */
@Slf4j
@Service("meetingReserveService")
public class MeetingReserveServiceImpl extends ServiceImpl<MeetingReserveRepository, MeetingReserve> implements IMeetingReserveService {

    @Autowired
    private IUserSpaceService userSpaceService;

    @Autowired
    IDepartmentApiService departmentApiService;

    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private IMeetingReserveSignService meetingReserveSignService;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private IMeetingReserveFileService meetingReserveFileService;

    @Autowired
    private IMessageCommonService messageCommonService;

    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executorService;

    @Autowired
    @Qualifier("asyncEventBusExecutor")
    private Executor busExecutorService;

    @Autowired
    private IMeetingRoomDeviceRelService meetingRoomDeviceRelService;

    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Autowired
    private IMeetingReserveSeatService meetingReserveSeatService;

    @Autowired
    private IMeetingAttendantTaskService meetingAttendantTaskService;

    @Autowired
    private IMeetingTempReserveService meetingTempReserveService;

    @Autowired
    private MeetingAttendantRoomServiceImpl meetingAttendantRoomServiceImpl;
    @Autowired
    private MeetingAttendantRoomRepository meetingAttendantRoomRepository;

    @Autowired
    private IIocDeviceService iocDeviceService;

    @Value("${iot.meetingDevice.productKey:fJY3PeX6fh7nK2TZ}")
    private String meetingDeviceProductKey;

    @Value("${hrcenter.department.cangnan:10010}")
    private String parentDepartmentId;

    @Autowired
    private ISceneControlService sceneControlService;

    @Autowired
    private ISpaceManagerService spaceManagerService;

    /**
     * 获取会议预约列表(分页).
     *
     * @Param param 会议预约查询条件
     * @Return 会议预约信息列表（分页）
     */
    @Override
    public IPage<MeetingReserveModel> page(MeetingReservePageParam param) {
        //权限校验
        Boolean admin = userApiService.parkAdmin();
        if (admin) {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        } else {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            param.setUserId(WebFrameworkUtils.getHeaderUserId());
        }
        if (ObjectUtil.isNotEmpty(param.getUserId())) {
            //普通角色
            List<Long> spaceIds = userSpaceService.findSpaceIds();
            if (CollectionUtils.isEmpty(spaceIds)) {
                return new Page<>(param.getCurrent(), param.getSize(), 0);
            }
            param.setSpaceIdList(spaceIds);
        }
        IPage<MeetingReserveModel> iPage = new Page<>(param.getCurrent(), param.getSize());
        IPage<MeetingReserveModel> page = this.getBaseMapper().pageReserve(iPage, param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        return ConvertUtil.pageConvert(page,page.getRecords());
    }

    /**
     * 获取我的会议列表(分页).
     *
     * @Param param 会议查询条件
     * @Return 会议信息列表（分页）
     */
    @Override
    public IPage<MeetingReserveModel> myPage(MeetingReservePageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
        IPage<MeetingReserveModel> iPage = new Page<>(param.getCurrent(), param.getSize());
        IPage<MeetingReserveModel> page = this.getBaseMapper().pageMyReserve(iPage, param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        return ConvertUtil.pageConvert(page,page.getRecords());
    }

    @Override
    public IPage<MeetingReserveFileModel> pageFile(MeetingReserveFilePageParam param) {
        IPage<MeetingReserveFileModel> page = meetingReserveFileService.pageBy(param.getReserveId(), param.getCurrent(), param.getSize());
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return page;
        }
        List<String> userIds = page.getRecords().stream().filter(f -> f.getCreatorId() != null)
                .map(MeetingReserveFileModel::getCreatorId).collect(Collectors.toList());
        //查询用户信息
        if (!CollectionUtils.isEmpty(userIds)) {
            List<UserInfoModel> userList = userApiService.getByStaffNos(userIds);
            Map<String, UserInfoModel> userMap = userList.stream().collect(Collectors.toMap(UserInfoModel::getId, user -> user));
            page.getRecords().stream().filter(f -> f.getCreatorId() != null).forEach(file -> {
                file.setUserName(userMap.get(file.getCreatorId()).getUserName());
                file.setStaffid(userMap.get(file.getCreatorId()).getStaffid());
            });
        }
        //是否创建人
        Optional.ofNullable(WebFrameworkUtils.getHeaderUserId()).ifPresent(userId ->
                page.getRecords().stream().filter(f -> f.getCreatorId() != null).forEach(file -> file.setOneself(userId.equals(file.getCreatorId()))));
        return page;
    }

    @Override
    public Boolean updateRealEndTime(MeetingReserveTimeParam param) {
        MeetingReserve reserve = getById(param.getId());
        //通用操作检查
        commonCheck(reserve);
        //管理员操作，不校验状态
        reserve.setRealEndTime(param.getRealEndTime());
        //更改有效信息
        if (Status.disabled.getKey().equals(reserve.getInValidFlag())) {
            UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
            reserve.setOperateUid(user.getId());
            reserve.setOperateUname(user.getUserName());
            reserve.setOperateStaffid(user.getStaffid());
            reserve.setOperateTime(param.getRealEndTime());
        }
        AssertUtils.isTrue(updateById(reserve), "操作无效");
        return true;
    }

    @Override
    public Boolean valid(MeetingReserveValidParam param) {
        AssertUtils.isTrue(inValidConfig(param.getId(), param.getOperateReason(), Status.disabled.getKey()), "操作无效");
        return true;
    }

    @Override
    public Boolean inValid(MeetingReserveInValidParam param) {
        AssertUtils.isTrue(inValidConfig(param.getId(), param.getOperateReason(), Status.enabled.getKey()), "操作无效");
        return true;
    }

    @Override
    public MeetingReserveDetailModel detail(Long id) {
        MeetingReserve meetingReserve = getById(id);
        AssertUtils.isFalse(ObjectUtil.isEmpty(meetingReserve) || !WebFrameworkUtils.getHeaderTenantId().equals(meetingReserve.getTenantId()), "当前园区无该会议预约信息，请知悉");
        MeetingReserveDetailModel model = BeanUtils.convertTo(meetingReserve, MeetingReserveDetailModel::new);
        model.setRealStartReason(MeetingReserveStartTypeEnum.getName(meetingReserve.getRealStartType()));
        model.setRealEndReason(MeetingReserveEndTypeEnum.getName(meetingReserve.getRealEndType()));
        //座位
        Optional.ofNullable(meetingRoomRepository.selectById(meetingReserve.getRoomId())).ifPresent(room -> model.setRowSeat(room.getRowSeat()));
        model.setSeatList(meetingReserveSeatService.findByReserveId(id));
        return model;
    }

    @Override
    public Boolean saveFile(AppMeetingFileParam param) {
        return meetingReserveFileService.add(BeanUtils.convertTo(param, MeetingReserveFile::new));
    }

    @Override
    public Boolean removeFile(Long id) {
        MeetingReserveFile file = meetingReserveFileService.getById(id);
        AssertUtils.notNull(file, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.isTrue(file.getCreatorId().equals(WebFrameworkUtils.getHeaderUserId()), "仅能删除自己上传的文件");
        return meetingReserveFileService.remove(id);
    }

    @Override
    public AppMeetingReserveCountModel pageCount() {
        AppMeetingReservePageParam param = new AppMeetingReservePageParam();
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        param.setUserId(WebFrameworkUtils.getHeaderUserId());

        AppMeetingReserveCountModel countModel = new AppMeetingReserveCountModel();
        //今天会议数量
        param.setStartTime(com.cgnpc.bbxpark.common.utils.DateUtil.getFirstTimeOfDate(new Date()));
        param.setEndTime(com.cgnpc.bbxpark.common.utils.DateUtil.getLastTimeOfDate(new Date()));
        countModel.setTodayCount(getBaseMapper().pageCount(param));
        //未来两日会议数量
        param.setStartTime(com.cgnpc.bbxpark.common.utils.DateUtil.getFirstTimeOfDateOffset(new Date(), 1));
        param.setEndTime(com.cgnpc.bbxpark.common.utils.DateUtil.getLastTimeOfDateOffset(new Date(), 2));
        countModel.setTwoDayCount(getBaseMapper().pageCount(param));
        return countModel;
    }

    @Override
    public IPage<AppMeetingReserveModel> pageApp(AppMeetingReservePageParam param) {
        //租户隔离
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
//        param.setCancelFlag((int) Status.disabled.getKey());

        IPage<AppMeetingReserveModel> page = getBaseMapper().pageApp(new Page<>(param.getCurrent(), param.getSize()), param);
        List<AppMeetingReserveModel> list = page.getRecords();
        if (CollectionUtil.isEmpty(list)) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        //告警数据组装
        List<Long> roomIdList = list.stream().map(AppMeetingReserveModel::getRoomId).collect(Collectors.toList());
        List<MeetingRoomDeviceRel> roomDeviceRels = meetingRoomDeviceRelService.getBaseMapper().selectList(Wrappers.<MeetingRoomDeviceRel>lambdaQuery().in(MeetingRoomDeviceRel::getRoomId, roomIdList));
        Map<Long, List<MeetingRoomDeviceRel>> roomDeviceRelMap = CollectionUtils.isEmpty(roomDeviceRels) ? null : roomDeviceRels.stream().collect(Collectors.groupingBy(MeetingRoomDeviceRel::getRoomId));
        //数据组装
        List<Long> spaceIdList = list.stream().map(AppMeetingReserveModel::getSpaceId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> map = CollectionUtils.isEmpty(spaceIdList) ? null : parkSpaceService.findFullSpaceMap(spaceIdList, param.getTenantId());
        //空间责任人
        List<SpaceManager> spaceManagers = spaceManagerService.list(Wrappers.<SpaceManager>lambdaQuery()
                //会议室运维人员
                .eq(SpaceManager::getType, 8)
                .eq(SpaceManager::getDeleted, Status.enabled.getKey())
                .in(CollectionUtil.isNotEmpty(spaceIdList), SpaceManager::getSpaceId, spaceIdList));
        Map<Long, SpaceManager> sapceMangerMap = CollectionUtils.isEmpty(spaceManagers) ? Collections.emptyMap() : spaceManagers.stream().collect(Collectors.toMap(SpaceManager::getSpaceId, Function.identity(), (k1,k2)->k1));
        list.forEach(model -> {
            //空间路径
            if (map != null && model.getSpaceId() != null && map.containsKey(model.getSpaceId())) {
                Optional.ofNullable(map.get(model.getSpaceId())).ifPresent(space -> model.setSpaceName(space.getFullPath()));
            }
            //签到人数量
            Integer count = meetingReserveSignService.count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, model.getId()).ne(MeetingReserveSign::getType, MeetingSignTypeEnum.NOT.getCode()));
            model.setSignCount(Long.valueOf(count));

            //设备告警信息
            if (roomDeviceRelMap != null && roomDeviceRelMap.containsKey(model.getRoomId())) {
                List<Long> deviceIds = roomDeviceRelMap.get(model.getRoomId()).stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
                model.setDeviceNum(Long.valueOf(deviceIds.size()));
                List<AlarmInfo> alarmInfos = alarmInfoService.findAlarmInfoList(deviceIds);
                Optional.ofNullable(alarmInfos).ifPresent(infos -> model.setAlarmInfoModels(BeanUtils.convertListTo(alarmInfos, AlarmInfoModel::new)));
                Optional.ofNullable(sapceMangerMap.get(model.getSpaceId())).ifPresent(manager -> model.setSpaceManagerModel(BeanUtils.convertTo(sapceMangerMap.get(model.getSpaceId()), SpaceManagerModel::new)));
            }
        });
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), list);
    }

    @Override
    public IPage<AppMeetingReserveModel> attendantPageApp(AppMeetingReservePageParam param) {
        List<MeetingAttendantRoom> roomList = meetingAttendantRoomServiceImpl.listByUserId(WebFrameworkUtils.getHeaderUserId());
        if (CollectionUtil.isEmpty(roomList)) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        param.setRoomIdList(roomList.stream().map(MeetingAttendantRoom::getRoomId).collect(Collectors.toList()));
        return pageApp(param);
    }

    @Override
    @SneakyThrows
    public AppMeetingReserveDetailModel detailApp(Long id) {
        MeetingReserve meetingReserve = getById(id);
        AssertUtils.isFalse(ObjectUtil.isEmpty(meetingReserve) || !WebFrameworkUtils.getHeaderTenantId().equals(meetingReserve.getTenantId()), "当前园区无该会议预约信息，请知悉");
        //数据组装
        AppMeetingReserveDetailModel model = BeanUtils.convertTo(meetingReserve, AppMeetingReserveDetailModel::new);
        model.setRealStartReason(MeetingReserveStartTypeEnum.getName(meetingReserve.getRealStartType()));
        model.setRealEndReason(MeetingReserveEndTypeEnum.getName(meetingReserve.getRealEndType()));
        //是否即将开始
        model.setStartFlag(DateUtil.between(meetingReserve.getStartTime(), new Date(), DateUnit.MINUTE) <= 30);
        //是否系统结束
        model.setSysFinishFlag(MeetingReserveStatusEnum.END.getCode().equals(meetingReserve.getStatus()) && MeetingReserveEndTypeEnum.AUTO.getCode().equals(meetingReserve.getRealEndType()));
        //是否发起人
        model.setReserveFlag(meetingReserve.getReserveUid().equals(WebFrameworkUtils.getHeaderUserId()));
        //会议室相关信息
        CompletableFuture<Void> roomFuture = CompletableFuture.runAsync(() -> {
            if (meetingReserve.getRoomId() != null) {
                MeetingRoom room = meetingRoomRepository.selectById(meetingReserve.getRoomId());
                model.setWarnContent(room.getWarnContent());
                model.setPrint(room.getPrint());
                model.setRowSeat(room.getRowSeat());
                if (ObjectUtil.isNotEmpty(room.getSpaceId())) {
                    Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(room.getSpaceId()), WebFrameworkUtils.getHeaderTenantId());
                    Optional.ofNullable(fullSpaceMap.get(room.getSpaceId())).ifPresent(space -> model.setSpaceName(space.getFullPath()));
                }
            }
        }, executorService);
        //签到
        CompletableFuture<Void> signFuture = CompletableFuture.runAsync(() -> {
            List<MeetingSignModel> signList = meetingReserveSignService.findByReserveId(id);
            model.setSignCount(CollectionUtil.isEmpty(signList) ? 0 : signList.stream().filter(p->ObjectUtil.isNotEmpty(p.getSignTime())).collect(Collectors.toList()).size());
            model.setSignList(signList);
        }, executorService);
        //本人是否签到信息
        String headerUserId = WebFrameworkUtils.getHeaderUserId();
        CompletableFuture<Void> mySignFuture = CompletableFuture.runAsync(() -> {
            int count = meetingReserveSignService.count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, model.getId()).eq(MeetingReserveSign::getSignUid, headerUserId)
                    .ne(MeetingReserveSign::getType, MeetingSignTypeEnum.NOT.getCode()));
            model.setSignFlag(count > 0 ? true : false);
        }, executorService);
        //附件信息
        CompletableFuture<Void> fileFuture = CompletableFuture.runAsync(() -> {
            List<MeetingReserveFileModel> fileList = meetingReserveFileService.findByReserveId(id);
            model.setFileList(fileList);
        }, executorService);
        //排座信息
        CompletableFuture<Void> seatFuture = CompletableFuture.runAsync(() -> {
            List<MeetingReserveSeatModel> seatList = meetingReserveSeatService.findByReserveId(id);
            model.setSeatList(seatList);
        }, executorService);
        //会服信息
        CompletableFuture<Void> serviceFuture = CompletableFuture.runAsync(() -> {
            List<MeetingAttendantTaskModel> taskList = meetingAttendantTaskService.findByReserveId(id);
            List<MeetingAttendantTaskModel> taskBefore = taskList.stream().filter(p -> MeetingAttendantTaskTypeEnum.BEFORE.getValue().equals(p.getServiceType())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(taskBefore)) {
                model.setServiceStatus(taskBefore.get(0).getServiceStatus());
                model.setTaskId(taskBefore.get(0).getId());
            }
            model.setTaskList(taskList);
        }, executorService);
        //同步
        CompletableFuture.allOf(roomFuture, signFuture, mySignFuture, fileFuture, seatFuture, serviceFuture).get();
        //是否展示排座
        if (CollectionUtil.isNotEmpty(model.getSeatList())) {
            model.setRowSeat(Integer.valueOf(Status.enabled.getKey()));
        }
        //是否展示打印
        long filePintCount = CollectionUtil.isEmpty(model.getFileList()) ? 0L : model.getFileList().stream().filter(item -> Integer.valueOf(Status.enabled.getKey()).equals(item.getPrinting())).count();
        if (filePintCount > 0L) {
            model.setPrint(Integer.valueOf(Status.enabled.getKey()));
        }
        return model;
    }

    @Override
    public AppMeetingReserveSimpleModel getSimple(Long id) {
        MeetingReserve meetingReserve = getById(id);
        AssertUtils.isFalse(ObjectUtil.isEmpty(meetingReserve) || !WebFrameworkUtils.getHeaderTenantId().equals(meetingReserve.getTenantId()), "当前园区无该会议预约信息，请知悉");

        AppMeetingReserveSimpleModel simpleModel = new AppMeetingReserveSimpleModel();
        simpleModel.setId(meetingReserve.getId());
        simpleModel.setLastRuleTime(meetingReserve.getLastRuleTime());
        return simpleModel;
    }

    @Override
    public Boolean editRule(AppMeetingSignRuleParam param) {
        MeetingReserve reserve = getById(param.getId());
        //公用的状态检查
        reserveCheck(reserve);
        //状态检查
        AssertUtils.isTrue(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()), "会议状态已变更，无法更改签到规则");
        BeanUtils.copyProperties(param, reserve);
        reserve.setLastRuleTime(new Date());
        AssertUtils.isTrue(updateById(reserve), "会议状态已变更，无法更改签到规则");
        return true;
    }

    @Override
    public Boolean appSaveFile(AppMeetingFileParam param) {
        MeetingReserve reserve = getById(param.getReserveId());
        //公用的状态检查
        reserveCheck(reserve);
        MeetingReserveFile file = BeanUtils.convertTo(param, MeetingReserveFile::new);
        file.setReserveId(param.getReserveId());
        return meetingReserveFileService.add(file);
    }

    @Override
    public Boolean appRemoveFile(Long id) {
        MeetingReserveFile file = meetingReserveFileService.getById(id);
        AssertUtils.notNull(file, SystemResultCode.RESULT_DATA_NONE.message());
        MeetingReserve reserve = getById(file.getReserveId());
        AssertUtils.notNull(reserve, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.isTrue(reserve.getReserveUid().equals(WebFrameworkUtils.getHeaderUserId()), "非发起人，无权操作");
        //校验会前布置的打印服务
        meetingAttendantTaskService.handleServiceBeforePrint(file.getReserveId(), id);
        //公用的操作检查
        return meetingReserveFileService.remove(id);
    }

    @Override
    public Boolean cancel(Long id) {
        MeetingReserve reserve = getById(id);
        //公用的操作检查
        reserveCheck(reserve);
        //状态检查
        AssertUtils.isTrue(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()), "会议状态已变更，无法取消会议");
        //类型检查
        AssertUtils.isFalse(MeetingReserveTypeEnum.VIDEO.getValue().equals(reserve.getMeetingType()), "视频会议，无法取消会议");
        reserve.setCancelFlag(Status.enabled.getKey());
        reserve.setCancelTime(new Date());
        AssertUtils.isTrue(updateById(reserve), "操作无效");
        //处理取消的会议
        meetingAttendantTaskService.handleTaskCancelReserve(id);
        //会议取消通知
        Map<String, String> variables = new HashMap<>(4);
        variables.put("date", DateUtils.format(reserve.getStartTime(), "yyyy-MM-dd HH:mm") + "-" + DateUtils.format(reserve.getEndTime(), "HH:mm"));
        variables.put("roomName", reserve.getRoomName());
        variables.put("reserveName", reserve.getReserveName());
        Set<String> userIdSet =  this.listByRoomId(reserve.getRoomId());
        messageCommonService.sendMessage(MessageConstant.MEETING_CANCEL, reserve.getTenantId(), null, userIdSet, variables);
        return true;
    }

    @Override
    public Boolean finish(AppMeetingFinishParam param) {
        MeetingReserve reserve = getById(param.getId());
        //公用的操作检查
        commonCheck(reserve);
        if (MeetingReserveEndTypeEnum.RESERVE.getCode().equals(param.getEndType())) {
            AssertUtils.isTrue(reserve.getReserveUid().equals(WebFrameworkUtils.getHeaderUserId()), "非发起人，无权操作");
        }
        //状态检查
        AssertUtils.isTrue(MeetingReserveStatusEnum.GOING.getCode().equals(reserve.getStatus()), "当前会议状态无法结束");
        reserve.setRealEndTime(new Date());
        reserve.setRealEndType(ObjectUtil.isEmpty(param.getEndType()) ? MeetingReserveEndTypeEnum.RESERVE.getCode() : param.getEndType());
        reserve.setStatus(MeetingReserveStatusEnum.END.getCode());
        //更改有效信息
        if (Status.disabled.getKey() == reserve.getInValidFlag()) {
            reserve.setOperateTime(new Date());
        }
        reserve.setDuration(DateUtil.between(reserve.getRealStartTime(), reserve.getRealEndTime(), DateUnit.MINUTE));
        AssertUtils.isTrue(updateById(reserve), "操作无效");
//        if (Status.enabled.getKey() == param.getCloseFlag()) {
        //关闭设备
//        }
        //处理结束的会议
        meetingAttendantTaskService.handleTaskEndReserve(reserve);
        //会议视频设备延时
        try {
            String deviceId = getMeetingDevice(reserve);
            if (ObjectUtil.isNotEmpty(deviceId)) {
                sceneControlService.endMeeting(deviceId);
            }
        } catch (Exception e) {
            log.error("会议视频设备结束会议失败，失败原因：{}", e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean resetRealEndTime(Long id, Date endTime, Integer endType) {
        MeetingReserve reserve = getById(id);
        //公用的操作检查
        commonCheck(reserve);
        if (MeetingReserveEndTypeEnum.RESERVE.getCode().equals(endType)) {
            AssertUtils.isTrue(reserve.getReserveUid().equals(WebFrameworkUtils.getHeaderUserId()), "非发起人，无权操作");
        }
        //状态检查
        AssertUtils.isTrue(MeetingReserveStatusEnum.END.getCode().equals(reserve.getStatus()), "会议未结束，无法重置结束时间");
        AssertUtils.isFalse(MeetingReserveEndTypeEnum.RESERVE.getCode().equals(reserve.getRealEndType()), "会议发起人结束，无法重置结束时间");
        reserve.setRealEndTime(ObjectUtil.isEmpty(endTime) ? new Date() : endTime);
        reserve.setRealEndType(ObjectUtil.isEmpty(endType) ? MeetingReserveEndTypeEnum.RESERVE.getCode() : endType);
        reserve.setDuration(DateUtil.between(reserve.getRealStartTime(), reserve.getRealEndTime(), DateUnit.MINUTE));
        AssertUtils.isTrue(updateById(reserve), "会议状态已变更，无法重置会议结束时间");
        return true;
    }

    @Override
    public AppSimpleReserveModel getNearest() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        String userId = WebFrameworkUtils.getHeaderUserId();
        MeetingReserve reserve = getOne(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(tenantId != null, MeetingReserve::getTenantId, tenantId)
                .eq(MeetingReserve::getReserveUid, userId)
                .ne(MeetingReserve::getStatus, MeetingReserveStatusEnum.END.getCode())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .ge(MeetingReserve::getEndTime, new Date()).orderByAsc(MeetingReserve::getEndTime).last("limit 1"));
        if (reserve == null || reserve.getId() == null) {
            return new AppSimpleReserveModel();
        }
        AppSimpleReserveModel model = BeanUtils.convertTo(reserve, AppSimpleReserveModel::new);
        int signFlag = meetingReserveSignService.count(Wrappers.<MeetingReserveSign>lambdaQuery().eq(MeetingReserveSign::getReserveId, model.getId()).eq(MeetingReserveSign::getSignUid, userId));
        if (signFlag < 1) {
            model.setSignFlag(Boolean.TRUE);
        }
        return model;
    }


    /**
     * 新增会议预约.
     *
     * @Param param 会议预约信息
     * @Return 新增会议预约是否成功
     */
    @Override
    public Boolean add(MeetingSaveParam param) {
        MeetingReserve meetingReserve = BeanUtils.convertTo(param, MeetingReserve::new);
        meetingReserve.setId(null);
        //用户信息
        String userId = WebFrameworkUtils.getHeaderUserId();
        UserInfoModel user = getUser(userId);
        meetingReserve.setReserveUname(user.getUserName());
        meetingReserve.setReserveStaffid(user.getStaffid());
        meetingReserve.setReserveUid(userId);
        meetingReserve.setReserveDepartment(user.getDepartmentName());
        meetingReserve.setLastRuleTime(new Date());
        meetingReserve.setThirdReserveId(param.getThirdReserveId());
        meetingReserve.setOperateTime(meetingReserve.getEndTime());
        meetingReserve.setOperateUname("系统");
        meetingReserve.setOperateReason("正常召开");
        MeetingRoom room = meetingRoomRepository.selectOne(Wrappers.<MeetingRoom>lambdaQuery().eq(MeetingRoom::getThirdRoomId, param.getThirdRoomId()));
        if (room != null) {
            meetingReserve.setRoomId(room.getId());
            meetingReserve.setRoomName(room.getRoomName());
        }
        meetingReserve.setRevision(0);
        return this.save(meetingReserve);
    }

    /**
     * 删除会议预约.
     *
     * @Param id 会议预约标识
     * @Return 删除会议预约是否成功
     */
    @Override
    public Boolean remove(Long id) {
        MeetingReserve meetingReserve = this.getById(id);
        AssertUtils.notNull(meetingReserve, SystemResultCode.RESULT_DATA_NONE.message());
        return this.removeById(id);
    }


    /***
     * @Description 查询会议预约信息
     * @author huangyongtao
     * @date 2024/8/27 17:58
     * @param param
     */
    public List<MeetingReserveModel> findReserve(MeetingReserveListParam param) {
        List<MeetingReserveModel> reserveModels = this.getBaseMapper().findReserve(param);
        return reserveModels;
    }

    /***
     * @Description 会议预约无效任务
     * @author huangyongtao
     * @date 2024/8/29 17:12
     */
    @Override
    public Boolean invalidTask() {
        String dayFormat = DateUtils.formatYMD(new Date());
        Date startTime = DateUtils.format(dayFormat + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        List<MeetingReserve> meetingReserves = this.list(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(MeetingReserve::getInValidFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getRealEndType, MeetingReserveEndTypeEnum.AUTO.getCode())
                .eq(MeetingReserve::getOperateUname, "系统")
                .ge(MeetingReserve::getStartTime, startTime)
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .le(MeetingReserve::getEndTime, new Date()));
        if (CollectionUtil.isEmpty(meetingReserves)) {
            return Boolean.TRUE;
        }
        List<Long> reserveIds = meetingReserves.stream().map(MeetingReserve::getId).collect(Collectors.toList());
        List<MeetingReserveSign> meetingReserveSigns = meetingReserveSignService.list(Wrappers.<MeetingReserveSign>lambdaQuery().in(MeetingReserveSign::getReserveId, reserveIds));
        Map<Long, List<MeetingReserveSign>> signMap = meetingReserveSigns.stream().collect(Collectors.groupingBy(MeetingReserveSign::getReserveId));
        List<Long> noReserveIds = new ArrayList<>();
        meetingReserves.forEach(item -> {
            if (!signMap.containsKey(item.getId())) {
                noReserveIds.add(item.getId());
            }
        });
        if (CollectionUtil.isEmpty(noReserveIds)) {
            return Boolean.TRUE;
        }
        LambdaUpdateWrapper<MeetingReserve> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MeetingReserve::getInValidFlag, Status.enabled.getKey());
        updateWrapper.set(MeetingReserve::getOperateReason, "签到人数为0");
        updateWrapper.set(MeetingReserve::getOperateUname, "系统");
        updateWrapper.set(MeetingReserve::getOperateTime, new Date());
        updateWrapper.in(MeetingReserve::getId, noReserveIds);
        return this.update(updateWrapper);
    }

    /***
     * @Description 更新会议预约实际结束时间任务
     * @author huangyongtao
     * @date 2024/8/29 17:12
     */
    @Override
    public Boolean realEndTimeTask() {
        List<MeetingReserve> meetingReserves = this.list(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getStatus, MeetingReserveStatusEnum.GOING.getCode())
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .le(MeetingReserve::getEndTime, new Date()));
        if (CollectionUtil.isEmpty(meetingReserves)) {
            return Boolean.TRUE;
        }
        meetingReserves.forEach(item -> {
            item.setRealEndTime(item.getEndTime());
            item.setRealEndType(MeetingReserveEndTypeEnum.AUTO.getCode());
            item.setStatus(MeetingReserveStatusEnum.END.getCode());
            item.setDuration(DateUtil.between(item.getRealStartTime(), item.getRealEndTime(), DateUnit.MINUTE));
            meetingAttendantTaskService.handleTaskEndReserve(item);
        });
        return this.updateBatchById(meetingReserves);
    }

    /***
     * 更新会议实际开始时间
     * 并发送会议无人签到提醒
     * @Description 更新会议预约实际开始时间任务
     * @author huangyongtao
     * @date 2024/9/24 11:10
     */
    @Override
    public Boolean realStartTimeTask() {
        List<MeetingReserve> meetingReserves = this.list(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .isNull(MeetingReserve::getRealStartTime)
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .le(MeetingReserve::getStartTime, new Date()));
        if (CollectionUtil.isEmpty(meetingReserves)) {
            return Boolean.TRUE;
        }
        meetingReserves.forEach(item -> {
            item.setRealStartTime(item.getStartTime());
            item.setRealStartType(MeetingReserveStartTypeEnum.AUTO.getCode());
            item.setStatus(MeetingReserveStatusEnum.GOING.getCode());
            meetingAttendantTaskService.handleTaskStartReserve(item.getId(), item.getRoomId());
            //会议无人签到提醒(会服人员)
            Map<String, String> variables = new HashMap<>(4);
            variables.put("date", DateUtils.format(item.getStartTime(), "yyyy-MM-dd HH:mm") + "-" + DateUtils.format(item.getEndTime(), "HH:mm"));
            variables.put("roomName", item.getRoomName());
            variables.put("reserveName", item.getReserveName());
            Set<String> userIdSet =  this.listByRoomId(item.getRoomId(),item.getTenantId());
            messageCommonService.sendMessage(MessageConstant.MEETING_NO_SIGN_WARN_ATTENDANT, item.getTenantId(), item.getId(), userIdSet, variables);
            //会议无人签到提醒(预约人)
            messageCommonService.sendMessage(MessageConstant.MEETING_NO_SIGN_WARN, item.getTenantId(), item.getId(), item.getReserveUid(), variables);
        });
        return this.updateBatchById(meetingReserves);
    }

    private  Set<String> listByRoomId(Long roomId){
        return listByRoomId(roomId,WebFrameworkUtils.getHeaderTenantId());
    }

    private Set<String> listByRoomId(Long roomId,Long tenantId){
        List<MeetingAttendantRoom> list = meetingAttendantRoomRepository.selectList(Wrappers.<MeetingAttendantRoom>lambdaQuery().eq(MeetingAttendantRoom::getRoomId, roomId)
                .eq(MeetingAttendantRoom::getTenantId, tenantId));
        return list.stream().map(MeetingAttendantRoom::getUserId).collect(Collectors.toSet());
    }

    /***
     * @Description 会议预约人签到通知任务
     * @author huangyongtao
     * @date 2024/8/29 19:12
     */
    @Override
    public Boolean signNoticeTask() {
        Date localTime = new Date();
        String dayFormat = DateUtils.formatYMD(localTime);
        Date endTime = DateUtils.format(dayFormat + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        List<MeetingReserve> meetingReserves = this.list(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(MeetingReserve::getInValidFlag, Status.disabled.getKey())
                .le(MeetingReserve::getEndTime, endTime)
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .gt(MeetingReserve::getEndTime, localTime));
        if (CollectionUtil.isEmpty(meetingReserves)) {
            return Boolean.TRUE;
        }
        List<Long> reserveIds = meetingReserves.stream().map(MeetingReserve::getId).collect(Collectors.toList());
        List<MeetingReserveSign> meetingReserveSigns = meetingReserveSignService.list(Wrappers.<MeetingReserveSign>lambdaQuery().in(MeetingReserveSign::getReserveId, reserveIds));
        Map<Long, List<MeetingReserveSign>> signMap = meetingReserveSigns.stream().collect(Collectors.groupingBy(MeetingReserveSign::getReserveId));
        List<Long> noReserveIds = new ArrayList<>();
        for (MeetingReserve item : meetingReserves) {
            if (!signMap.containsKey(item.getId())) {
                noReserveIds.add(item.getId());
            } else {
                List<MeetingReserveSign> signList = signMap.get(item.getId()).stream().filter(p -> p.getSignUid().equals(item.getReserveUid())).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(signList)) {
                    noReserveIds.add(item.getId());
                }
            }
        }
        if (CollectionUtil.isEmpty(noReserveIds)) {
            return Boolean.TRUE;
        }
        for (MeetingReserve item : meetingReserves) {
            Long diff = DateUtil.between(localTime, item.getEndTime(), DateUnit.MINUTE);
            if (noReserveIds.contains(item.getId()) && diff > 0 && diff <= 5) {
                //this.sendMessage(item);
                //消息通知
                Map<String, String> variables = new HashMap<>(4);
                variables.put("reserveName", item.getReserveName());
                messageCommonService.sendMessage(MessageConstant.MEETING_SIGN_WARN, item.getTenantId(), item.getId(), item.getReserveUid(), variables);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean waitingConfirmNoticeTask() {
        Date localTime = DateUtils.parse(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
        Date thirtyMinutesLater = new Date(localTime.getTime() + TimeUnit.MINUTES.toMillis(30));
        //查出三十分钟后开始的会议
        List<MeetingReserve> meetingList = list(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .eq(MeetingReserve::getInValidFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getStartTime, thirtyMinutesLater));
        if (CollectionUtils.isEmpty(meetingList)) {
            return Boolean.TRUE;
        }
        //查询需要会服确认的会服任务
        List<Long> meetingIds = meetingList.stream().map(MeetingReserve::getId).collect(Collectors.toList());
        List<MeetingAttendantTask> taskList = meetingAttendantTaskService.list(Wrappers.<MeetingAttendantTask>lambdaQuery().in(MeetingAttendantTask::getReserveId, meetingIds)
                .eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()).eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.UNHANDLE.getValue()));
        //有未处理的会议id
        if (CollectionUtils.isEmpty(taskList)) {
            return Boolean.TRUE;
        }
        Map<Long, Long> meetingIdMap = taskList.stream().collect(Collectors.toMap(MeetingAttendantTask::getReserveId, MeetingAttendantTask::getId, (k1, k2) -> k1));
        meetingList.stream().filter(i -> meetingIdMap.containsKey(i.getId())).forEach(item -> {
            //会服待确认提醒
            Map<String, String> variables = new HashMap<>(4);
            variables.put("roomName", item.getRoomName());
            variables.put("reserveName", item.getReserveName());
            Set<String> userIdSet =  this.listByRoomId(item.getRoomId(),item.getTenantId());
            messageCommonService.sendMessage(MessageConstant.WAITING_CONFIRM_WARN, item.getTenantId(), meetingIdMap.get(item.getId()), userIdSet, variables);
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean meetingDeviceWarnTask() {
        Date localTime = DateUtils.parse(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
        Date thirtyMinutesLater = new Date(localTime.getTime() + TimeUnit.MINUTES.toMillis(30));
        //查出三十分钟后开始的会议
        List<MeetingReserve> meetingList = list(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getInValidFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getStartTime, thirtyMinutesLater));
        if (CollectionUtils.isEmpty(meetingList)) {
            return Boolean.TRUE;
        }
        meetingList.forEach(item -> {
            //查询会议室设备
            MeetingRoomDeviceRelListParam param = new MeetingRoomDeviceRelListParam();
            param.setTenantId(item.getTenantId());
            param.setRoomId(item.getRoomId());
            List<MeetingRoomDeviceRelModel> deviceList = meetingRoomDeviceRelService.list(param);
            if (!CollectionUtils.isEmpty(deviceList)) {
                List<Long> deviceIdList = deviceList.stream().map(MeetingRoomDeviceRelModel::getDeviceId).collect(Collectors.toList());
                List<AlarmInfo> alarmInfoList = alarmInfoService.findAlarmInfoList(deviceIdList);
                if (!CollectionUtils.isEmpty(alarmInfoList)) {
                    alarmInfoList.forEach(alarmInfo -> {
                        Map<String, String> variables = new HashMap<>(4);
                        variables.put("roomName", item.getRoomName());
                        variables.put("deviceName", alarmInfo.getAlarmDevice());
                        variables.put("content", alarmInfo.getAlarmRuleType());
                        variables.put("date", DateUtils.format(item.getStartTime(), "yyyy-MM-dd HH:mm") + "-" + DateUtils.format(item.getEndTime(), "HH:mm"));
                        //设备告警提醒-发起人
                        messageCommonService.sendMessage(MessageConstant.DEVICE_WARN, item.getTenantId(), item.getId(), item.getReserveUid(), variables);
                        //设备告警提醒-会服
                        Set<String> userIdSet =  this.listByRoomId(item.getRoomId(),item.getTenantId());
                        messageCommonService.sendMessage(MessageConstant.DEVICE_WARN_ATTENDANT, item.getTenantId(), item.getId(), userIdSet, variables);
                    });
                }
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean meetingDelayTask() {
        Date localDate = DateUtils.parse(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
        Date thirtyMinutesLater = new Date(localDate.getTime() + TimeUnit.MINUTES.toMillis(30));
        //查出三十分钟后结束的会议
        List<MeetingReserve> meetingList = list(Wrappers.<MeetingReserve>lambdaQuery()
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .eq(MeetingReserve::getInValidFlag,  Status.disabled.getKey())
                .eq(MeetingReserve::getEndTime, thirtyMinutesLater)
                .le(MeetingReserve::getStartTime, localDate));
        if (CollectionUtils.isEmpty(meetingList)) {
            return Boolean.TRUE;
        }
        meetingList.forEach(item -> {
            Map<String, String> variables = new HashMap<>(4);
            variables.put("date", DateUtils.format(item.getStartTime(), "yyyy-MM-dd HH:mm") + "-" + DateUtils.format(item.getEndTime(), "HH:mm"));
            variables.put("roomName", item.getRoomName());
            variables.put("reserveName", item.getReserveName());
            messageCommonService.sendMessage(MessageConstant.MEETING_DELAY, item.getTenantId(), item.getId(), item.getReserveUid(), variables);
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean checkTime(MeetingReserveParam param) {
        AssertUtils.isFalse(MeetingReserveTypeEnum.VIDEO.getValue().equals(param.getMeetingType()) && DateUtil.between(param.getStartTime(), param.getEndTime(), DateUnit.MINUTE) < 30, "视频会议的会议时长至少30分钟！");
        AssertUtils.isFalse(new Date().after(param.getStartTime()), "会议预约开始时间已过期！");
        int count = this.count(Wrappers.<MeetingReserve>lambdaQuery().eq(MeetingReserve::getRoomId, param.getRoomId())
                .lt(MeetingReserve::getStartTime, param.getEndTime())
                .gt(MeetingReserve::getEndTime, param.getStartTime())
                .eq(MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .in(MeetingReserve::getStatus, Arrays.asList(MeetingReserveStatusEnum.START.getCode(), MeetingReserveStatusEnum.GOING.getCode()))
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey()));
        AssertUtils.isFalse(count > 0, "会议室该预约时间段已被占用，请重新预约");
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized AppMeetingReserveDraftModel appAdd(MeetingReserveAppSaveParam param) {
        //校验会议室是否存在
        MeetingRoom room = meetingRoomRepository.selectById(param.getRoomId());
        AssertUtils.isTrue(room != null && room.getDeleted().equals(Delete.NORMAL.getKey()),"会议室不存在或已被删除");
        MeetingReserve meetingReserve = BeanUtils.convertTo(param, MeetingReserve::new);
        meetingReserve.setId(null);
        //用户信息
        String userId = WebFrameworkUtils.getHeaderUserId();
        UserInfoModel user = getUser(userId);
        meetingReserve.setReserveUname(user.getUserName());
        meetingReserve.setReserveStaffid(user.getStaffid());
        meetingReserve.setReserveUid(userId);
        meetingReserve.setLastRuleTime(new Date());
        meetingReserve.setOperateTime(meetingReserve.getEndTime());
        meetingReserve.setOperateUname("系统");
        meetingReserve.setOperateReason("正常召开");
        meetingReserve.setRevision(0);
        //二级部门id
        Optional<String[]> depart = findSecondDepart(user.getDepartmentIdPath(),user.getDepartmentNamePath());
        depart.ifPresent(d->{
            meetingReserve.setDepartmentId(d[0]);
            meetingReserve.setDepartmentName(d[1]);
        });
        try {
            checkReserve(meetingReserve, param);
            if (MeetingReserveTypeEnum.VIDEO.getValue().equals(meetingReserve.getMeetingType())) {
                handleMeetingDevice(meetingReserve);
            }
            meetingReserve.setDuration(DateUtil.between(meetingReserve.getStartTime(), meetingReserve.getEndTime(), DateUnit.MINUTE));
        } catch (Exception e) {
            meetingReserve.setDraft(Status.enabled.getKey());
            meetingReserve.setFailReason(e.getMessage());
        }
        this.save(meetingReserve);
        handleReserve(param, meetingReserve);
        return BeanUtils.convertTo(meetingReserve, AppMeetingReserveDraftModel::new);
    }

    private Optional<String[]> findSecondDepart(String departmentIdPath,String departmentNamePath){
        List<String> ids = Arrays.asList(departmentIdPath.split("\\\\"));
        List<String> names = Arrays.asList(departmentNamePath.split("\\\\"));
        return IntStream.range(0,ids.size() - 1).filter(i -> ids.get(i).equals(parentDepartmentId))
                .mapToObj(i -> new String[]{ids.get(i + 1),names.get(i + 1)}).findFirst();
    }

    @Override
    @SneakyThrows
    public AppMeetingReserveDetailModel detailEditApp(Long id) {
        MeetingReserve meetingReserve = getById(id);
        AssertUtils.isFalse(ObjectUtil.isEmpty(meetingReserve) || !WebFrameworkUtils.getHeaderTenantId().equals(meetingReserve.getTenantId()), "当前园区无该会议预约信息，请知悉");
        //数据组装
        AppMeetingReserveDetailModel model = BeanUtils.convertTo(meetingReserve, AppMeetingReserveDetailModel::new);

        //参会人信息
        CompletableFuture<Void> signFuture = CompletableFuture.runAsync(() -> {
            List<MeetingSignModel> signList = meetingReserveSignService.findByReserveId(id);
            model.setSignList(signList.stream().filter(p -> Integer.valueOf(Status.enabled.getKey()).equals(p.getInvited())).collect(Collectors.toList()));
        }, executorService);
        //附件信息
        CompletableFuture<Void> fileFuture = CompletableFuture.runAsync(() -> {
            List<MeetingReserveFileModel> fileList = meetingReserveFileService.findByReserveId(id);
            model.setFileList(fileList);
        }, executorService);
        //排座信息
        CompletableFuture<Void> seatFuture = CompletableFuture.runAsync(() -> {
            List<MeetingReserveSeatModel> seatList = meetingReserveSeatService.findByReserveId(id);
            model.setSeatList(seatList);
        }, executorService);
        //同步
        CompletableFuture.allOf(signFuture, fileFuture, seatFuture).get();
        return model;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AppMeetingReserveDraftModel appEdit(MeetingReserveAppSaveParam param) {
        //校验会议室是否存在
        MeetingRoom room = meetingRoomRepository.selectById(param.getRoomId());
        AssertUtils.isTrue(room != null && room.getDeleted().equals(Delete.NORMAL.getKey()),"会议室不存在或已被删除");
        MeetingReserve meetingReserve = BeanUtils.convertTo(param, MeetingReserve::new);
        meetingReserve.setLastRuleTime(new Date());
        meetingReserve.setOperateTime(meetingReserve.getEndTime());
        try {
            checkReserve(meetingReserve, param);
            if (MeetingReserveTypeEnum.VIDEO.getValue().equals(meetingReserve.getMeetingType())) {
                handleMeetingDevice(meetingReserve);
            }
            meetingReserve.setDraft(Status.disabled.getKey());
            meetingReserve.setFailReason("");
            meetingReserve.setDuration(DateUtil.between(meetingReserve.getStartTime(), meetingReserve.getEndTime(), DateUnit.MINUTE));
        } catch (Exception e) {
            meetingReserve.setDraft(Status.enabled.getKey());
            meetingReserve.setFailReason(e.getMessage());
        }
        this.updateById(meetingReserve);

        //删除参会人
        meetingReserveSignService.removeByReserveId(meetingReserve.getId());
        //删除会议文件
        meetingReserveFileService.removeByReserveId(meetingReserve.getId());
        //删除会议排座
        meetingReserveSeatService.removeByReserveId(meetingReserve.getId());
        //删除会服
        meetingAttendantTaskService.removeByReserveId(meetingReserve.getId());
        handleReserve(param, meetingReserve);
        return BeanUtils.convertTo(meetingReserve, AppMeetingReserveDraftModel::new);
    }

    @Override
    public Boolean appDelete(MeetingReserveAppSaveParam param) {
        MeetingReserve reserve = this.getById(param.getId());
        AssertUtils.isFalse(ObjectUtil.isEmpty(reserve) || !WebFrameworkUtils.getHeaderTenantId().equals(reserve.getTenantId()), "当前园区无该会议预约信息，请知悉");
        AssertUtils.isFalse(Integer.valueOf(Status.disabled.getKey()).equals(reserve.getDraft()), "不是会议草稿，无法删除");
        //删除参会人
        meetingReserveSignService.removeByReserveId(param.getId());
        //删除会议文件
        meetingReserveFileService.removeByReserveId(param.getId());
        //删除会议排座
        meetingReserveSeatService.removeByReserveId(param.getId());
        //删除会服
        meetingAttendantTaskService.removeByReserveId(param.getId());
        return this.removeById(param.getId());
    }

    @Override
    public IPage<AppMeetingReserveModel> draftPage(AppMeetingReservePageParam param) {
        LambdaQueryWrapper<MeetingReserve> query = new LambdaQueryWrapper<>();
        query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderUserId()), MeetingReserve::getReserveUid, WebFrameworkUtils.getHeaderUserId());
        query.eq(MeetingReserve::getDraft, Status.enabled.getKey());
        query.orderByDesc(MeetingReserve::getCreateTime);
        IPage<MeetingReserve> page = this.page(new Page<>(param.getCurrent(), param.getSize()), query);

        if (CollectionUtil.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        List<AppMeetingReserveModel> list = BeanUtils.convertListTo(page.getRecords(), AppMeetingReserveModel::new);
        //数据组装
        List<Long> roomIds = list.stream().map(AppMeetingReserveModel::getRoomId).collect(Collectors.toList());
        List<MeetingRoom> rooms = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery().select(MeetingRoom::getSpaceId).in(MeetingRoom::getId, roomIds));
        List<Long> spaceIdList = rooms.stream().map(MeetingRoom::getSpaceId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> map = CollectionUtils.isEmpty(spaceIdList) ? null : parkSpaceService.findFullSpaceMap(spaceIdList, param.getTenantId());
        list.forEach(model -> {
            //空间路径
            if (map != null && model.getSpaceId() != null && map.containsKey(model.getSpaceId())) {
                Optional.ofNullable(map.get(model.getSpaceId())).ifPresent(space -> model.setSpaceName(space.getFullPath()));
            }
        });
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), list);
    }

    @Override
    public Boolean delay(MeetingReserveDelayParam param) {
        MeetingReserve reserve = getById(param.getId());
        //公用的操作检查
        commonCheck(reserve);
        //状态检查
        AssertUtils.isFalse(MeetingReserveStatusEnum.END.getCode().equals(reserve.getStatus()), "会议状态已变更，无法延时会议");
        Date endTime = reserve.getEndTime();
        Date endTimeMax = DateUtil.endOfDay(endTime);
        Date endTimeEnd = DateUtil.offsetMinute(endTime, param.getDelayTime());
        AssertUtils.isFalse(endTimeEnd.after(endTimeMax), "会议延时失败，失败原因：会议结束时间不能跨天");

        MeetingReserve reserveNow = this.getOne(Wrappers.<MeetingReserve>lambdaQuery().eq(MeetingReserve::getRoomId, reserve.getRoomId())
                .lt(MeetingReserve::getStartTime, endTimeEnd)
                .gt(MeetingReserve::getEndTime, endTime)
                .eq(MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .in(MeetingReserve::getStatus, Arrays.asList(MeetingReserveStatusEnum.START.getCode(), MeetingReserveStatusEnum.GOING.getCode()))
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag,  Status.disabled.getKey())
                .orderByAsc(MeetingReserve::getStartTime).last("limit 1"));
        if (ObjectUtil.isNotEmpty(reserveNow)) {
            String startTimeNow = DateUtil.format(reserveNow.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            String endTimeNow = DateUtil.format(reserveNow.getEndTime(), "yyyy-MM-dd HH:mm:ss");
            throw GenericException.fail(String.format("会议延时失败，失败原因：与下一次会议时间（%s - %s）冲突", startTimeNow, endTimeNow));
        }

        MeetingTempReserve tempReserveNow = meetingTempReserveService.getOne(Wrappers.<MeetingTempReserve>lambdaQuery().eq(MeetingTempReserve::getRoomId, reserve.getRoomId())
                .lt(MeetingTempReserve::getStartTime, endTimeEnd)
                .gt(MeetingTempReserve::getEndTime, endTime)
                .eq(MeetingTempReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .orderByAsc(MeetingTempReserve::getStartTime).last("limit 1"));
        if (ObjectUtil.isNotEmpty(tempReserveNow)) {
            String startTimeNow = DateUtil.format(tempReserveNow.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            String endTimeNow = DateUtil.format(tempReserveNow.getEndTime(), "yyyy-MM-dd HH:mm:ss");
            throw GenericException.fail(String.format("会议延时失败，失败原因：与下一次会议时间（%s - %s）冲突", startTimeNow, endTimeNow));
        }
        //会议视频设备延时
        try {
            String deviceId = getMeetingDevice(reserve);
            if (ObjectUtil.isNotEmpty(deviceId)) {
                sceneControlService.extendMeeting(deviceId, param.getDelayTime());
            }
        } catch (Exception e) {
            log.error("会议视频设备延时失败，失败原因：{}", e.getMessage());
        }
        MeetingReserve reserveNew = new MeetingReserve();
        reserveNew.setId(param.getId());
        reserveNew.setEndTime(endTimeEnd);
        reserveNew.setDelayTime(reserve.getDelayTime() + param.getDelayTime());
        reserveNew.setDuration(DateUtil.between(reserve.getStartTime(), endTimeEnd, DateUnit.MINUTE));
        return this.updateById(reserveNew);
    }

    @Override
    public Boolean call(Long id) {
        MeetingReserve reserve = getById(id);
        commonCheck(reserve);
        //状态检查
        AssertUtils.isFalse(MeetingReserveStatusEnum.END.getCode().equals(reserve.getStatus()), "会议状态已变更，无法呼叫会服");
        meetingAttendantTaskService.handleTaskCallReserve(reserve);
        return true;
    }

    @SneakyThrows
    private AppMeetingReserveDetailModel serviceBeforeDetail(MeetingAttendantTask meetingAttendantTask, MeetingReserve meetingReserve) {
        //数据组装
        AppMeetingReserveDetailModel model = BeanUtils.convertTo(meetingReserve, AppMeetingReserveDetailModel::new);
        model.setStartFlag(DateUtil.between(meetingReserve.getStartTime(), new Date(), DateUnit.MINUTE) <= 30);
        //设置会服任务的信息
        model.setServiceStatus(meetingAttendantTask.getServiceStatus());
        model.setServiceType(meetingAttendantTask.getServiceType());
        model.setHandleUid(meetingAttendantTask.getHandleUid());
        model.setHandleUname(meetingAttendantTask.getHandleUname());
        model.setHandleStaffid(meetingAttendantTask.getHandleStaffid());
        model.setHandleTime(meetingAttendantTask.getHandleTime());
        UserInfoModel user = getUser(model.getReserveUid());
        model.setReserveMobile(user.getMobile());
        //会议室相关信息
        CompletableFuture<Void> roomFuture = CompletableFuture.runAsync(() -> {
            if (meetingReserve.getRoomId() != null) {
                MeetingRoom room = meetingRoomRepository.selectById(meetingReserve.getRoomId());
                model.setWarnContent(room.getWarnContent());
                model.setPrint(room.getPrint());
                model.setRowSeat(room.getRowSeat());
                if (ObjectUtil.isNotEmpty(room.getSpaceId())) {
                    Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(room.getSpaceId()), WebFrameworkUtils.getHeaderTenantId());
                    Optional.ofNullable(fullSpaceMap.get(room.getSpaceId())).ifPresent(space -> model.setSpaceName(space.getFullPath()));
                }
            }
        }, executorService);
        //附件信息
        CompletableFuture<Void> fileFuture = CompletableFuture.runAsync(() -> {
            List<MeetingReserveFileModel> fileList = meetingReserveFileService.findByReserveId(meetingReserve.getId());
            model.setFileList(fileList);
        }, executorService);
        //排座信息
        CompletableFuture<Void> seatFuture = CompletableFuture.runAsync(() -> {
            List<MeetingReserveSeatModel> seatList = meetingReserveSeatService.findByReserveId(meetingReserve.getId());
            model.setSeatList(seatList);
        }, executorService);
        //会服信息
        CompletableFuture<Void> serviceFuture = CompletableFuture.runAsync(() -> {
            List<MeetingAttendantTaskDetailModel> taskDetailList = meetingAttendantTaskService.serviceDetailByTaskId(meetingAttendantTask.getId());
            model.setTaskDetailList(taskDetailList);
        }, executorService);
        //同步
        CompletableFuture.allOf(roomFuture, fileFuture, seatFuture, serviceFuture).get();
        return model;
    }

    @Override
    @SneakyThrows
    public AppMeetingReserveDetailModel serviceDetail(Long id, Integer ServiceType) {
        MeetingAttendantTask meetingAttendantTask = meetingAttendantTaskService.getById(id);
        AssertUtils.isFalse(ObjectUtil.isEmpty(meetingAttendantTask) || !WebFrameworkUtils.getHeaderTenantId().equals(meetingAttendantTask.getTenantId()), "当前园区无该会服信息，请知悉");
        MeetingReserve meetingReserve = getById(meetingAttendantTask.getReserveId());
        AssertUtils.isFalse(ObjectUtil.isEmpty(meetingReserve) || !WebFrameworkUtils.getHeaderTenantId().equals(meetingReserve.getTenantId()), "当前园区无该会议预约信息，请知悉");
        if (MeetingAttendantTaskTypeEnum.BEFORE.getValue().equals(ServiceType) || MeetingAttendantTaskTypeEnum.BEFORE.getValue().equals(meetingAttendantTask.getServiceType())) {
            return this.serviceBeforeDetail(meetingAttendantTask, meetingReserve);
        }
        //数据组装
        AppMeetingReserveDetailModel model = BeanUtils.convertTo(meetingReserve, AppMeetingReserveDetailModel::new);
        model.setServiceStatus(meetingAttendantTask.getServiceStatus());
        model.setServiceType(meetingAttendantTask.getServiceType());
        model.setResetEndTimeFlag(!MeetingReserveEndTypeEnum.RESERVE.getCode().equals(model.getRealEndType()));
        UserInfoModel user = getUser(model.getReserveUid());
        model.setReserveMobile(user.getMobile());
        //会议室相关信息
        CompletableFuture<Void> roomFuture = CompletableFuture.runAsync(() -> {
            if (meetingReserve.getRoomId() != null) {
                MeetingRoom room = meetingRoomRepository.selectById(meetingReserve.getRoomId());
                model.setWarnContent(room.getWarnContent());
                model.setPrint(room.getPrint());
                model.setRowSeat(room.getRowSeat());
                if (ObjectUtil.isNotEmpty(room.getSpaceId())) {
                    Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(room.getSpaceId()), WebFrameworkUtils.getHeaderTenantId());
                    Optional.ofNullable(fullSpaceMap.get(room.getSpaceId())).ifPresent(space -> model.setSpaceName(space.getFullPath()));
                }
            }
        }, executorService);
        //会服信息
        CompletableFuture<Void> serviceFuture = CompletableFuture.runAsync(() -> {
            List<MeetingAttendantTaskModel> taskList = meetingAttendantTaskService.findByReserveId(meetingReserve.getId());
            model.setTaskList(taskList);
        }, executorService);
        //同步
        CompletableFuture.allOf(roomFuture, serviceFuture).get();
        return model;
    }


    /***
     * @Description 获取用户部门信息
     * @author huangyongtao
     * @date 2025/1/2 14:15
     * @param userId
     */
    public List<OrgDepartmentNode> getUserDepartment(String userId) {
        OrgDepartmentNode org = departmentApiService.getOrgByStaffNo(userId);
        return CollectionUtil.newArrayList(org);
    }

    private void handleReserve(MeetingReserveAppSaveParam param, MeetingReserve meetingReserve) {
        String loginId = WebFrameworkUtils.getHeaderUserId();
        //处理参会人
        if (CollectionUtil.isNotEmpty(param.getUserIdList())) {
            busExecutorService.execute(() -> {
                meetingReserveSignService.signSaveByReserve(meetingReserve, param.getUserIdList(), loginId);
            });
        }
        //处理会议文件
        if (CollectionUtil.isNotEmpty(param.getFileParamList())) {
            meetingReserveFileService.addBatch(meetingReserve.getId(), param.getFileParamList());
        }
        //处理会议排座
        if (CollectionUtil.isNotEmpty(param.getSeatParamList())) {
            meetingReserveSeatService.addBatch(meetingReserve.getId(), param.getSeatParamList());
        }
        //处理会服
        meetingAttendantTaskService.add(meetingReserve.getId(), param);
    }


    private void checkReserve(MeetingReserve meetingReserve, MeetingReserveAppSaveParam param) {
        AssertUtils.notNull(meetingReserve.getStartTime(), "会议开始时间信息填写错误，请修改后再预约");
        AssertUtils.notNull(meetingReserve.getEndTime(), "会议结束时间信息填写错误，请修改后再预约");
        AssertUtils.notNull(meetingReserve.getReserveDepartmentId(), "使用部门信息填写错误，请修改后再预约");
        AssertUtils.notNull(meetingReserve.getSummary(), "会议摘要信息填写错误，请修改后再预约");
        AssertUtils.notNull(meetingReserve.getRoomId(), "会议室信息填写错误，请修改后再预约");
        MeetingRoom room = meetingRoomRepository.selectById(meetingReserve.getRoomId());
        AssertUtils.notNull(room, "会议室不存在");
        if (CollectionUtil.isNotEmpty(param.getSeatParamList()) && Integer.valueOf(Status.disabled.getKey()).equals(room.getRowSeat())) {
            throw GenericException.fail("会议室不支持排座");
        }
        if (CollectionUtil.isNotEmpty(param.getFileParamList()) && Integer.valueOf(Status.disabled.getKey()).equals(room.getPrint())) {
            long filePintCount = param.getFileParamList().stream().filter(item -> Integer.valueOf(Status.enabled.getKey()).equals(item.getPrinting())).count();
            AssertUtils.isFalse(filePintCount > 0L, "会议室不支持打印");
        }
        MeetingReserveParam timeParam = new MeetingReserveParam();
        timeParam.setStartTime(meetingReserve.getStartTime());
        timeParam.setEndTime(meetingReserve.getEndTime());
        timeParam.setRoomId(meetingReserve.getRoomId());
        checkTime(timeParam);
        meetingTempReserveService.checkTempTime(BeanUtils.convertTo(timeParam, MeetingTempReserveParam::new));
        meetingAttendantTaskService.checkService(param);
    }

    private void handleMeetingDevice(MeetingReserve meetingReserve) {
        String deviceId = getMeetingDevice(meetingReserve);
        AssertUtils.isFalse(ObjectUtil.isEmpty(deviceId), "会议室没有视频会议设备！");
        try {
            String startTime = DateUtil.format(meetingReserve.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            String endTime = DateUtil.format(meetingReserve.getEndTime(), "yyyy-MM-dd HH:mm:ss");
            Boolean videoFlag = sceneControlService.scheduleMeeting(startTime, endTime, deviceId, meetingReserve.getReserveName());
            AssertUtils.isTrue(videoFlag, "视频会议设备异常！");
        } catch (Exception e) {
            throw GenericException.fail("视频会议设备异常！");
        }
    }

    private String getMeetingDevice(MeetingReserve meetingReserve) {
        String deviceId = "";
        List<MeetingRoomDeviceRel> relList = meetingRoomDeviceRelService.list(new LambdaQueryWrapper<MeetingRoomDeviceRel>().eq(MeetingRoomDeviceRel::getRoomId, meetingReserve.getRoomId()));
        if (CollectionUtils.isEmpty(relList)) {
            return deviceId;
        }
        List<Long> deviceIds = relList.stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
        List<IocDevice> deviceList = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>()
                .select(IocDevice::getId, IocDevice::getIotProductCode, IocDevice::getIotDeviceDn)
                .in(IocDevice::getId, deviceIds).eq(IocDevice::getDeleted, Status.enabled.getKey())
                .isNotNull(IocDevice::getIotProductCode)
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), IocDevice::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        for (IocDevice info : deviceList) {
            if (meetingDeviceProductKey.equals(info.getIotProductCode())) {
                deviceId = info.getIotDeviceDn();
                break;
            }
        }
        return deviceId;
    }


    /***
     * @Description 获取用户信息
     * @author huangyongtao
     * @date 2024/8/2 10:34
     * @param userId
     */
    private UserInfoModel getUser(String userId) {
        return Objects.requireNonNull(userApiService.getByStaffNo(userId));
    }


    /**
     * 通用的操作检查:
     * 1.会议是否存在
     * 2.会议是否取消
     *
     * @param reserve 会议信息
     */
    private void commonCheck(MeetingReserve reserve) {
        AssertUtils.isFalse(ObjectUtil.isEmpty(reserve) || !WebFrameworkUtils.getHeaderTenantId().equals(reserve.getTenantId()), "当前园区无该会议预约信息，请知悉");
        AssertUtils.isFalse(Status.enabled.getKey().equals(reserve.getCancelFlag()), "会议已取消，操作失败");
    }

    /**
     * 发起人的操作检查:
     * 1.通用操作检查
     * 2.发起人操作权限检查
     *
     * @param reserve 会议信息
     */
    private void reserveCheck(MeetingReserve reserve) {
        commonCheck(reserve);
        AssertUtils.isTrue(reserve.getReserveUid().equals(WebFrameworkUtils.getHeaderUserId()), "非发起人，无权操作");
    }


    /**
     * 会议无效/有效配置
     * 管理员操作，未取消会议不做状态限制
     *
     * @param id          会议id
     * @param reason      操作原因
     * @param inValidFlag 是否无效:0->是;1->否
     * @return 操作结果
     */
    private Boolean inValidConfig(Long id, String reason, Integer inValidFlag) {
        MeetingReserve reserve = getById(id);
        //通用操作检查
        commonCheck(reserve);
        UserInfoModel userInfo = getUser(WebFrameworkUtils.getHeaderUserId());
        reserve.setOperateUid(userInfo.getId());
        reserve.setOperateUname(userInfo.getUserName());
        reserve.setOperateStaffid(userInfo.getStaffid());
        reserve.setOperateTime(new Date());
        reserve.setInValidFlag(inValidFlag);
        reserve.setOperateReason(reason);
        return updateById(reserve);
    }
}
