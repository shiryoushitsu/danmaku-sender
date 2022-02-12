package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("Aul实体类")
@Data
public class AviutlExo {

    @ApiModelProperty("分段名")
    private String  partName;

    @ApiModelProperty("屏幕长度")
    private Integer width;

    @ApiModelProperty("屏幕高度")
    private Integer height;

    @ApiModelProperty("起始帧数")
    private Integer start;

    @ApiModelProperty("结束帧数")
    private Integer end;

    @ApiModelProperty("开始时间（秒）")
    private Double startTime;

    @ApiModelProperty("结束时间（秒）")
    private Double endTime;

    @ApiModelProperty("生存时间/持续时间（秒）")
    private Double lifeTime;

    @ApiModelProperty("移动时间（毫秒）")
    private Integer moveTime;

    @ApiModelProperty("延迟时间（毫秒）")
    private Integer delayTime = 0;

    @ApiModelProperty("效果")
    private String _name;

    @ApiModelProperty("文本")
    private String text;

    @ApiModelProperty("X坐标(Aviutl中坐标)")
    private Integer X;

    @ApiModelProperty("Y坐标(Aviutl中坐标)")
    private Integer Y;

    @ApiModelProperty("Z坐标/改用为层级/会换算为毫秒数")
    private Integer Z;

    @ApiModelProperty("起始X坐标")
    private Integer startX;

    @ApiModelProperty("起始Y坐标")
    private Integer startY;

    @ApiModelProperty("结束X坐标")
    private Integer endX;

    @ApiModelProperty("结束Y坐标")
    private Integer endY;

    @ApiModelProperty("中心X坐标")
    private Integer centerX;

    @ApiModelProperty("中心Y坐标")
    private Integer centerY;

    @ApiModelProperty("Y轴旋转")
    private Integer YRotation = 0;

    @ApiModelProperty("Z轴旋转")
    private Integer ZRotation;

    @ApiModelProperty("Z轴旋转结束")
    private Integer endZRotation;

    @ApiModelProperty("Z轴旋转缓动")
    private Integer easingZRotation;

    @ApiModelProperty("字体")
    private String font;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("大小（中字）/文字大小")
    private Integer size;

    @ApiModelProperty("透明度（中字）/透明度")
    private String alpha;

    @ApiModelProperty("链/中间点/链为1时文字取前一个文字")
    private Integer chain = 0;

    @ApiModelProperty("主物件名称")
    private String objName;

    @ApiModelProperty("层级")
    private Integer layer;

    @ApiModelProperty("描边")
    private Integer outline = 0;

    @ApiModelProperty("线性加速")
    private Integer linearSpeedup;

    @ApiModelProperty("对齐方式")
    private Integer align;

    @ApiModelProperty("文本字数")
    private Integer textLength;

    @ApiModelProperty("处理状态（是否删除 1正常 0删除）")
    private Integer state = 1;

    @ApiModelProperty("Aul对象是否显示有效（0正常 1无效）")
    private Integer disable = 0;

//    额外扩展效果
    @ApiModelProperty("阴影/复制文本对象操作")
    private Integer shadow = 0;

    @ApiModelProperty("描边效果")
    private AulOutline aulOutline;

    @ApiModelProperty("阴影效果")
    private AulShadow aulShadow;

    @ApiModelProperty("动画效果")
    private List<AulAnmEffect> aulAnmEffectList;

    @ApiModelProperty("当前名")
    private String currentName;

    @ApiModelProperty("全局基本效果")
    private XmlEffect xmlEffect;

}
