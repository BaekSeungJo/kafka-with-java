package com.pipeline;

import com.pipeline.config.ElasticSearchSinkConnectorConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : Producer-Consumer
 * Class: ElasticSearchSinkConnector
 * Created by baegseungjo on 2021/11/10
 * <p>
 * Description:
 */
public class ElasticSearchSinkConnector extends SinkConnector {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchSinkConnector.class);

    private Map<String, String> configProperties;

    @Override
    public String version() {
        return "1.0";
    }

    /**
     * 커넥터 최조 실행시 실행되는 메서드로, 설정 정보가 올바른지 검증한다.
     *
     * @param props
     */
    @Override
    public void start(Map<String, String> props) {
        this.configProperties = props;
        try {
            new ElasticSearchSinkConnectorConfig(props);
        } catch (ConfigException e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }

    /**
     * 커넥터 실행시 태스크 역활하는 클래스 선언
     * 여러 태스크 운영시 태스크 분기 로직을 넣을 수 있다. ( ex : 타깃 애플리케이션의 버전에 맞는 로직을 담은 태스크를 여러개 구성시 사용 )
     *
     * @return
     */
    @Override
    public Class<? extends Task> taskClass() {
        return ElasticSearchSinkTask.class;
    }

    /**
     * 태스크별로 다른 설정값을 부여할 경우에 로직 처리 가능.
     * 현재는 모든 태스크가 같은 설정값을 사용한다.
     *
     * @param maxTasks
     * @return
     */
    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>();
        Map<String, String> taskProps = new HashMap<>();
        taskProps.putAll(configProperties);
        for(int i = 0; i < maxTasks; i++) {
            taskConfigs.add(taskProps);
        }

        return taskConfigs;
    }

    /**
     * 커넥터 생성할 때 설정값을 올바르게 입력했는데 검증할 CONFIG 반환
     *
     * @return
     */
    @Override
    public ConfigDef config() {
        return ElasticSearchSinkConnectorConfig.CONFIG;
    }

    /**
     * 커넥터 종료시 호출
     * 현재는 로그를 남긴다.
     */
    @Override
    public void stop() {
        logger.info("Stop elasticsearch connector");
    }
}
