package com.example.consumer;

import com.example.producer.ProducerWithExatPartition;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

/**
 * Project : Consumer-Producer
 * Class: ConsumerWithRebalanceListener
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class ConsumerWithRebalanceListener {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerWithRebalanceListener.class);
    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String TOPIC_NAME = "test";
    private static final String GROUP_NAME = "test-group";

    private static Consumer<String, String> consumer;
    private static Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_NAME);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        consumer = new KafkaConsumer<String, String>(config);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME), new RebalanceListener());

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}", record);
                currentOffset.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1, null)
                );

                consumer.commitSync(currentOffset);
            }
        }
    }

    private static class RebalanceListener implements ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            consumer.commitSync(currentOffset);
            logger.info("Partitions are revoked");
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            logger.info("Partitions are assigned");
        }
    }
}
