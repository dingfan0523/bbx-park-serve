package com.cgnpc.bbxpark.config.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.unit.DataSize;

import java.time.Duration;
import java.util.*;

/**
 * @author gujun
 * @version 1.0
 * @data 2022/08/06
 */

@Component
@ConfigurationProperties(prefix = "ioc.kafka")
public class KafkaComponentProperties {

    private boolean enabled = false;

    // CollUtil.newArrayList("10.8.43.85:19092")
    private List<String> servers = new ArrayList<>();

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    private String clientId;

    /**
     * 分区数，用于创建多分区topic
     */
    private Integer numPartitions;

    /**
     * 副本数，用于创建多副本topic
     */
    private Integer replicationFactor;

    private final Map<String, String> properties = new HashMap<>();

    private final Consumer consumer = new Consumer();

    private final Producer producer = new Producer();

    private final Template template = new Template();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(Integer numPartitions) {
        this.numPartitions = numPartitions;
    }

    public Integer getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(Integer replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Producer getProducer() {
        return producer;
    }

    public Template getTemplate() {
        return template;
    }

    private Map<String, Object> buildCommonProperties() {
        Map<String, Object> properties = new HashMap<>();
        if (this.servers != null) {
            properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, this.servers
        );
        }
        if (this.clientId != null) {
            properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, this.clientId);
        }
        if (!CollectionUtils.isEmpty(this.properties)) {
            properties.putAll(this.properties);
        }
        return properties;
    }

    public Map<String, Object> buildConsumerProperties() {
        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(this.consumer.buildProperties());
        return properties;
    }

    /**
     * Create an initial map of producer properties from the state of this instance.
     * <p>
     * This allows you to add additional properties, if necessary, and override the
     * default kafkaProducerFactory bean.
     * @return the producer properties initialized with the customizations defined on this
     * instance
     */
    public Map<String, Object> buildProducerProperties() {
        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(this.producer.buildProperties());
        return properties;
    }

    public static class Template {

        /**
         * Default topic to which messages are sent.
         */
        private String defaultTopic;

        public String getDefaultTopic() {
            return this.defaultTopic;
        }

        public void setDefaultTopic(String defaultTopic) {
            this.defaultTopic = defaultTopic;
        }

    }

    public static class Consumer {

        /**
         * Frequency with which the consumer offsets are auto-committed to Kafka if
         * 'enable.auto.commit' is set to true.
         */
        private Duration autoCommitInterval = Duration.ofMillis(100);

        /**
         * What to do when there is no initial offset in Kafka or if the current offset no
         * longer exists on the server.
         */
        private String autoOffsetReset = "latest";

        /**
         * Comma-delimited list of host:port pairs to use for establishing the initial
         * connections to the Kafka cluster. Overrides the global property, for consumers.
         */
        private List<String> servers
;

        /**
         * ID to pass to the server when making requests. Used for server-side logging.
         */
        private String clientId;

        /**
         * Whether the consumer's offset is periodically committed in the background.
         */
        private Boolean enableAutoCommit = true;


        /**
         * Unique string that identifies the consumer group to which this consumer
         * belongs.
         */
        private String groupId;


        /**
         * Deserializer class for keys.
         */
        private Class<?> keyDeserializer = StringDeserializer.class;

        /**
         * Deserializer class for values.
         */
        private Class<?> valueDeserializer = StringDeserializer.class;


        /**
         * Additional consumer-specific properties used to configure the client.
         */
        private final Map<String, String> properties = new HashMap<>();

        public Duration getAutoCommitInterval() {
            return this.autoCommitInterval;
        }

        public void setAutoCommitInterval(Duration autoCommitInterval) {
            this.autoCommitInterval = autoCommitInterval;
        }

        public String getAutoOffsetReset() {
            return this.autoOffsetReset;
        }

        public void setAutoOffsetReset(String autoOffsetReset) {
            this.autoOffsetReset = autoOffsetReset;
        }

        public List<String> getBootstrapServers() {
            return this.servers
;
        }

        public void setBootstrapServers(List<String> servers
) {
            this.servers
 = servers
;
        }

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public Boolean getEnableAutoCommit() {
            return this.enableAutoCommit;
        }

        public void setEnableAutoCommit(Boolean enableAutoCommit) {
            this.enableAutoCommit = enableAutoCommit;
        }

        public String getGroupId() {
            return this.groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public Class<?> getKeyDeserializer() {
            return this.keyDeserializer;
        }

        public void setKeyDeserializer(Class<?> keyDeserializer) {
            this.keyDeserializer = keyDeserializer;
        }

        public Class<?> getValueDeserializer() {
            return this.valueDeserializer;
        }

        public void setValueDeserializer(Class<?> valueDeserializer) {
            this.valueDeserializer = valueDeserializer;
        }

        public Map<String, String> getProperties() {
            return this.properties;
        }

        public Map<String, Object> buildProperties() {
            Properties properties = new Properties();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(this::getAutoCommitInterval).asInt(Duration::toMillis)
                    .to(properties.in(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
            map.from(this::getAutoOffsetReset).to(properties.in(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
            map.from(this::getBootstrapServers).to(properties.in(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
            map.from(this::getClientId).to(properties.in(ConsumerConfig.CLIENT_ID_CONFIG));
            map.from(this::getEnableAutoCommit).to(properties.in(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
            //map.from(this::getFetchMaxWait).asInt(Duration::toMillis)
            //        .to(properties.in(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG));
            //map.from(this::getFetchMinSize).asInt(DataSize::toBytes)
            //        .to(properties.in(ConsumerConfig.FETCH_MIN_BYTES_CONFIG));
            map.from(this::getGroupId).to(properties.in(ConsumerConfig.GROUP_ID_CONFIG));
            //map.from(this::getHeartbeatInterval).asInt(Duration::toMillis)
            //        .to(properties.in(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG));
            map.from(this::getKeyDeserializer).to(properties.in(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
            map.from(this::getValueDeserializer).to(properties.in(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
            //map.from(this::getMaxPollRecords).to(properties.in(ConsumerConfig.MAX_POLL_RECORDS_CONFIG));
            return properties.with(this.properties);
        }

    }

    public static class Producer {

        /**
         * Number of acknowledgments the producer requires the leader to have received
         * before considering a request complete.
         */
        private String acks = "1";

        /**
         * Default batch size. A small batch size will make batching less common and may
         * reduce throughput (a batch size of zero disables batching entirely).
         */
        private DataSize batchSize = DataSize.ofBytes(16384);

        /**
         * Comma-delimited list of host:port pairs to use for establishing the initial
         * connections to the Kafka cluster. Overrides the global property, for producers.
         */
        private List<String> servers
;

        /**
         * Total memory size the producer can use to buffer records waiting to be sent to
         * the server.
         */
        private DataSize bufferMemory = DataSize.ofBytes(33554432);

        /**
         * ID to pass to the server when making requests. Used for server-side logging.
         */
        private String clientId;


        /**
         * Serializer class for keys.
         */
        private Class<?> keySerializer = StringSerializer.class;

        /**
         * Serializer class for values.
         */
        private Class<?> valueSerializer = StringSerializer.class;

        /**
         * When greater than zero, enables retrying of failed sends.
         */
        private Integer retries;


        /**
         * Additional producer-specific properties used to configure the client.
         */
        private final Map<String, String> properties = new HashMap<>();

        public String getAcks() {
            return this.acks;
        }

        public void setAcks(String acks) {
            this.acks = acks;
        }

        public DataSize getBatchSize() {
            return this.batchSize;
        }

        public void setBatchSize(DataSize batchSize) {
            this.batchSize = batchSize;
        }

        public List<String> getBootstrapServers() {
            return this.servers
;
        }

        public void setBootstrapServers(List<String> servers
) {
            this.servers
 = servers
;
        }

        public DataSize getBufferMemory() {
            return this.bufferMemory;
        }

        public void setBufferMemory(DataSize bufferMemory) {
            this.bufferMemory = bufferMemory;
        }

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public Class<?> getKeySerializer() {
            return this.keySerializer;
        }

        public void setKeySerializer(Class<?> keySerializer) {
            this.keySerializer = keySerializer;
        }

        public Class<?> getValueSerializer() {
            return this.valueSerializer;
        }

        public void setValueSerializer(Class<?> valueSerializer) {
            this.valueSerializer = valueSerializer;
        }

        public Integer getRetries() {
            return this.retries;
        }

        public void setRetries(Integer retries) {
            this.retries = retries;
        }


        public Map<String, String> getProperties() {
            return this.properties;
        }

        public Map<String, Object> buildProperties() {
            Properties properties = new Properties();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(this::getAcks).to(properties.in(ProducerConfig.ACKS_CONFIG));
            map.from(this::getBatchSize).asInt(DataSize::toBytes).to(properties.in(ProducerConfig.BATCH_SIZE_CONFIG));
            map.from(this::getBootstrapServers).to(properties.in(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
            map.from(this::getBufferMemory).as(DataSize::toBytes)
                    .to(properties.in(ProducerConfig.BUFFER_MEMORY_CONFIG));
            map.from(this::getClientId).to(properties.in(ProducerConfig.CLIENT_ID_CONFIG));
            //map.from(this::getCompressionType).to(properties.in(ProducerConfig.COMPRESSION_TYPE_CONFIG));
            map.from(this::getKeySerializer).to(properties.in(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
            map.from(this::getRetries).to(properties.in(ProducerConfig.RETRIES_CONFIG));
            map.from(this::getValueSerializer).to(properties.in(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
            return properties.with(this.properties);
        }
    }

    private static class Properties extends HashMap<String, Object> {

        public <V> java.util.function.Consumer<V> in(String key) {
            return (value) -> put(key, value);
        }

        public Properties with(Map<String, String> properties) {
            putAll(properties);
            return this;
        }

    }
}
