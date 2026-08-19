package com.cgnpc.bbxpark.device.service;


import com.cgnpc.bbxpark.device.domain.AlarmKafkaInfo;
import com.cgnpc.cud.core.service.IBaseService;

public interface IAlarmKafkaInfoService extends IBaseService<AlarmKafkaInfo> {

    boolean saveDeviceAlertConsume(String msg, Long offset);

    int restoreDeviceAlertConsume(String msg, Long offset);
}
