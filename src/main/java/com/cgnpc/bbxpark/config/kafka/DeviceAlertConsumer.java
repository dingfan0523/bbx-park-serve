package com.cgnpc.bbxpark.config.kafka;

import com.cgnpc.bbxpark.device.service.IAlarmInfoService;
import com.cgnpc.bbxpark.device.service.IAlarmKafkaInfoService;
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
 * 表册管理消费者
 *
 * @author lee
 * @date 2022/11/2 18:22
 */
@Slf4j
@Component
public class DeviceAlertConsumer implements InitializingBean {

    @Value("${ioc.kafka.deviceAlertTopics:iot_to_ioc_device_alert}")
    String deviceAlertTopics;

    @Value("${ioc.kafka.cancelAlterDeviceTopic:iot_to_ioc_cancel_alter_device_topic}")
    String cancelAlterDeviceTopic;

    @Autowired
    private IAlarmKafkaInfoService alarmKafkaInfoService;
    @Autowired
    private IAlarmInfoService alarmInfoService;


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
    @KafkaListener(topics = "${ioc.kafka.deviceAlertTopics:iot_to_ioc_device_alert}",
            groupId = "${ioc.kafka.group:c01}",
            autoStartup = "${ioc.kafka.customer.enabled:false}",
            containerFactory = "deviceAlertConsumerFactory")
    public void deviceAlertTopics(ConsumerRecord<String, String> record, final Acknowledgment ack) {
        try {
            log.info("========>deviceAlertTopics key -> {} offset -> {} value -> {}", record.key(), record.offset(),record.value());
            log.info("=====>KAFKA收到消息|设备告警|key:{}|offset:{}|value:\n{}",record.key(),record.offset(),record.value());
            // 消息记录
            executor.execute(()-> alarmKafkaInfoService.saveDeviceAlertConsume(record.value(), record.offset()));
            // 业务处理
            executor.execute(() -> alarmInfoService.saveAlarmInfo(record.value(), record.offset()));
        } catch (Exception e) {
            log.info("=====>KAFKA收到消息|设备告警|消费失败|报错:{}|value:\n{}",Throwables.getStackTraceAsString(e),record);
            log.error("deviceAlertTopics 失败，{},{}", record, Throwables.getStackTraceAsString(e));
        }finally {
            ack.acknowledge();
        }
    }


    /**
     * 告警恢复
     * @param record
     * @param ack
     */
    @KafkaListener(topics = "${ioc.kafka.cancelAlterDeviceTopic:iot_to_ioc_cancel_alter_device_topic}",
            groupId = "${ioc.kafka.group:c01}",
            autoStartup = "${ioc.kafka.customer.enabled:false}",
            containerFactory = "deviceAlertConsumerFactory")
    public void cancelAlterDeviceTopic(ConsumerRecord<String, String> record, final Acknowledgment ack) {
        try {
            log.info("=====>KAFKA收到消息|设备撤销告警|key:{}|offset:{}|value:\n{}",record.key(),record.offset(),record.value());
            log.info("========>cancelAlterDeviceTopic key -> {} offset -> {} value -> {}", record.key(), record.offset(),record.value());
            // 消息记录
            executor.execute(()-> alarmKafkaInfoService.restoreDeviceAlertConsume(record.value(), record.offset()));
            // 业务处理
            executor.execute(()-> alarmInfoService.restoreAlarmInfo(record.value(), record.offset()));
        } catch (Exception e) {
            log.info("=====>KAFKA收到消息|设备撤销告警|消费失败|报错:{}|value:\n{}",Throwables.getStackTraceAsString(e),record);
            log.error("cancelAlterDeviceTopic 失败，{},{}", record, Throwables.getStackTraceAsString(e));
        }finally {
            ack.acknowledge();
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (kafkaProducer != null) {
            // 创建多分区消息
            kafkaProducer.createPusherTopic(deviceAlertTopics);
            kafkaProducer.createPusherTopic(cancelAlterDeviceTopic);
        }
    }
}
