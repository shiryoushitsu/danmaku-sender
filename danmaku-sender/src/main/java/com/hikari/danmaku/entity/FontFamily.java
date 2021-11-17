package com.hikari.danmaku.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hikari.danmaku.entity.SeniorDanmaku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("字体字符画")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FontFamily {

    @ApiModelProperty("文本内容")
    private String text;

    @ApiModelProperty("字体名称")
    private String font;

    @ApiModelProperty("字体宽度")
    private Integer fontWidth;

    @ApiModelProperty("字体高度")
    private Integer fontHeight;

}