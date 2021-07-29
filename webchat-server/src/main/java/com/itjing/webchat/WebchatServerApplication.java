package com.itjing.webchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itjing.webchat.mapper")
public class WebchatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebchatServerApplication.class, args);
    }

}
