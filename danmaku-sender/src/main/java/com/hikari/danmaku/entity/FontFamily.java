package com.hikari.danmaku.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hikari.danmaku.entity.SeniorDanmaku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("字体字符画")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FontFamily {

    @ApiModelProperty("字体名称")
    private String font;

    @ApiModelProperty("字体大小")
    private Integer fontSize;

    @ApiModelProperty("字体高度")
    private Integer fontHeight;

    @ApiModelProperty("字体宽度")
    private Integer fontWidth;

}