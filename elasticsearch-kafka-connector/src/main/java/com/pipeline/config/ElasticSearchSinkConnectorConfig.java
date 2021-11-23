package com.pipeline.config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;

/**
 * Project : Producer-Consumer
 * Class: ElasticSearchSinkConnectorConfig
 * Created by baegseungjo on 2021/11/10
 * <p>
 * Description:
 */
public class ElasticSearchSinkConnectorConfig extends AbstractConfig {
    public static final String ES_CLUSTER_HOST = "es.host";
    private static final String ES_CLUSTER_HOST_DEFAULT_VALUE = "localhost";
    private static final String ES_CLUSTER_HOST_DOC = "엘라스틱서치 호스트를 입력";

    public static final String ES_CLUSTER_PORT = "es.port";
    private static final String ES_CLUSTER_PORT_DEFAULT_VALUE = "9200";
    private static final String ES_CLUSTER_PORT_DOC = "엘라스틱서치 포트를 입력ㄲ";

    public static final String ES_INDEX = "es.index";
    private static final String ES_INDEX_DEFAULT_VALUE = "kafka-connector-index";
    private static final String ES_INDEX_DOC = "엘라스틱서치 인덱스를 입력";

    // 커넥터에 설정값이 정상적으로 들어왔는지 검증하기 위해 사용
    // 신규 커넥터 생성시 key-value 형태의 json으로 body 생서앟여 post 요청
    public static ConfigDef CONFIG = new ConfigDef()
            .define(ES_CLUSTER_HOST, Type.STRING, ES_CLUSTER_HOST_DEFAULT_VALUE, Importance.HIGH, ES_CLUSTER_HOST_DOC)
            .define(ES_CLUSTER_PORT, Type.INT, ES_CLUSTER_PORT_DEFAULT_VALUE, Importance.HIGH, ES_CLUSTER_PORT_DOC)
            .define(ES_INDEX, Type.STRING, ES_INDEX_DEFAULT_VALUE, Importance.HIGH, ES_INDEX_DOC);



    public ElasticSearchSinkConnectorConfig(Map<String, String> props) {
        super(CONFIG, props);
    }
}
