package com.hikari.danmaku.service.impl;


import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.hikari.danmaku.constants.Mode;
import com.hikari.danmaku.entity.BaseDanmaku;
import com.hikari.danmaku.service.intf.IFileService;
import com.hikari.danmaku.service.intf.ILogService;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import com.hikari.danmaku.vo.SendDanmakuVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.io.file.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@Service
public class LogService implements ILogService {

    public final static String startDivider   = "───────────────────────开始──────────────────────"+"\n";

    public final static String endDivider     = "───────────────────────结束──────────────────────"+"\n";

    public final static String sendDivider    = "───────────────────────发送──────────────────────"+"\n";

    public final static String previewDivider = "───────原时间───现时间───歌词内容────────────────────"+"\n";

    public final static String stopDivider    = "───────────────────────停止───────────────────────"+"\n";

    public final static String finishDivider  = "──────────────────全部弹幕发送完毕──────────────────"+"\n";

    public final static String overDivider  = "──────────────发送弹幕已超{}条，发送终止────────────"+"\n";


    // 配置文本模板
    public final static String configTem      = "模式：{} 颜色：{}\n" +
                                                 "视频：{} 分P名：{}\n";
    // 预览模板（次数、原时间、现时间、内容）
    public final static String previewTem     = "第{}{}句 │ {} │ {} │ {}"+"\n";

    // 发送成功模板（时间、次数、内容）
    public final static String sendSuccessTem = "{} 第{}次发送成功：{}\n";

    public final static String retryTem       = "{} 弹幕发送频率过快，将{}min后重试"+"\n";

    public final static String delayRetryTem  = "{} 第{}次延时发送成功: {}"+"\n";



    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private SimpleDateFormat nyFomart = new SimpleDateFormat("yyyyMM");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(LogService.class);

    @Override
    public FileWriter writePlus(FileWriter writer, String msg, Object... arguments) throws IOException{
        //将参数输入模板生成文字
        String msgTxt = StrUtil.format(msg,arguments);
        //控制台输出日志
        System.out.println(msgTxt);
        //前端输出日志
        writer.append(msgTxt);
        return writer;
    }

    @Override
    public  String configLog(SendDanmakuVo sendDanmaku){
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

    @Override
    public  String xmlConfigLog(SendDanmakuVo sendDanmaku){
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("───────────────────────开始──────────────────────\n");
//        stringBuffer.append("发送状态：").append(sendDanmaku.getSendState()).append(" ");
        stringBuffer.append("视频：").append(sendDanmaku.getVideoTitle()).append(" ");
        stringBuffer.append("分P名：").append(sendDanmaku.getPartTitle()).append(" ");
//        stringBuffer.append("发送条数：").append(fileLog.getCount()).append("\n");
        stringBuffer.append("\n───────原时间───现时间───xml弹幕内容─────────────────────\n");

        return  stringBuffer.toString();
    }

    @Override
    public String previewLog(Integer currentRow, Integer startTime, Integer acturalTime, String content ){
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
            space="  ";
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
        return  stringBuffer.toString();
    }


    @Override
    public String xmlPreviewLog(Integer currentRow,Integer startTime,Integer acturalTime, String content, BaseDanmaku baseDanmaku){
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
            space="  ";
        }else {
            space="";
        }
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("第").append(space).append(currentRow).append("句").append(" │ ");
        stringBuffer.append(startTimeStr).append(" │ ");
        stringBuffer.append(acturalTimeStr).append(" │ ");
        stringBuffer.append(content).append(" │ ");
        stringBuffer.append(baseDanmaku.getMode()).append(",");
        stringBuffer.append(baseDanmaku.getFontSize()).append(",");
        stringBuffer.append(baseDanmaku.getColor());
        stringBuffer.append("\n");
        return  stringBuffer.toString();
    }



}
