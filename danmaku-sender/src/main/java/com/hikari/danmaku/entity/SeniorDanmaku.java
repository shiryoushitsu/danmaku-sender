package com.hikari.danmaku.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("高级弹幕实体类")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeniorDanmaku extends BaseDanmaku {

    private Double startX;

    private Double startY;

    private String alpha;

    private Double duration;

    private String text;

    private Integer zRotate;

    private Integer yRotate;

    private Double endX;

    private Double endY;

    private Integer moveDuration;

    private Integer delayDuration;

    private Integer access;

    private String fontFamily;

    private Integer bord;
}