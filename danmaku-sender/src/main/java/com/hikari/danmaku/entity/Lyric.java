package com.hikari.danmaku.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Lyric  implements Serializable {

    private Integer time; //转化为秒的时间

    private String timeStr; //文本时间

    private String text;//时间文本与歌词文本

    public Lyric(Integer time, String timeStr, String text) {
        this.time = time;
        this.timeStr = timeStr;
        this.text = text;
    }
}
