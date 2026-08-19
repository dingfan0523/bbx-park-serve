package com.cgnpc.bbxpark.device.service;

import com.cgnpc.bbxpark.device.domain.DeviceHealthRecord;
import com.cgnpc.cud.core.service.IBaseService;

public interface IDeviceHealthRecordService extends IBaseService<DeviceHealthRecord> {
    void statisticsDeviceHealthData();
}
