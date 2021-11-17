package com.hikari.danmaku.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import cn.hutool.json.XML.*;
import com.hikari.danmaku.entity.BaseDanmaku;
import com.hikari.danmaku.entity.Danmaku;
import com.hikari.danmaku.entity.Position;
import com.hikari.danmaku.entity.SeniorDanmaku;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cn.hutool.json.XML.toJSONObject;


public class TestUtil {
    public static void main(String[] args) {
//       getVideoDanmakuXml("411813545");
        SeniorDanmaku p = new SeniorDanmaku();

        if(p.getAccess() != null && 15 == p.getAccess()){
            System.out.println(p.getAccess());
        }

    }




    /**
     *
     * @param cid
     * @return
     */
    public static String getVideoDanmakuXml(String cid){
        String url ="https://comment.bilibili.com/";
        url = url + cid + ".xml";
        String result1= HttpUtil.get(url);
        JSONObject jsonObject = new JSONObject();
        JSONObject jxml =  toJSONObject(result1);
        JSONArray dXml =  jxml.getJSONObject("i").getJSONArray("d");

        List<SeniorDanmaku> seniorDanmakuList = new ArrayList<>();
        for(int i = 0;i < dXml.size(); i++ ){
            JSONObject js =(JSONObject)dXml.get(i);
            SeniorDanmaku baseDanmaku =XmlUtil.parseXmlAttribute(js.get("p").toString());
            baseDanmaku.setContent(js.get("content").toString());

            //只显示m7高级弹幕
            if( 7 == baseDanmaku.getMode()){
                seniorDanmakuList.add(baseDanmaku);
            }
        }
        // 按弹幕在视频出现时间顺序
//        Collections.sort(seniorDanmakuList, new Comparator<SeniorDanmaku>() {
//            public int compare(SeniorDanmaku arg0, SeniorDanmaku arg1) {
//                double hits0 = Double.valueOf(arg0.getStartTime()) ;
//                double hits1 = Double.valueOf(arg1.getStartTime());
//                if (hits1 < hits0) {
//                    return 1;
//                } else if (hits1 == hits0) {
//                    return 0;
//                } else {
//                    return -1;
//                }
//            }
//        });

        for(SeniorDanmaku seniorDanmaku :seniorDanmakuList){
            System.out.println(XmlUtil.buildDanmakuXml(seniorDanmaku));
        }

        return "";
    }
}
