package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class XmlEffect {

    @ApiModelProperty("是否描边")
    private boolean isOutline;

    @ApiModelProperty("是否实现层级效果（用z轴来增加时间，用时间覆盖来实现层级效果）")
    private boolean isLayer;

    @ApiModelProperty("是否线性加速")
    private boolean isLinearSpeedup;

    @ApiModelProperty("是否坐标百分比")
    private boolean isPercentage;

    @ApiModelProperty("是否提前开始时间（提前50ms，解决连续两段弹幕之间闪烁问题）")
    private boolean isAdvanceStartTime;

    @ApiModelProperty("是否延长结束时间（延长50ms，解决连续两段弹幕之间闪烁问题）")
    private boolean isDelayEndTime;


}
