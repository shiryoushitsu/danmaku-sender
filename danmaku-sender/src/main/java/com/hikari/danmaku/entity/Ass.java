package com.hikari.danmaku.entity;

import lombok.Data;

@Data
public class Ass {

    private Integer startTime;//开始时间

    private Integer endTime;//结束时间

    private String startTimeString;//开始时间

    private String endTimeString;//结束时间

    private Integer syl;//间隔

    private String content;//歌词内容
}
