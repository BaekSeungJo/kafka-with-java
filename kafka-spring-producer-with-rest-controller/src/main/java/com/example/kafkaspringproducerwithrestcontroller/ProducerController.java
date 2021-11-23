package com.example.kafkaspringproducerwithrestcontroller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project : Producer-Consumer
 * Class: RestApiProducer
 * Created by baegseungjo on 2021/11/07
 * <p>
 * Description:
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ProducerController {
    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/api/select")
    public void selectColor(
            @RequestHeader("user-agent") String userAgentName,
            @RequestParam(value = "color") String colorName,
            @RequestParam(value = "user") String userName) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        Date now = new Date();
        Gson gson = new Gson();
        UserEventVO userEventVO = new UserEventVO(sdfDate.format(now), userAgentName, colorName, userName);
        String jsonColorLog = gson.toJson(userEventVO);
        logger.info("send : {}", jsonColorLog);

        kafkaTemplate.send("select-color", jsonColorLog).addCallback(
                new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        logger.error(ex.getMessage(), ex);
                    }

                    @Override
                    public void onSuccess(SendResult<String, String> result) {
                        logger.info(result.toString());
                    }
                }
        );

    }
}
