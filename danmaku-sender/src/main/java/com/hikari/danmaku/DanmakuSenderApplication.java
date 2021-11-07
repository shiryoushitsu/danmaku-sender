package com.hikari.danmaku;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.hikari.danmaku.mapper")
@SpringBootApplication
public class DanmakuSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanmakuSenderApplication.class, args);
        System.out.println("----------弹幕发射场启动成功----------");
    }

}
