package com.cgnpc.bbxpark.config.kafka;

import com.cgnpc.bbxpark.device.service.IDeviceOperationLogService;
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

import java.util.List;
import java.util.concurrent.Executor;


/**
 * 表册管理消费者
 *
 * @author lee
 * @date 2022/11/2 18:22
 */
@Slf4j
@Component
public class OperationLogConsumer implements InitializingBean {

    @Value("${ioc.kafka.operation_log_topic:iot_to_ioc_device_operation_log}")
    String operationLogTopic;

    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executor;

    @Autowired
    private IDeviceOperationLogService deviceOperationLogService;


    @KafkaListener(topics = "${ioc.kafka.operation_log_topic:iot_to_ioc_device_operation_log}",
            groupId = "${ioc.kafka.group:c0}",
            autoStartup = "${ioc.kafka.customer.enabled:false}",
            containerFactory = "consumerFactory")
    public void operationLog(List<ConsumerRecord<String, String> > records, final Acknowledgment ack) {
        try {
            for (ConsumerRecord<String, String> record : records ) {
                log.info("operationLog key -> {} offset -> {} value -> {}", record.key(), record.offset(),record.value());
                // 异步保存
                executor.execute(()-> deviceOperationLogService.saveDeviceOperationLog(record.value()));
            }
        } catch (Exception e) {
            log.error("operationLog 失败，{},{}", records, Throwables.getStackTraceAsString(e));
        } finally {
            ack.acknowledge();
        }
    }


    @Autowired(required = false)
    private KafkaProducer kafkaProducer;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (kafkaProducer != null) {
            // 创建多分区消息
            kafkaProducer.createPusherTopic(operationLogTopic);
        }
    }
}
