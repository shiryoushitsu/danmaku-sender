package com.hikari.danmaku.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

//坐标实例，用于坐标数组
@Data
@AllArgsConstructor
public class Position {

    private double x;

    private double y;

}