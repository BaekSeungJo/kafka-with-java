package com.example.springkafkalistenercontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class SpringKafkaListenerContainerApplication {
    private static final Logger logger = LoggerFactory.getLogger(SpringKafkaListenerContainerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaListenerContainerApplication.class, args);
    }

    @KafkaListener(topics = "my-test",
            groupId = "test-group",
            containerFactory = "consumerContainerFactory")
    public void customListener(String data) {
        logger.info(data);
    }
}
