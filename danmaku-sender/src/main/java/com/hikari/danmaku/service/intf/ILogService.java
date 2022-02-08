package com.hikari.danmaku.service.intf;

import com.alibaba.fastjson.JSONObject;
import com.hikari.danmaku.entity.BaseDanmaku;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import com.hikari.danmaku.vo.SendDanmakuVo;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.io.file.FileWriter;

import java.io.IOException;


public interface ILogService {

    FileWriter writePlus(FileWriter writer, String msg, Object... arguments) throws IOException;

    String configLog(SendDanmakuVo sendDanmaku);

    String xmlConfigLog(SendDanmakuVo sendDanmaku);

    String previewLog(Integer currentRow,Integer startTime,Integer acturalTime, String content);

    String xmlPreviewLog(Integer currentRow, Integer startTime, Integer acturalTime, String content , BaseDanmaku danmakuPreview);
}
