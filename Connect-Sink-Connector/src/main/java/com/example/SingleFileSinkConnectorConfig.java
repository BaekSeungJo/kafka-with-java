package com.example;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

/**
 * Project : Producer-Consumer
 * Class: SingleFileSinkConnectorConfig
 * Created by baegseungjo on 2021/10/26
 * <p>
 * Description:
 */
public class SingleFileSinkConnectorConfig extends AbstractConfig {

    public static final String DIR_FILE_NAME = "file";
    public static final String DIR_FILE_NAME_DEFAULT_VALUE = "/tmp/kafka.txt";
    public static final String DIR_FILE_NAME_DOC = "저장할 디렉톨리와 파일 이름";

    public static ConfigDef CONFIG = new ConfigDef()
            .define(
                    DIR_FILE_NAME,
                    ConfigDef.Type.STRING,
                    DIR_FILE_NAME_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH,
                    DIR_FILE_NAME_DOC);


    public SingleFileSinkConnectorConfig(Map<String, String> props) {
        super(CONFIG, props);
    }
}
