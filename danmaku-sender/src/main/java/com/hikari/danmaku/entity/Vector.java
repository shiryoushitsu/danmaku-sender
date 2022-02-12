package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import java.util.List;

@ApiModel("svg命令")
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