package com.hikari.danmaku.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("发送弹幕请求类")
@Data
public class SendDanmakuM1Vo extends SendDanmakuVo {


    @ApiModelProperty("sendPrefix")
    private String sendPrefix;


    @ApiModelProperty("sendSuffix")
    private String sendSuffix;


}