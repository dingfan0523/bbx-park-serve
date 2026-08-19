package com.cgnpc.bbxpark.device.service.impl;


import com.cgnpc.bbxpark.device.domain.AlarmHandleRecord;
import com.cgnpc.bbxpark.device.mapper.AlarmHandleRecordRepository;
import com.cgnpc.bbxpark.device.service.IAlarmHandleRecordService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AlarmHandleRecordServiceImpl extends BaseServiceImpl<AlarmHandleRecordRepository, AlarmHandleRecord> implements IAlarmHandleRecordService {
}
