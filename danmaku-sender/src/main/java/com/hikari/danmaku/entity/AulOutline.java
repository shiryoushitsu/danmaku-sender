package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("Aul描边效果")
@Data
public class AulOutline {

    @ApiModelProperty("描边/复制文本对象操作")
    private Integer outline;

    @ApiModelProperty("描边大小")
    private Integer outlineSize;

    @ApiModelProperty("描边模糊（透明度）")
    private String outlineAlpha;

    @ApiModelProperty("描边颜色")
    private String outlineColor;

    private String isFinal;
}