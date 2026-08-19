package com.cgnpc.bbxpark.device.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.device.domain.DeviceScreenRecord;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.mapper.DeviceScreenRecordRepository;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.device.service.IDeviceScreenRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DeviceScreenRecordServiceImpl extends ServiceImpl<DeviceScreenRecordRepository, DeviceScreenRecord> implements IDeviceScreenRecordService {
   @Autowired
   private IocDeviceRepository iocDeviceRepository;
    @Override
    public List<DeviceScreenRecord> selectList(DeviceScreenRecord deviceScreenRecord) {
        return Collections.emptyList();
    }

    @Override
    public DeviceScreenRecord save(String deviceId, Integer type) {
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery().eq(IocDevice::getIotDeviceDn,deviceId).eq(IocDevice::getDeleted, Delete.NORMAL.getKey()));
        if(CollectionUtils.isNotEmpty(devices)){
            DeviceScreenRecord record = new DeviceScreenRecord();
            record.setDeviceId(devices.get(0).getId());
            record.setType(type);
            record.setRecordTime(new Date());
            record.setTenantId(devices.get(0).getTenantId());
            save(record);
            return record;
        }
        return null;
    }
}
