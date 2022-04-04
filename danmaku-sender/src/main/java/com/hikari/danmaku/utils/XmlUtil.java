package com.hikari.danmaku.utils;

import com.hikari.danmaku.constants.Mode;
import com.hikari.danmaku.entity.AviutlExo;
import com.hikari.danmaku.entity.BaseDanmaku;
import com.hikari.danmaku.entity.SeniorDanmaku;
import com.hikari.danmaku.entity.XmlEffect;
import com.hikari.danmaku.vo.SendDanmakuM1Vo;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.hikari.danmaku.utils.CommonUtil.df3m7;

public class XmlUtil {
    public static void main(String[] args) throws Exception {
//        System.out.println(XmlUtil.m7ToBas("C:\\杂物\\放弃音乐.xml"));
//        System.out.println(XmlUtil.m7ToBas("C:\\杂物\\潜水艇.xml"));
        String fileName = "信者";
//        String fileName = "速度线";
//        String fileName = "hello闪字";
        String inputExoPath = "C:\\Users\\hikari\\Desktop\\"+  fileName+ ".exo";
        String outputXmlPath = "C:\\Users\\hikari\\Desktop\\"+ fileName +".xml";

        int screenX = 665;
        int screenY = 375;
        List<BaseDanmaku> dmList = analyseXml(outputXmlPath,true);

//        List<SeniorDanmaku> sdmList = new ArrayList<>();
//        for(BaseDanmaku baseDanmaku : dmList){
//            SeniorDanmaku sdm = baseToSeniorDanmaku(baseDanmaku);
//            sdm.setStartX( sdm.getStartX() + 92);
//            sdm.setStartY( Math.ceil(sdm.getStartY() + 92));
//
//            sdm.setEndX( Math.ceil(sdm.getEndX() + 92));
//            sdm.setEndY( Math.ceil(sdm.getEndY() + 92));
//
//            String reContent = DanmakuUtil.wrapperDanmaku(sdm);
//            baseDanmaku.setContent(reContent);
//            sdmList.add(sdm);
//        }
        // 重组xml
        System.out.println(buildSendXml(dmList));

        // 插件预览用xml
        String previewXmlStr = previewSendXml(dmList);
        String preStr = "let s=`"+ previewXmlStr.replace("\n","") +"`" + "\n";
        preStr = preStr + "ldanmu=xml2danmu(s)"+ "\n";
        preStr = preStr + "window.ldldanmu[window.ldldanmu.length-1].ldanmu=ldanmu";
        System.out.println(preStr);
    }

    public static List<BaseDanmaku> analyseXml(String url ,Boolean isColor16) throws Exception{
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
            if(attrArray[3].length() == 6 && isColor16){
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

            //全弹幕下载的左斜杠换行符转换成现在的换行符
            String danmakuTextStr = danmaku.getText().replaceAll("/n","\\\\n");
            danmakuEntity.setContent(danmakuTextStr);

            //普通弹幕
            // <d p="2.89900,1,25,16777215,1595498498,0,f4e96d98,35838400054427653">高产似那啥</d>
            // <d p="17.31400,1,25,16777215,1595498504,0,c73978ce,35838403122036739">要热评</d>

            dmList.add(danmakuEntity);
        }

        return dmList;
    }


    // 解析弹幕xml的属性
    public static SeniorDanmaku parseXmlAttribute(String attributeStr) {
        String[] attrArray = attributeStr.split(",");
        SeniorDanmaku danmakuEntity = new SeniorDanmaku();
        danmakuEntity.setStartTime(CommonUtil.df3(Double.valueOf(attrArray[0])));
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

        if(attrArray.length >=9){
            String weight = attrArray[8];
            danmakuEntity.setWeight(Integer.valueOf(weight));
        }

        return danmakuEntity;
    }

    // 组装xml（传高级弹幕对象，返回一条xml弹幕）
    public static String buildDanmakuXml(SeniorDanmaku seniorDanmaku) {
        StringBuffer danmakuStr = new StringBuffer();
        danmakuStr.append("<d p=\"");
        danmakuStr.append(seniorDanmaku.getStartTime()).append(",");
        danmakuStr.append(seniorDanmaku.getMode()).append(",");
        danmakuStr.append(seniorDanmaku.getFontSize()).append(",");
        danmakuStr.append(seniorDanmaku.getColor()).append(",");
        danmakuStr.append(DateUtil.LongToTime(seniorDanmaku.getTimestamp().longValue()*1000L)).append(",");
        danmakuStr.append(seniorDanmaku.getPool());
        danmakuStr.append(",null,null\">");
        danmakuStr.append(seniorDanmaku.getContent());
        danmakuStr.append("</d>");
        return danmakuStr.toString();
    }

    // 解析高级弹幕属性（普通弹幕转高级弹幕）
    public static SeniorDanmaku baseToSeniorDanmaku(BaseDanmaku baseDanmaku) {
        SeniorDanmaku seniorDanmaku = new SeniorDanmaku();
        BeanUtils.copyProperties(baseDanmaku, seniorDanmaku);

        String regEx="[\\[\\]\"]";
        String danmakuTextStr = baseDanmaku.getContent().replaceAll(regEx,"");//不想保留原来的字符串可以直接写成 “str = str.replaceAll(regEX,aa);”
        String[] textArray = danmakuTextStr.split(",");
        seniorDanmaku.setStartX(Double.parseDouble(textArray[0]));
        seniorDanmaku.setStartY(Double.parseDouble(textArray[1]));

        seniorDanmaku.setAlpha(textArray[2]);
        seniorDanmaku.setDuration(Double.parseDouble(textArray[3]));
        seniorDanmaku.setText(textArray[4]);
        if(textArray[5].equals("")){
            textArray[5] = "0";
        }
        seniorDanmaku.setZRotate(Integer.valueOf(textArray[5]));
        if(textArray[6].equals("")){
            textArray[6] = "0";
        }
        seniorDanmaku.setYRotate(Integer.valueOf(textArray[6]));
        seniorDanmaku.setEndX(Double.parseDouble(textArray[7]));
        seniorDanmaku.setEndY(Double.parseDouble(textArray[8]));

        seniorDanmaku.setMoveDuration(Integer.valueOf(textArray[9]));
        seniorDanmaku.setDelayDuration(Integer.valueOf(textArray[10]));
        seniorDanmaku.setAccess(Integer.valueOf(textArray[11]));

        seniorDanmaku.setFontFamily(textArray[12]);
        seniorDanmaku.setBord(Integer.valueOf(textArray[13]));


        return seniorDanmaku;
    }


    //解析m7高级弹幕转为bas弹幕
    public static String m7ToBas(String url) throws Exception{
        String strs = "def text template(content=\"\",fontSize=\"\",fontFamily = \"\",x=\"\",y=\"\",alpha=\"\",color=\"\") \n" +
                "{" +
                "   content = content\n" +
                "    fontSize = fontSize\n" +
                "    fontFamily = fontFamily\n" +
                "    x = x\n" +
                "    y = y\n" +
                "    alpha = alpha\n" +
                "    color = color\n" +
                "    textShadow = 0\n" +
                "    bold = 1\n" +
                "    rotateY = 0\n" +
                "    rotateZ = 0\n" +
                "    anchorX = 0\n" +
                "    anchorY = 0\n" +
                "    zIndex = 1\n" +
                "}\n" ;

        List<BaseDanmaku>  danmakuList = XmlUtil.analyseXml(url,false);
        for(int i = 0 ; i < danmakuList.size() ; i++){
            SeniorDanmaku seniorDanmaku = baseToSeniorDanmaku(danmakuList.get(i));
//            System.out.println(seniorDanmaku);
            String xStartStr = "";
            String yStartStr = "";
            String xEndStr = "";
            String yEndStr = "";
            //坐标是否为百分比
            double xStart = seniorDanmaku.getStartX();
            double yStart = seniorDanmaku.getStartY();
            double xEnd = seniorDanmaku.getEndX();
            double yEnd = seniorDanmaku.getEndY();
            if(xStart <= 1 ){
                xStart = CommonUtil.d2(xStart * 100);
                yStart = CommonUtil.d2(yStart * 100);
                xEnd = CommonUtil.d2(xEnd * 100);
                yEnd = CommonUtil.d2(yEnd * 100);
                xStartStr= xStart + "%";
                yStartStr = yStart + "%";
                xEndStr = xEnd + "%";
                yEndStr = yEnd + "%";
            }
            //透明度
            String alphaStr = seniorDanmaku.getAlpha();
            //1.根据\n分隔字符串，string截取
            String[] alphaArray = alphaStr.split("-");
            double alphaDuration = Double.valueOf(alphaArray[1]) - Double.valueOf(alphaArray[0]);

            strs =strs + "let t" + i + "=template(" +
                    "content=\""+  seniorDanmaku.getText()  +"\"," +
                    "fontSize=\""+  seniorDanmaku.getFontSize()  +"\"," +
                    "fontFamily=\""+  seniorDanmaku.getFontFamily()  +"\"," +
                    "x="+  xStartStr  +"," +
                    "y="+  yStartStr +"," +
                    "alpha=\"0\"," +
                    "color=\"0x"+  seniorDanmaku.getColor()  +"\"" +
                    ")\n";

            double duration_plus = Double.valueOf(seniorDanmaku.getDuration())*1000 - Double.valueOf( seniorDanmaku.getMoveDuration()) - Double.valueOf( seniorDanmaku.getDelayDuration())     ;//持续时间=存活时间-移动时间-延迟时间
            double moveDuration_plus =Double.valueOf(seniorDanmaku.getMoveDuration());
            double startTime_plus =Double.valueOf(seniorDanmaku.getStartTime());//出现时间为  弹幕发送时间
            double delay_plus = seniorDanmaku.getDelayDuration();//延迟时间

            if(duration_plus<0){
                moveDuration_plus = Double.valueOf(seniorDanmaku.getDuration())*1000;
                duration_plus =0;
            }
            double sumDuration = delay_plus + moveDuration_plus + duration_plus;
            double alphaDelayPer = delay_plus/sumDuration * alphaDuration;
            double alphamovePer = moveDuration_plus/sumDuration * alphaDuration;
            double alphaDuraPer = duration_plus/sumDuration * alphaDuration;
//        double duratio_plus = Double.valueOf(String.format("%.1f", duratio_plus ));

            if("1-1".equals(alphaStr)){
                StringBuffer danmakuStr = new StringBuffer();
                danmakuStr.append("set t").append(i).append("{alpha=1} ").append(startTime_plus).append("s,\"step-end\" ");
                if(delay_plus != 0){
                    danmakuStr.append("then set t").append(i).append("{}").append(delay_plus).append("ms\n");
                }
                danmakuStr.append("then set t").append(i)
                        .append("{x = ").append(xEndStr)
                        .append(" y = ").append(yEndStr)
                        .append("}").append(moveDuration_plus).append("ms ").append("then set t").append(i).append("{}").append(duration_plus).append("ms").append("\n");

                strs =strs + danmakuStr.toString();
            }else if(alphaDuration <= 0){
                alphaDelayPer = (delay_plus/sumDuration -1 )* alphaDuration  + Double.valueOf(alphaArray[1]) ;
                alphamovePer = (moveDuration_plus/sumDuration -1) * alphaDuration+ Double.valueOf(alphaArray[1]) ;
                alphaDuraPer = ( duration_plus/sumDuration -1)* alphaDuration + Double.valueOf(alphaArray[1]);

                strs =strs +"set t" + i + "{} "+ startTime_plus +"s //出现时间\n" +
                        "then set t" + i + "{ alpha="+ alphaArray[0] +" }0s \n" + //透明度为0到1
                        "then set t" + i + "{alpha="+ alphaDelayPer +"}"+ delay_plus +"ms //高级弹幕中的延迟\n" +
                        "then set t" + i + "{ alpha="+ alphamovePer  +"  " +
                        "x = "+ xEndStr  +"," +
                        "y = "+ yEndStr+ "" +
                        "}"+ moveDuration_plus +"ms//移动\n"+
                        "then set t" + i + "{alpha="+ alphaDuraPer  +" }"+ duration_plus +"ms   //持续时间\n";
            }else {
                strs =strs +"set t" + i + "{} "+ startTime_plus +"s //出现时间\n" +
                        "then set t" + i + "{ alpha="+ alphaArray[0] +" }0s \n" + //透明度为0到1
                        "then set t" + i + "{alpha="+ alphaDelayPer +"}"+ delay_plus +"ms //高级弹幕中的延迟\n" +
                        "then set t" + i + "{ alpha="+ alphamovePer  +"  " +
                        "x = "+ xEndStr  +"," +
                        "y = "+ yEndStr+ "" +
                        "}"+ moveDuration_plus +"ms  //移动\n"+
                        "then set t" + i + "{alpha="+ alphaDuraPer  +" }"+ duration_plus +"ms   //持续时间\n";
            }

        }


        return strs;
    }


    // 读取m7属性组装bas
    public static String buildM7Xml(SeniorDanmaku seniorDanmaku) {
        StringBuffer danmakuStr = new StringBuffer();
        danmakuStr.append("<d p=\"");
        danmakuStr.append(seniorDanmaku.getStartTime()).append(",");
        danmakuStr.append(seniorDanmaku.getMode()).append(",");
        danmakuStr.append(seniorDanmaku.getFontSize()).append(",");
        danmakuStr.append(seniorDanmaku.getColor()).append(",");
        danmakuStr.append(DateUtil.LongToTime(seniorDanmaku.getTimestamp().longValue()*1000L)).append(",");
        danmakuStr.append(seniorDanmaku.getPool());
        danmakuStr.append(",null,null\">");
        danmakuStr.append(seniorDanmaku.getContent());
        danmakuStr.append("</d>");
        return danmakuStr.toString();
    }




    // 预览xml
    public static String previewSendXml(List<BaseDanmaku> xmlDanmaku){
        StringBuffer headStr = new StringBuffer();
        headStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><i>\n");
        StringBuffer previewBodyStr = new StringBuffer();
        for(int i = 0;i < xmlDanmaku.size(); i++) {
            BaseDanmaku danmaku = xmlDanmaku.get(i);
            StringBuffer danmakuPreviewStr = new StringBuffer();
            danmakuPreviewStr.append("<d p=\"");
            danmakuPreviewStr.append(danmaku.getStartTime()).append(",");
            danmakuPreviewStr.append(danmaku.getMode()).append(",");
            danmakuPreviewStr.append(danmaku.getFontSize()).append(",");
            danmakuPreviewStr.append(ColorUtil.hexto10(danmaku.getColor())).append(",");
            danmakuPreviewStr.append("null,0,null,null\">");
//            if(currentXmlEffect.getIsColor10()){
//                exoBean.setColor(ColorUtil.hexto10(exoBean.getColor()));
//            }
//
            String content = danmaku.getContent();
            content = content.replace("\\","\\\\");
            content = content.replace("`","&apos;");
            danmaku.setContent(content);
            if(danmaku.getContent()!= null && (danmaku.getContent().contains("<") ||danmaku.getContent().contains(">"))){
                String text = danmaku.getContent();
                text = text.replace("<","&lt;");
                text = text.replace(">","&gt;");
                danmaku.setContent(text);
            }
            danmakuPreviewStr.append(danmaku.getContent()).append("</d>\n");
            previewBodyStr.append(danmakuPreviewStr);
        }
        StringBuffer endStr = new StringBuffer();
        endStr.append("</i>");

        StringBuffer previewXml = new StringBuffer();
        previewXml.append(headStr).append(previewBodyStr).append(endStr);
        return  previewXml.toString();

    }

    // 预览xml
    public static String buildSendXml(List<BaseDanmaku> xmlDanmaku){
        StringBuffer headStr = new StringBuffer();
        headStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><i>\n");
        StringBuffer previewBodyStr = new StringBuffer();
        for(int i = 0;i < xmlDanmaku.size(); i++) {
            BaseDanmaku danmaku = xmlDanmaku.get(i);
            StringBuffer danmakuPreviewStr = new StringBuffer();
            danmakuPreviewStr.append("<d p=\"");
            danmakuPreviewStr.append(danmaku.getStartTime()).append(",");
            danmakuPreviewStr.append(danmaku.getMode()).append(",");
            danmakuPreviewStr.append(danmaku.getFontSize()).append(",");
            danmakuPreviewStr.append(ColorUtil.hexto10(danmaku.getColor())).append(",");
            danmakuPreviewStr.append("null,0,null,null\">");
//            if(currentXmlEffect.getIsColor10()){
//                exoBean.setColor(ColorUtil.hexto10(exoBean.getColor()));
//            }
//
            String content = danmaku.getContent();
//            content = content.replace("\\","\\\\");
            content = content.replace("`","&apos;");
            danmaku.setContent(content);
            if(danmaku.getContent()!= null && (danmaku.getContent().contains("<") ||danmaku.getContent().contains(">"))){
                String text = danmaku.getContent();
                text = text.replace("<","&lt;");
                text = text.replace(">","&gt;");
                danmaku.setContent(text);
            }
            danmakuPreviewStr.append(danmaku.getContent()).append("</d>\n");
            previewBodyStr.append(danmakuPreviewStr);
        }
        StringBuffer endStr = new StringBuffer();
        endStr.append("</i>");

        StringBuffer previewXml = new StringBuffer();
        previewXml.append(headStr).append(previewBodyStr).append(endStr);
        return  previewXml.toString();

    }
}
