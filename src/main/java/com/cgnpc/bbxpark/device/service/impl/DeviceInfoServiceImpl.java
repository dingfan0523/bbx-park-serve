package com.cgnpc.bbxpark.device.service.impl;

import com.cgnpc.bbxpark.device.domain.DeviceInfo;
import com.cgnpc.bbxpark.device.mapper.DeviceInfoRepository;
import com.cgnpc.bbxpark.device.service.IDeviceInfoService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DeviceInfoServiceImpl extends BaseServiceImpl<DeviceInfoRepository, DeviceInfo> implements IDeviceInfoService {
}
