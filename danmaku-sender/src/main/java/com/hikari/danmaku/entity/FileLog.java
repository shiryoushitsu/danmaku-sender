package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("弹幕日志类")
@Data
public class FileLog {

    @ApiModelProperty("弹幕日志文件路径")
    private String fileLogPath;

    @ApiModelProperty("发送状态")
    private String state;

    @ApiModelProperty("视频")
    private String title;

    @ApiModelProperty("分p")
    private Integer part;

    @ApiModelProperty("模式")
    private Integer mode;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("发送条数")
    private Integer count;

    @ApiModelProperty("发送开始时间")
    private Data postStartTime;

    @ApiModelProperty("发送结束时间")
    private Data postEndTime;

    @ApiModelProperty("发送日志")
    private String log;
}