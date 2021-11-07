package com.hikari.danmaku.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hikari.danmaku.common.Response;
import com.hikari.danmaku.common.ResponseResult;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import com.hikari.danmaku.vo.SendDanmakuVo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

public class HttpUtil {

    private static String dmPostUrl = "http://api.bilibili.com/x/v2/dm/post";

    public static ResponseResult dmPost(String content, int startTimes, SendDanmakuVo sendDanmakuVO)  throws Exception{
        String sessdata = sendDanmakuVO.getCookie();
        String csrf = sendDanmakuVO.getCsrf();
        String bvid = sendDanmakuVO.getBvid();
        if(sessdata != null  ) { sessdata =  sessdata.trim(); }
        if(csrf != null  ) {  csrf = csrf.trim(); }
        if(bvid != null  ) {  bvid = bvid.trim(); }

        PostMethod postMethod = new PostMethod(dmPostUrl) ;
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded") ;
        postMethod.setRequestHeader("Cookie",  "SESSDATA=" + sessdata) ;
        postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36") ;
//        postMethod.setRequestHeader("Referer", "https://www.bilibili.com/video/BV1Z54y1S7wN?p=2") ;
        //预处理
        ////时间戳
        String timestampPlus = Long.toString(System.currentTimeMillis()/1000L) + "000000";

        ////弹幕出现时间（毫秒）
        String progress = String.valueOf(startTimes);
        String color = ColorUtil.hexto10(sendDanmakuVO.getColor());

        //参数设置，需要注意的就是里边不能传NULL，要传空字符串
        NameValuePair[] data = {
                new NameValuePair("type","1"),
                new NameValuePair("oid", sendDanmakuVO.getOid()),
                new NameValuePair("msg",content),
                new NameValuePair("bvid",bvid),
                new NameValuePair("progress",progress),
                new NameValuePair("color",color),
                new NameValuePair("fontsize", sendDanmakuVO.getFontSize().toString()),
                new NameValuePair("pool", sendDanmakuVO.getPool().toString()),
                new NameValuePair("mode", sendDanmakuVO.getMode().toString()),
                new NameValuePair("rnd",timestampPlus),
                new NameValuePair("csrf", csrf)
        };
        postMethod.setRequestBody(data);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");

        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        httpClient.executeMethod(postMethod);
        String result = postMethod.getResponseBodyAsString() ;


        JSONObject jsonObjectP = JSON.parseObject(result);
        int code = Integer.valueOf(jsonObjectP.getString("code")) ;
        String message = jsonObjectP.getString("message");
//        ResponseResult responseResult = new ResponseResult;
//        responseResult.setCode(code);
//        responseResult.setMsg(message);

        return  Response.makeRsp(code,message);
    }




    public static String getBiliVideoInfo(String bvid)  throws Exception{
        String url ="http://api.bilibili.com/x/web-interface/view";
        url = url +"?bvid=" + bvid;
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(url);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
//            logger.info("返回信息为{}", result);
            return result;
        } catch (IOException e) {
        }
        return null;
    }


}
