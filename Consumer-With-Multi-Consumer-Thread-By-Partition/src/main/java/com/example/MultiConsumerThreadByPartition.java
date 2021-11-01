package com.example;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project : Producer-Consumer
 * Class: MultiConsumerThreadByPartition
 * Created by baegseungjo on 2021/11/02
 * <p>
 * Description:
 */
public class MultiConsumerThreadByPartition {
    private static final Logger logger = LoggerFactory.getLogger(MultiConsumerThreadByPartition.class);

    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String TOPIC_NAME = "my-test";
    private static final String GROUP_ID = "test-group";

    private final static List<ConsumerWorker> workerThread = new ArrayList<>();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        int CONSUMER_COUNT = getPartitionCount(TOPIC_NAME);
        logger.info("Set thread count : {}", CONSUMER_COUNT);

        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < CONSUMER_COUNT; i++) {
            ConsumerWorker worker = new ConsumerWorker(config, TOPIC_NAME, i);
            workerThread.add(worker);
            executorService.execute(worker);
        }

    }

    static int getPartitionCount(String topic) {
        logger.info("Get {} partition size", topic);
        int partitions;
        Properties adminConfig = new Properties();
        adminConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        AdminClient admin = AdminClient.create(adminConfig);
        try {
            DescribeTopicsResult result = admin.describeTopics(Arrays.asList(topic));
            Map<String, KafkaFuture<TopicDescription>> values = result.values();
            KafkaFuture<TopicDescription> topicDescription = values.get(topic);
            partitions = topicDescription.get().partitions().size();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            partitions = getDefaultPartitionSize();
        }

        admin.close();
        return partitions;
    }

    static int getDefaultPartitionSize() {
        logger.info("getDefaultPartitionSize");
        int partitions = 1;
        Properties adminConfig = new Properties();
        adminConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        AdminClient admin = AdminClient.create(adminConfig);
        try {
            for(Node node : admin.describeCluster().nodes().get()) {
                ConfigResource cr = new ConfigResource(ConfigResource.Type.BROKER, "0");
                DescribeConfigsResult describeConfigsResult = admin.describeConfigs(Collections.singleton(cr));
                Config cf = describeConfigsResult.all().get().get(cr);
                Optional<ConfigEntry> optionalConfigEntry = cf.entries().stream().filter(v -> v.name().equals("num.partitions")).findFirst();
                ConfigEntry numPartitionConfig = optionalConfigEntry.orElseThrow(Exception::new);
                partitions = Integer.parseInt(numPartitionConfig.value());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        admin.close();
        return partitions;
    }

    static class ShutdownThread extends Thread {
        @Override
        public void run() {
            workerThread.forEach(ConsumerWorker::shutdown);
            System.out.println("Bye");
        }
    }
}
