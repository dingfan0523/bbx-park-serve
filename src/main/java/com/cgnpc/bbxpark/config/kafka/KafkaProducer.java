package com.cgnpc.bbxpark.config.kafka;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * kafka 生产者
 * 区别sprint封装的kafka，可自定义topic分区等属性
 *
 * @author gujun
 * @date: 2024/09/10 10:27
 */
@Slf4j
public class KafkaProducer {

    private KafkaComponentProperties kafkaComponentProperties;

    public KafkaProducer(KafkaComponentProperties kafkaComponentProperties) {
        this.kafkaComponentProperties = kafkaComponentProperties;
    }

    public boolean createPusherTopic(@NonNull String topicName) throws ExecutionException, InterruptedException, TimeoutException {
        try (AdminClient adminClient = KafkaAdminClient.create(kafkaComponentProperties.buildProducerProperties());){
            ListTopicsResult topicsResult = adminClient.listTopics();
            KafkaFuture<Set<String>> namesFuture = topicsResult.names();
            Set<String> names = namesFuture.get();
            if (names.contains(topicName)){
                log.warn("[{}] already exist", topicName);
                return false;
            }
            NewTopic newTopic = new NewTopic(topicName,
                    Optional.ofNullable(kafkaComponentProperties.getNumPartitions()).orElse(1),
                    (short) (Optional.ofNullable(kafkaComponentProperties.getReplicationFactor()).orElse(1) + 0.0));
            CreateTopicsResult topics = adminClient.createTopics(Collections.singleton(newTopic));
            KafkaFuture<Void> all = topics.all();
            all.get(5, TimeUnit.SECONDS);
            return all.isDone();
        }
    }

}
