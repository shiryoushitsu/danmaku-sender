package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("Aul动画效果")
@Data
public class AulAnmEffect {

    @ApiModelProperty("track0")
    private String  track0;

    @ApiModelProperty("track1")
    private String track1;

    @ApiModelProperty("track2")
    private String track2;

    @ApiModelProperty("track3")
    private String track3;

    @ApiModelProperty("check0")
    private Integer check0;

    @ApiModelProperty("filter")
    private Integer filter;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("param")
    private String param;

}