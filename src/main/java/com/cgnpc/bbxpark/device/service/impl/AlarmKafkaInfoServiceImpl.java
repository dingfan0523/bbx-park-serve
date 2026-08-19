package com.cgnpc.bbxpark.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.cgnpc.bbxpark.device.domain.AlarmKafkaInfo;
import com.cgnpc.bbxpark.device.mapper.AlarmKafkaInfoRepository;
import com.cgnpc.bbxpark.device.service.IAlarmKafkaInfoService;
import com.cgnpc.bbxpark.space.dto.kafka.ThingModelKaFkaMessage;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class AlarmKafkaInfoServiceImpl extends BaseServiceImpl<AlarmKafkaInfoRepository, AlarmKafkaInfo> implements IAlarmKafkaInfoService {
	@Autowired
	private AlarmKafkaInfoRepository alarmKafkaInfoRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveDeviceAlertConsume(String msg, Long offset) {
		ThingModelKaFkaMessage kafkaModel = JSON.parseObject(msg, ThingModelKaFkaMessage.class);
		kafkaModel.setDeviceIds(Collections.singletonList(kafkaModel.getDeviceId()));
		AlarmKafkaInfo info = buildAlarmKafkaInfo(kafkaModel, msg, offset);
        boolean flag = save(info);
        log.info("=====>KAFKA收到消息|保存Kafka告警信息|保存结果:{}|数据:{}",flag,info);
        return flag;
	}

	@Override
	public int restoreDeviceAlertConsume(String msg, Long offset) {
		ThingModelKaFkaMessage kafkaModel = JSON.parseObject(msg, ThingModelKaFkaMessage.class);
		kafkaModel.setDeviceIds(Collections.singletonList(kafkaModel.getDeviceId()));
		AlarmKafkaInfo info = buildRestoreAlarmKafkaInfo(kafkaModel, msg, offset);
		int num = alarmKafkaInfoRepository.insert(info);
        log.info("=====>KAFKA收到消息|保存Kafka撤销告警消息|保存结果:{}|数据:{}",num,info);
		return 1;
	}

	private AlarmKafkaInfo buildAlarmKafkaInfo(ThingModelKaFkaMessage kafkaModel, String msg, Long offset){
		AlarmKafkaInfo info = new AlarmKafkaInfo();
		info.setId(null);
		info.setAlarmSource(1);
		info.setSourceId(kafkaModel.getRuleId());
		info.setAction(1);
		info.setDeviceDn(String.join("&", kafkaModel.getDeviceIds()));
		info.setAlarmName(kafkaModel.getAlterName());
		info.setAlarmLevel(kafkaModel.getLevel());
		if(kafkaModel.getOccurred() != null){
			Date date = new Date();
			date.setTime(kafkaModel.getOccurred());
			info.setAlarmTime(date);
		}
		info.setAlarmDesc(kafkaModel.getContent());

		info.setContentJson(msg);
		info.setOffset(offset);
		return info;
	}
	private AlarmKafkaInfo buildRestoreAlarmKafkaInfo(ThingModelKaFkaMessage kafkaModel, String msg, Long offset){
		AlarmKafkaInfo info = buildAlarmKafkaInfo(kafkaModel, msg, offset);
		info.setAction(2);
		return info;
	}
}
