package com.cgnpc.bbxpark.device.service.impl;

import com.cgnpc.bbxpark.device.domain.AlarmIgnoreConfig;
import com.cgnpc.bbxpark.device.mapper.AlarmIgnoreConfigRepository;
import com.cgnpc.bbxpark.device.service.IAlarmIgnoreConfigService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class AlarmIgnoreConfigServiceImpl extends BaseServiceImpl<AlarmIgnoreConfigRepository, AlarmIgnoreConfig> implements IAlarmIgnoreConfigService {

}
