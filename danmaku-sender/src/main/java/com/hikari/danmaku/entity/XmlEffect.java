package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("EXO转换XML全局基本效果")
@Data
public class XmlEffect {

    @ApiModelProperty("是否描边")
    private Boolean isOutline = false;

    @ApiModelProperty("是否实现层级效果（用z轴来增加时间，用时间覆盖来实现层级效果）")
    private Boolean isLayer = true;

    @ApiModelProperty("是否换行都转成新文本（距离默认字号）")
    private Boolean isForceLine = false;

    @ApiModelProperty("是否线性加速")
    private Boolean isLinearSpeedup = false;

    @ApiModelProperty("是否坐标百分比")
    private Boolean isPercentage = true;

    @ApiModelProperty("是否提前开始时间（提前50ms，解决连续两段弹幕之间闪烁问题）")
    private Boolean isAdvanceStartTime = false;

    @ApiModelProperty("是否延长结束时间（延长50ms，解决连续两段弹幕之间闪烁问题）")
    private Boolean isDelayEndTime = false;

    @ApiModelProperty("是否十进制颜色")
    private Boolean isColor10 = false;

    @ApiModelProperty("是否中间点强制分句")
    private Boolean isPointCut = false;

    @ApiModelProperty("")
    private Integer ZrotationTimes = 10;

}
