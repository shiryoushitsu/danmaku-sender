package com.hikari.danmaku.constants;

public enum Mode {
    NORMAL("普通弹幕", 1),
    BOTTOM("底部弹幕", 4),
    TOP("顶部弹幕", 5),
    M7("顶部弹幕", 7),
    M9("BAS弹幕", 9);

    private String name;
    private Integer code;

    Mode(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public static Integer getCode(String name) {
        Mode[] modeEnums = values();
        for (Mode mode : modeEnums) {
            if (mode.getName().equals(name)) {
                return mode.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        Mode[] modeEnums = values();
        for (Mode mode : modeEnums) {
            if (mode.getCode().equals(code)) {
                return mode.getName();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() { return code; }

}
