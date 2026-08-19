package com.cgnpc.bbxpark.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 表册消费者工厂配置
 *
 * @author lee
 * @date 2022/11/14 15:33
 */
@Configuration
public class ConsumerFactoryConfiguration {

    @Resource
    KafkaComponentProperties kafkaComponentProperties;


    /**
     * 通用配置
     */
    @Bean("consumerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> consumerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setAckTime(60000);
        // 批量拉取数据
        factory.setBatchListener(true);
        // KafkaMessageListenerContainer实例数= 服务器机器数量*concurrency ;
        factory.setConcurrency(1);
        return factory;
    }

    @Bean("deviceAlertConsumerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> deviceAlertConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(oneConsumerConfigs()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setAckTime(60000);
        // 批量拉取数据
        factory.setBatchListener(false);
        // KafkaMessageListenerContainer实例数= 服务器机器数量*concurrency ;
        factory.setConcurrency(1);
        return factory;
    }

    @Bean("deviceScreenConsumerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> deviceScreenConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(oneConsumerConfigs()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setAckTime(60000);
        // 批量拉取数据
        factory.setBatchListener(false);
        // KafkaMessageListenerContainer实例数= 服务器机器数量*concurrency ;
        factory.setConcurrency(1);
        return factory;
    }



    private Map<String, Object> consumerConfigs() {
        Map<String, Object> consumerProperties = kafkaComponentProperties.buildConsumerProperties();
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 60 * 60 * 1000);
        return consumerProperties;
    }

    /**
     * 一次处理一条
     * @return
     */
    private Map<String, Object> oneConsumerConfigs() {
        Map<String, Object> consumerProperties = kafkaComponentProperties.buildConsumerProperties();
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 每次只拉去一条数据
        consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 60 * 60 * 1000);
        return consumerProperties;
    }



}
