package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "贝塞尔")
@Data
@AllArgsConstructor
public class CubicBezier {

    //时间进度（x轴坐标）
    private double time;

    //进度（y轴坐标）
    private double progression;

}