package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("Aul阴影效果")
@Data
public class AulShadow {

    @ApiModelProperty("阴影/复制文本对象操作")
    private Integer shadow;

    @ApiModelProperty("阴影X坐标")
    private Integer shadowX;

    @ApiModelProperty("阴影Y坐标")
    private Integer shadowY;

    @ApiModelProperty("阴影浓度/透明度")
    private String shadowAlpha;

    @ApiModelProperty("阴影颜色")
    private String shadowColor;

}