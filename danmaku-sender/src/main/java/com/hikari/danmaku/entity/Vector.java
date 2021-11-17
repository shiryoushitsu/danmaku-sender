package com.hikari.danmaku.entity;

import java.util.List;

//命令
public class Vector {

    private String str;

    private List<Position> position;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
        this.position = position;
    }
}