package com.example.processorapi;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

/**
 * Project : Producer-Consumer
 * Class: SimpleKafkaProcessor
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class SimpleKafkaProcessor {

    private static final String APPLICATION_NAME = "processor-application";
    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String STREAM_LOG = "stream_log";
    private static final String STREAM_LOG_FILTER = "stream_log_filter";

    public static void main(String[] args) {
        Properties pros = new Properties();
        pros.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        pros.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        pros.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        pros.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        Topology topology = new Topology();
        topology.addSource("Source", STREAM_LOG)
                .addProcessor("Process", () -> new FilterProcessor(), "Source")
                .addSink("Sink", STREAM_LOG_FILTER, "Process");

        KafkaStreams streams = new KafkaStreams(topology, pros);
        streams.start();
    }
}
