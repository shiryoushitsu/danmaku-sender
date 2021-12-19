package com.hikari.danmaku.constants;

public enum Font {
    FANGZHEN("方正像素16", 16,16,16),
    YAHEI("微软雅黑", 16,15,17),
    SIMHEI("黑体", 16,15,17),
    JFDOTJISKAN24("JF Dot jiskan24", 24,24,25),
    JFDOTK12("JF Dot K12", 12,12,13),
    JFDOTMPLUS10("JF Dot M+ 10", 10,11,10),
    JFDOTNAGA10("JF Dot NagaMin 10", 10,11,11),
    ZPIX("Zpix", 12,11,13);

    String name;
    Integer size;
    Integer height;
    Integer width;


    Font(String name,Integer size, Integer height, Integer width) {
        this.name = name;
        this.size = size;
        this.height = height;
        this.width = width;
    }


    public String getName() {
        return name;
    }
    public Integer getSize() {
        return size;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }
}
