package com.cgnpc.bbxpark.device.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.device.domain.AlarmDevice;
import com.cgnpc.bbxpark.device.domain.AlarmInfo;
import com.cgnpc.bbxpark.device.domain.DeviceHealthRecord;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.mapper.AlarmDeviceRepository;
import com.cgnpc.bbxpark.device.mapper.AlarmInfoRepository;
import com.cgnpc.bbxpark.device.mapper.DeviceHealthRecordRepository;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.device.service.IDeviceHealthRecordService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceHealthRecordServiceImpl extends BaseServiceImpl<DeviceHealthRecordRepository, DeviceHealthRecord> implements IDeviceHealthRecordService {
    @Autowired
    private ITenantInfoService tenantInfoService;
    @Autowired
    private IocDeviceRepository iocDeviceRepository;
    @Autowired
    private AlarmInfoRepository alarmInfoRepository;
    @Autowired
    private AlarmDeviceRepository alarmDeviceRepository;

    @Override
    public void statisticsDeviceHealthData() {
        //获取昨天的开始时间和结束时间
        Date yesterdayStart = DateUtil.getFirstTimeOfDateOffset(new Date(),-1);
        Date yesterdayEnd = DateUtil.getLastTimeOfDateOffset(new Date(),-1);
        List<TenantInfo> tenants = tenantInfoService.list(Wrappers.<TenantInfo>lambdaQuery().eq(TenantInfo::getDeleted, Delete.NORMAL.getKey()));
        tenants.forEach(tenant -> {
            //分租户查询启用的智能化设备列表
            List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery()
                    .eq(IocDevice::getEnableStatus, Status.enabled.getKey())
                    .ne(IocDevice::getIotDevicePlatform,0)
                    .eq(IocDevice::getDeleted,Delete.NORMAL.getKey())
                    .eq(IocDevice::getTenantId,tenant.getId()));
            //查询当天产生过告警的设备id
            List<AlarmInfo> alarms = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().between(AlarmInfo::getAlarmLastTime, yesterdayStart,yesterdayEnd).eq(AlarmInfo::getTenantId,tenant.getId()));
            List<Long> alarmIds = alarms.stream().map(AlarmInfo::getId).collect(Collectors.toList());
            List<AlarmDevice> alarmDevices = CollectionUtils.isNotEmpty(alarmIds) ? alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds)) : Collections.emptyList();
            List<Long> deviceIds = alarmDevices.stream().map(AlarmDevice::getDeviceId).distinct().collect(Collectors.toList());
            List<DeviceHealthRecord> list = devices.stream().map(d->{
                DeviceHealthRecord record = new DeviceHealthRecord();
                record.setDeviceId(d.getId());
                record.setDeviceName(d.getDeviceName());
                record.setSpaceId(d.getSpaceId());
                record.setOnline(d.getIotDeviceStatus());
                record.setAlarm(deviceIds.contains(d.getId()) ? 1 : 0);
                record.setTenantId(tenant.getId());
                record.setRecordTime(yesterdayEnd);
                return record;
            }).collect(Collectors.toList());
            saveBatch(list);
        });
    }
}
