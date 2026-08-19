package com.cgnpc.bbxpark.device.service;

import com.cgnpc.bbxpark.device.domain.DeviceScreenRecord;
import com.cgnpc.cud.core.service.IBaseService;

public interface IDeviceScreenRecordService extends IBaseService<DeviceScreenRecord> {
    DeviceScreenRecord save(String deviceId,Integer type);
}
