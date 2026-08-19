package com.cgnpc.bbxpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.device.domain.DeviceEcStatistics;

public interface IDeviceEcStatisticsService extends IService<DeviceEcStatistics> {
    void calculateYesterdayStatistics();
}
