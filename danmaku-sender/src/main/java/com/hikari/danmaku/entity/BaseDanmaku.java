package com.hikari.danmaku.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//例子<d p="9.60000,7,83,16737621,1596203398,0,f777395a,36207970800369671">["96","144","1-1","4.2","鼓動に合わせて",0,0,"96","144",500,0,1,"SimHei",1]</d>

@ApiModel("弹幕实体类")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDanmaku {

    @ApiModelProperty("弹幕开始时间")
    private String startTime;

    @ApiModelProperty("弹幕类型")
    private Integer mode;

    @ApiModelProperty("字体大小")
    private Integer fontSize;

    @ApiModelProperty("颜色")
    private String color ;

    @ApiModelProperty("发送弹幕时间戳")
    private Integer timestamp;

    @ApiModelProperty("弹幕池")
    private Integer pool ;

    @ApiModelProperty("发送者uid的crc32")
    private String uid;

    @ApiModelProperty("弹幕唯一Id")
    private String rowId;

    @ApiModelProperty("弹幕权重")
    private Integer weight;

    @ApiModelProperty("弹幕内容")
    private String content;

}