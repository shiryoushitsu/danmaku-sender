package com.hikari.danmaku.utils;


import com.hikari.danmaku.entity.FontFamily;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hikari.danmaku.utils.ColorUtil.convertRGBToHex;


public class AsciiUtil {
    public static void main(String []args) {
        checkEnvironmentFont();
        FontFamily fontFamily = new FontFamily();
        getFontAscii("梦想", fontFamily);
//      String path="D:\\kbw.png";
//      getBlackWhite(path);
    }

    //检查系统可用字体
    public static void checkEnvironmentFont(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        for (String s : fontFamilies) {
            System.out.println(s);
        }
    }

    //检查系统可用字体
    public static String getFontAscii(String text, FontFamily fontFamily){
        String content = text;
        BufferedImage image = new BufferedImage(content.length()*16, 16, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setFont(new Font("方正像素16", Font.PLAIN, 16));
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

}