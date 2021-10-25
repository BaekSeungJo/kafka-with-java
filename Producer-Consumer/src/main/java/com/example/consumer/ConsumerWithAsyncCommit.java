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
 * Class: ConsumerWithAsyncCommit
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class ConsumerWithAsyncCommit {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerWithAsyncCommit.class);
    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String TOPIC_NAME = "test";
    private static final String GROUP_NAME = "test-group";

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_NAME);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}", record);
            }

            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    if(exception != null) {
                        System.out.println("Commit failed");
                    } else {
                        System.out.println("Commit successed");
                    }

                    Set<Map.Entry<TopicPartition, OffsetAndMetadata>> entries = offsets.entrySet();
                    for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : entries) {
                        System.out.println(entry.getKey().toString());
                        System.out.println(entry.getValue().toString());
                    }

                    if(exception != null) {
                        logger.error(exception.getMessage(), exception);
                    }
                }
            });
        }
    }
}
