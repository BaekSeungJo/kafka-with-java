package com.example.springkafkacommitlistener;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@SpringBootApplication
public class SpringKafkaCommitListenerApplication {
    private static final Logger logger = LoggerFactory.getLogger(SpringKafkaCommitListenerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaCommitListenerApplication.class, args);
    }

    /**
     * BatchAcknowledgingMessageListener 사용
     *
     * @param records
     * @param ack
     */
    @KafkaListener(topics = "my-test",
            groupId = "test-group-01")
    public void commitListener(ConsumerRecords<String, String> records, Acknowledgment ack) {
        records.forEach(record -> logger.info(record.toString()));
        ack.acknowledge();
    }

    /**
     * BatchConsumerAwareMessageListener 사용
     *
     * @param records
     * @param consumer
     */
    @KafkaListener(topics = "my-test",
            groupId = "test-group-02")
    public void consumerCommitListener(ConsumerRecords<String, String> records, Consumer<String, String> consumer) {
        records.forEach(record -> logger.info(record.toString()));
        consumer.commitAsync();
    }
}
