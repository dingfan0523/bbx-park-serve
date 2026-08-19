package com.cgnpc.bbxpark.device.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.device.domain.DeviceEcStatistics;
import com.cgnpc.bbxpark.device.domain.DeviceScreenRecord;
import com.cgnpc.bbxpark.device.mapper.DeviceEcStatisticsRepository;
import com.cgnpc.bbxpark.device.mapper.DeviceScreenRecordRepository;
import com.cgnpc.bbxpark.device.service.IDeviceEcStatisticsService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomDeviceRel;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomDeviceRelRepository;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceEcStatisticsServiceImpl extends ServiceImpl<DeviceEcStatisticsRepository, DeviceEcStatistics> implements IDeviceEcStatisticsService {
    @Autowired
    private DeviceScreenRecordRepository deviceScreenRecordRepository;
    @Autowired
    private ITenantInfoService tenantInfoService;
    @Autowired
    private MeetingRoomDeviceRelRepository meetingRoomDeviceRelRepository;

    public void calculateYesterdayStatistics() {
        Date yesterdayStart = DateUtil.getFirstTimeOfDateOffset(new Date(), -1);
        Date yesterdayEnd = DateUtil.getLastTimeOfDateOffset(new Date(), -1);

        List<TenantInfo> tenants = tenantInfoService.list(Wrappers.<TenantInfo>lambdaQuery().eq(TenantInfo::getDeleted, Delete.NORMAL.getKey()));
        tenants.forEach(tenant -> {
            log.info("开始统计租户{}下{}的设备节能数据", tenant.getName(), yesterdayStart);
            //获取会议室绑定设备
            List<MeetingRoomDeviceRel> roomDevices = meetingRoomDeviceRelRepository.selectList(Wrappers.<MeetingRoomDeviceRel>lambdaQuery()
                    .eq(MeetingRoomDeviceRel::getDeleted,Delete.NORMAL.getKey()).eq(MeetingRoomDeviceRel::getTenantId,tenant.getId()));
            List<Long> deviceIds = roomDevices.stream().map(MeetingRoomDeviceRel::getDeviceId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(deviceIds)){
                return;
            }
            //获取场景执行数据
            List<DeviceScreenRecord> records = deviceScreenRecordRepository.selectList(Wrappers.<DeviceScreenRecord>lambdaQuery()
                    .between(DeviceScreenRecord::getRecordTime, yesterdayStart, yesterdayEnd).eq(DeviceScreenRecord::getTenantId, tenant.getId())
                    .orderByAsc(DeviceScreenRecord::getRecordTime));
            Map<Long, List<DeviceScreenRecord>> recordMap = records.stream().collect(Collectors.groupingBy(DeviceScreenRecord::getDeviceId));
            List<DeviceEcStatistics> statisticsList = new ArrayList<>();
            deviceIds.forEach(deviceId->{
                //查询该设备统计日期前的最后一条记录（以此来确定该设备在统计日期初始状态是开灯还是关灯）
                DeviceScreenRecord lastBefore = selectLastBefore(deviceId, yesterdayStart, tenant.getId());
                if(!recordMap.containsKey(deviceId) && lastBefore == null){
                    //该设备没有历史记录，并且当天也没有记录，那就不统计该设备的数据
                }else {
                    double openHours = calculateOpenHoursForDevice(recordMap.get(deviceId), lastBefore, yesterdayStart, yesterdayEnd);
                    double ecHours = 24.0 - openHours;
                    //假设一个会议室的所有灯功率加起来是1kw
                    double ecKwh = ecHours * 1;
                    DeviceEcStatistics deviceEcStatistics = new DeviceEcStatistics();
                    deviceEcStatistics.setStatisticsTime(yesterdayEnd);
                    deviceEcStatistics.setTenantId(tenant.getId());
                    deviceEcStatistics.setDeviceId(deviceId);
                    deviceEcStatistics.setEcDuration(ecHours);
                    deviceEcStatistics.setEcKwh(ecKwh);
                    //测试阶段可能多次执行定时任务，所以统计当前设备的昨日节能数据时，需先删除当前设备昨日的节能统计数据，以免重复统计
                    remove(Wrappers.<DeviceEcStatistics>lambdaQuery().eq(DeviceEcStatistics::getDeviceId,deviceId)
                            .between(DeviceEcStatistics::getStatisticsTime,yesterdayStart,yesterdayEnd).eq(DeviceEcStatistics::getTenantId,tenant.getId()));
                    statisticsList.add(deviceEcStatistics);
                }
            });
//            recordMap.forEach((deviceId, list) -> {
//                //查询该设备统计日期前的最后一条记录（以此来确定该设备在统计日期初始状态是开灯还是关灯）
//                DeviceScreenRecord lastBefore = selectLastBefore(deviceId, yesterdayStart, tenant.getId());
//                if (list.isEmpty() && lastBefore == null) {
//                    //设备没有历史记录 && 当天也没有记录，那就不需要统计
//                } else {
//                    double openHours = calculateOpenHoursForDevice(list, lastBefore, yesterdayStart, yesterdayEnd);
//                    double ecHours = 24.0 - openHours;
//                    //假设一个会议室的所有灯功率加起来是1kw
//                    double ecKwh = ecHours * 1;
//                    DeviceEcStatistics deviceEcStatistics = new DeviceEcStatistics();
//                    deviceEcStatistics.setStatisticsTime(yesterdayEnd);
//                    deviceEcStatistics.setTenantId(tenant.getId());
//                    deviceEcStatistics.setDeviceId(deviceId);
//                    deviceEcStatistics.setEcDuration(ecHours);
//                    deviceEcStatistics.setEcKwh(ecKwh);
//                    statisticsList.add(deviceEcStatistics);
//                }
//            });
            saveBatch(statisticsList);
        });
    }

    private double calculateOpenHoursForDevice(List<DeviceScreenRecord> list, DeviceScreenRecord lastBefore, Date start, Date end) {
        list = CollectionUtils.isEmpty(list) ? Collections.emptyList() : list;
        //确定初始状态
        int initialStatus;
        if (lastBefore != null) {
            //根据最后一条记录来判断初始状态是开灯还是关灯
            initialStatus = lastBefore.getType() == 1 ? 1 : 0;
        } else {
            //没有历史记录，那根据当天的第一条记录推断是开还是关
            initialStatus = list.get(0).getType() == 1 ? 0 : 1;
        }
        long totalOpenMillis = 0;
        int currentStatus = initialStatus;
        Date lastOpenStart = currentStatus == 1 ? start : null;
        //遍历当天记录，计算开灯时长
        for (DeviceScreenRecord record : list) {
            if (record.getType() == 1) {
                //开灯事件
                if (currentStatus == 0) {
                    //从关灯->开灯
                    lastOpenStart = record.getRecordTime();
                } else {
                    //从开灯->开灯，也算开灯时间
                    lastOpenStart = record.getRecordTime();
                    totalOpenMillis += record.getRecordTime().getTime() - lastOpenStart.getTime();
                }
                currentStatus = 1;
            } else if (record.getType() == 2) {
                //关灯事件
                if (currentStatus == 1) {
                    //从开灯->关灯,计算开灯时间
                    totalOpenMillis += record.getRecordTime().getTime() - lastOpenStart.getTime();
                }
                currentStatus = 0;
            }
        }
        if (currentStatus == 1) {
            //如果当天最后一条记录仍是开灯，那说明开灯到第二天了，所以最后一条开灯记录到当天结束时间也算开灯时间
            totalOpenMillis += end.getTime() - lastOpenStart.getTime();
        }
        //当天开灯时间(单位：小时)
        return totalOpenMillis / (1000.0 * 3600.0);
    }

    DeviceScreenRecord selectLastBefore(Long deviceId, Date time, Long tenantId) {
        List<DeviceScreenRecord> list = deviceScreenRecordRepository.selectList(Wrappers.<DeviceScreenRecord>lambdaQuery()
                .eq(DeviceScreenRecord::getDeviceId, deviceId).le(DeviceScreenRecord::getRecordTime, time)
                .eq(DeviceScreenRecord::getTenantId, tenantId)
                .orderByDesc(DeviceScreenRecord::getRecordTime).last("LIMIT 1"));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
