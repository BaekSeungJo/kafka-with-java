package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Project : Producer-Consumer
 * Class: ConsumerWorker
 * Created by baegseungjo on 2021/11/02
 * <p>
 * Description:
 */
public class ConsumerWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerWorker.class);

    private final Properties config;
    private final String topic;
    private final String threadName;
    private KafkaConsumer<String, String> consumer;

    public ConsumerWorker(Properties config, String topic, int number) {
        this.config = config;
        this.topic = topic;
        this.threadName = "consumer-thread-" + number;
    }

    @Override
    public void run() {
        consumer = new KafkaConsumer<String, String>(config);
        consumer.subscribe(Arrays.asList(topic));
        try {
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("{}", record);
                }
                consumer.commitSync();
            }
        } catch (WakeupException e) {
            System.out.println(threadName + " trigger WakeupException");
        } finally {
            consumer.commitSync();
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
