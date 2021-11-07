package com.hikari.danmaku.utils;

import com.hikari.danmaku.constants.Mode;
import com.hikari.danmaku.entity.BaseDanmaku;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlUtil {
    public static void main(String[] args) {

    }

    public static List<BaseDanmaku> analyseXml(String url) throws Exception{
        SAXReader reader = new SAXReader();
        String path = url;
        File file = new File(url);  //将文件读取，这样路径如果有中文一样可以正常读取

        Document document = reader.read(new File(path));
        /* 遍历所有指定名称子元素 */
        Element root = document.getRootElement();
        List<BaseDanmaku> dmList = new ArrayList<>();

        int count =0;
        for (Iterator i = root.elementIterator("d"); i.hasNext();) {
            count = count + 1;

            Element danmaku = (Element) i.next();
            Attribute dmAttr = danmaku.attribute("p");

            String[] attrArray = dmAttr.getValue().split(",");
            BaseDanmaku danmakuEntity = new BaseDanmaku();
            danmakuEntity.setStartTime(attrArray[0]);
            danmakuEntity.setMode(Integer.valueOf(attrArray[1]) );
            danmakuEntity.setFontSize(Integer.valueOf(attrArray[2]));
            String strHex ="";
            if(attrArray[3].length() == 6){
                strHex = attrArray[3];
            }else {
                int valueTen = Integer.valueOf(attrArray[3]);
                strHex =Integer.toHexString(valueTen);
            }

            int timestamp = 0;
            if(StringUtils.isNumeric(attrArray[4])){
                timestamp = Integer.valueOf(attrArray[4]);
            }

            danmakuEntity.setColor(strHex);
            danmakuEntity.setTimestamp(timestamp);
            danmakuEntity.setPool(Integer.valueOf(attrArray[5]));
            danmakuEntity.setUid(attrArray[6]);
            danmakuEntity.setRowId(attrArray[7]);
            danmakuEntity.setContent(danmaku.getText());

            //普通弹幕
            // <d p="2.89900,1,25,16777215,1595498498,0,f4e96d98,35838400054427653">高产似那啥</d>
            // <d p="17.31400,1,25,16777215,1595498504,0,c73978ce,35838403122036739">要热评</d>

            dmList.add(danmakuEntity);
        }

        return dmList;
    }
}
