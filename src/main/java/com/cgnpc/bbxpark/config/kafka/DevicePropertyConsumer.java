package com.cgnpc.bbxpark.config.kafka;

import com.alibaba.fastjson.JSONObject;
import com.cgnpc.bbxpark.device.service.IDeviceScreenRecordService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import java.util.concurrent.Executor;


/**
 * 设备属性监听
 *
 * @author lee
 * @date 2022/11/2 18:22
 */
@Slf4j
@Component
public class DevicePropertyConsumer implements InitializingBean {

    @Value("${ioc.kafka.deviceScreenTopic:device_screen_topic}")
    String deviceScreenTopic;

//    @Value("${ioc.kafka.cancelAlterDeviceTopic:iot_to_ioc_cancel_alter_device_topic}")
//    String cancelAlterDeviceTopic;
//
//    @Autowired
//    private IAlarmKafkaInfoService alarmKafkaInfoService;
//    @Autowired
//    private IAlarmInfoService alarmInfoService;
    @Autowired
    private IDeviceScreenRecordService deviceScreenRecordService;

    @Autowired(required = false)
    private KafkaProducer kafkaProducer;


    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executor;


    /**
     * 告警发生
     * @param record
     * @param ack
     */
    @KafkaListener(topics = "${ioc.kafka.deviceScreenTopic:device_screen_topic}",
            groupId = "${ioc.kafka.group:dev}",
            autoStartup = "${ioc.kafka.customer.enabled:false}",
            containerFactory = "deviceScreenConsumerFactory")
    public void deviceScreenTopics(ConsumerRecord<String, String> record, final Acknowledgment ack) {
        try {
            log.info("========>deviceScreenTopic key -> {} offset -> {} value -> {}", record.key(), record.offset(),record.value());
            log.info("=====>KAFKA收到消息|场景记录|key:{}|offset:{}|value:\n{}",record.key(),record.offset(),record.value());
            String value = record.value();
            JSONObject json = JSONObject.parseObject(value);
            JSONObject data = json.getJSONObject("data");
            String lightState = data.getString("lightState");
            String deviceId = json.getString("deviceId");
            executor.execute(()-> deviceScreenRecordService.save(deviceId, "1".equals(lightState) ? 1:2));
            // 消息记录
//            executor.execute(()-> alarmKafkaInfoService.saveDeviceAlertConsume(record.value(), record.offset()));
            // 业务处理
//            executor.execute(() -> alarmInfoService.saveAlarmInfo(record.value(), record.offset()));
        } catch (Exception e) {
            log.info("=====>KAFKA收到消息|场景记录|消费失败|报错:{}|value:\n{}",Throwables.getStackTraceAsString(e),record);
            log.error("deviceScreenTopic 失败，{},{}", record, Throwables.getStackTraceAsString(e));
        }finally {
            ack.acknowledge();
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (kafkaProducer != null) {
            // 创建多分区消息
            kafkaProducer.createPusherTopic(deviceScreenTopic);
        }
    }
}
