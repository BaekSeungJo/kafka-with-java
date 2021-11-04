package com.example.springkafkatemplateproducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@SpringBootApplication
public class SpringKafkaTemplateProducerApplication implements CommandLineRunner {
    private static final String TOPIC_NAME = "my-test";

    @Autowired
    private KafkaTemplate<String, String> customKafkaTemplate;

    public static void main(String[] args) {
//        SpringApplication.run(SpringKafkaTemplateProducerApplication.class, args);
        SpringApplication application = new SpringApplication(SpringKafkaTemplateProducerApplication.class);
        application.run();
    }

    @Override
    public void run(String... args) {
        ListenableFuture<SendResult<String, String>> future = customKafkaTemplate.send(TOPIC_NAME, "test-custom-template-producer");
        future.addCallback(new KafkaSendCallback<String, String>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("result : " + result.toString());
            }

            @Override
            public void onFailure(KafkaProducerException ex) {
                System.out.println("ex : " + ex.getMessage());
            }
        });
    }
}
