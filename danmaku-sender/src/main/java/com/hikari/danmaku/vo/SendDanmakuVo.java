package com.hikari.danmaku.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hikari.danmaku.entity.SeniorDanmaku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("发送弹幕请求类")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendDanmakuVo extends SeniorDanmaku {

    @ApiModelProperty("cookie")
    private String cookie;

    @ApiModelProperty("csrf")
    private String csrf;

    @ApiModelProperty("bvid")
    private String bvid;

    @ApiModelProperty("part")
    private Integer part;


    @ApiModelProperty("sendFirstDmTime")
    private String sendFirstDmTime;

    @ApiModelProperty("sendFirstDmOffset")
    private Integer sendFirstDmOffset;


    @ApiModelProperty("sendInterval")
    private Integer sendInterval;

    @ApiModelProperty("发送模式 预览 测试 正式")
    private Integer sendMode;

    @ApiModelProperty("sendStartDmRow")
    private Integer sendStartDmRow;

    @ApiModelProperty("视频具体id")
    private String oid;

    @ApiModelProperty("视频名称")
    private String videoTitle;

    @ApiModelProperty("分P名")
    private String partTitle;

    @ApiModelProperty("请求id")
    private String requestId;

    @ApiModelProperty("发送状态（预览、发送中、停止、完成）")
    private String sendState;

    @ApiModelProperty("附加随机时间")
    private Integer sendRandomTime;

    @ApiModelProperty("附加随机时间")
    private Integer sendRetryInterval;

}