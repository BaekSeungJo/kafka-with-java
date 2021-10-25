package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

/**
 * Project : Producer-Consumer
 * Class: KStreamJoinGlobalKTable
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class KStreamJoinGlobalKTable {

    private static final String APPLICATION_NAME = "global-table-join-application";
    private static final String BOOTSTRAP_SERVER = "my-kafka:9092";
    private static final String ADDRESS_GLOBAL_TABLE = "address_v2";
    private static final String ORDER_STREAM = "order";
    private static final String ORDER_JOIN_STREAM = "order_join";

    public static void main(String[] args) {
        Properties pros = new Properties();
        pros.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        pros.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        pros.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        pros.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        GlobalKTable<String, String> addressGlobalTable = builder.globalTable(ADDRESS_GLOBAL_TABLE);
        KStream<String, String> orderStream = builder.stream(ORDER_STREAM);

        orderStream.join(addressGlobalTable,
                (orderKey, orderValue) -> orderKey,
                (order, address) -> order + " send to " + address)
                .to(ORDER_JOIN_STREAM);

        KafkaStreams streams = new KafkaStreams(builder.build(), pros);
        streams.start();
    }
}
