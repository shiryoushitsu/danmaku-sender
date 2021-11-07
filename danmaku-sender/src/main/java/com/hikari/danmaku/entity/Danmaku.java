package com.hikari.danmaku.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
//例子<d p="9.60000,7,83,16737621,1596203398,0,f777395a,36207970800369671">["96","144","1-1","4.2","鼓動に合わせて",0,0,"96","144",500,0,1,"SimHei",1]</d>

@ApiModel(description = "弹幕实体类")
public class Danmaku {

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("弹幕类型")
    private String mode;

    private String fonesize;
    private String color;
    private String timestamp;
    private String type;
    private String uidHash;
    private String dmuuid;

    private String xStart;
    private String yStart;
    private String alpha;


    private String duration;
    private String danmaku;
    private String zRotate;
    private String yRotate;
    private String xEnd;
    private String yEnd;

    private String moveDuration;
    private String delay;
    private String access;
    private String fontFamily;
    private String bord;

    public String getMoveDuration() {
        return moveDuration;
    }

    public void setMoveDuration(String moveDuration) {
        this.moveDuration = moveDuration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFonesize() {
        return fonesize;
    }

    public void setFonesize(String fonesize) {
        this.fonesize = fonesize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUidHash() {
        return uidHash;
    }

    public void setUidHash(String uidHash) {
        this.uidHash = uidHash;
    }

    public String getDmuuid() {
        return dmuuid;
    }

    public void setDmuuid(String dmuuid) {
        this.dmuuid = dmuuid;
    }

    public String getxStart() {
        return xStart;
    }

    public void setxStart(String xStart) {
        this.xStart = xStart;
    }

    public String getyStart() {
        return yStart;
    }

    public void setyStart(String yStart) {
        this.yStart = yStart;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDanmaku() {
        return danmaku;
    }

    public void setDanmaku(String danmaku) {
        this.danmaku = danmaku;
    }

    public String getzRotate() {
        return zRotate;
    }

    public void setzRotate(String zRotate) {
        this.zRotate = zRotate;
    }

    public String getyRotate() {
        return yRotate;
    }

    public void setyRotate(String yRotate) {
        this.yRotate = yRotate;
    }

    public String getxEnd() {
        return xEnd;
    }

    public void setxEnd(String xEnd) {
        this.xEnd = xEnd;
    }

    public String getyEnd() {
        return yEnd;
    }

    public void setyEnd(String yEnd) {
        this.yEnd = yEnd;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getBord() {
        return bord;
    }

    public void setBord(String bord) {
        this.bord = bord;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
}