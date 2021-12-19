package com.hikari.danmaku.vo;

import com.hikari.danmaku.entity.XmlEffect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class XmlEffectVo extends XmlEffect {

    @ApiModelProperty("请求id")
    private String requestId;

}
