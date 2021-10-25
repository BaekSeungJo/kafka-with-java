package com.example;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

/**
 * Project : Producer-Consumer
 * Class: SimpleStreamApplication
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class SimpleStreamApplication {

    private static final String APPLICATION_NAME = "streams-application";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private static final String STREAM_LOG = "stream_log";
    private static final String STREAM_LOG_COPY = "stream_log_copy";

    public static void main(String[] args) {
        Properties pros = new Properties();
        pros.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        pros.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        pros.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        pros.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> streamLog = builder.stream(STREAM_LOG);
        streamLog.to(STREAM_LOG_COPY);

        KafkaStreams streams = new KafkaStreams(builder.build(), pros);
        streams.start();
    }
}
