package com.hikari.danmaku.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "发送弹幕请求类")
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

    @ApiModelProperty("移动时间（秒）")
    private Integer moveTime;

    @ApiModelProperty("延迟时间（毫秒）")
    private Integer delayTime = 0;

    @ApiModelProperty("效果")
    private String _name;

    @ApiModelProperty("文本")
    private String text;

    @ApiModelProperty("X坐标")
    private Integer X;

    @ApiModelProperty("Y坐标")
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

    @ApiModelProperty("Z轴旋转")
    private Integer ZRotation;

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

//    额外扩展效果
    @ApiModelProperty("阴影/复制文本对象操作")
    private Integer shadow = 0;

    @ApiModelProperty("阴影效果")
    private AulShadow aulShadow;

    @ApiModelProperty("动画效果")
    private List<AulAnmEffect> aulAnmEffectList;

    public AulShadow getAulShadow() {
        return aulShadow;
    }

    public void setAulShadow(AulShadow aulShadow) {
        this.aulShadow = aulShadow;
    }

    public List<AulAnmEffect> getAulAnmEffectList() {
        return aulAnmEffectList;
    }

    public void setAulAnmEffectList(List<AulAnmEffect> aulAnmEffectList) {
        this.aulAnmEffectList = aulAnmEffectList;
    }

    public Integer getZ() {
        return Z;
    }

    public void setZ(Integer z) {
        Z = z;
    }

    public Integer getShadow() {
        return shadow;
    }

    public void setShadow(Integer shadow) {
        this.shadow = shadow;
    }

    public Integer getLinearSpeedup() {
        return linearSpeedup;
    }

    public void setLinearSpeedup(Integer linearSpeedup) {
        this.linearSpeedup = linearSpeedup;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public Integer getZRotation() {
        return ZRotation;
    }

    public void setZRotation(Integer ZRotation) {
        this.ZRotation = ZRotation;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Integer getOutline() {
        return outline;
    }

    public void setOutline(Integer outline) {
        this.outline = outline;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public String toString() {
        return "AviutlExo{" +
                "partName='" + partName + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", start=" + start +
                ", end=" + end +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lifeTime=" + lifeTime +
                ", moveTime=" + moveTime +
                ", _name='" + _name + '\'' +
                ", text='" + text + '\'' +
                ", X=" + X +
                ", Y=" + Y +
                ", startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", ZRotation=" + ZRotation +
                ", font='" + font + '\'' +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", alpha='" + alpha + '\'' +
                ", chain=" + chain +
                ", objName='" + objName + '\'' +
                ", layer=" + layer +
                ", outline=" + outline +
                '}';
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public Double getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(Double lifeTime) {
        this.lifeTime = lifeTime;
    }

    public Integer getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(Integer moveTime) {
        this.moveTime = moveTime;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getX() {
        return X;
    }

    public void setX(Integer x) {
        X = x;
    }

    public Integer getY() {
        return Y;
    }

    public void setY(Integer y) {
        Y = y;
    }

    public Integer getStartX() {
        return startX;
    }

    public void setStartX(Integer startX) {
        this.startX = startX;
    }

    public Integer getStartY() {
        return startY;
    }

    public void setStartY(Integer startY) {
        this.startY = startY;
    }

    public Integer getEndX() {
        return endX;
    }

    public void setEndX(Integer endX) {
        this.endX = endX;
    }

    public Integer getEndY() {
        return endY;
    }

    public void setEndY(Integer endY) {
        this.endY = endY;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public Integer getChain() {
        return chain;
    }

    public void setChain(Integer chain) {
        this.chain = chain;
    }
}
