package com.example.kafkaspringproducerwithrestcontroller;

/**
 * Project : Producer-Consumer
 * Class: UserEventVO
 * Created by baegseungjo on 2021/11/07
 * <p>
 * Description:
 */
public class UserEventVO {
    private String timestamp;
    private String userAgent;
    private String colorName;
    private String userName;

    public UserEventVO(String timestamp, String userAgent, String colorName, String userName) {
        this.timestamp = timestamp;
        this.userAgent = userAgent;
        this.colorName = colorName;
        this.userName = userName;
    }
}
