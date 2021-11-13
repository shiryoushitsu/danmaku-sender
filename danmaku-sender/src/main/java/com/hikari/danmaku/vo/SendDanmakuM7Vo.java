package com.hikari.danmaku.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("发送弹幕请求类")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendDanmakuM7Vo extends SendDanmakuVo {


    @ApiModelProperty("sendPrefix")
    private String sendPrefix;


    @ApiModelProperty("sendSuffix")
    private String sendSuffix;


    @ApiModelProperty("inStartX")
    private Double inStartX;

    @ApiModelProperty("inStartY")
    private Double inStartY;

    @ApiModelProperty("inEndX")
    private Double inEndX;

    @ApiModelProperty("inEndY")
    private Double inEndY;

    @ApiModelProperty("inAlpha")
    private String inAlpha;

    @ApiModelProperty("淡入生存时间(秒)")
    private Double inDuration;

    @ApiModelProperty("淡入运动耗时(毫秒)")
    private Integer inMoveDuration;

    @ApiModelProperty("是否有淡入段")
    private Boolean inChecked;

    @ApiModelProperty("outStartX")
    private Double outStartX;

    @ApiModelProperty("outStartY")
    private Double outStartY;

    @ApiModelProperty("outEndX")
    private Double outEndX;

    @ApiModelProperty("outEndY")
    private Double outEndY;

    @ApiModelProperty("outAlpha")
    private String outAlpha;


    @ApiModelProperty("淡出生存时间(秒)")
    private Double outDuration;

    @ApiModelProperty("淡出运动耗时(毫秒)")
    private Integer outMoveDuration;

    @ApiModelProperty("是否有淡出段")
    private Boolean outChecked;

    @ApiModelProperty("lrc间隙时间")
    private Integer lrcIntervalTime;


    @ApiModelProperty("重叠时间")
    private Integer overlapTime;

    @ApiModelProperty("效果模板")
    private String effectTem;
}