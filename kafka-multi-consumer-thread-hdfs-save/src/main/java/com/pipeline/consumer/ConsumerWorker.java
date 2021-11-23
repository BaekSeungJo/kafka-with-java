package com.pipeline.consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : Producer-Consumer
 * Class: ConsumerWorker
 * Created by baegseungjo on 2021/11/09
 * <p>
 * Description:
 */
public class ConsumerWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerWorker.class);
    private static Map<Integer, List<String>> bufferString = new ConcurrentHashMap<>();
    private static Map<Integer, Long> currentFileOffset = new ConcurrentHashMap<>();

    private static final int FLUSH_RECORD_COUNT = 10;
    private Properties prop;
    private String topic;
    private String threadName;
    private KafkaConsumer<String, String> consumer;

    public ConsumerWorker(Properties prop, String topic, int number) {
        logger.info("Generate ConsumerWorker");
        this.prop = prop;
        this.topic = topic;
        this.threadName = "consumer-thread-" + number;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);
        consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record : records) {
                    addHdfsFileBuffer(record);
                }
                saveBufferToHdfsFile(consumer.assignment());
            }
        } catch (WakeupException e) {
            logger.warn("Wakeup consumer");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            consumer.close();
        }
    }

    /**
     * ConsumeRecord 하나를 받아서 ConcurentHashMap의 key(partitionNo)에 해당하는 List에 추가한다.
     * hdfs 파일 시스템에 장애 발생시 복구 시점을 알기 위ㅣ해 currentFileOffset에 첫번째 레코드의 offset을 저장한다.
     *
     * 하나의 consumer은 여러 파티션의 데이터를 받을 수 있기 때문에 Concurernt Collection을 사용한다.
     *
     * @param record
     */
    private void addHdfsFileBuffer(ConsumerRecord<String, String> record) {
        List<String> buffer = bufferString.getOrDefault(record.partition(), new ArrayList<>());
        buffer.add(record.value());
        bufferString.put(record.partition(), buffer);

        if (buffer.size() == 1) {
            currentFileOffset.put(record.partition(), record.offset());
        }
    }

    /**
     * hdfs 파일 시스템에 저장할 수 있는지 검증한다.
     *
     * @param partitions
     */
    private void saveBufferToHdfsFile(Set<TopicPartition> partitions) {
        partitions.forEach(p -> checkFlushCount(p.partition()));
    }

    /**
     * 한 파일에 10개의 레코드를 저장하기 위해 검증 및 저장한다.
     *
     * @param partitionNo
     */
    private void checkFlushCount(int partitionNo) {
        if(bufferString.get(partitionNo) != null) {
            if(bufferString.get(partitionNo).size() > FLUSH_RECORD_COUNT -1) {
                save(partitionNo);
            }
        }
    }

    /**
     * 실제 hdfs 파일 시스템에 파티션 번호, 시작 offset을 이용하여 저장한다.
     *
     * hdfs 파일 시스템에 저장 완료 후, 파티션의 레코드 데이터를 담는 List를 초기화한다.
     *
     * @param partitionNo
     */
    private void save(int partitionNo) {
        if(bufferString.get(partitionNo).size() > 0) {
            try {
                String fileName = "/Users/baegseungjo/data/color-" + partitionNo + "-" + currentFileOffset.get(partitionNo) + ".log";
                Configuration configuration = new Configuration();
                configuration.set("fs.defaultFs", "hdfs://localhost:9000");
                FileSystem hdfsFileSystem = FileSystem.get(configuration);
                FSDataOutputStream fileOutputStream = hdfsFileSystem.create(new Path(fileName));
                fileOutputStream.writeBytes(StringUtils.join(bufferString.get(partitionNo), "\n"));
                fileOutputStream.close();

                bufferString.put(partitionNo, new ArrayList<>());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 프로세스 종료 시, 남아있는 파티션별 레코드들을 hdfs 파일 시스템에 저장한다.
     */
    private void saveRemainBufferToHdfsFile() {
        bufferString.forEach((partitionNo, v) -> this.save(partitionNo));
    }

    /**
     * 프로세스 종료시 호출받는 callback 메서드
     */
    public void stopAndWakeup() {
        logger.info("stopAndWakeup");
        consumer.wakeup();
        saveRemainBufferToHdfsFile();
    }
}
