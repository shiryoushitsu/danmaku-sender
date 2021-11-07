package com.hikari.danmaku.constants;

public enum Easing {
    E1("liner", "cubic-bezier(0, 0, 1, 1)"),

    E2("easeInSine", "cubic-bezier(0.47, 0, 0.745, 0.715)"),
    E3("easeOutSine", "cubic-bezier(0.39, 0.575, 0.565, 1)"),
    E4("easeInOutSine", "cubic-bezier(0.445, 0.05, 0.55, 0.95)"),

    E6("easeInQuad", "cubic-bezier(0.55, 0.085, 0.68, 0.53)"),
    E7("easeOutQuad", "cubic-bezier(0.25, 0.46, 0.45, 0.94)"),
    E8("easeInOutQuad", "cubic-bezier(0.455, 0.03, 0.515, 0.955)"),

    E10("easeInCubic", "cubic-bezier(0.55, 0.055, 0.675, 0.19)"),
    E11("easeOutCubic", "cubic-bezier(0.215, 0.61, 0.355, 1)"),
    E12("easeInOutCubic", "cubic-bezier(0.645, 0.045, 0.355, 1)"),

    E14("easeInQuart", "cubic-bezier(0.895, 0.03, 0.685, 0.22)"),
    E15("easeOutQuart", "cubic-bezier(0.165, 0.84, 0.44, 1)"),
    E16("easeInOutQuart", "cubic-bezier(0.77, 0, 0.175, 1)"),

    E18("easeInQuint", "cubic-bezier(0.755, 0.05, 0.855, 0.06)"),
    E19("easeOutQuint", "cubic-bezier(0.23, 1, 0.32, 1)"),
    E20("easeInOutQuint", "cubic-bezier(0.86, 0, 0.07, 1)"),

    E22("easeInExpo", "cubic-bezier(0.95, 0.05, 0.795, 0.035)"),
    E23("easeOutExpo", "cubic-bezier(0.19, 1, 0.22, 1)"),
    E24("easeInOutExpo", "cubic-bezier(1, 0, 0, 1)"),

    E26("easeInCirc", "cubic-bezier(0.6, 0.04, 0.98, 0.335)"),
    E27("easeOutCirc", "cubic-bezier(0.075, 0.82, 0.165, 1)"),
    E28("easeInOutCirc", "cubic-bezier(0.785, 0.135, 0.15, 0.86)"),

    E34("easeInBack", "cubic-bezier(0.6, -0.28, 0.735, 0.045)"),
    E35("easeOutBack", "cubic-bezier(0.175, 0.885, 0.32, 1.275)"),
    E36("easeInOutBack", "cubic-bezier(0.68, -0.55, 0.265, 1.55)"),
    ;

    String name;
    String code;

    Easing(String name, String code) {
        this.name = name;
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public String getCode() { return code; }

}
