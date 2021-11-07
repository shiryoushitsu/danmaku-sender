package com.hikari.danmaku.utils;

import cn.hutool.core.util.StrUtil;
import com.hikari.danmaku.constants.Mode;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class FileLogUtil {
    public static void main(String[] args) {
//        previewLog(1,1500,1600,"好啊");

        test(1,"previewTem",  "123", "china", "csdn", "com");
        String previewTem = "第{}{}句 │ {} │ {} │ {}";
//        StrUtil.format("hello", "world", "123", "china", "csdn", "com");


    }

    public static void test(Integer b, String a, String... arguments) {
        String previewTem = "第{}{}句 │ {} │ {} │ {}";
        StrUtil.format(a, arguments);
        for (int i = 0; i < arguments.length; i++) {
//            System.out.println(arguments[i]);
        }
        System.out.println(StrUtil.format(previewTem,arguments));
    }


    public static String configLog(SendDanmakuM1Vo sendDanmaku){
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("───────────────────────开始──────────────────────\n");
//        stringBuffer.append("发送状态：").append(sendDanmaku.getSendState()).append(" ");
        stringBuffer.append("模式：").append(Mode.getName(sendDanmaku.getMode())).append(" ");
        stringBuffer.append("颜色：").append(sendDanmaku.getColor()).append("\n");
        stringBuffer.append("视频：").append(sendDanmaku.getVideoTitle()).append(" ");
        stringBuffer.append("分P名：").append(sendDanmaku.getPartTitle()).append(" ");
//        stringBuffer.append("发送条数：").append(fileLog.getCount()).append("\n");
        stringBuffer.append("\n───────原时间───现时间───歌词内容─────────────────────\n");

        return  stringBuffer.toString();
    }


    public static String previewLog(Integer currentRow,Integer startTime,Integer acturalTime, String content){
        String previewTem = "第{}{}句 │ {} │ {} │ {}";

        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm:ss.SSS");
        hourFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        minuteFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String startTimeStr =  "";
        String acturalTimeStr =  "";

        int hour = 1000 * 60 * 60;
        if(startTime >= hour){
            startTimeStr = hourFormat.format(startTime);
        }else {
            startTimeStr = minuteFormat.format(startTime);
        }
        if(acturalTime >= hour){
            acturalTimeStr = hourFormat.format(acturalTime);
        }else {
            acturalTimeStr = minuteFormat.format(acturalTime);
        }
        String space = "";
        if(currentRow < 10){
            space=" ";
        }else {
            space="";
        }
        StringBuffer stringBuffer =new StringBuffer();
//        stringBuffer.append("第1句  │00:25.080 │ 00:25.080│ 放眼望去 朝着这白金世界");
        stringBuffer.append("第").append(space).append(currentRow).append("句").append(" │ ");
        stringBuffer.append(startTimeStr).append(" │ ");
        stringBuffer.append(acturalTimeStr).append(" │ ");
        stringBuffer.append(content);
        stringBuffer.append("\n");
        System.out.println(StrUtil.format(previewTem, space,currentRow, currentRow, acturalTimeStr, content));
        return  stringBuffer.toString();
    }
}
