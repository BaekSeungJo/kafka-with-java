package com.example.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

/**
 * Project : Consumer-Producer
 * Class: ConsumerWithExatPartition
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class ConsumerWithExatPartition {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerWithExatPartition.class);
    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String TOPIC_NAME = "test";
    private static final int PARTITION_NUM = 0;

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
        consumer.assign(Collections.singleton(new TopicPartition(TOPIC_NAME, PARTITION_NUM)));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}", record);
            }
        }
    }
}
