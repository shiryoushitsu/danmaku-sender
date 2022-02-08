package com.hikari.danmaku.utils;


import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.hikari.danmaku.entity.FontFamily;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.hikari.danmaku.utils.ColorUtil.convertRGBToHex;


public class AsciiUtil {
    public static void main(String []args) {
        checkEnvironmentFont();
        //正文字
//        List<List<String>> arrayList = txtToList("D:\\补全空格.txt");
//        printTxtList(restoreTxtList(completedBlankSpace(arrayList)));
//        List<List<String>> arrayList = txtToList("D:\\正LOVE.txt");

//        List<List<String>> arrayList = txtToList("D:\\正bilibili干杯.txt");
//        List<List<String>> revolveList = revolveList(arrayList);
//        List<List<String>> blockReverse = blockReverse(revolveList);
//
//
//        for(List<String>  lineStr : revolveList){
//            String line = "";
//            for(String str : lineStr){
//                line = line + str;
//            }
//            System.out.println(line);
//        }
//
//        for(List<String>  lineStr : blockReverse){
//            String line = "";
//            for(String str : lineStr){
//                line = line + str;
//
//            }
//            System.out.println(line);
//        }


//        checkEnvironmentFont();
//        FontFamily fontFamily = new FontFamily();
//        getFontAscii("梦想", fontFamily);
//      String path="D:\\kbw.png";
//      getBlackWhite(path);
    }

    /**
     * 检查系统可用字体
     */
    public static void checkEnvironmentFont(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        for (String s : fontFamilies) {
            System.out.println(s);
        }
    }

    /**
     * 获取字的字符字
     */
    public static String getFontAscii(String text, FontFamily fontFamily){
        String content = text;
        BufferedImage image = new BufferedImage(content.length() * fontFamily.getFontWidth(), fontFamily.getFontHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setFont(new Font(fontFamily.getFont(), Font.PLAIN, fontFamily.getFontSize()));
        g.drawString(content, 0, image.getHeight() - 2);
        int [] p = image.getRGB(0, 0, image.getWidth(), image.getHeight(), new int[image.getWidth()*image.getHeight()], 0, image.getWidth());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                String str = p[i*image.getWidth() + j] == -1 ? "█" : "　" + (j == image.getWidth()-1 ? "\n" : "");
                System.out.print(str);
                stringBuffer.append(str);
//                System.out.print(p[i*image.getWidth() + j] == -1 ? "■" : "□" + (j == image.getWidth()-1 ? "\n" : ""));

            }
        }
        return stringBuffer.toString();
    }


    public static void getBlackWhite(String image) {
        int[] rgb = new int[3];
        File file = new File(image);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minX = bi.getMinX();
        int minY = bi.getMinY();
        List<String> contentList = new ArrayList<>();
        String str ="";
        int point;
        for(int y = minY; y < height; y++) {
            String  charStr ="";
            for(int x = minX; x < width; x++) {
                //获取包含这个像素的颜色信息的值, int型
                int pixel = bi.getRGB(x, y);
                //从pixel中获取rgb的值
                rgb[0] = (pixel & 0xff0000) >> 16; //r
                rgb[1] = (pixel & 0xff00) >> 8; //g
                rgb[2] = (pixel & 0xff); //b
//                System.out.print("("+rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
                point=bi.getRGB(x, y)&0x0000ff;
//                getLogger().info(point+"");
//                for (int j = 0; j < image.getWidth(); j++) {
                    String oneChar ="";
                if("ffffff".equals(convertRGBToHex(rgb[0],rgb[1],rgb[2]))||"FFFFFF".equals(convertRGBToHex(rgb[0],rgb[1],rgb[2]))){
                    oneChar ="　";
                }
                else{
                    oneChar = "█";
                }
//                    if(point>=127){
//                        oneChar ="　";
//                    }
//                    else{
//                        oneChar = "█";
//                    }
                    charStr = charStr + oneChar;

//                System.out.print(p[i*image.getWidth() + j] == -1 ? "█" : "　" + (j == image.getWidth()-1 ? "\n" : ""));
//                }
//                System.out.print("16进制颜色："+convertRGBToHex(rgb[0],rgb[1],rgb[2]) );
//                convertRGBToHex(rgb[0],rgb[1],rgb[2]);

            }
            contentList.add(charStr);
            System.out.println(charStr);
        }

    }


    /**
     * 去除四周空格
     */
    public static List<List<String>> removeAroundSpace(List<List<String>> txtList){
        // 去除上下空格
        txtList = restoreTxtList(txtList);
        // 补全右边空格
        txtList = completedBlankSpace(txtList);
        // 二维行列替换
        txtList = exchangeList(txtList);
        // 去除上下空格
        txtList = restoreTxtList(txtList);
        // 二维行列替换
        txtList = exchangeList(txtList);
        return txtList;
    };


    /**
     * 补全右边空格（字符字用）
     */
    public static List<List<String>> completedBlankSpace(List<List<String>> txtList) {
        List<List<String>> arrayList = txtList;

        // 查询最长
        int maxLength = 0;
        for (List<String> linesList : arrayList){
            int currentLength = linesList.size();
            if(maxLength < currentLength){
                maxLength = currentLength;
            }
        }

        //补齐全角空格
        for (List<String> linesList : arrayList){
            int currentLength =  linesList.size();
            if(maxLength > currentLength){
                for(int i = 0 ;i<maxLength - currentLength; i++){
                    linesList.add("　");
                }
            }
        }

        return arrayList;
    }

    /**
     * 去除右边空格（pc端和手机端空格差异底部和顶部弹幕才有，m7没此问题） todo
     */
    public static List<List<String>> removeBlankSpace(List<List<String>> txtList) {
        List<List<String>> arrayList = txtList;

//        // 查询最长
//        int maxLength = 0;
//        for (List<String> linesList : arrayList){
//            int currentLength = linesList.size();
//            if(maxLength < currentLength){
//                maxLength = currentLength;
//            }
//        }
//
//        //补齐全角空格
//        for (List<String> linesList : arrayList){
//            int currentLength = linesList.size();
//            if(maxLength > currentLength){
//                for(int i = 0 ;i<maxLength - currentLength; i++){
//                    linesList.add("　");
//                }
//            }
//        }

        return arrayList;
    }


    /**
     * 去除上下空格（字符字用）
     */
    public static List<List<String>> restoreTxtList(List<List<String>> txtList)  {
        List<List<String>> arrayList = txtList;
        // 1. 从上直到非空格行，直接删除行
        List<List<String>> restoreList = new ArrayList<>();
        boolean topFlag = false;
        for (List<String> linesList : arrayList){
            if(topFlag){
                restoreList.add(linesList);
            }else {
                for (String charStr : linesList){
                    if(!" ".equals(charStr) && !"　".equals(charStr) && !"　".equals(charStr)){
                        topFlag = true;
                        restoreList.add(linesList);
                        break;
                    }
                }
            }
        }

        // 2. 从下直到非空格行，直接删除行
        List<List<String>> restoreBottomList = new ArrayList<>();
        boolean bottomFlag = false;
        for (int i = 0;i< restoreList.size();i++){
            if(bottomFlag){
                restoreBottomList.add(restoreList.get(restoreList.size()  - i - 1));
            }else {
                List<String> linesList = restoreList.get(restoreList.size()  - i - 1);
                for (String charStr : linesList){
                    if(!" ".equals(charStr) && !"　".equals(charStr)){
                        bottomFlag = true;
                        restoreList.add(linesList);
                        break;
                    }
                }
            }
        }
        Collections.reverse(restoreBottomList);
        return restoreBottomList;
    }


    /**
     * 控制台打印txt二维数组
     */
    public static void printTxtList(List<List<String>> txtList) {
        for(List<String>  lineStr : txtList){
            String line = "";
            for(String str : lineStr){
                line = line + str;
            }
            System.out.println(line);
        }

//        return arrayList;
    }


    /**
     * 解析txt返回二维数组
     */
    public static  List<List<String>> txtToList(String path)  {
        List<List<String>> arrayList = new ArrayList<>();
        FileReader fileReader = new FileReader(path);
        List<String> linesList =  fileReader.readLines();
        for (String line : linesList){
            List<String> stringList = new ArrayList<>();
            for(int i = 0 ;i < line.length();i++){
                stringList.add(String.valueOf(line.charAt(i)));
            }
            arrayList.add(stringList);
        }
        return arrayList;
    }

    /**
     * 换行String返回二维数组
     */
    public static List<List<String>> lnToList(String lnStr)  {
        List<List<String>> arrayList = new ArrayList<>();
        String[] split = lnStr.split("\\n");
        List<String> linesList =  Arrays.asList(split);
        for (String line : linesList){
            List<String> stringList = new ArrayList<>();
            for(int i = 0 ;i < line.length();i++){
                stringList.add(String.valueOf(line.charAt(i)));
            }
            arrayList.add(stringList);
        }
        return arrayList;
    }

    /**
     * 二维数组转换行String
     */
    public static String listToln(List<List<String>> stringList)  {
        String lnString = "";
        for(List<String>  lineStr : stringList){
            String line = "";
            for(String str : lineStr){
                line = line + str;
            }
            lnString = lnString + line + "\n";
        }
        return lnString;
    }



    /**
     * 二维组180度翻转
     */
    public static  List<List<String>> revolveList(List<List<String>> txtList)  {
        List<List<String>> arrayList = new ArrayList<>();
        for(int i = 0 ;i < txtList.size();i++){
            List<String> newList = new ArrayList<>();
            List<String> lineList = txtList.get(txtList.size() - i - 1);
            for(int j = 0 ;j < lineList.size();j++){
                newList.add(lineList.get(lineList.size() - j - 1));
            }
            arrayList.add(newList);
        }
        return arrayList;
    }

    /**
     * 空格与黑色块互转
     */
    public static List<List<String>> blockReverse(List<List<String>> txtList)  {
        List<List<String>> arrayList = new ArrayList<>();
        for(int i = 0 ;i < txtList.size(); i++){
            List<String> newList = new ArrayList<>();
            List<String> lineList = txtList.get(i);
            for(int j = 0 ;j < lineList.size();j++){
                String textChar = lineList.get(j);
                if("　".equals(textChar)){
                    textChar = "█";
                }else if("█".equals(textChar)){
                    textChar = "　";
                }
                newList.add(textChar);
            }
            arrayList.add(newList);
        }
        return arrayList;
    }

    /**
     * 二维行列替换
     */
    public static  List<List<String>> exchangeList(List<List<String>> txtList)  {
        int length = txtList.get(0).size();//长
        int width = txtList.size();//宽
        List<List<String>>  exchangeArrayList  = new ArrayList<>();
        for(int i = 0;i<length;i++){
            List<String>  subExchangeArrayList  = new ArrayList<>();
            for(int j = 0;j<width;j++){
                String y = txtList.get(j).get(i).toString();
                subExchangeArrayList.add(y);
            }
            exchangeArrayList.add(subExchangeArrayList);
        }
        return exchangeArrayList;
    }
}