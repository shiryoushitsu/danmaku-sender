package com.hikari.danmaku.utils;

import com.hikari.danmaku.entity.SeniorDanmaku;
import com.hikari.danmaku.vo.SendDanmakuM7Vo;

/**
 * create by AwaiHikari on 2020-07-01 20:50
 */

public  class DanmakuUtil {

    public static void main(String[] argv) throws Exception{

    }



    //组装高级弹幕
    public static String  wrapperDanmaku(SeniorDanmaku seniorDanmaku){
        StringBuffer danmakuStr = new StringBuffer();
        danmakuStr.append("[");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getStartX())).append("\"").append(",");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getStartY())).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getAlpha()).append("\"").append(",");
        danmakuStr.append("\"").append(CommonUtil.df3(seniorDanmaku.getDuration())).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getText()).append("\"").append(",");
        danmakuStr.append(seniorDanmaku.getZRotate()).append(",");//Z_Rotation
        danmakuStr.append(seniorDanmaku.getYRotate()).append(",");//Y_Rotation
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getEndX())).append("\"").append(",");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getEndY())).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getMoveDuration()).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getDelayDuration()).append("\"").append(",");//Delay_Time
        danmakuStr.append(seniorDanmaku.getBord()).append(",");//Outline
        danmakuStr.append("\"").append(seniorDanmaku.getFontFamily()).append("\"").append(",");
        danmakuStr.append(seniorDanmaku.getAccess());//Linear_Speedup
        danmakuStr.append("]");
        return danmakuStr.toString();
    }

    //组装高级弹幕(淡入)
    public static String  wrapperInDanmaku(SendDanmakuM7Vo seniorDanmaku){
        StringBuffer danmakuStr = new StringBuffer();
        danmakuStr.append("[");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getInStartX())).append("\"").append(",");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getInStartY())).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getInAlpha()).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getInDuration()).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getText()).append("\"").append(",");
        danmakuStr.append(seniorDanmaku.getZRotate()).append(",");//Z_Rotation
        danmakuStr.append(seniorDanmaku.getYRotate()).append(",");//Y_Rotation
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getInEndX())).append("\"").append(",");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getInEndY())).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getInMoveDuration()).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getDelayDuration()).append("\"").append(",");//Delay_Time
        danmakuStr.append(seniorDanmaku.getBord()).append(",");//Outline
        danmakuStr.append("\"").append(seniorDanmaku.getFontFamily()).append("\"").append(",");
        danmakuStr.append(seniorDanmaku.getAccess());//Linear_Speedup
        danmakuStr.append("]");
        return danmakuStr.toString();
    }

    //组装高级弹幕(淡出)
    public static String  wrapperOutDanmaku(SendDanmakuM7Vo seniorDanmaku){
        StringBuffer danmakuStr = new StringBuffer();
        danmakuStr.append("[");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getOutStartX())).append("\"").append(",");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getOutStartY())).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getOutAlpha()).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getOutDuration()).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getText()).append("\"").append(",");
        danmakuStr.append(seniorDanmaku.getZRotate()).append(",");//Z_Rotation
        danmakuStr.append(seniorDanmaku.getYRotate()).append(",");//Y_Rotation
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getOutEndX())).append("\"").append(",");
        danmakuStr.append("\"").append(CommonUtil.doubleTrans(seniorDanmaku.getOutEndY())).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getOutMoveDuration()).append("\"").append(",");
        danmakuStr.append("\"").append(seniorDanmaku.getDelayDuration()).append("\"").append(",");//Delay_Time
        danmakuStr.append(seniorDanmaku.getBord()).append(",");//Outline
        danmakuStr.append("\"").append(seniorDanmaku.getFontFamily()).append("\"").append(",");
        danmakuStr.append(seniorDanmaku.getAccess());//Linear_Speedup
        danmakuStr.append("]");
        return danmakuStr.toString();
    }
}