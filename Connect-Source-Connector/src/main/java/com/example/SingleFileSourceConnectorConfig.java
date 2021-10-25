package com.example;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

/**
 * Project : Producer-Consumer
 * Class: SingleFileSourceConnectorConfig
 * Created by baegseungjo on 2021/10/26
 * <p>
 * Description:
 */
public class SingleFileSourceConnectorConfig extends AbstractConfig {

    public static final String DIR_FILE_NAME = "file";
    public static final String DIR_FILE_NAME_DEFAULT_VALUE = "/tmp/kafka.txt";
    public static final String DIR_FILE_NAME_DOC = "읽을 파일  경로와 이름";

    public static final String TOPIC_NAME = "topic";
    public static final String TOPIC_DEFAULT_VALUE = "test";
    public static final String TOPIC_DOC = "보낼 토픽 이름";

    public static ConfigDef CONFIG = new ConfigDef()
            .define(
                    DIR_FILE_NAME,
                    ConfigDef.Type.STRING,
                    DIR_FILE_NAME_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH,
                    DIR_FILE_NAME_DOC)
            .define(
                    TOPIC_NAME,
                    ConfigDef.Type.STRING,
                    TOPIC_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH,
                    TOPIC_DOC);

    public SingleFileSourceConnectorConfig(Map<String, String> props) {
        super(CONFIG, props);
    }
}
