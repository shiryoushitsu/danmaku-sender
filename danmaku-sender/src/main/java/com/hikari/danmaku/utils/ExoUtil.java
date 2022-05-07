package com.hikari.danmaku.utils;

import cn.hutool.core.util.NumberUtil;
import com.hikari.danmaku.constants.Easing;
import com.hikari.danmaku.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hikari.danmaku.utils.CAUtil.parseCAJson;
import static com.hikari.danmaku.utils.CommonUtil.*;

public class ExoUtil {

    // 15寸 760 510  38.8% / 14寸 850 560 43.4% /1px约等于0.002百分比
    public static void main(String[] args) {
        String fileName = "涂黑弹幕";
        String inputExoPath = "C:\\Users\\hikari\\Desktop\\"+  fileName+ ".exo";
        String outputXmlPath = "C:\\Users\\hikari\\Desktop\\"+ fileName +".xml";

        XmlEffect XmlEffect = new XmlEffect();
        XmlEffect.setIsOutline(false);
        XmlEffect.setIsLayer(true);
        XmlEffect.setIsLinearSpeedup(false);
        XmlEffect.setIsAdvanceStartTime(false);
        XmlEffect.setIsDelayEndTime(true); //结束延迟50ms
        XmlEffect.setIsForceLine(false);//是否换行都转成新文本
        XmlEffect.setIsPercentage(false);//是否百分比
        XmlEffect.setIsColor10(false);

        XmlEffect.setIsPointCut(false); // 中间点强制分句
//        XmlEffect.setForceLine(true);//是否换行都转成新文本（m7滚动配置）
//        XmlEffect.setPercentage(false);//使用px（m7滚动配置）
//             XmlEffect;

        //1.解析exo文本返回分段List
        List<Map<String,String>> exoList = parseExoToList(inputExoPath);

        //2.将分段List优化为exo对象list
        List<AviutlExo> exoObjectList = parseExoList(exoList, XmlEffect);

        //3.将处理好的exo对象list转为m7xml
        String xmlStr = exoToXml(exoObjectList);

        //输出字符串到文件
        writeStringToFile(outputXmlPath,xmlStr);

    }

    /**
     * 1、解析exo返回分段List
     *
     * @param path
     * @return
     */
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
                                }else if (as.length == 3)  {
                                    partMap.put(as[0],as[1] + "="+ as[2]) ;
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

    /**
     * 2、解析exoList返回段对象
     *
     * @param exoList
     * @return
     */
    public static List<AviutlExo> parseExoList(List<Map<String, String>>exoList, XmlEffect xmlEffect) {
        int screenWidth = 0, screenHeight = 0;

        List<AviutlExo> partExoList =  new ArrayList<>();
        AviutlExo curExoVO = new AviutlExo();
        for(int i = 0; i < exoList.size();i++){
            Map<String,String> exoListMap = new LinkedHashMap<>();
            exoListMap = exoList.get(i);
            //不包含点的，即主段
            if(!exoListMap.get("partName").contains(".")){
                AviutlExo exoVO = new AviutlExo();
                //全局配置
                exoVO.setXmlEffect(xmlEffect);
                exoVO.setWidth(screenWidth);
                exoVO.setHeight(screenHeight);

                curExoVO = exoVO;
                partExoList.add(curExoVO);
            }
            for(Map.Entry<String, String> entry : exoListMap.entrySet()) {
                // 2.5、处理每段属性，返回exo对象 todo
                curExoVO = mapToExo(entry.getKey(),entry.getValue(),curExoVO);
            }
            if("exedit".equals(curExoVO.getPartName())){
                screenWidth = curExoVO.getWidth();
                screenHeight = curExoVO.getHeight();
            }


        }

        // 2.6、针对m7，exo对象各个字段影响修正（如换行、坐标瞄点）
        for(AviutlExo aviutlExo : partExoList){
            //垂直向上 左
            if(aviutlExo.getAlign() != null){
                if( 15 == aviutlExo.getAlign()){
                    String text = aviutlExo.getText();
                    text = CommonUtil.rowToColumn(text);
                    aviutlExo.setText(text);
                }else if( 4 == aviutlExo.getAlign()  ){//中心对齐
                    int X = aviutlExo.getX();
                    int startX = aviutlExo.getStartX();
                    int endX = aviutlExo.getEndX();
                    int Y = aviutlExo.getY();
                    int startY = aviutlExo.getStartY();
                    int endY = aviutlExo.getEndY();

                    aviutlExo.setCenterX(startX);
                    aviutlExo.setCenterY(startY);

                    int fontsize = aviutlExo.getSize();
                    int textLength = aviutlExo.getText().length();

                    int diff = Math.floorDiv(textLength * fontsize,2);
                    aviutlExo.setX(X - diff);
                    aviutlExo.setStartX(startX - diff);
                    aviutlExo.setEndX(endX - diff);

                    aviutlExo.setY(Y - Math.floorDiv(fontsize,2));
                    aviutlExo.setStartY(startY - Math.floorDiv(fontsize,2));
                    aviutlExo.setEndY(endY - Math.floorDiv(fontsize,2));
                }
            }

        }


        //只保留文本和图形文件，去除不显示的文件（把勾选去掉的文件）
        Iterator<AviutlExo> itr = partExoList.iterator();
        while (itr.hasNext()){
            AviutlExo tmp = itr.next();
            if(!("文本".equals(tmp.getObjName()) ||"图形".equals(tmp.getObjName()) ||"组控制".equals(tmp.getObjName()) ) || 1 == tmp.getDisable()){
                itr.remove();
            }
        }

        //将一个中间点、两段合并为一段，修改移动时间、总时间
        for(int k = 0;k < partExoList.size();k++){
            if(1 == partExoList.get(k).getChain() && k > 0 && xmlEffect.getIsPointCut()){
                //强制中间点分段
                AviutlExo currentExo = partExoList.get(k);
                AviutlExo lastExo = partExoList.get(k-1);
                currentExo.setText(lastExo.getText());//补齐文本
                currentExo.setFont(lastExo.getFont());//补齐字体
                currentExo.setColor(lastExo.getColor());//补齐颜色
            }else if(1 == partExoList.get(k).getChain() && k > 0 && checkAnmEffect(partExoList, k)) {
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

        // 组控制坐标处理（找出相同start，对坐标进行偏移）
        for(AviutlExo aviutlExo : partExoList){
            if("组控制".equals(aviutlExo.get_name())){
                int offsetStartX = aviutlExo.getStartX() - aviutlExo.getWidth()/2;
                int offsetEndX = aviutlExo.getEndX() - aviutlExo.getWidth()/2;
                int offsetStartY = aviutlExo.getStartY() - aviutlExo.getHeight()/2;
                int offsetEndY = aviutlExo.getEndY() - aviutlExo.getHeight()/2;
                int start = aviutlExo.getStart();
                for(AviutlExo startEqualExo: partExoList){
                    if(start == startEqualExo.getStart()){
                        startEqualExo.setStartX(startEqualExo.getStartX() + offsetStartX);
                        startEqualExo.setEndX(startEqualExo.getEndX() + offsetEndX);
                        startEqualExo.setStartY(startEqualExo.getStartY() + offsetStartY);
                        startEqualExo.setEndY(startEqualExo.getEndY() + offsetEndY);
                    }
                }
            }
        }
        Iterator<AviutlExo> itrControl = partExoList.iterator();
        while (itrControl.hasNext()){
            AviutlExo tmp = itrControl.next();
            if("组控制".equals(tmp.getObjName())){
                itrControl.remove();
            }
        }



        //阴影处理
        for(int s = 0;s < partExoList.size(); s++){
            AulShadow aulShadow =partExoList.get(s).getAulShadow();
            Optional<AulShadow> shadowOpt = Optional.ofNullable(aulShadow);
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


        //描边处理
        for(int o = 0;o < partExoList.size(); o++){
            AulOutline aulOutline = partExoList.get(o).getAulOutline();
            Optional<AulOutline> outlineOpt = Optional.ofNullable(aulOutline);
            if (outlineOpt.isPresent()) {
                int outlineSize = outlineOpt.get().getOutlineSize();
                String outlineAlpha = outlineOpt.get().getOutlineAlpha();
                String outlineColor = outlineOpt.get().getOutlineColor();

                if("000000".equals(outlineColor)){
                    partExoList.get(o).setOutline(1);//1、当描边颜色为黑时，添加高级弹幕默认描边
                    continue;
                }

                List<Position> simpleInterval = normalInterval(outlineSize);
                for(Position positionInterval : simpleInterval){
                    AviutlExo outlineTextExo = new AviutlExo();
                    BeanUtils.copyProperties(partExoList.get(o), outlineTextExo);
                    outlineTextExo.setStartX(outlineTextExo.getStartX() + (int)positionInterval.getX());
                    outlineTextExo.setEndX(outlineTextExo.getEndX() + (int)positionInterval.getX());
                    outlineTextExo.setX(outlineTextExo.getX() + (int)positionInterval.getX());

                    outlineTextExo.setStartY(outlineTextExo.getStartY() + (int)positionInterval.getY());
                    outlineTextExo.setEndY(outlineTextExo.getEndY() + (int)positionInterval.getY());
                    outlineTextExo.setY(outlineTextExo.getY() + (int)positionInterval.getY());

                    outlineTextExo.setAlpha(outlineAlpha);
                    outlineTextExo.setColor(outlineColor);
                    outlineTextExo.setAulOutline(null);
                    partExoList.add(outlineTextExo);
                }
                partExoList.get(o).setStartTime(partExoList.get(o).getStartTime() + 0.001);
            }
        }


        List<AviutlExo>  splitList =  new ArrayList<>();
        //逐帧旋转
        for(int z = 0;z < partExoList.size();z++){
            if(partExoList.get(z).getEndZRotation() != null){

                if(partExoList.get(z).getEasingZRotation() != null){
                    List<CubicBezier> cubicList = CubicBezierUtil.parseCubicByTimes(Easing.getCode(partExoList.get(z).getEasingZRotation()),xmlEffect.getZrotationTimes() -1);
                    for(int zz = 0 ; zz < xmlEffect.getZrotationTimes();zz++){
                        AviutlExo rotatePartExo = new AviutlExo();
                        BeanUtils.copyProperties(partExoList.get(z), rotatePartExo);
                        double sTime = rotatePartExo.getStartTime();
                        double sRotation = rotatePartExo.getZRotation();
                        double lifeTime = rotatePartExo.getLifeTime();
                        double pDuration = NumberUtil.div(lifeTime, (double)xmlEffect.getZrotationTimes());
                        double diffRotation = rotatePartExo.getEndZRotation() - sRotation;
                        double pRotation = NumberUtil.div(diffRotation, xmlEffect.getZrotationTimes() - 1);
                        cubicList.get(zz).getProgression();//进度百分比

                        rotatePartExo.setLifeTime(pDuration + 0.05);
                        rotatePartExo.setStartTime(sTime + zz * pDuration);
//                        rotatePartExo.setZRotation((int)Math.floor(sRotation + zz * pRotation));
                        rotatePartExo.setZRotation((int)Math.floor(sRotation + cubicList.get(zz).getProgression() * diffRotation ));
                        rotatePartExo.setEndZRotation(null);



                        int X = rotatePartExo.getX();
                        int startX = rotatePartExo.getStartX();
                        int centerX = rotatePartExo.getCenterX();
                        int endX = rotatePartExo.getEndX();
                        int Y = rotatePartExo.getY();
                        int startY = rotatePartExo.getStartY();
                        int centerY = rotatePartExo.getCenterY();
                        int endY = rotatePartExo.getEndY();
                        Position position = CoordinateUtil.rotateTransform(startX,startY,centerX,centerY,- cubicList.get(zz).getProgression() * diffRotation );
                        rotatePartExo.setStartX((int)position.getX());
                        rotatePartExo.setStartY((int)position.getY());
                        rotatePartExo.setEndX((int)position.getX());
                        rotatePartExo.setEndY((int)position.getY());

                        splitList.add(rotatePartExo);
                    }
                }
            }
        }
        partExoList.addAll(splitList);
        Iterator<AviutlExo> itrZ = partExoList.iterator();
        while (itrZ.hasNext()){
            AviutlExo tmp = itrZ.next();
            if (tmp.getEndZRotation() != null){
                itrZ.remove();
            }
        }

        // 换行强转文本

        List<AviutlExo> addList = new ArrayList<>();
        if(xmlEffect.getIsForceLine()){
            Iterator<AviutlExo> itrLine = partExoList.iterator();
            while (itrLine.hasNext()){
                AviutlExo tmp = itrLine.next();
                String tempText = tmp.getText();
                if (tempText.contains("\\n")){
                    String[] split = tempText.split("\\\\n");//分割
                    for(int ll = 0; ll < split.length ; ll++){
                        AviutlExo lineExo = new AviutlExo();
                        BeanUtils.copyProperties(tmp, lineExo);
                        lineExo.setText(split[ll]);
                        double startY = lineExo.getStartY();
                        startY = startY + ll * lineExo.getSize();// 正常
//                        startY = startY + (split.length - ll - 1) * lineExo.getSize();//字符旋转用
                        double endY = lineExo.getEndY();
                        endY = endY + ll  * lineExo.getSize();// 正常
//                        endY = endY + (split.length - ll - 1)  * lineExo.getSize();//字符旋转用
                        lineExo.setStartY((int)startY);
                        lineExo.setEndY((int)endY);
                        addList.add(lineExo);
                    }
                    tmp.setState(0);
                }

                if (tmp.getState() == 0){
                    itrLine.remove();
                }
            }
        }
        partExoList.addAll(addList);


        // 2.8 处理图形圆
        partExoList = parseCircle(partExoList);

        //2.9 分开处理anm效果 todo
        List<String> anmList = anmList();
        for(String anm : anmList){
            partExoList = addAnmEffect(partExoList, anm);
        }


//        String jsonFileName = "CA_berrygo";
//        String inputJsonPath = "C:\\Users\\hikari\\Desktop\\" + jsonFileName + ".json";
//        List<Map<String,String>> jsonList = CAUtil.parseCAJson(inputJsonPath);
//        List<AviutlExo> caAddList = new ArrayList<>();
//        //CA替换
//        Iterator<AviutlExo> itrLine = partExoList.iterator();
//        while (itrLine.hasNext()){
//            AviutlExo tmp = itrLine.next();
//            if("ca".equals(tmp.getText())){
//                for(Map<String,String> mapObject : jsonList){
//                    String color =  mapObject.get("color");
//                    String comment =  mapObject.get("comment");
//                    comment = comment.replace("\n","\\n");
//                    AviutlExo axo = new AviutlExo();
//                    BeanUtils.copyProperties(tmp, axo);
//                    axo.setColor(color);
//                    axo.setText(comment);
//                    caAddList.add(axo);
//                }
//                tmp.setState(0);
//            }
//            if (tmp.getState() == 0){
//                itrLine.remove();
//            }
//        }
//        partExoList.addAll(caAddList);


        //M7额外全局逻辑处理
        for(AviutlExo avi : partExoList ){
            if("文本".equals(avi.getObjName()) ){
                //是否有层级
                if(xmlEffect.getIsLayer()){
                    avi.setStartTime(d3(avi.getStartTime() + avi.getZ()*0.001));
                }
                //是否线性加速
                if(avi.getLinearSpeedup() == null){
                    avi.setLinearSpeedup(0);
                }
//                if(xmlEffect.getIsLinearSpeedup()){
//                    avi.setLinearSpeedup(1);
//                }else {
//                    avi.setLinearSpeedup(0);
//                }
                //是否直接加描边
                if(xmlEffect.getIsOutline()){
                    avi.setOutline(1);
                }
            }
        }

        //时间排序
        partExoList = partExoList.stream().sorted(Comparator.comparing(AviutlExo::getStartTime)).collect(Collectors.toList());

        return partExoList;
    }

    /**
     * 3、将处理好的exoList转为m7xml
     *
     * @param exoList
     * @return
     */
    public static String exoToXml(List<AviutlExo> exoList){
        System.out.println("******************************************");
        StringBuffer xml = new StringBuffer();
        StringBuffer headStr = new StringBuffer();
        headStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><i>\n");
        StringBuffer bodyStr = new StringBuffer();
        StringBuffer previewBodyStr = new StringBuffer();
        List<String> overLength = new ArrayList<>();
        for(int i = 0;i < exoList.size(); i++){
            AviutlExo exoBean = exoList.get(i);
            XmlEffect currentXmlEffect = exoBean.getXmlEffect();
            if("文本".equals(exoBean.getObjName()) ){
                String startX = String.valueOf(exoBean.getStartX());
                String startY = String.valueOf(exoBean.getStartY());
                String endX = String.valueOf(exoBean.getEndX());
                String endY = String.valueOf(exoBean.getEndY());
                if(currentXmlEffect.getIsPercentage()){
                    if(exoBean.getWidth() == null || exoBean.getHeight() == null || exoBean.getStartX() == null || exoBean.getStartY() == null || exoBean.getEndX() == null || exoBean.getEndY() == null){
                        System.out.println(exoBean);
                    }else {
                        //处理大于1和小于0情况
                        startX = df3m7( (double)exoBean.getStartX()/exoBean.getWidth());
                        startY = df3m7( (double)exoBean.getStartY()/exoBean.getHeight()) ;
                        endX =  df3m7( (double)exoBean.getEndX()/exoBean.getWidth()) ;
                        endY = df3m7( (double)exoBean.getEndY()/exoBean.getHeight()) ;
                    }
                }

                StringBuffer danmakuStr = new StringBuffer();
                StringBuffer danmakuContentStr = new StringBuffer();
                StringBuffer danmakuPreviewStr = new StringBuffer();
                danmakuStr.append("<d p=\"");
                danmakuStr.append(exoBean.getStartTime()).append(",");
                danmakuStr.append("7").append(",");
                danmakuStr.append(exoBean.getSize()).append(",");
                danmakuPreviewStr.append(danmakuStr); //预览
                danmakuPreviewStr.append(ColorUtil.hexto10(exoBean.getColor())).append(",");
                danmakuPreviewStr.append("null,0,null,null\">");
                if(currentXmlEffect.getIsColor10()){
                    exoBean.setColor(ColorUtil.hexto10(exoBean.getColor()));
                }
                System.out.println(i);
                if(exoBean.getText()!= null && (exoBean.getText().contains("<") || exoBean.getText().contains(">"))  || exoBean.getText().contains("\\")){
                    String text = exoBean.getText();
                    text = text.replace("<","&lt;");
                    text = text.replace(">","&gt;");
                    text = text.replace("\\","\\\\");
                    text = text.replace("\\\\n","\\n");
                    exoBean.setText(text);
                }

                danmakuStr.append(exoBean.getColor()).append(",");
                danmakuStr.append("null,0,null,null\">");

                danmakuContentStr.append("[");
                danmakuContentStr.append("\"").append(startX).append("\"").append(",");
                danmakuContentStr.append("\"").append(startY).append("\"").append(",");
//                danmakuStr.append(startX).append(",");
//                danmakuStr.append(startY).append(",");
                danmakuContentStr.append("\"").append(exoBean.getAlpha()).append("\"").append(",");
                danmakuContentStr.append("\"").append(exoBean.getLifeTime()).append("\"").append(",");
                danmakuContentStr.append("\"").append(exoBean.getText()).append("\"").append(",");
                danmakuContentStr.append(exoBean.getZRotation()).append(",");//Z_Rotation
                danmakuContentStr.append(exoBean.getYRotation()).append(",");//Y_Rotation
                danmakuContentStr.append("\"").append(endX).append("\"").append(",");
                danmakuContentStr.append("\"").append(endY).append("\"").append(",");
//                danmakuStr.append(endX).append(",");
//                danmakuStr.append(endY).append(",");
                danmakuContentStr.append(exoBean.getMoveTime()).append(",");
                danmakuContentStr.append(exoBean.getDelayTime()).append(",");//Delay_Time
                danmakuContentStr.append(exoBean.getOutline()).append(",");//Outline
                danmakuContentStr.append("\"").append(exoBean.getFont()).append("\"").append(",");
                danmakuContentStr.append(exoBean.getLinearSpeedup());//Linear_Speedup
                danmakuContentStr.append("]");

                danmakuStr = danmakuStr.append(danmakuContentStr);
                danmakuPreviewStr.append(danmakuContentStr).append("</d>\n");
                danmakuStr.append("</d>\n");
                bodyStr.append(danmakuStr);
                previewBodyStr.append(danmakuPreviewStr);

                int charLength = LengthUtil.getCharLength(danmakuContentStr.toString());
                if(charLength > 300){
                    String overMsg = "第" + (i + 1) + "条弹幕长度超过300，总长" + charLength +"\n" + danmakuContentStr.toString();
                    overMsg = overMsg.substring(0, overMsg.length() - 1);
                    overLength.add(overMsg);
                }
                if(exoBean.getSize() < 10 || exoBean.getSize() > 127 ){
                    overLength.add("第" + (i + 1)  + "条弹幕字体大小为" + exoBean.getSize() + "超过限制");
                }

            }else if("图形".equals(exoBean.getObjName())){
                if(0 == exoBean.getType()){
                    // 纯色
                    previewBodyStr.append("<d p=\""+ exoBean.getStartTime() +",7,127,"+ ColorUtil.hexto10(exoBean.getColor())+",null,0,null,null\">[\"0\",\"0\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0\",\"0\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"Microsoft YaHei\",0]</d>\n");
                    if(currentXmlEffect.getIsColor10()){
                        exoBean.setColor(ColorUtil.hexto10(exoBean.getColor()));
                    }
                    String bgGraph = "<d p=\""+ exoBean.getStartTime() +",7,127,"+exoBean.getColor()+",null,0,null,null\">[\"0\",\"0\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0\",\"0\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"Microsoft YaHei\",0]</d>\n";
                    bodyStr.append(bgGraph);
                }else if(1 == exoBean.getType()){
                    // 圆


                }

            }
        }
        StringBuffer endStr = new StringBuffer();
        endStr.append("</i>");

        xml.append(headStr);
        xml.append(bodyStr);
        xml.append(endStr);

        System.out.println(xml);
        System.out.println("******************************************");
        System.out.println("总共：" + exoList.size() + "条弹幕");
        System.out.println("******************************************");
        if(overLength.size() > 0){
            for(String overMsg: overLength){
                System.out.println(overMsg);
            }
            System.out.println("******************************************");
        }
        StringBuffer previewXml = new StringBuffer();
        previewXml.append(headStr).append(previewBodyStr).append(endStr);
        System.out.println("let s=`"+ previewXml.toString().replace("\n","") +"`");
        System.out.println("ldanmu=xml2danmu(s)");
        System.out.println("window.ldldanmu[window.ldldanmu.length-1].ldanmu=ldanmu");
        System.out.println("******************************************");

        return  xml.toString();
    }

    /**
     * ex、将处理好的exoList转为预览m7xml
     *
     * @param exoList
     * @return
     */
    public static String exoToPreviewXml(List<AviutlExo> exoList){
        StringBuffer headStr = new StringBuffer();
        headStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><i>\n");
        StringBuffer bodyStr = new StringBuffer();
        StringBuffer previewBodyStr = new StringBuffer();
        for(int i = 0;i < exoList.size(); i++){
            AviutlExo exoBean = exoList.get(i);
            XmlEffect currentXmlEffect = exoBean.getXmlEffect();
            if("文本".equals(exoBean.getObjName()) ){
                String startX = String.valueOf(exoBean.getStartX());
                String startY = String.valueOf(exoBean.getStartY());
                String endX = String.valueOf(exoBean.getEndX());
                String endY = String.valueOf(exoBean.getEndY());
                if(currentXmlEffect.getIsPercentage()){
                    if(exoBean.getWidth() == null || exoBean.getHeight() == null || exoBean.getStartX() == null || exoBean.getStartY() == null || exoBean.getEndX() == null || exoBean.getEndY() == null){
                        System.out.println(exoBean);
                    }else {
                        //处理大于1和小于0情况
                        startX = df3m7( (double)exoBean.getStartX()/exoBean.getWidth()) ;
                        startY = df3m7( (double)exoBean.getStartY()/exoBean.getHeight()) ;
                        endX =  df3m7( (double)exoBean.getEndX()/exoBean.getWidth()) ;
                        endY = df3m7( (double)exoBean.getEndY()/exoBean.getHeight()) ;
                    }
                }

                StringBuffer danmakuStr = new StringBuffer();
                StringBuffer danmakuContentStr = new StringBuffer();
                StringBuffer danmakuPreviewStr = new StringBuffer();
                danmakuStr.append("<d p=\"");
                danmakuStr.append(exoBean.getStartTime()).append(",");
                danmakuStr.append("7").append(",");
                danmakuStr.append(exoBean.getSize()).append(",");
                danmakuPreviewStr.append(danmakuStr); //预览
                danmakuPreviewStr.append(ColorUtil.hexto10(exoBean.getColor())).append(",");
                danmakuPreviewStr.append("null,0,null,null\">");
                if(currentXmlEffect.getIsColor10()){
                    exoBean.setColor(ColorUtil.hexto10(exoBean.getColor()));
                }

                if(exoBean.getText()!= null && (exoBean.getText().contains("<") ||exoBean.getText().contains(">"))){
                    String text = exoBean.getText();
                    text = text.replace("<","&lt;");
                    text = text.replace(">","&gt;");
                    exoBean.setText(text);
                }

                danmakuStr.append(exoBean.getColor()).append(",");
                danmakuStr.append("null,0,null,null\">");
                danmakuContentStr.append("[");
                danmakuContentStr.append("\"").append(startX).append("\"").append(",");
                danmakuContentStr.append("\"").append(startY).append("\"").append(",");
                danmakuContentStr.append("\"").append(exoBean.getAlpha()).append("\"").append(",");
                danmakuContentStr.append("\"").append(exoBean.getLifeTime()).append("\"").append(",");
                danmakuContentStr.append("\"").append(exoBean.getText()).append("\"").append(",");
                danmakuContentStr.append(exoBean.getZRotation()).append(",");//Z_Rotation
                danmakuContentStr.append(0).append(",");//Y_Rotation
                danmakuContentStr.append("\"").append(endX).append("\"").append(",");
                danmakuContentStr.append("\"").append(endY).append("\"").append(",");
                danmakuContentStr.append(exoBean.getMoveTime()).append(",");
                danmakuContentStr.append(exoBean.getDelayTime()).append(",");//Delay_Time
                danmakuContentStr.append(exoBean.getOutline()).append(",");//Outline
                danmakuContentStr.append("\"").append(exoBean.getFont()).append("\"").append(",");
                danmakuContentStr.append(exoBean.getLinearSpeedup());//Linear_Speedup
                danmakuContentStr.append("]");

                danmakuStr = danmakuStr.append(danmakuContentStr);
                danmakuPreviewStr.append(danmakuContentStr).append("</d>\n");
                danmakuStr.append("</d>\n");
                bodyStr.append(danmakuStr);
                previewBodyStr.append(danmakuPreviewStr);

            }else if("图形".equals(exoBean.getObjName())  && 0 == exoBean.getType()){
                previewBodyStr.append("<d p=\""+ exoBean.getStartTime() +",7,127,"+ ColorUtil.hexto10(exoBean.getColor())+",null,0,null,null\">[\"0\",\"0\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0\",\"0\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"Microsoft YaHei\",0]</d>\n");
                if(currentXmlEffect.getIsColor10()){
                    exoBean.setColor(ColorUtil.hexto10(exoBean.getColor()));
                }
                String bgGraph = "<d p=\""+ exoBean.getStartTime() +",7,127,"+exoBean.getColor()+",null,0,null,null\">[\"0\",\"0\",\"1-1\",\""+exoBean.getLifeTime()+"\",\"███████████\\n███████████\\n███████████\\n███████████\\n███████████\",0,0,\"0\",\"0\",\""+exoBean.getMoveTime()+"\",\"0\",0,\"Microsoft YaHei\",0]</d>\n";
                bodyStr.append(bgGraph);
            }
        }
        StringBuffer endStr = new StringBuffer();
        endStr.append("</i>");

        StringBuffer previewXml = new StringBuffer();
        previewXml.append(headStr).append(previewBodyStr).append(endStr);
        return  previewXml.toString();
    }


    /**
     * 2.5、处理每段属性，返回exo对象
     *
     * @param key
     * @param value
     * @param exoBean
     * @return
     */
    public static AviutlExo mapToExo(String key ,String value , AviutlExo exoBean){
        XmlEffect xmlEffect = exoBean.getXmlEffect();
        if("动画效果".equals(exoBean.get_name())){
            List<AulAnmEffect> currentListAnm = new ArrayList<>();
            AulAnmEffect aviAumBean = new AulAnmEffect();
            if( exoBean.getAulAnmEffectList() != null){
                int size = exoBean.getAulAnmEffectList().size();
                currentListAnm =  exoBean.getAulAnmEffectList();
                if("动画效果".equals(value)){
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
                        exoBean.setCurrentName(value);
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
                        aviAumBean.setParam(value);
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
                        exoBean.setCurrentName(value);
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
                        aviAumBean.setParam(value);
                        break;
                    case "name":
                        aviAumBean.setName(value);
                        break;
                }
                currentListAnm.add(aviAumBean);
            }

            exoBean.setAulAnmEffectList(currentListAnm);
        }
        else if("阴影".equals(exoBean.get_name())){
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
                    exoBean.setCurrentName(value);
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
        }
        else if("描边".equals(exoBean.get_name())){
            AulOutline aOutline = exoBean.getAulOutline();
            if(exoBean.getAulOutline() == null){
                aOutline = new AulOutline();
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
                    exoBean.setCurrentName(value);
                    break;
                case "大小":
                    aOutline.setOutlineSize(Integer.valueOf(value));
                    break;
                case "模糊":
                    aOutline.setOutlineAlpha(doubleTrans(1 - Double.valueOf(value)*0.01 ) + "-" + doubleTrans(1 - Double.valueOf(value)*0.01));
                    break;
                case "color":
                    aOutline.setOutlineColor(value);
                    break;
            }
            exoBean.setAulOutline(aOutline);
        }
        else {
            switch (key) {
                case "partName":
                    exoBean.setPartName(value);
                    break;
                case "width":
                    exoBean.setWidth(Integer.valueOf(value));
                    break;
                case "height":
                    exoBean.setHeight(Integer.valueOf(value));
                    break;
                case "text":
                    String text = unicodeToString(exoTextToUnicode(value));

//                    if(text.length()>100){
//                        text  = "(字符串长度已超100字符)";
//                    }
                    //换行转义
                    if(text.contains("\n")||text.contains("\r")){
                        text = text.replaceAll("\r","");
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

                    exoBean.setTextLength(text.length());//原始文本长度
                    exoBean.setText(text);
                    break;
                case "X":
                    if("标准变换".equals(exoBean.getCurrentName()) || "拓展变换".equals(exoBean.getCurrentName()) || "组控制".equals(exoBean.getCurrentName())) {
                        if (value.contains(",")) {
                            String[] xArray = value.split(",");
                            Integer x1 = Double.valueOf(xArray[0]).intValue();
                            exoBean.setX(x1);
                            exoBean.setStartX(x1 + exoBean.getWidth() / 2);
                            Integer x2 = Double.valueOf(xArray[1]).intValue();
                            exoBean.setEndX(x2 + exoBean.getWidth() / 2);

                            if(xArray.length > 3){
                                String access = xArray[3];
                                //b站默认easein
                                if("2".equals(access)){
                                    exoBean.setLinearSpeedup(1);
                                }else if("3".equals(access)) {
                                    exoBean.setLinearSpeedup(0);
                                }else {

                                }
                            }
                        } else {
                            Integer x1 = Double.valueOf(value).intValue();

                            exoBean.setX(x1);
                            exoBean.setStartX(x1 + exoBean.getWidth() / 2);
                            exoBean.setEndX(x1 + exoBean.getWidth() / 2);
                        }
                    }

                    break;
                case "Y":
                    if("标准变换".equals(exoBean.getCurrentName()) || "拓展变换".equals(exoBean.getCurrentName()) || "组控制".equals(exoBean.getCurrentName())) {
                        if(value.contains(",")){
                            String[] yArray = value.split(",");
                            exoBean.setY(Double.valueOf(yArray[0]).intValue());
                            exoBean.setStartY(Double.valueOf(yArray[0]).intValue() + exoBean.getHeight()/2);
                            exoBean.setEndY(Double.valueOf(yArray[1]).intValue() + exoBean.getHeight()/2);
                        }else {
                            exoBean.setY(Double.valueOf(value).intValue());
                            exoBean.setStartY(Double.valueOf(value).intValue() + exoBean.getHeight()/2);
                            exoBean.setEndY(Double.valueOf(value).intValue() + exoBean.getHeight()/2);
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
                    exoBean.setCurrentName(value);
                    break;
                case "_disable":
                    exoBean.setDisable(Integer.valueOf(value));
                    break;
                case "start":
                    exoBean.setStart(Integer.valueOf(value));
                    double startTime = d2((Integer.valueOf(value)-1) / 30D);
                    if(xmlEffect != null){
                        if( xmlEffect.getIsAdvanceStartTime() ){
                         startTime = startTime - 0.05;
                        }
                    }
                    exoBean.setStartTime(startTime);
                    break;
                case "end":
                    exoBean.setEnd(Integer.valueOf(value));
                    double endTime = d2((Integer.valueOf(value)) / 30D);
                    if(xmlEffect != null){
                        //【针对pc端闪频问题】生存时间小于50ms转为60ms
                        if( (endTime - exoBean.getStartTime() )<= 0.05){
                            endTime = exoBean.getStartTime() + 0.06;
                        }

                        //【针对pc端闪频问题】生存时间100ms以上才补正
                        // || (endTime - exoBean.getStartTime()) > 0.1
                        if(xmlEffect.getIsDelayEndTime() ){
                            endTime = endTime + 0.05;
                        }
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
                    if("文本".equals(exoBean.getCurrentName())||"图形".equals(exoBean.getCurrentName())) {
                        exoBean.setColor(value);
                    }
                    break;
                case "大小":
                    if("文本".equals(exoBean.getCurrentName())||"图形".equals(exoBean.getCurrentName())){
                        exoBean.setSize(Double.valueOf(value).intValue());
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
                    if (value.contains(",")) {
                        String[] ZRotateArray = value.split(",");
                        Integer startRotate = Double.valueOf(ZRotateArray[0]).intValue();
                        Integer endRotate = Double.valueOf(ZRotateArray[1]).intValue();
                        if(ZRotateArray.length > 3){
                            String access = ZRotateArray[3];
                            exoBean.setEasingZRotation(Integer.valueOf(access));
                        }
                        exoBean.setZRotation(startRotate);
                        exoBean.setEndZRotation(endRotate);
                    } else {
                        int zRotate = Double.valueOf(value).intValue();
                        if(zRotate < 0) {
                            zRotate = zRotate + 360;
                        }
                        exoBean.setZRotation(zRotate);
                    }
                    break;
                case "Z轴旋转":
                    if (value.contains(",")) {
                        String[] ZRotateArray = value.split(",");
                        Integer startRotate = Double.valueOf(ZRotateArray[0]).intValue();
                        Integer endRotate = Double.valueOf(ZRotateArray[1]).intValue();
                        if(ZRotateArray.length > 3){
                            String access = ZRotateArray[3];
                            exoBean.setEasingZRotation(Integer.valueOf(access));
                        }
                        exoBean.setZRotation(startRotate);
                        exoBean.setEndZRotation(endRotate);
                    } else {
                        int zRotate = Double.valueOf(value).intValue();
                        if(zRotate < 0) {
                            zRotate = zRotate + 360;
                        }
                        exoBean.setZRotation(zRotate);
                    }
                    break;
                case "Y轴旋转":
                    int yRotate = Double.valueOf(value).intValue();
                    if(yRotate < 0) {
                        yRotate = yRotate + 360;
                    }
                    exoBean.setYRotation(yRotate);
                    break;
                case "type":
                    exoBean.setType(Integer.valueOf(value));
                    break;
                case "chain":
                    exoBean.setChain(Double.valueOf(value).intValue());
                    break;
                case "浓度":
                    if("阴影".equals(exoBean.getCurrentName())){
                        AulShadow ashadow = new AulShadow();
                        ashadow.setShadowAlpha(doubleTrans(1-Double.valueOf(value)*0.01 ) + "-" + doubleTrans(1-Double.valueOf(value)*0.01));
                        exoBean.setAulShadow(ashadow);
                    }
                    break;
                case "align":
                    exoBean.setAlign(Integer.valueOf(value));
                    break;
                default:
            }
        }
        return exoBean;

    }

    /**
     * 2.9、解析exoList返回段对象
     *
     * @param exoList
     * @param anmName
     * @return
     */
    public static List<AviutlExo>  addAnmEffect(List<AviutlExo>  exoList, String anmName) {
        List<AviutlExo> partExoList = exoList;
        List<AviutlExo> newExoList = new ArrayList<>();
        for(int i = 0; i < partExoList.size();i++){
            AviutlExo  aviExo = partExoList.get(i);
            List<AulAnmEffect> aulAnmEffectList = aviExo.getAulAnmEffectList();
            if(aulAnmEffectList != null) {
                for(int j = 0;j < aulAnmEffectList.size();j++){
                    AulAnmEffect aulAnmEffect = new AulAnmEffect();
                    aulAnmEffect = aulAnmEffectList.get(j);
                    Optional<AulAnmEffect> anmOpt = Optional.ofNullable(aulAnmEffect);
                    String  name = "";
                    if (anmOpt.isPresent()) {
                        String  track0 =  anmOpt.get().getTrack0();
                        String  track1 =  anmOpt.get().getTrack1();
                        String  track2 =  anmOpt.get().getTrack2();
                        String  track3 =  anmOpt.get().getTrack3();
                        String  param =  anmOpt.get().getParam();
                        name = anmOpt.get().getName();

                        if("分段移动".equals(name) && name.equals(anmName)){
                            System.out.println("分段移动");
                            //如果找到个路径相随的，就 todo


                        }


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


                        if("打字".equals(name) && name.equals(anmName)){
                            int startX = aviExo.getStartX();
                            int endX = aviExo.getStartX();
                            int fontSize = aviExo.getSize();
                            String text = aviExo.getText();
                            char[] chars = text.toCharArray();
                            for (int c = 0; c < chars.length; c++) {
                                AviutlExo newcharExo = new AviutlExo();
                                BeanUtils.copyProperties(aviExo, newcharExo);
                                double v = 1/Double.valueOf(track0); // 单位：秒/文字
                                newcharExo.setText(CommonUtil.addBlankSpace(String.valueOf(chars[c]),c));//加空格
                                newcharExo.setStartTime( newcharExo.getStartTime() + v * c);
                                newcharExo.setLifeTime(d3(newcharExo.getLifeTime() - v * c ));
                                newcharExo.setMoveTime(newcharExo.getMoveTime() - (int)(v * c * 1000));

                                newcharExo.setAulAnmEffectList(null);
                                newExoList.add(newcharExo);
                            }
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

                                if(track0array.length > 3){
                                    String access = track0array[3];
                                    //b站默认easein
                                    if("2".equals(access)){
                                        newcharExo.setLinearSpeedup(1);
                                    }else if("3".equals(access)) {
                                        newcharExo.setLinearSpeedup(0);
                                    }else {
                                        newcharExo.setLinearSpeedup(0);
                                    }
                                }

                                newcharExo.setAulAnmEffectList(null);
                                newExoList.add(newcharExo);
                                // obj.ox = obj.track1+(obj.ox-obj.track1)*obj.track0/100*s
                                // obj.ox = obj.ox + obj.track0/100
                                // obj.oy = obj.track3+(obj.oy-obj.track3)*obj.track2/100*s
                                // exoBean.setPartName(value);
                            }
                        }

                        if("文字背景".equals(name) && name.equals(anmName)){
                            String text = aviExo.getText();
                            String block = "";
                            for(int t = 0; t < aviExo.getTextLength(); t++){
                                block = block + "█";
                            }
                            if(aviExo.getAlign() != null){
                                if( 15 == aviExo.getAlign()){
                                    block = CommonUtil.rowToColumn(block);
                                }
                            }

                            AviutlExo newTextBGExo = new AviutlExo();
                            BeanUtils.copyProperties(aviExo, newTextBGExo);
                            String blockAlpha = doubleTrans(Double.valueOf(track1)*0.01 ) + "-" + doubleTrans(Double.valueOf(track1)*0.01);
                            String blockColor =ColorUtil.cutColor(param.split("=")[1]);
//
                            newTextBGExo.setText(block); // 文字
                            newTextBGExo.setAlpha(blockAlpha); // 透明度
                            newTextBGExo.setColor(blockColor); // 颜色

                            newTextBGExo.setOutline(0); // 描边
                            newTextBGExo.setFont("SimHei"); // 文字

                            aviExo.setStartTime(NumberUtil.add(aviExo.getStartTime(),Double.valueOf(0.001)));
                            aviExo.setLifeTime(NumberUtil.sub(aviExo.getLifeTime(),Double.valueOf(0.001)));
                            newExoList.add(newTextBGExo);
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
                       if("打字".equals(anmOpt.get().getName()) && anmOpt.get().getName().equals(anmName) ){
                           itr.remove();
                       }
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

    //按顺序排列anm效果列表【添加动画效果前先在这里添加】
    public static List<String> anmList() {
        List<String> anmList = new ArrayList<>();
        anmList.add("增加前段时间");
        anmList.add("增加后段时间");
        anmList.add("分段移动");

        //一转多
        anmList.add("打字");
        anmList.add("换行转弹幕");
        anmList.add("顺序入场");
        anmList.add("字间扩展收缩");

        anmList.add("文字背景");

        return anmList;
    }

    //m7边框（16条）
    public static List<Position> boldList(Double centerX, Double centerY, Double interval) {
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

    //m7边框（4条）
    public static List<Position> simpleBoldList(Integer centerX, Integer centerY, Integer interval) {
        List<Position> positionList = new ArrayList<>();
        positionList.add(new Position(centerX - interval,centerY - interval));
        positionList.add(new Position(centerX - interval,centerY + interval));
        positionList.add(new Position(centerX + interval,centerY + interval));
        positionList.add(new Position(centerX + interval,centerY - interval));
        return positionList;
    }

    //m7边框偏差值（4条）
    public static List<Position> simpleInterval(Integer interval) {
        List<Position> positionList = new ArrayList<>();
        positionList.add(new Position( - interval, - interval));
        positionList.add(new Position( - interval, + interval));
        positionList.add(new Position( + interval, + interval));
        positionList.add(new Position( + interval, - interval));

        return positionList;
    }

    //m7边框偏差值（8条）
    public static List<Position> normalInterval(Integer interval) {
        List<Position> positionList = new ArrayList<>();

        positionList.add(new Position( 0, - interval));
        positionList.add(new Position( 0, + interval));
        positionList.add(new Position( + interval,0));
        positionList.add(new Position( - interval,0));

        positionList.add(new Position( - interval, - interval));
        positionList.add(new Position( - interval, + interval));
        positionList.add(new Position( + interval, + interval));
        positionList.add(new Position( + interval, - interval));

        return positionList;
    }




    public static boolean checkAnmEffect(List<AviutlExo> partExoList, int k) {
            AviutlExo aviExo = partExoList.get(k);
            AviutlExo lastAviExo = partExoList.get(k-1);
            boolean flag = true;
            List<AulAnmEffect> aulAnmEffectList = aviExo.getAulAnmEffectList();
            if(aulAnmEffectList != null) {
                for(int j = 0; j < aulAnmEffectList.size(); j++){
                    AulAnmEffect aulAnmEffect = new AulAnmEffect();
                    aulAnmEffect = aulAnmEffectList.get(j);
                    Optional<AulAnmEffect> anmOpt = Optional.ofNullable(aulAnmEffect);
                    String name = "";
                    if (anmOpt.isPresent()) {
                        String track0 = anmOpt.get().getTrack0();
                        String track1 = anmOpt.get().getTrack1();
                        String track2 = anmOpt.get().getTrack2();
                        String track3 = anmOpt.get().getTrack3();
                        String param = anmOpt.get().getParam();
                        name = lastAviExo.getAulAnmEffectList().get(j).getName();//链式没有效果名，取链头的效果名称

                        if("分段移动".equals(name)){
                            System.out.println("分段移动");
                            flag = false;
                            aulAnmEffect.setName(name);
                            // 单纯分段
                            aviExo.setText(lastAviExo.getText());//补齐文本
                            aviExo.setFont(lastAviExo.getFont());//补齐字体
                            aviExo.setColor(lastAviExo.getColor());//补齐颜色
                        }

                    }
                }
            }
        return flag;
    }

    // 圆转换
    public static  List<AviutlExo> parseCircle(List<AviutlExo> partExoList) {
        List<AviutlExo> newExoList = new ArrayList<>();
        for(AviutlExo exo :partExoList){
            if("图形".equals(exo.getObjName()) && 1 == exo.getType()){
                for(int i = 0; i < 36 ;i++){
                    AviutlExo newTextBGExo = new AviutlExo();
                    BeanUtils.copyProperties(exo,newTextBGExo);
                    newTextBGExo.setObjName("文本");
                    newTextBGExo.setZRotation(i * 10);
                    newTextBGExo.setFont("SimHei");
                    newTextBGExo.setText("　　　　　｜");
                    newTextBGExo.setSize((int)Math.floor(newTextBGExo.getSize()/10.8));
                    newExoList.add(newTextBGExo);
                }
                exo.setState(0);
            }
        }
        partExoList.addAll(newExoList);

        Iterator<AviutlExo> itrControl = partExoList.iterator();
        while (itrControl.hasNext()){
            AviutlExo tmp = itrControl.next();
            if(0 == tmp.getState()){
                itrControl.remove();
            }
        }
        return partExoList;
    }



}
