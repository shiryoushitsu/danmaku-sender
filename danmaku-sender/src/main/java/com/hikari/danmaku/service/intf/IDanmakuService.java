package com.hikari.danmaku.service.intf;

import com.hikari.danmaku.entity.Ass;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import com.hikari.danmaku.vo.SendDanmakuM7Vo;

import java.util.List;
import java.util.Map;


public interface IDanmakuService {

    void initDanmakuM1(SendDanmakuM1Vo sendDanmakuM1Vo, List<Ass> danmakuList) throws Exception;

    void initDanmakuM7(SendDanmakuM7Vo sendDanmakuM7Vo, List<Ass> danmakuList) throws Exception;


    String updateColor(String color16);

    Map<String, Object> getBiliVideoInfo(String bvid) throws Exception;

    boolean checkStop(String fileLogPath);
}
