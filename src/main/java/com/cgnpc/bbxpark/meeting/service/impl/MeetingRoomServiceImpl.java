
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.config.eventbus.AttendantTaskEvent;
import com.cgnpc.bbxpark.config.eventbus.MeetingServeDelEvent;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.meeting.domain.*;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.model.common.CommonInfo;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomRepository;
import com.cgnpc.bbxpark.meeting.service.*;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/***
 * @Description 会议室服务实现
 * @author huangyongtao
 * @date 2024/8/23 15:45
 */
@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomRepository, MeetingRoom> implements IMeetingRoomService {
    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private IUserSpaceService userSpaceService;

    @Autowired
    private IIocDeviceService iocDeviceService;

    @Autowired
    private IMeetingRoomDeviceRelService meetingRoomDeviceRelService;

    @Autowired
    private IMeetingRoomServiceService meetingRoomServiceService;

    @Autowired
    private IMeetingReserveService meetingReserveService;

    @Autowired
    private IMeetingServiceService meetingServiceService;

    @Autowired
    private IMeetingTempReserveService meetingTempReserveService;

    @Autowired
    private IMeetingAttendantTaskService meetingAttendantTaskService;

    @Autowired
    private IMeetingAttendantTaskDetailService meetingAttendantTaskDetailService;

    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executorService;

    @Autowired
    private IMeetingAttendantRoomService meetingAttendantRoomService;

    @Value("${iot.meetingDevice.productKey:fJY3PeX6fh7nK2TZ}")
    private String meetingDeviceProductKey;


    /**
     * 获取会议室列表(分页).
     *
     * @Param param 会议室查询条件
     * @Return 会议室信息列表（分页）
     */
    @Override
    public IPage<MeetingRoomModel> page(MeetingRoomPageParam param) {
        //权限校验
        buildRole(param);
        return handlePage(param);
    }

    @Override
    @SneakyThrows
    public List<MeetingRoomModel> list(MeetingRoomListParam param) {
        MeetingRoomPageParam pageParam = BeanUtils.convertTo(param, MeetingRoomPageParam::new);
        //权限校验
        buildRole(pageParam);
        List<MeetingRoom> rooms = list(buildQuery(pageParam));
        if (CollectionUtils.isEmpty(rooms)) {
            return Collections.emptyList();
        }
        List<MeetingRoomModel> list = BeanUtils.convertListTo(rooms, MeetingRoomModel::new);
        //空间信息
        CompletableFuture<Void> spaceFuture = CompletableFuture.runAsync(()-> spaceHandle(list,param.getTenantId()),executorService);
        //设备信息
        CompletableFuture<Void> deviceFuture = CompletableFuture.runAsync(()-> deviceHandle(list,param.getTenantId()),executorService);
        //同步
        CompletableFuture.allOf(spaceFuture,deviceFuture).get();
        return list;
    }

    @Override
    public IPage<MeetingRoomDeviceModel> pageDevice(MeetingRoomDevicePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        IPage<MeetingRoomDeviceModel> roomDevicePage = getBaseMapper().pageDevice(new Page<>(param.getCurrent(), param.getSize()),tenantId, param);
        if(CollectionUtils.isEmpty(roomDevicePage.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        //设备在线状态
        Map<Long, Boolean> deviceStatusMap = new HashMap<>(8);
        List<Long> deviceIdList = roomDevicePage.getRecords().stream().map(MeetingRoomDeviceModel::getDeviceId).collect(Collectors.toList());
        List<IocDevice> deviceInfos = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>().in(CollectionUtil.isNotEmpty(deviceIdList), IocDevice::getId, deviceIdList));

        Optional.ofNullable(deviceInfos).ifPresent(deviceList-> deviceList.forEach(device->deviceStatusMap.put(device.getId(), Status.enabled.getKey().equals(device.getOnlineStatus()))));
        roomDevicePage.getRecords().stream().filter(r->deviceStatusMap.containsKey(r.getDeviceId())).forEach(rel -> rel.setOnline(deviceStatusMap.get(rel.getDeviceId())));
        return ConvertUtil.pageConvert(roomDevicePage,roomDevicePage.getRecords());
    }

    @Override
    public List<MeetingRoomDeviceModel> listDevice(MeetingRoomDeviceListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return getBaseMapper().listDevice(tenantId,param);
    }

    @Override
    public List<AppMeetingRoomDeviceModel> listDeviceApp(MeetingRoomDeviceListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<MeetingRoomDeviceModel> deviceList = getBaseMapper().listDevice(tenantId, param);
        return deviceList.stream().map(device->{
            AppMeetingRoomDeviceModel model = BeanUtils.convertTo(device,AppMeetingRoomDeviceModel::new);
            model.setThingModelList(iocDeviceService.thingModelList(model.getDeviceId()));
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<MeetingRoomServiceModel> pageService(MeetingRoomServicePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        IPage<MeetingRoomServiceModel> page = this.getBaseMapper().pageService(new Page<>(param.getCurrent(), param.getSize()), tenantId, param);
        return ConvertUtil.pageConvert(page,page.getRecords());
    }

    @Override
    public Boolean addSpace(MeetingRoomSpaceParam param) {
        MeetingRoom meetingRoom = getById(param.getId());
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        meetingRoom.setSpaceId(param.getSpaceId());
        LambdaUpdateWrapper<MeetingRoom> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MeetingRoom::getSpaceId, param.getSpaceId());
        updateWrapper.eq(MeetingRoom::getId, param.getId());
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addDevice(MeetingRoomDeviceParam param) {
        MeetingRoom meetingRoom = getById(param.getId());
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        Boolean flag = meetingRoomDeviceRelService.remove(param.getId());
        if (CollectionUtils.isEmpty(param.getDeviceIdList())) {
            return flag;
        }
        List<MeetingRoomDeviceRel> list = param.getDeviceIdList().stream().map(deviceId -> {
            MeetingRoomDeviceRel rel = new MeetingRoomDeviceRel();
            rel.setDeviceId(deviceId);
            rel.setRoomId(param.getId());
            return rel;
        }).collect(Collectors.toList());
        return meetingRoomDeviceRelService.add(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addService(MeetingRoomServiceParam param) {
        MeetingRoom meetingRoom = getById(param.getId());
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        //查出会议室旧的会服数据
        List<MeetingRoomService> oldList = meetingRoomServiceService.list(Wrappers.<MeetingRoomService>lambdaQuery().eq(MeetingRoomService::getRoomId, param.getId()));
        List<Long> oldServiceIdList = oldList.stream().map(MeetingRoomService::getServiceId).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(param.getServiceIdList())){
            //全部删除
            meetingRoomServiceService.removeByRoomId(param.getId());
        }
        //需要新增的会服
        List<Long> addedServiceIdList = new ArrayList<>(param.getServiceIdList());
        addedServiceIdList.removeAll(oldServiceIdList);
        if(!CollectionUtils.isEmpty(addedServiceIdList)){
            //新增会议室-会服关联数据
            List<MeetingRoomService> list = addedServiceIdList.stream().map(serviceId -> {
                MeetingRoomService roomService = new MeetingRoomService();
                roomService.setRoomId(param.getId());
                roomService.setServiceId(serviceId);
                roomService.setId(null);
                return roomService;
            }).collect(Collectors.toList());
            return meetingRoomServiceService.saveBatch(list);
        }
        //需要删除的会服
        List<Long> removedServiceIdList = new ArrayList<>(oldServiceIdList);
        removedServiceIdList.removeAll(param.getServiceIdList());
        if(!CollectionUtils.isEmpty(removedServiceIdList)){
            meetingRoomServiceService.remove(Wrappers.<MeetingRoomService>lambdaQuery().eq(MeetingRoomService::getRoomId, param.getId()).in(MeetingRoomService::getServiceId, removedServiceIdList));
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean config(MeetingRoomConfigParam param) {
        LambdaUpdateWrapper<MeetingRoom> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MeetingRoom::getWarnContent, param.getWarnContent());
        updateWrapper.set(MeetingRoom::getRowSeat, param.getRowSeat());
        updateWrapper.set(MeetingRoom::getPrint, param.getPrint());
        updateWrapper.eq(MeetingRoom::getId, param.getId());
        return update(updateWrapper);
    }

    @Override
    public Boolean editVolume(MeetingRoomVolumeParam param) {
        LambdaUpdateWrapper<MeetingRoom> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MeetingRoom::getRoomVolume, param.getRoomVolume());
        updateWrapper.eq(MeetingRoom::getId, param.getId());
        return update(updateWrapper);
    }

    @Override
    @SneakyThrows
    public MeetingRoomDetailModel detail(Long id) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        MeetingRoom meetingRoom = getById(id);
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        MeetingRoomDetailModel model = BeanUtils.convertTo(meetingRoom, MeetingRoomDetailModel::new);
        model.setAttendantList(meetingAttendantRoomService.listByRoomId(id));
        //空间信息
        CompletableFuture<Void> spaceFuture = CompletableFuture.runAsync(()-> spaceHandle(model,tenantId),executorService);
        //设备信息
        CompletableFuture<Void> deviceFuture = CompletableFuture.runAsync(()-> deviceHandle(model,tenantId),executorService);
        //会服
        CompletableFuture<Void> serviceFuture = CompletableFuture.runAsync(()-> serviceHandle(model,tenantId),executorService);
        //同步
        CompletableFuture.allOf(spaceFuture,deviceFuture,serviceFuture).get();
        return model;
    }




    /**
     * 移动端-获取会议室列表(分页).
     *
     * @Param param 会议室查询条件
     * @Return 会议室信息列表（分页）
     */
    @Override
    @Deprecated
    public IPage<MeetingRoomModel> appPage(MeetingRoomPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return this.handlePage(param);
    }

    /**
     * 移动端-分页查询会议室占用情况
     *
     * @Param param 会议室查询条件
     * @Return 会议室信息列表（分页）
     */
    @Override
    @Deprecated
    public IPage<MeetingRoomModel> occupyPage(MeetingRoomPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        IPage<MeetingRoomModel> result = this.handlePage(param);
        List<MeetingRoomModel> meetingRoomModels = result.getRecords();
        if (CollectionUtils.isEmpty(meetingRoomModels)) {
            return result;
        }
        List<Long> roomIds = meetingRoomModels.stream().map(MeetingRoomModel::getId).collect(Collectors.toList());
        MeetingReserveListParam reserveListParam = new MeetingReserveListParam();
        reserveListParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        reserveListParam.setStartTime(param.getStartTime());
        reserveListParam.setEndTime(param.getEndTime());
        reserveListParam.setRoomIdList(roomIds);
        reserveListParam.setCancelFlag((int) Status.disabled.getKey());
        List<MeetingReserveModel> reserveModels = meetingReserveService.findReserve(reserveListParam);
        if (CollectionUtil.isNotEmpty(reserveModels)) {
            Map<Long, List<MeetingReserveModel>> reserveMap = reserveModels.stream().collect(Collectors.groupingBy(MeetingReserveModel::getRoomId));
            meetingRoomModels.forEach(item -> item.setMeetingReserveModels(reserveMap.get(item.getId())));
        }

        MeetingTempReserveListParam tempParam = new MeetingTempReserveListParam();
        tempParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        tempParam.setStartTime(param.getStartTime());
        tempParam.setEndTime(param.getEndTime());
        tempParam.setLocalTime(new Date());
        tempParam.setRoomIdList(roomIds);
        List<MeetingTempReserveModel> tempReserveModels = meetingTempReserveService.list(tempParam);
        if (CollectionUtil.isNotEmpty(tempReserveModels)) {
            Map<Long, List<MeetingTempReserveModel>> tempReserveMap = tempReserveModels.stream().collect(Collectors.groupingBy(MeetingTempReserveModel::getRoomId));
            meetingRoomModels.forEach(item -> item.setMeetingTempReserveModel(tempReserveMap.get(item.getId())));
        }
        return result;
    }

    @Override
    public IPage<MeetingRoomModel> attendantAppOccupyPage(MeetingRoomPageParam param) {
        List<MeetingAttendantRoom> roomList = meetingAttendantRoomService.listByUserId(WebFrameworkUtils.getHeaderUserId());
        if(CollectionUtil.isEmpty(roomList)){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        param.setRoomIdList(roomList.stream().map(MeetingAttendantRoom::getRoomId).collect(Collectors.toList()));
        return occupyPage(param);
    }

    /**
     * 新增会议室.
     *
     * @Param param 会议室信息
     * @Return 新增会议室是否成功
     */
    @Override
    @Transactional
    public Boolean add(MeetingRoomSaveParam param) {
        MeetingRoom meetingRoom = BeanUtils.convertTo(param, MeetingRoom::new);
        meetingRoom.setId(null);
        if(save(meetingRoom)){
            Optional.ofNullable(meetingServiceService.getByCommon()).ifPresent(serve->{
                MeetingRoomService roomService = new MeetingRoomService();
                roomService.setRoomId(meetingRoom.getId());
                roomService.setServiceId(serve.getId());
                meetingRoomServiceService.save(roomService);
            });
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * 删除会议室.
     *
     * @Param id 会议室标识
     * @Return 删除会议室是否成功
     */
    @Override
    public synchronized Boolean remove(Long id) {
        MeetingRoom meetingRoom = this.getById(id);
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.isFalse(meetingRoom.getDeleted() == null ||Status.enabled.getKey() != meetingRoom.getDeleted(),"会议室不存在（后台未找到该会议室）");
        meetingRoomDeviceRelService.remove(id);
        meetingRoom.setDeleted(Status.disabled.getKey());
        return updateById(meetingRoom);
    }

    @Override
    public Boolean edit(MeetingRoomParam param) {
        MeetingRoom meetingRoom = this.getById(param.getId());
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        return updateById(BeanUtils.convertTo(param, MeetingRoom::new));
    }

    @Override
    public MeetingRoomDetailModel simpleDetail(Long id) {
        MeetingRoom meetingRoom = getById(id);
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        MeetingRoomDetailModel model = BeanUtils.convertTo(meetingRoom, MeetingRoomDetailModel::new);
        handleMeetingDevice(model);
        return model;
    }

    private void handleMeetingDevice(MeetingRoomDetailModel model){
        List<MeetingRoomDeviceRel> relList = meetingRoomDeviceRelService.list(new LambdaQueryWrapper<MeetingRoomDeviceRel>().eq(MeetingRoomDeviceRel::getRoomId, model.getId()));
        if(CollectionUtils.isEmpty(relList)){
            return;
        }
        List<Long> deviceIds = relList.stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
        List<IocDevice> deviceList = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>().select(IocDevice::getId, IocDevice::getIotProductCode)
                .in(IocDevice::getId, deviceIds)
                .eq(IocDevice::getDeleted, (int) Status.enabled.getKey())
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), IocDevice::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<String> productKeys = deviceList.stream().map(IocDevice::getIotProductCode).collect(Collectors.toList());
        if(productKeys.contains(meetingDeviceProductKey)){
            model.setMeetingDevice(true);
        }
    }

    @Override
    public void handleTaskEvent(AttendantTaskEvent event) {
        MeetingRoom room = new MeetingRoom();
        room.setId(event.getRoomId());
        if(MeetingAttendantTaskTypeEnum.BEFORE.getValue().equals(event.getServiceType())){
            if(Status.disabled.getKey().equals(event.getServiceValid())){
                room.setUsed(Status.enabled.getKey());
            }
            if(MeetingAttendantTaskStatusEnum.COMPLETE.getValue().equals(event.getServiceStatus()) && !CollectionUtils.isEmpty(event.getServiceIds())){
                //增加会议服务次数
                LambdaUpdateWrapper<MeetingRoomService> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(MeetingRoomService::getRoomId,event.getRoomId());
                wrapper.in(MeetingRoomService::getServiceId,event.getServiceIds());
                wrapper.setSql("provide_count = provide_count + 1");
                meetingRoomServiceService.update(wrapper);
            }
        }else if(MeetingAttendantTaskTypeEnum.IN.getValue().equals(event.getServiceType())){
            if(MeetingAttendantTaskStatusEnum.UNHANDLE.getValue().equals(event.getServiceStatus())){
                room.setCalling(Status.enabled.getKey());
            }else if(MeetingAttendantTaskStatusEnum.COMPLETE.getValue().equals(event.getServiceStatus())){
                room.setCalling(Status.disabled.getKey());
            }
        }else if(MeetingAttendantTaskTypeEnum.AFTER.getValue().equals(event.getServiceType())){
            if(MeetingAttendantTaskStatusEnum.UNHANDLE.getValue().equals(event.getServiceStatus())){
                room.setSwept(Status.disabled.getKey());
                room.setUsed(Status.disabled.getKey());
                room.setCalling(Status.disabled.getKey());
            }else if(MeetingAttendantTaskStatusEnum.COMPLETE.getValue().equals(event.getServiceStatus())){
                room.setSwept(Status.enabled.getKey());
            }
        }
        this.updateById(room);
    }

    @Override
    public void handleServeDel(MeetingServeDelEvent event) {
        meetingRoomServiceService.remove(Wrappers.<MeetingRoomService>lambdaQuery().eq(MeetingRoomService::getServiceId,event.getServiceId()));
    }

   /***
    * @Description 更新会议室的使用状态
    * @author huangyongtao
    * @date 2025/1/7 19:27
    */
    @Override
    public void updateUsedTask() {
        List<MeetingRoom> roomList = this.list(Wrappers.<MeetingRoom>lambdaQuery().eq(MeetingRoom::getDeleted, Status.enabled.getKey()));
        if (CollectionUtil.isEmpty(roomList)) {
            return;
        }
        List<MeetingRoom> updateRoomList = new ArrayList<>();
        roomList.forEach(item -> {
            int count = meetingReserveService.count(Wrappers.<MeetingReserve>lambdaQuery().eq(MeetingReserve::getRoomId, item.getId())
                    .eq(MeetingReserve::getStatus, MeetingReserveStatusEnum.GOING.getCode())
                    .eq(MeetingReserve::getDraft, (int) Status.disabled.getKey())
                    .eq(MeetingReserve::getCancelFlag, (int) Status.disabled.getKey()));
            Boolean reserveFlag = count > 0;
            Boolean tempReserveFlag = meetingTempReserveService.count(Wrappers.<MeetingTempReserve>lambdaQuery().eq(MeetingTempReserve::getRoomId, item.getId())
                    .lt(MeetingTempReserve::getStartTime, new Date())
                    .gt(MeetingTempReserve::getEndTime, new Date()))>0;
            MeetingRoom updateRoom = new MeetingRoom();
            updateRoom.setId(item.getId());
            if(Status.enabled.getKey().equals(item.getUsed())){
                if(!reserveFlag && !tempReserveFlag){
                    updateRoom.setUsed(Status.disabled.getKey());
                    updateRoomList.add(updateRoom);
                }
            }else{
                if(reserveFlag || tempReserveFlag){
                    updateRoom.setUsed(Status.enabled.getKey());
                    updateRoomList.add(updateRoom);
                }
            }
        });
        if(CollectionUtil.isNotEmpty(updateRoomList)){
            this.updateBatchById(updateRoomList);
        }
    }

    @Override
    public MeetingRoomUsedCountModel usedCount() {
        List<MeetingAttendantRoom> roomList = meetingAttendantRoomService.listByUserId(WebFrameworkUtils.getHeaderUserId());
        MeetingRoomUsedCountModel model = new MeetingRoomUsedCountModel();
        if(CollectionUtil.isEmpty(roomList)){
            return model;
        }
        List<Long> roomIds = roomList.stream().map(MeetingAttendantRoom::getRoomId).collect(Collectors.toList());
        List<MeetingRoom> rooms = this.list(Wrappers.<MeetingRoom>lambdaQuery().in(MeetingRoom::getId, roomIds).eq(MeetingRoom::getDeleted, Status.enabled.getKey()));
        if(CollectionUtil.isEmpty(roomList)){
            return model;
        }
        model.setTotalNum((long) rooms.size());
        model.setUsedNum(rooms.stream().filter(item -> Status.enabled.getKey().equals(item.getUsed())).count());
        model.setFreeNum(rooms.stream().filter(item -> Status.disabled.getKey().equals(item.getUsed())).count());
        return model;
    }

    @Override
    public List<MeetingRoomAttendantModel> attendantList(MeetingRoomListParam param) {
        if(ObjectUtil.isNotEmpty(param.getSwept())){
            param.setUsed(Status.disabled.getKey());
        }
        List<MeetingAttendantRoom> roomList = meetingAttendantRoomService.listByUserId(WebFrameworkUtils.getHeaderUserId());
        if(CollectionUtil.isEmpty(roomList)){
            return new ArrayList<>();
        }
        List<Long> roomIdList = roomList.stream().map(MeetingAttendantRoom::getRoomId).collect(Collectors.toList());
        List<MeetingRoom> rooms = this.list(Wrappers.<MeetingRoom>lambdaQuery()
                .in(MeetingRoom::getId, roomIdList)
                .like(ObjectUtil.isNotEmpty(param.getRoomName()), MeetingRoom::getRoomName, param.getRoomName())
                .eq(ObjectUtil.isNotEmpty(param.getSwept()), MeetingRoom::getSwept, param.getSwept())
                .eq(ObjectUtil.isNotEmpty(param.getUsed()), MeetingRoom::getUsed, param.getUsed())
                .eq(ObjectUtil.isNotEmpty(param.getCalling()), MeetingRoom::getCalling, param.getCalling())
                .eq(MeetingRoom::getDeleted, Status.enabled.getKey()));
        if(CollectionUtil.isEmpty(rooms)){
            return new ArrayList<>();
        }
        List<MeetingRoomAttendantModel> roomAttendantModels = BeanUtils.convertListTo(rooms, MeetingRoomAttendantModel::new);
        List<Long> roomIds = roomAttendantModels.stream().map(MeetingRoomAttendantModel::getId).collect(Collectors.toList());
        //查询会议信息
        List<MeetingReserve> reserves = meetingReserveService.list(Wrappers.<MeetingReserve>lambdaQuery().in(MeetingReserve::getRoomId, roomIds)
                .in(MeetingReserve::getStatus, Arrays.asList(MeetingReserveStatusEnum.START.getCode(), MeetingReserveStatusEnum.GOING.getCode()))
                .eq(MeetingReserve::getDraft, Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
                .orderByAsc(MeetingReserve::getStartTime));
        if (CollectionUtils.isEmpty(reserves)) {
            return roomAttendantModels;
        }
        List<MeetingReserveAttendantModel> meetingReserveAttendantModelList = BeanUtils.convertListTo(reserves, MeetingReserveAttendantModel::new);
        //查询会议室服务信息
        List<MeetingRoomService> roomServices = meetingRoomServiceService.list(Wrappers.<MeetingRoomService>lambdaQuery().select(MeetingRoomService::getRoomId, MeetingRoomService::getServiceId).in(MeetingRoomService::getRoomId, roomIds));
        //处理会议的会前布置服务
        handleReserveAttendant(meetingReserveAttendantModelList, roomServices);
        Map<Long, List<MeetingReserveAttendantModel>> reserveMap = meetingReserveAttendantModelList.stream().collect(Collectors.groupingBy(MeetingReserveAttendantModel::getRoomId));
        roomAttendantModels.forEach(item -> item.setMeetingReserveAttendantModelList(reserveMap.get(item.getId())));
        return roomAttendantModels;
    }

    @Override
    public Boolean editImage(MeetingRoomParam param) {
        MeetingRoom meetingRoom = this.getById(param.getId());
        AssertUtils.notNull(meetingRoom, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.isFalse(ObjectUtil.isEmpty(param.getImageUrl()), "会议室图片不能为空");
        MeetingRoom room = new MeetingRoom();
        room.setImageUrl(param.getImageUrl());
        room.setId(param.getId());
        return updateById(room);
    }

    private void handleReserveAttendant(List<MeetingReserveAttendantModel> meetingReserveAttendantModelList, List<MeetingRoomService> roomServices){
        Map<Long, List<MeetingRoomService>> roomServiceMap = CollectionUtil.isEmpty(roomServices) ? new HashMap<>() : roomServices.stream().collect(Collectors.groupingBy(MeetingRoomService::getRoomId));
        //查询会议的会前布置信息
        List<Long> reserveIds = meetingReserveAttendantModelList.stream().map(MeetingReserveAttendantModel::getId).collect(Collectors.toList());
        List<MeetingAttendantTask> tasks = meetingAttendantTaskService.list(Wrappers.<MeetingAttendantTask>lambdaQuery()
                .in(MeetingAttendantTask::getReserveId, reserveIds)
                .eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
        if(CollectionUtil.isNotEmpty(tasks)){
            Map<Long, List<MeetingAttendantTask>> taskMap = tasks.stream().collect(Collectors.groupingBy(MeetingAttendantTask::getReserveId));
            List<Long> taskIds = tasks.stream().map(MeetingAttendantTask::getId).collect(Collectors.toList());
            List<MeetingAttendantTaskDetail> taskDetails = meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().in(MeetingAttendantTaskDetail::getTaskId, taskIds));
            Map<Long, List<MeetingAttendantTaskDetail>> taskDetailMap = taskDetails.stream().collect(Collectors.groupingBy(MeetingAttendantTaskDetail::getTaskId));
            for(MeetingReserveAttendantModel model : meetingReserveAttendantModelList){
                //会议室绑定的会服
                List<Long> serviceIds = CollectionUtil.isEmpty(roomServiceMap.get(model.getRoomId())) ? new ArrayList<>() : roomServiceMap.get(model.getRoomId()).stream().map(MeetingRoomService::getServiceId).collect(Collectors.toList());
                //是否即将开始
                model.setStartFlag(DateUtil.between(model.getStartTime(), new Date(), DateUnit.MINUTE) <= 30);
                List<MeetingAttendantTask> taskList = taskMap.get(model.getId());
                if(CollectionUtil.isNotEmpty(taskList)){
                    model.setTaskId(taskList.get(0).getId());
                    model.setServiceStatus(taskList.get(0).getServiceStatus());
                    model.setServiceValid(taskList.get(0).getServiceValid());
                    List<MeetingAttendantTaskDetail> taskDetailList = taskDetailMap.get(model.getTaskId());
                    List<MeetingAttendantTaskDetailModel> taskDetailModelList = BeanUtils.convertListTo(taskDetailList, MeetingAttendantTaskDetailModel::new);
                    taskDetailModelList.forEach(detail -> {
                        if(ObjectUtil.isNotEmpty(detail.getServiceId()) && !serviceIds.contains(detail.getServiceId())){
                            detail.setValid(Status.disabled.getKey());
                        }
                    });
                    model.setTaskDetailModelList(taskDetailModelList);
                }
            }
        }
    }

    /**
     * 构建角色筛选条件
     */
    private void buildRole(MeetingRoomPageParam param) {
        Boolean admin = userApiService.parkAdmin();
        if (admin){
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        } else {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            param.setUserId(WebFrameworkUtils.getHeaderUserId());
        }
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<MeetingRoom> buildQuery(MeetingRoomPageParam param) {
        LambdaQueryWrapper<MeetingRoom> query = new LambdaQueryWrapper<>();
        query.like(ObjectUtil.isNotEmpty(param.getRoomName()), MeetingRoom::getRoomName, param.getRoomName());
        query.in(CollectionUtil.isNotEmpty(param.getRoomIdList()), MeetingRoom::getId, param.getRoomIdList());
        if(param.getSpaceId() != null){
            //空间筛选
            List<Long> childrenIdList = parkSpaceService.findChildrenIdList(param.getSpaceId(),WebFrameworkUtils.getHeaderTenantId());
            Optional.ofNullable(childrenIdList).ifPresent(list->param.setSpaceIdList(childrenIdList));
        }
        // 园区管理员角色 查询当前园区
        query.eq(param.getUserId() == null && param.getTenantId() != null, MeetingRoom::getTenantId, param.getTenantId());
        // 普通角色 查询有空间权限的
        if(param.getUserId() != null && param.getTenantId() != null){
            //查询用户拥有的空间id集合
            List<Long> spaceIdList = userSpaceService.findSpaceIds();
            if(CollectionUtils.isEmpty(spaceIdList)){
                query.eq(MeetingRoom::getSpaceId,-1);
            }else {
                spaceIdList = CollectionUtils.isEmpty(param.getSpaceIdList()) ? spaceIdList : (List<Long>)CollUtil.intersection(spaceIdList,parkSpaceService.findChildrenIdList(param.getSpaceId(),WebFrameworkUtils.getHeaderTenantId()));
                if(CollectionUtils.isEmpty(spaceIdList)){
                    query.eq(MeetingRoom::getSpaceId,-1);
                }else {
                    param.setSpaceIdList(spaceIdList);
                }
            }
        }
        //空间筛选
        query.in(!CollectionUtils.isEmpty(param.getSpaceIdList()),MeetingRoom::getSpaceId,param.getSpaceIdList());
        query.eq(MeetingRoom::getDeleted, Delete.NORMAL.getKey());
        query.orderByDesc(MeetingRoom::getCreateTime);
        return query;
    }

    /**
     * 分页查询及处理
     */
    @SneakyThrows
    private IPage<MeetingRoomModel> handlePage(MeetingRoomPageParam param) {
        IPage<MeetingRoom> page = page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
        }
        List<MeetingRoomModel> list = BeanUtils.convertListTo(page.getRecords(), MeetingRoomModel::new);
        //空间信息
        CompletableFuture<Void> spaceFuture = CompletableFuture.runAsync(()-> spaceHandle(list,param.getTenantId()),executorService);
        //设备信息
        CompletableFuture<Void> deviceFuture = CompletableFuture.runAsync(()-> deviceHandle(list,param.getTenantId()),executorService);
        //同步
        CompletableFuture.allOf(spaceFuture,deviceFuture).get();
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), list);
    }


    /**
     * 空间信息处理
     *
     * 继承了CommonInfo的实体都可以为其添加空间信息
     * 避免不同方法的返回实体不同导致无法统一为其设置空间信息
     * @param t 继承了CommonInfo的实体
     * @param tenantId 租户id
     * @param <T>  泛型实体
     */
    private <T extends CommonInfo> void spaceHandle(T t, Long tenantId){
        if(t.getSpaceId() != null){
            Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(t.getSpaceId()), tenantId);
            Optional.ofNullable(fullSpaceMap.get(t.getSpaceId())).ifPresent(model -> t.setSpaceName(model.getFullPath()));
        }
    }

    /**
     * 空间信息批量处理
     *
     * 继承了CommonInfo的实体都可以为其添加空间信息
     * 避免不同方法的返回实体不同导致无法统一为其设置空间信息
     * @param list 继承了CommonInfo的实体集合
     * @param tenantId  租户id
     */
    private void spaceHandle(List<? extends CommonInfo> list,Long tenantId) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //获取空间id集合
        List<Long> spaceIdList = list.stream().map(CommonInfo::getSpaceId).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(spaceIdList, tenantId);
        if(!CollectionUtils.isEmpty(fullSpaceMap)){
            list.forEach(r->{
                ParkSpaceFullModel parkSpaceFullModel = fullSpaceMap.get(r.getSpaceId());
                Optional.ofNullable(parkSpaceFullModel).ifPresent(model -> r.setSpaceName(model.getFullPath()));
            });
        }
    }

    /**
     * 设备信息处理
     *
     * 继承了CommonInfo的实体都可以为其添加设备信息
     * 避免不同方法的返回实体不同导致无法统一为其设置设备信息
     * @param t 继承了CommonInfo的实体
     * @param tenantId 租户id
     * @param <T> 泛型实体
     */
    private <T extends CommonInfo> void deviceHandle(T t,Long tenantId){
        //获取会议室-设备集合
        List<MeetingRoomDeviceRel> relList = meetingRoomDeviceRelService.list(Wrappers.<MeetingRoomDeviceRel>lambdaQuery().eq(MeetingRoomDeviceRel::getRoomId,t.getId()).eq(tenantId != null,MeetingRoomDeviceRel::getTenantId,tenantId).orderByDesc(MeetingRoomDeviceRel::getCreateTime).orderByAsc(MeetingRoomDeviceRel::getId));
        List<Long> deviceIdList = relList.stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(deviceIdList)){
            return;
        }
        //根据会议室的设备id批量查询设备集合
        List<IocDevice> deviceInfoList = iocDeviceService.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId,IocDevice::getDeviceName).in(IocDevice::getId, deviceIdList).eq(IocDevice::getDeleted, (int) Status.enabled.getKey()).eq(tenantId != null,IocDevice::getTenantId,tenantId))
                .stream().sorted(Comparator.comparing(d->deviceIdList.indexOf(d.getId()))).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(deviceInfoList)){
            t.setDeviceIdList(deviceInfoList.stream().map(IocDevice::getId).collect(Collectors.toList()));
            t.setDeviceNameList(deviceInfoList.stream().map(IocDevice::getDeviceName).collect(Collectors.toList()));
        }
    }

    /**
     * 设备信息批量处理
     *
     * 继承了CommonInfo的实体都可以为其添加设备信息
     * 避免不同方法的返回实体不同导致无法统一为其设置设备信息
     * @param list 继承了CommonInfo的实体集合
     * @param tenantId  租户id
     */
    private void deviceHandle(List<? extends CommonInfo> list,Long tenantId) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //获取会议室id集合
        List<Long> roomIdList = list.stream().map(CommonInfo::getId).collect(Collectors.toList());
        //查询会议室-设备集合
        List<MeetingRoomDeviceRel> relList = meetingRoomDeviceRelService.list(new LambdaQueryWrapper<MeetingRoomDeviceRel>().in(MeetingRoomDeviceRel::getRoomId, roomIdList).eq(tenantId != null, MeetingRoomDeviceRel::getTenantId,tenantId));
        if(CollectionUtils.isEmpty(relList)){
            return;
        }
        Map<Long, List<MeetingRoomDeviceRel>> roomDeviceMap = relList.stream().collect(Collectors.groupingBy(MeetingRoomDeviceRel::getRoomId));
        //根据会议室的设备id批量查询设备信息
        List<Long> deviceIds = relList.stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
        List<IocDevice> deviceList = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>().select(IocDevice::getDeviceName, IocDevice::getId, IocDevice::getIotProductCode).eq(IocDevice::getDeleted, (int) Status.enabled.getKey()).in(IocDevice::getId, deviceIds).eq(tenantId != null, IocDevice::getTenantId, tenantId));
        if(CollectionUtils.isEmpty(deviceList)){
            return;
        }
        Map<Long, String> deviceMap = deviceList.stream().collect(Collectors.toMap(IocDevice::getId, r->Optional.ofNullable(r.getDeviceName()).orElse(""), (k1, k2) -> k1));
        Map<Long, String> deviceProductMap = deviceList.stream().filter(p->ObjectUtil.isNotEmpty(p.getIotProductCode())).collect(Collectors.toMap(IocDevice::getId, IocDevice::getIotProductCode, (k1, k2) -> k1));
        //数据聚合
        list.forEach(r->{
            r.setDeviceIdList(new ArrayList<>());
            r.setDeviceNameList(new ArrayList<>());
            Optional.ofNullable(roomDeviceMap.get(r.getId())).ifPresent(l->l.stream()
                    .filter(r1->deviceMap.containsKey(r1.getDeviceId())).forEach(rel -> {
                r.getDeviceIdList().add(rel.getDeviceId());
                r.getDeviceNameList().add(deviceMap.get(rel.getDeviceId()));
                if(meetingDeviceProductKey.equals(deviceProductMap.get(rel.getDeviceId()))){
                    r.setMeetingDevice(true);
                }
            }));
        });
    }

    /**
     * 会服信息处理
     *
     * 继承了CommonInfo的实体都可以为其添加会服信息
     * 避免不同方法的返回实体不同导致无法统一为其设置会服信息
     * @param t 继承了CommonInfo的实体
     * @param tenantId 租户id
     * @param <T> 泛型实体
     */
    private <T extends CommonInfo> void serviceHandle(T t,Long tenantId){
        //获取会议室-会服集合
        List<MeetingRoomService> roomServiceList = meetingRoomServiceService.list(Wrappers.<MeetingRoomService>lambdaQuery().eq(MeetingRoomService::getRoomId,t.getId()).eq(tenantId != null,MeetingRoomService::getTenantId,tenantId));
        List<Long> serviceIdList = roomServiceList.stream().map(MeetingRoomService::getServiceId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(serviceIdList)){
            return;
        }
        //根据会议室的会服id批量查询会服集合
        List<MeetingService> serviceList = meetingServiceService.list(Wrappers.<MeetingService>lambdaQuery().in(MeetingService::getId, serviceIdList).eq(tenantId != null,MeetingService::getTenantId,tenantId).eq(MeetingService::getDeleted,Status.enabled.getKey()));
        if(!CollectionUtils.isEmpty(serviceList)){
            t.setServiceIdList(serviceList.stream().map(MeetingService::getId).collect(Collectors.toList()));
            t.setServiceNameList(serviceList.stream().map(MeetingService::getName).collect(Collectors.toList()));
        }
    }
}
