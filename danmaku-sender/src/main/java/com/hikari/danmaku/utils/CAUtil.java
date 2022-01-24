package com.hikari.danmaku.utils;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.hikari.danmaku.entity.Ass;
import com.hikari.danmaku.entity.AviutlExo;
import com.hikari.danmaku.entity.Lyric;
import com.hikari.danmaku.entity.XmlEffect;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hikari.danmaku.utils.CommonUtil.df3m7;
import static com.hikari.danmaku.utils.ExoUtil.writeStringToFile;

/**
 * n站字符画工具类
 */

public class CAUtil {
    public static void main(String []args) throws Exception{
        String fileName = "CA_berrygo_0101";
        String inputJsonPath = "C:\\Users\\hikari\\Desktop\\" + fileName + ".json";
        String outputXmlPath = "C:\\Users\\hikari\\Desktop\\"+ fileName +"-danmaku.xml";
        List<Map<String,String>> jsonList = parseCAJson(inputJsonPath);
        String xmlStr = json2xml(jsonList);
//        System.out.println();
        //输出字符串到文件
        writeStringToFile(outputXmlPath,xmlStr);

    }


    /**
     * 1.json转map
     */
    public static List<Map<String,String>> parseCAJson(String jsonPath) {
        int removeTop = 11;
        int width = 14;
        int height = 14;

        FileReader reader = new FileReader(jsonPath);
        String strJson = reader.readString();
        JSONArray jsonArray = JSONUtil.parseArray(strJson);
        List<Map<String,String>> txtList = new ArrayList<>();
        for(int i = 0;i < jsonArray.size() ;i++){
            Map<String,String> jsonMap = (Map)jsonArray.get(i);
            Map<String,String> resultMap = new HashMap<>();
            String command = jsonMap.get("command");
            String comment = jsonMap.get("comment");
            String color = command.substring(command.length()-6);
            for(int k = 0;k < removeTop;k++){
                comment = comment.replaceFirst("\n","");
            }
            comment = comment.replaceAll(" ","");

            String[] split = comment.split("\\n");//分割
            String pathText = "";
            for (String s : split) {
                if(s.length() > 0){
                    s = s.substring(10);
                    int lineLength = s.length();
                    if(lineLength < width){
                        int less = width - lineLength;
                        String lBlock = "";
                        for(int l = 0 ; l<less;l++){
                            lBlock = lBlock + "　";
                        }
                        s = s + lBlock;
                    }else {
                        s = s.substring(0, width);
                    }

                }
//                System.out.println(s);

                pathText = pathText + s + "\n";
            }
//            System.out.println("------------");
            comment = pathText;
//            System.out.println(comment);
            resultMap.put("comment",comment);
            resultMap.put("color",color);
            txtList.add(resultMap);
        }
//        System.out.println(txtList);
        return txtList;
    }

    /**
     * 2.list转xml
     */
    public static String json2xml(List<Map<String,String>> jsonList){
        System.out.println("******************************************");
        StringBuffer xml = new StringBuffer();
        StringBuffer headStr = new StringBuffer();
        headStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><i>\n");
        StringBuffer bodyStr = new StringBuffer();
        for(int i = 0;i < jsonList.size(); i++){
            Map<String,String> mapObject = jsonList.get(i);
            String comment = mapObject.get("comment");
            String color = mapObject.get("color");

            // danmakuStr.append("<d p=\"5.6,7,30,ffffff,null,0,null,null\">[\"230\",\"380\",\"1-1\",\"5.4\",\"如 果 我 成 为 大 统 领\",0,0,\"230\",\"380\",\"0\",0,0,\"Microsoft YaHei\",1]</d>");

            String startX = String.valueOf(0);
            String startY = String.valueOf(0);
            String endX = String.valueOf(0);
            String endY = String.valueOf(0);


            StringBuffer danmakuStr = new StringBuffer();
            danmakuStr.append("<d p=\"");
            danmakuStr.append(10).append(",");
            danmakuStr.append("7").append(",");
            danmakuStr.append(10).append(",");

            comment = comment.replace("\n","\\n");
            color = ColorUtil.hexto10(color);


            danmakuStr.append(color).append(",");
            danmakuStr.append("null,0,null,null\">[");
            danmakuStr.append("\"").append(startX).append("\"").append(",");
            danmakuStr.append("\"").append(startY).append("\"").append(",");
            danmakuStr.append("\"").append("1-1").append("\"").append(",");
            danmakuStr.append("\"").append("4").append("\"").append(",");
            danmakuStr.append("\"").append(comment).append("\"").append(",");
            danmakuStr.append(0).append(",");//Z_Rotation
            danmakuStr.append(0).append(",");//Y_Rotation
            danmakuStr.append("\"").append(endX).append("\"").append(",");
            danmakuStr.append("\"").append(endY).append("\"").append(",");
            danmakuStr.append("\"").append(0).append("\"").append(",");
            danmakuStr.append("\"").append(0).append("\"").append(",");//Delay_Time
            danmakuStr.append(0).append(",");//Outline
            danmakuStr.append("\"").append("SimHei").append("\"").append(",");
            danmakuStr.append(0);//Linear_Speedup
            danmakuStr.append("]</d>\n");

            bodyStr.append(danmakuStr);

        }
        StringBuffer endStr = new StringBuffer();
        endStr.append("</i>");

        xml.append(headStr);
        xml.append(bodyStr);
        xml.append(endStr);

        System.out.println(xml);
        System.out.println("******************************************");
        System.out.println("let s=`"+ xml.toString().replace("\n","") +"`");
        System.out.println("ldanmu=xml2danmu(s)");
        System.out.println("window.ldldanmu[window.ldldanmu.length-1].ldanmu=ldanmu");
        System.out.println("******************************************");



        return  xml.toString();


    }


}