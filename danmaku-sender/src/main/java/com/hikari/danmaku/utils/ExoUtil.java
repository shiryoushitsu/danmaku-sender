package com.hikari.danmaku.utils;


import com.hikari.danmaku.entity.*;
import org.springframework.beans.BeanUtils;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static com.hikari.danmaku.utils.CommonUtil.*;

public class ExoUtil {

    final private static double frameSecond = 0.033;
    private static Integer screenWidth =  0;
    private static Integer screenHeight =  0;
    private static String currentName =  "";
    private static Integer currentChain =  0;
    private static XmlEffect currentTXmlEffect ;


    public static void main(String[] args) {
        String fileName = "3333";
        String inputExoPath = "D:\\"+  fileName+ ".exo";
        String outputXmlPath = "D:\\"+ fileName +".xml";

        //1.解析exo文本返回分段List
        List<Map<String,String>> exoList = parseExoToList(inputExoPath);
        //2.将分段List优化为exo对象list
        List<AviutlExo> exoObjectList = parseExoList(exoList);
        //3.将处理好的exo对象list转为m7xml
        String xmlStr =exoToXml(exoObjectList);

        //输出字符串到文件
        writeStringToFile(outputXmlPath,xmlStr);

    }


    //1、解析exo返回分段List
    public static List<Map<String,String>> parseExoToList(String path)  {
        String encoding = "utf-8";
        List<Map<String,String>> partList = new ArrayList<>();
        Map<String,String> partMap  = new LinkedHashMap<>();
        try{
            File file = new File(path);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                encoding = codeString(file);
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineStr = null; // 每次读取一行字符串
                int times = 0;
                while ((lineStr = bufferedReader.readLine()) != null) {
                    if (lineStr != null && !lineStr.isEmpty()) {
                        // 解析[xxx]
                        String pattern = "\\[(.*?)]";
                        Pattern r = Pattern.compile(pattern);
                        Matcher matcher = r.matcher(lineStr);
                        if(matcher.matches()){
                            if(times != 0 ){
                                partList.add(partMap);
                            }
                            Map<String,String> currentPartMap  = new LinkedHashMap<>();
                            currentPartMap.put("partName",matcher.group(1));
                            partMap = currentPartMap;
                            times = times + 1;
                        }else {
                            if(lineStr.contains("=")){
                                String[] as = lineStr.split("=");
                                if(as.length == 2) {
                                    partMap.put(as[0],as[1]);
                                }
                            }
                        }
                    }
                }
                partList.add(partMap);
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            e.printStackTrace();
        }
//        System.out.println(partList);
        return partList;
    }

    //2、解析exoList返回段对象
    public static List<AviutlExo>  parseExoList(List<Map<String,String>>exoList) {
        XmlEffect XmlEffect = new XmlEffect();
        XmlEffect.setOutline(false);
        XmlEffect.setLayer(true);
        XmlEffect.setLinearSpeedup(false);
        XmlEffect.setAdvanceStartTime(false);
        XmlEffect.setDelayEndTime(true);
        currentTXmlEffect = XmlEffect;

        List<AviutlExo> partExoList =  new ArrayList<>();
        AviutlExo curExoVO = new AviutlExo();
        for(int i = 0; i < exoList.size();i++){
            Map<String,String> exoListMap = new LinkedHashMap<>();
            exoListMap =exoList.get(i);
            //不包含点的，即主段
            if(!exoListMap.get("partName").contains(".")){
                AviutlExo exoVO = new AviutlExo();
                curExoVO = exoVO;
                partExoList.add(curExoVO);
            }
            for(Map.Entry<String, String> entry : exoListMap.entrySet()) {
                curExoVO = mapToExo(entry.getKey(),entry.getValue(),curExoVO);
            }
        }

        //保留文本文件
        Iterator<AviutlExo> itr = partExoList.iterator();
        while (itr.hasNext()){
            AviutlExo tmp = itr.next();
            if(!("文本".equals(tmp.getObjName())||"图形".equals(tmp.getObjName()))){
                itr.remove();
            }
        }

        //将一个中间点、两段合并为一段，修改移动时间、总时间
        for(int k = 0;k < partExoList.size();k++){
            //链式
            if(1 == partExoList.get(k).getChain() && k > 0) {
                //先移动后停止（前面的x、y移动不同）
                int s1 = partExoList.get(k-1).getStartX();
                int s2 = partExoList.get(k-1).getEndX();
                int s3 = partExoList.get(k-1).getStartY();
                int s4 = partExoList.get(k-1).getEndY();
                boolean b1=  s1 !=s2;
                boolean b2=  s3 !=s4;
//                System.out.println(s1 +" " + s2  +" " + s3  +" " + s4  +" " + b1  +" " + b2);
                if((!partExoList.get(k-1).getStartX().equals(partExoList.get(k-1).getEndX()) ) || ( !partExoList.get(k-1).getStartY().equals(partExoList.get(k-1).getEndY()) )  )
                {
                    int moveTime = (int)Math.round( partExoList.get(k-1).getLifeTime()*1000 );
                    double preLifeTime =  partExoList.get(k-1).getStartTime();
                    double curEndTime =  partExoList.get(k).getEndTime();
                    double allLifeTime =  partExoList.get(k).getEndTime() - preLifeTime;

                    partExoList.get(k-1).setMoveTime(moveTime);
                    partExoList.get(k-1).setLifeTime(d2(allLifeTime));
                    partExoList.get(k-1).setEndTime(curEndTime);
                    partExoList.set(k, partExoList.get(k-1));
                }
                else if( (!partExoList.get(k).getStartX().equals(partExoList.get(k).getEndX()) ) || ( !partExoList.get(k).getStartY().equals(partExoList.get(k).getEndY()) ) ){
                    int delayTime = (int)Math.round( partExoList.get(k-1).getLifeTime()*1000 );
                    int moveTime = (int)Math.round( partExoList.get(k).getLifeTime()*1000 );
                    double preLifeTime =  partExoList.get(k-1).getStartTime();
                    double curEndTime =  partExoList.get(k).getEndTime();
                    double allLifeTime =  partExoList.get(k).getEndTime() - preLifeTime;

                    partExoList.get(k-1).setDelayTime(delayTime);
                    partExoList.get(k-1).setMoveTime(moveTime);
                    partExoList.get(k-1).setLifeTime(d2(allLifeTime));
                    partExoList.get(k-1).setEndTime(curEndTime);
                    partExoList.set(k, partExoList.get(k-1));
                }



                //todo
                //先移动后停止（前面的x、y移动相同 且 后面x、y不同）




            }
        }
        //再次去重
        removeDuplicateWithOrder(partExoList);


        //阴影处理，新建文件
        for(int s = 0;s < partExoList.size();s++){
            AulShadow AulShadow =partExoList.get(s).getAulShadow();
            Optional<AulShadow> shadowOpt = Optional.ofNullable(AulShadow);
            if (shadowOpt.isPresent()) {
                int shadowX =  shadowOpt.get().getShadowX();
                int shadowY =  shadowOpt.get().getShadowY();
                String  shadowAlpha =  shadowOpt.get().getShadowAlpha();
                String  shadowColor =  shadowOpt.get().getShadowColor();
                AviutlExo  shadowTextExo = new AviutlExo();
                BeanUtils.copyProperties(partExoList.get(s), shadowTextExo);
                shadowTextExo.setStartX(shadowTextExo.getStartX()+ shadowX);
                shadowTextExo.setStartY(shadowTextExo.getStartY()+ shadowY);
                shadowTextExo.setEndX(shadowTextExo.getEndX()+ shadowX);
                shadowTextExo.setEndY(shadowTextExo.getEndY()+ shadowY);
//                shadowTextExo.setAlpha(shadowAlpha);
                shadowTextExo.setColor(shadowColor);
                shadowTextExo.setAulShadow(null);
                partExoList.get(s).setStartTime(partExoList.get(s).getStartTime() + 0.001);
                partExoList.add(shadowTextExo);
            }
        }

        //分开处理anm效果
        List<String> anmList = anmList();
        for(String anm:anmList){

            partExoList = addAnmEffect(partExoList,anm);
        }


        //M7额外逻辑处理
        for(AviutlExo  avi :partExoList ){
            if("文本".equals(avi.getObjName()) ){
                //是否有层级
                if(XmlEffect.isLayer()){
                    avi.setStartTime(d3(avi.getStartTime() + avi.getZ()*0.001));
                }
                //是否线性加速
                if(XmlEffect.isLinearSpeedup()){
                    avi.setLinearSpeedup(1);
                }else {
                    avi.setLinearSpeedup(0);
                }
                //是否直接加描边
                if(XmlEffect.isOutline()){
                    avi.setOutline(1);
                }
            }
        }

        //时间排序
        partExoList = partExoList.stream().sorted(Comparator.comparing(AviutlExo::getStartTime)).collect(Collectors.toList());


        return partExoList;
    }

    //3、将处理好的exoList转为m7xml
    public static String exoToXml(List<AviutlExo> exoList){
        System.out.println("******************************************");
        XmlEffect XmlEffect = new XmlEffect();
        XmlEffect.setPercentage(true);

        StringBuffer xml = new StringBuffer();

        StringBuffer headStr = new StringBuffer();
        headStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><i>\n");
        StringBuffer bodyStr = new StringBuffer();
        for(int i = 0;i<exoList.size();i++){
            AviutlExo exoBean = exoList.get(i);

            if("文本".equals(exoBean.getObjName()) ){
                // danmakuStr.append("<d p=\"5.6,7,30,ffffff,null,0,null,null\">[\"230\",\"380\",\"1-1\",\"5.4\",\"如 果 我 成 为 大 统 领\",0,0,\"230\",\"380\",\"0\",0,0,\"Microsoft YaHei\",1]</d>");

                String startX = String.valueOf(exoBean.getStartX());
                String startY = String.valueOf(exoBean.getStartY());
                String endX = String.valueOf(exoBean.getStartX());
                String endY = String.valueOf(exoBean.getEndY());
                if(XmlEffect.isPercentage()){
                    //处理大于1和小于0情况
                     startX = df3m7( (double)exoBean.getStartX()/screenWidth ) ;
                     startY = df3m7( (double)exoBean.getStartY()/screenHeight) ;
                     endX =  df3m7( (double)exoBean.getEndX()/screenWidth) ;
                     endY = df3m7( (double)exoBean.getEndY()/screenHeight) ;
                }

                StringBuffer danmakuStr = new StringBuffer();
                danmakuStr.append("<d p=\"");
                danmakuStr.append(exoBean.getStartTime()).append(",");
                danmakuStr.append("7").append(",");
                danmakuStr.append(exoBean.getSize()).append(",");
                danmakuStr.append(exoBean.getColor()).append(",");
                danmakuStr.append("null,0,null,null\">[");
                danmakuStr.append("\"").append(startX).append("\"").append(",");
                danmakuStr.append("\"").append(startY).append("\"").append(",");
                danmakuStr.append("\"").append(exoBean.getAlpha()).append("\"").append(",");
                danmakuStr.append("\"").append(exoBean.getLifeTime()).append("\"").append(",");
                danmakuStr.append("\"").append(exoBean.getText()).append("\"").append(",");
                danmakuStr.append(exoBean.getZRotation()).append(",");//Z_Rotation
                danmakuStr.append(0).append(",");//Y_Rotation
                danmakuStr.append("\"").append(endX).append("\"").append(",");
                danmakuStr.append("\"").append(endY).append("\"").append(",");
                danmakuStr.append("\"").append(exoBean.getMoveTime()).append("\"").append(",");
                danmakuStr.append("\"").append(exoBean.getDelayTime()).append("\"").append(",");//Delay_Time
                danmakuStr.append(exoBean.getOutline()).append(",");//Outline
                danmakuStr.append("\"").append(exoBean.getFont()).append("\"").append(",");
                danmakuStr.append(exoBean.getLinearSpeedup());//Linear_Speedup
                danmakuStr.append("]</d>\n");

                bodyStr.append(danmakuStr);
            }else if("图形".equals(exoBean.getObjName()) ){
                String bgGraph = "<d p=\""+ exoBean.getStartTime() +",7,127,"+exoBean.getColor()+",null,0,null,null\">[\"0\",\"0\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0\",\"0\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"SimHei\",0]</d>\n" +
                        "<d p=\""+ exoBean.getStartTime() +",7,127,"+exoBean.getColor()+",null,0,null,null\">[\"0.079\",\"0\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0.079\",\"0\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"SimHei\",0]</d>\n" +
                        "<d p=\""+ exoBean.getStartTime() +",7,127,"+exoBean.getColor()+",null,0,null,null\">[\"0\",\"0.118\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0\",\"0.118\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"SimHei\",0]</d>\n" +
                        "<d p=\""+ exoBean.getStartTime() +",7,127,"+exoBean.getColor()+",null,0,null,null\">[\"0.079\",\"0.118\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0.079\",\"0.118\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"SimHei\",0]</d>\n";
                bodyStr.append(bgGraph);
            }
        }
        StringBuffer endStr = new StringBuffer();
        endStr.append("</i>");

        xml.append(headStr);
        xml.append(bodyStr);
        xml.append(endStr);

        System.out.println(xml);
        System.out.println("******************************************");
        return  xml.toString();
    }

    //2.5、处理每段属性，返回exo对象
    public static AviutlExo mapToExo(String key ,String value ,AviutlExo exoBean){
        if("动画效果".equals(exoBean.get_name())){
            List<AulAnmEffect> currentListAnm = new ArrayList<>();
            AulAnmEffect aviAumBean = new AulAnmEffect();
            if( exoBean.getAulAnmEffectList() != null){
                int size = exoBean.getAulAnmEffectList().size();
                currentListAnm =  exoBean.getAulAnmEffectList();
                if( "动画效果".equals(value)){
                    aviAumBean = new AulAnmEffect();
                    currentListAnm.add(aviAumBean);
                    size = size + 1;
                }else {
                    aviAumBean = currentListAnm.get(size -1);
                }

                switch (key) {
                    case "partName":
                        exoBean.setPartName(value);
                        break;
                    case "_name":
                        if(exoBean.get_name()==null){
                            exoBean.setObjName(value);
                        }
                        exoBean.set_name(value);
                        currentName = value;
                        break;
                    case "track0":
                        aviAumBean.setTrack0(value);
                        break;
                    case "track1":
                        aviAumBean.setTrack1(value);
                        break;
                    case "track2":
                        aviAumBean.setTrack2(value);
                        break;
                    case "track3":
                        aviAumBean.setTrack3(value);
                        break;
                    case "param":
                        aviAumBean.setTrack3(value);
                        break;
                    case "name":
                        aviAumBean.setName(value);
                        break;
                }

                currentListAnm.set(size-1,aviAumBean);
            }else {
                aviAumBean = new AulAnmEffect();

                switch (key) {
                    case "partName":
                        exoBean.setPartName(value);
                        break;
                    case "_name":
                        if(exoBean.get_name()==null){
                            exoBean.setObjName(value);
                        }
                        exoBean.set_name(value);
                        currentName = value;
                        break;
                    case "track0":
                        aviAumBean.setTrack0(value);
                        break;
                    case "track1":
                        aviAumBean.setTrack1(value);
                        break;
                    case "track2":
                        aviAumBean.setTrack2(value);
                        break;
                    case "track3":
                        aviAumBean.setTrack3(value);
                        break;
                    case "param":
                        aviAumBean.setTrack3(value);
                        break;
                    case "name":
                        aviAumBean.setName(value);
                        break;
                }
                currentListAnm.add(aviAumBean);
            }


            exoBean.setAulAnmEffectList(currentListAnm);
        }else if("阴影".equals(exoBean.get_name())){
            AulShadow aShadow = exoBean.getAulShadow();
            if(exoBean.getAulShadow() == null){
                aShadow = new AulShadow();
            }
            switch (key) {
                case "partName":
                    exoBean.setPartName(value);
                    break;
                case "_name":
                    if(exoBean.get_name()==null){
                        exoBean.setObjName(value);
                    }
                    exoBean.set_name(value);
                    currentName = value;
                    break;
                case "X":
                    aShadow.setShadowX(Double.valueOf(value).intValue());
                    break;
                case "Y":
                    aShadow.setShadowY(Double.valueOf(value).intValue());
                    break;
                case "color":
                    aShadow.setShadowColor(value);
                    break;
                case "浓度":
                    aShadow.setShadowAlpha(doubleTrans(Double.valueOf(value)*0.01 ) + "-" + doubleTrans(Double.valueOf(value)*0.01));
                    break;
            }
            exoBean.setAulShadow(aShadow);
        } else {
            switch (key) {
                case "partName":
                    exoBean.setPartName(value);
                    break;
                case "width":
                    screenWidth = Integer.valueOf(value);
                    exoBean.setWidth(screenWidth);
                    break;
                case "height":
                    screenHeight = Integer.valueOf(value);
                    exoBean.setHeight(screenHeight);
                    break;
                case "text":
                    String text = unicodeToString(exoTextToUnicode(value));
//                    System.out.println(text);
                    if(text.length()>100){
                        text  = "(字符串长度已超100字符)";
                    }
                    //换行转义
                    if(text.contains("\n")||text.contains("\r")){
                        text = text.replaceAll("\r","");
//                        text = text.replaceAll("\n","&#x000A;");
                        text = text.replace("\n","\\n");
                    }
                    //解析aviutl文本中颜色标签 todo
                    boolean flag = text.contains("<") &&  text.contains(">");
                    if(flag){
                        String labelColor = text.substring(text.indexOf("<") + 2, text.indexOf(">"));
                        exoBean.setColor(labelColor);
                        text = text.substring(text.indexOf(">")+1);
                        System.out.println(labelColor);
                        System.out.println(text);
                    }

                    exoBean.setText(text);
                    break;
                case "X":
                    if("标准变换".equals(currentName)) {
                        if (value.contains(",")) {
                            String[] xArray = value.split(",");
                            Integer x1 = Double.valueOf(xArray[0]).intValue();
                            exoBean.setX(x1);
                            exoBean.setStartX(x1 + screenWidth / 2);
                            Integer x2 = Double.valueOf(xArray[1]).intValue();
                            exoBean.setEndX(x2 + screenWidth / 2);
                        } else {
                            Integer x1 = Double.valueOf(value).intValue();
                            exoBean.setX(x1);
                            exoBean.setStartX(x1 + screenWidth / 2);
                            exoBean.setEndX(x1 + screenWidth / 2);
                        }
                    }

                    break;
                case "Y":
                    if("标准变换".equals(currentName)) {
                        if(value.contains(",")){
                            String[] yArray = value.split(",");
                            exoBean.setY(Double.valueOf(yArray[0]).intValue());
                            exoBean.setStartY(Double.valueOf(yArray[0]).intValue() + screenHeight/2);
                            exoBean.setEndY(Double.valueOf(yArray[1]).intValue() + screenHeight/2);
                        }else {
                            exoBean.setY(Double.valueOf(value).intValue());
                            exoBean.setStartY(Double.valueOf(value).intValue() + screenHeight/2);
                            exoBean.setEndY(Double.valueOf(value).intValue() + screenHeight/2);
                        }
                    }

                    break;
                case "Z":
                    if(value.contains(",")){
                        String[] zArray = value.split(",");
                        exoBean.setZ(Double.valueOf(zArray[0]).intValue());
                    }else {
                        double txtZ = Double.valueOf(value);
                        if(txtZ<1 && txtZ>0){
                            txtZ = 1;
                        }
                        exoBean.setZ((int)txtZ);
                    }
                    break;
                case "_name":
                    if(exoBean.get_name()==null){
                        exoBean.setObjName(value);
                    }
                    exoBean.set_name(value);
                    currentName = value;
                    break;
                case "start":
                    exoBean.setStart(Integer.valueOf(value));
                    double startTime = d2((Integer.valueOf(value)-1) / 30D);
                    if(currentTXmlEffect.isAdvanceStartTime()){
                        startTime = startTime - 0.05;
                    }
                    exoBean.setStartTime(startTime);
                    break;
                case "end":
                    exoBean.setEnd(Integer.valueOf(value));
                    double endTime = d2((Integer.valueOf(value)) / 30D);
                    if(currentTXmlEffect.isDelayEndTime()){
                        endTime = endTime + 0.05;
                    }
                    exoBean.setEndTime(endTime);
                    //计算生存时间
                    double lifeTime = endTime - exoBean.getStartTime();
                    exoBean.setLifeTime(d2(lifeTime));
                    exoBean.setMoveTime((int)Math.round(lifeTime*1000));

                    break;
                case "font":
                    if("黑体".equals(value)){
//                        value = "SimHei, \"Microsoft JhengHei\"";
                        value = "SimHei";
                    }else if("宋体".equals(value)){
                        value = "SimSun";
                    }else if("微软雅黑".equals(value)){
                        value = "Microsoft YaHei";
                    }
                    exoBean.setFont(value);
                    break;
                case "layer":
                    exoBean.setLayer(Integer.valueOf(value));
                    break;
                case "color":
                    if("文本".equals(currentName)||"图形".equals(currentName)) {
                        exoBean.setColor(value);
                    }
                    break;
                case "大小":
                    if("文本".equals(currentName)){
                        exoBean.setSize(Double.valueOf(value).intValue());
                    } else if ("描边".equals(currentName)) {
                        exoBean.setOutline(1);
                    }
                    break;
                case "透明度":
                    if(value.contains(",")){
                        String[] sArray = value.split(",");
                        exoBean.setAlpha(doubleTrans(1-Double.valueOf(sArray[0])*0.01) + "-" + doubleTrans(1-Double.valueOf(sArray[1])*0.01) );
                    }else {
                        exoBean.setAlpha(doubleTrans(1-Double.valueOf(value)*0.01 ) + "-" + doubleTrans(1-Double.valueOf(value)*0.01) );
                    }
                    break;
                case "旋转":
                    int zRotate = Double.valueOf(value).intValue();
                    if(zRotate < 0) {
                        zRotate = zRotate + 360;
                    }
                    exoBean.setZRotation(zRotate);
                    break;
                case "chain":
                    exoBean.setChain(Double.valueOf(value).intValue());
                    break;
                case "浓度":
                    if("阴影".equals(currentName)){
                        AulShadow ashadow = new AulShadow();
                        ashadow.setShadowAlpha(doubleTrans(1-Double.valueOf(value)*0.01 ) + "-" + doubleTrans(1-Double.valueOf(value)*0.01));
                        exoBean.setAulShadow(ashadow);
                    }
                    break;
                default:
            }
        }
        return exoBean;

    }

    //2.6、解析exoList返回段对象
    public static List<AviutlExo>  addAnmEffect(List<AviutlExo>  exoList,String anmName) {
        List<AviutlExo> partExoList = exoList;
        List<AviutlExo> newExoList = new ArrayList<>();
        for(int i = 0; i < partExoList.size();i++){
            AviutlExo  aviExo = partExoList.get(i);
            List<AulAnmEffect> aulAnmEffectList = aviExo.getAulAnmEffectList();
            if(aulAnmEffectList != null) {
                for(int j = 0;j<aulAnmEffectList.size();j++){
                    AulAnmEffect aulAnmEffect = new AulAnmEffect();
                    aulAnmEffect = aulAnmEffectList.get(j);
                    Optional<AulAnmEffect> anmOpt = Optional.ofNullable(aulAnmEffect);
                    String  name = "";
                    if (anmOpt.isPresent()) {
                        String  track0 =  anmOpt.get().getTrack0();
                        String  track1 =  anmOpt.get().getTrack1();
                        String  track2 =  anmOpt.get().getTrack2();
                        String  track3 =  anmOpt.get().getTrack3();
                        name = anmOpt.get().getName();

                        if("增加前段时间".equals(name) && name.equals(anmName)){
                            double preTime = Double.valueOf(track0);
                            aviExo.setStartTime(d3(aviExo.getStartTime()+ preTime*0.001 ));
                            aviExo.setLifeTime(d3(aviExo.getLifeTime()- preTime*0.001 ));
                            aviExo.setMoveTime(aviExo.getMoveTime()- Double.valueOf(preTime).intValue()  );
                        }

                        if("增加后段时间".equals(name) && name.equals(anmName)){
                            double afterTime = Double.valueOf(track0);
                            aviExo.setEndTime(d3(aviExo.getEndTime()+ afterTime*0.001 ));
                            aviExo.setLifeTime(d3(aviExo.getLifeTime()+ afterTime*0.001 ));
                            aviExo.setMoveTime(aviExo.getMoveTime()+ Double.valueOf(afterTime).intValue()  );
                        }

                        if("换行转弹幕".equals(name) && name.equals(anmName)){
                            String  text = aviExo.getText();
                            String[] split = text.split("\\\\n");//分割
                            for(int a=0;a<split.length;a++){
                                int  startY = aviExo.getStartY();
                                int  endY = aviExo.getEndY();
                                int fontSize = aviExo.getSize();
                                AviutlExo newRowExo = new AviutlExo();
                                BeanUtils.copyProperties(aviExo, newRowExo);
                                newRowExo.setText(split[a]);
                                newRowExo.setStartY( startY + fontSize * a );
                                newRowExo.setEndY( endY + fontSize * a );
                                newRowExo.setAulAnmEffectList(removeAulAnmEffectList(aulAnmEffectList,anmName));
                                newExoList.add(newRowExo);
                            }
                        }


                        if("字间扩展收缩".equals(name) && name.equals(anmName)){
                            int  startX = aviExo.getStartX();
                            int  endX = aviExo.getStartX();
                            int fontSize = aviExo.getSize();
                            String  text = aviExo.getText();
                            char[] chars = text.toCharArray();
                            for (int c = 0; c < chars.length; c++) {
                                AviutlExo newcharExo = new AviutlExo();
                                BeanUtils.copyProperties(aviExo, newcharExo);
                                double  objOx =  fontSize * c;
                                double  objOx1 = objOx;
                                double  objOx2 = 0.0;
                                String[] track0array = track0.split(",");
                                double startPercentage =  Double.valueOf(track0array[0]);//扩散百分比
                                double endPercentage =  Double.valueOf(track0array[1]);
                                double centerX = -fontSize/2 + Double.valueOf(track1);

                                objOx1 = Double.valueOf(centerX + (objOx - centerX)*startPercentage/100*1).intValue();
                                objOx2 = Double.valueOf(centerX + (objOx - centerX)*endPercentage/100*1).intValue();

                                newcharExo.setStartX( Double.valueOf(startX + objOx1).intValue());
                                newcharExo.setEndX(Double.valueOf(endX + objOx2).intValue() );
                                newcharExo.setText( String.valueOf(chars[c]));
                                newcharExo.setAulAnmEffectList(null);
                                newExoList.add(newcharExo);
                                // obj.ox = obj.track1+(obj.ox-obj.track1)*obj.track0/100*s
                                // obj.ox = obj.ox + obj.track0/100
                                // obj.oy = obj.track3+(obj.oy-obj.track3)*obj.track2/100*s
                                // exoBean.setPartName(value);
                            }
                        }
                    }
                }
            }
        }

        //去除多余项
        Iterator<AviutlExo> itr = partExoList.iterator();
        while (itr.hasNext()){
            AviutlExo tmp = itr.next();
            List<AulAnmEffect> aulanmList = tmp.getAulAnmEffectList();
            if (aulanmList != null){
               for(AulAnmEffect aulanm:aulanmList){
                   Optional<AulAnmEffect> anmOpt = Optional.ofNullable(aulanm);
                   if (anmOpt.isPresent()) {
                       if("字间扩展收缩".equals(anmOpt.get().getName()) && anmOpt.get().getName().equals(anmName) ){
                           itr.remove();
                       }
                       if("换行转弹幕".equals(anmOpt.get().getName()) && anmOpt.get().getName().equals(anmName) ){
                           itr.remove();
                       }
                   }
               }
            }
        }
        partExoList.addAll(newExoList);

        return partExoList;
    }


    //utf-l6le转unicode
    public static String exoTextToUnicode(String str) {
        String unicodeStr ="";
        for(int i=0;i<str.length()/4;i++) {
            String utf16 = "\\u"+ str.substring(i*4+2,i*4+4)+ str.substring(i*4,i*4+2);
            if("\\u0000".equals(utf16)){
                break;
            }
            unicodeStr = unicodeStr + utf16;
        }
        return unicodeStr;
    }

    //unicode转中文
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    //删除ArrayList中重复元素，保持顺序
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

    //删除效果
    public static List<AulAnmEffect> removeAulAnmEffectList(List<AulAnmEffect> list,String anmName) {
        List<AulAnmEffect> newList = new ArrayList();
        newList.addAll(list);
        //去除多余项
        Iterator<AulAnmEffect> itr = newList.iterator();
        while (itr.hasNext()){
            AulAnmEffect tmp = itr.next();
            if(tmp.getName().equals(anmName)){
                itr.remove();
            }
        }
//        newList.add();
        return newList;
    }

    //输出字符串到文件
    public static void writeStringToFile(String filePath,String str) {
        try {
            File file = new File(filePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(str);// 往文件里写入字符串
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //按顺序排列anm效果列表
    public static List<String>  anmList() {
        List<String> anmList = new ArrayList<>();
        anmList.add("增加前段时间");
        anmList.add("增加后段时间");

        //一转多
        anmList.add("换行转弹幕");
        anmList.add("顺序入场");
        anmList.add("字间扩展收缩");

        return anmList;
    }

    //m7边框（16条）
    public static List<Position>  boldList(Double centerX, Double centerY, Double interval) {
        List<Position> positionList = new ArrayList<>();

        positionList.add(new Position(centerX - 2*interval,centerY - 2*interval));
        positionList.add(new Position(centerX - 2*interval,centerY - interval));
        positionList.add(new Position(centerX - 2*interval,centerY + interval));
        positionList.add(new Position(centerX - 2*interval,centerY + 2*interval));

        positionList.add(new Position(centerX - interval,centerY - 2*interval));
        positionList.add(new Position(centerX - interval,centerY - interval));
        positionList.add(new Position(centerX - interval,centerY + interval));
        positionList.add(new Position(centerX - interval,centerY + 2*interval));

        positionList.add(new Position(centerX + interval,centerY - 2*interval));
        positionList.add(new Position(centerX + interval,centerY - interval));
        positionList.add(new Position(centerX + interval,centerY + interval));
        positionList.add(new Position(centerX + interval,centerY + 2*interval));

        positionList.add(new Position(centerX + 2*interval,centerY - 2*interval));
        positionList.add(new Position(centerX + 2*interval,centerY - interval));
        positionList.add(new Position(centerX + 2*interval,centerY + interval));
        positionList.add(new Position(centerX + 2*interval,centerY + 2*interval));

        return positionList;
    }



}
