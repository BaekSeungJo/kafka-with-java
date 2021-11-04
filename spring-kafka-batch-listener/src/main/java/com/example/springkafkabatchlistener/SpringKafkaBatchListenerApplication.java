package com.example.springkafkabatchlistener;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

@SpringBootApplication
public class SpringKafkaBatchListenerApplication {
    public static final Logger logger = LoggerFactory.getLogger(SpringKafkaBatchListenerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaBatchListenerApplication.class, args);
    }

    @KafkaListener(topics = "my-test",
            groupId = "test-group-01")
    public void batchListener(ConsumerRecords<String, String> records) {
        records.forEach(record -> logger.info(record.toString()));
    }

    @KafkaListener(topics = "my-test",
            groupId = "test-group-02")
    public void batchListener(List<String> list) {
        list.forEach(recordValue -> logger.info(recordValue));
    }

    @KafkaListener(topics = "my-test",
            groupId = "test-group-03",
            concurrency = "3")
    public void concurrentBatchListener(ConsumerRecords<String, String> records) {
        records.forEach(record -> logger.info(record.toString()));
    }
}
