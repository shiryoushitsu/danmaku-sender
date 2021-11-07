package com.hikari.danmaku.constants;

public class MsgCode {
    private final static String startDivider   = "───────────────────────开始──────────────────────"+"\n";

    private final static String endDivider     = "───────────────────────结束──────────────────────"+"\n";

    private final static String previewDivider = "───────原时间───现时间───歌词内容────────────────────"+"\n";

    private final static String stopDivider    = "───────────────────────停止───────────────────────"+"\n";

    private final static String finishDivider  = "──────────────────全部弹幕发送完毕──────────────────"+"\n";

    private final static String overDivider  = "──────────────发送弹幕已超{}条，发送终止────────────"+"\n";


    // 配置文本模板
    private final static String configTem      = "模式：{} 颜色：{}\n" +
                                                 "视频：{} 分P名：{}\n";
    // 预览模板（次数、原时间、现时间、内容）
    private final static String previewTem     = "第{}{}句 │ {} │ {} │ {}"+"\n";

    // 发送成功模板（时间、次数、内容）
    private final static String sendSuccessTem = "{} 第{}次发送成功：{}"+"\n";

    private final static String retryTem       = "{} 弹幕发送频率过快，将{}min后重试"+"\n";

    private final static String delayRetryTem  = "{} 第{}次延时发送成功: {}"+"\n";
}
