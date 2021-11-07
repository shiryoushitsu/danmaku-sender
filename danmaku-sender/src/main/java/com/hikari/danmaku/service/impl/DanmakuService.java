package com.hikari.danmaku.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hikari.danmaku.entity.Ass;
import com.hikari.danmaku.service.intf.IDanmakuService;
import com.hikari.danmaku.utils.ColorUtil;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import com.hikari.danmaku.vo.SendDanmakuM7Vo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DanmakuService implements IDanmakuService {

    private Logger logger = LoggerFactory.getLogger(DanmakuService.class);

    @Override
    public void initDanmakuM1(SendDanmakuM1Vo sendDanmakuM1Vo, List<Ass> danmakuList) throws Exception{
        //处理前缀附加内容
        if(StringUtils.isNotBlank(sendDanmakuM1Vo.getSendPrefix())){
            String prefixContent =  sendDanmakuM1Vo.getSendPrefix();
            for(Ass vo:danmakuList){
                vo.setContent(prefixContent + vo.getContent());
            }
        }
        //处理后缀附加内容
        if(StringUtils.isNotBlank(sendDanmakuM1Vo.getSendSuffix())){
            String suffixContent =  sendDanmakuM1Vo.getSendSuffix();
            for(Ass vo:danmakuList){
                vo.setContent(vo.getContent() + suffixContent );
            }
        }


        // 根据bvid + pardId获取视频信息
        if(StringUtils.isNotBlank(sendDanmakuM1Vo.getBvid())){
            sendDanmakuM1Vo.setOid("无");
            sendDanmakuM1Vo.setVideoTitle("无");
            sendDanmakuM1Vo.setPartTitle("无");
        }else {
            if(sendDanmakuM1Vo.getPart() == null){
                sendDanmakuM1Vo.setPart(1);
            }
            Map<String,Object> videoMap = new HashMap<>();
            String title = "无";
            String pageCid = "无";
            String pagePart = "无";
            int part = 0;
            if(sendDanmakuM1Vo.getBvid()!=null){
                videoMap =  getBiliVideoInfo(sendDanmakuM1Vo.getBvid());
                title = (String)videoMap.get("title");
                part =  Integer.valueOf(sendDanmakuM1Vo.getPart())-1;
                JSONArray jsonArray =(JSONArray)videoMap.get("page");
                JSONObject jo = jsonArray.getJSONObject(part);
                pageCid = jo.getString("cid");
                pagePart = jo.getString("part");
            }
            sendDanmakuM1Vo.setOid(pageCid);
            sendDanmakuM1Vo.setVideoTitle(title);
            sendDanmakuM1Vo.setPartTitle(pagePart);
        }

    }

    @Override
    public void initDanmakuM7(SendDanmakuM7Vo sendDanmakuM7Vo, List<Ass> danmakuList) throws Exception{

        sendDanmakuM7Vo.setDelayDuration(0);

        if(sendDanmakuM7Vo.getZRotate() == null){
            sendDanmakuM7Vo.setZRotate(0);
        }
        if(sendDanmakuM7Vo.getYRotate() == null){
            sendDanmakuM7Vo.setYRotate(0);
        }
        if(sendDanmakuM7Vo.getOverlapTime() == null){
            sendDanmakuM7Vo.setOverlapTime(0);
        }


        //处理前缀附加内容
//        if(!sendDanmakuM1Vo.getSendPrefix().isEmpty()){
//            String prefixContent =  sendDanmakuM1Vo.getSendPrefix();
//            for(Ass vo:danmakuList){
//                vo.setContent(prefixContent + vo.getContent());
//            }
//        }
//        //处理后缀附加内容
//        if(!sendDanmakuM1Vo.getSendSuffix().isEmpty()){
//            String suffixContent =  sendDanmakuM1Vo.getSendSuffix();
//            for(Ass vo:danmakuList){
//                vo.setContent(vo.getContent() + suffixContent );
//            }
//        }

        // 根据bvid + pardId获取视频信息
        if(StringUtils.isNotBlank(sendDanmakuM7Vo.getBvid())){
            sendDanmakuM7Vo.setOid("无");
            sendDanmakuM7Vo.setVideoTitle("无");
            sendDanmakuM7Vo.setPartTitle("无");
        }else {
            if(sendDanmakuM7Vo.getPart() == null){
                sendDanmakuM7Vo.setPart(1);
            }
            Map<String,Object> videoMap = new HashMap<>();
            String title = "无";
            String pageCid = "无";
            String pagePart = "无";
            int part = 0;
            if(sendDanmakuM7Vo.getBvid()!=null){
                videoMap =  getBiliVideoInfo(sendDanmakuM7Vo.getBvid());
                title = (String)videoMap.get("title");
                part =  Integer.valueOf(sendDanmakuM7Vo.getPart())-1;
                JSONArray jsonArray =(JSONArray)videoMap.get("page");
                JSONObject jo = jsonArray.getJSONObject(part);
                pageCid = jo.getString("cid");
                pagePart = jo.getString("part");
            }
            sendDanmakuM7Vo.setOid(pageCid);
            sendDanmakuM7Vo.setVideoTitle(title);
            sendDanmakuM7Vo.setPartTitle(pagePart);
        }

    }


    @Override
    public boolean checkStop(String fileLogPath){
        FileReader reader = new FileReader(fileLogPath);
        List<String> linesList = reader.readLines();
        String lastLine = linesList.get(linesList.size()-1);
        int result = lastLine.indexOf("──停止──");
        if(result != -1){
            return true;
        }
        return false;
    }


    @Override
    public  Map<String, Object>  getBiliVideoInfo(String bvid) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        String url ="http://api.bilibili.com/x/web-interface/view";
        url = url +"?bvid=" + bvid;
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();


            String videoJson = result;
            JSONObject jsonObject = JSON.parseObject(videoJson);
            JSONObject jsonObjectSon = jsonObject.getJSONObject("data");
            String title = jsonObjectSon.getString("title");
            String defaultCid = jsonObjectSon.getString("cid");

            JSONArray array = jsonObjectSon.getJSONArray("pages");
            for (int i = 0; i < array.size(); i++) {
                JSONObject jo = array.getJSONObject(i);
                String pageCid = jo.getString("cid");
                String pageNumber = jo.getString("page");
                String pagePart = jo.getString("part");

            }
            resultMap.put("title",title);
            resultMap.put("cid",defaultCid);
            resultMap.put("page",array);
//            Response.makeOKRsp(resultMap);
        } catch (IOException e) {

        }







        return resultMap;
    }

    //微调改变颜色
    @Override
    public String updateColor(String color){
        String lastColorStr = color.substring(color.length()-1,color.length());
        String color10str = ColorUtil.hexto10(color);
        int color10 = Integer.parseInt(color10str);
        if("F".equals(lastColorStr)||"B".equals(lastColorStr)||"f".equals(lastColorStr)||"b".equals(lastColorStr)||"8".equals(lastColorStr)||"4".equals(lastColorStr)){
            color10 = color10 - 1;
        }else {
            color10 = color10 + 1;
        }
        color =  ColorUtil.tentohex(color10);
        return color;
    }



}
