package com.example.producer;

import com.example.callback.ProducerCallback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Project : Consumer-Producer
 * Class: ProducerWithAsyncCallback
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class ProducerWithAsyncCallback {
    private static final Logger logger = LoggerFactory.getLogger(ProducerWithAsyncCallback.class);
    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String TOPIC_NAME = "test";

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

        String message = "testMessage";

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, message);

        producer.send(record, new ProducerCallback());

        producer.flush();
        producer.close();
    }
}
