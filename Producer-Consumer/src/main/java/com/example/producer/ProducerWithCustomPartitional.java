package com.example.producer;

import com.example.partitioner.CustomPartitionel;
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
 * Class: ProducerWithCustomPartitional
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class ProducerWithCustomPartitional {

    private static final Logger logger = LoggerFactory.getLogger(ProducerWithCustomPartitional.class);
    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String TOPIC_NAME = "test";
    private static final String KEY_NAME = "Pangyo";

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitionel.class);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

        String message = "testMessage";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, KEY_NAME, message);

        try {
            RecordMetadata metadata = producer.send(record).get();
            logger.info(metadata.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
