package com.hikari.danmaku.utils;

import java.awt.*;

/**
 * create by AwaiHikari on 2020-07-01 20:50
 */

public class ColorUtil {

    public static void main(String[] argv) throws Exception{
        String color =hsv2rgb(80,1,1);
        System.out.println("16进制颜色："+ color);
    }

    //rgb转16进制颜色代码
    public static  String convertRGBToHex(int r, int g, int b) {
        String rFString, rSString, gFString, gSString, bFString, bSString, result;
        int red, green, blue;
        int rred, rgreen, rblue;
        red = r / 16;
        rred = r % 16;
        if (red == 10) rFString = "A";
        else if (red == 11) rFString = "B";
        else if (red == 12) rFString = "C";
        else if (red == 13) rFString = "D";
        else if (red == 14) rFString = "E";
        else if (red == 15) rFString = "F";
        else rFString = String.valueOf(red);

        if (rred == 10) rSString = "A";
        else if (rred == 11) rSString = "B";
        else if (rred == 12) rSString = "C";
        else if (rred == 13) rSString = "D";
        else if (rred == 14) rSString = "E";
        else if (rred == 15) rSString = "F";
        else rSString = String.valueOf(rred);

        rFString = rFString + rSString;

        green = g / 16;
        rgreen = g % 16;

        if (green == 10) gFString = "A";
        else if (green == 11) gFString = "B";
        else if (green == 12) gFString = "C";
        else if (green == 13) gFString = "D";
        else if (green == 14) gFString = "E";
        else if (green == 15) gFString = "F";
        else gFString = String.valueOf(green);

        if (rgreen == 10) gSString = "A";
        else if (rgreen == 11) gSString = "B";
        else if (rgreen == 12) gSString = "C";
        else if (rgreen == 13) gSString = "D";
        else if (rgreen == 14) gSString = "E";
        else if (rgreen == 15) gSString = "F";
        else gSString = String.valueOf(rgreen);

        gFString = gFString + gSString;

        blue = b / 16;
        rblue = b % 16;

        if (blue == 10) bFString = "A";
        else if (blue == 11) bFString = "B";
        else if (blue == 12) bFString = "C";
        else if (blue == 13) bFString = "D";
        else if (blue == 14) bFString = "E";
        else if (blue == 15) bFString = "F";
        else bFString = String.valueOf(blue);

        if (rblue == 10) bSString = "A";
        else if (rblue == 11) bSString = "B";
        else if (rblue == 12) bSString = "C";
        else if (rblue == 13) bSString = "D";
        else if (rblue == 14) bSString = "E";
        else if (rblue == 15) bSString = "F";
        else bSString = String.valueOf(rblue);
        bFString = bFString + bSString;
        result = rFString + gFString + bFString;
//        result = "#" + rFString + gFString + bFString;
        return result;

    }

    //hsv格式颜色转rgb格式
    public static  float[] hsb2rgb(float[] hsb) {
        float[] rgb= new float[3];
        //先令饱和度和亮度为100%，调节色相h
        for(int offset=240,i=0;i<3;i++,offset-=120) {
            //算出色相h的值和三个区域中心点(即0°，120°和240°)相差多少，然后根据坐标图按分段函数算出rgb。但因为色环展开后，红色区域的中心点是0°同时也是360°，不好算，索性将三个区域的中心点都向右平移到240°再计算比较方便
            float x=Math.abs((hsb[0]+offset)%360-240);
            //如果相差小于60°则为255
            if(x<=60) rgb[i]=255;
                //如果相差在60°和120°之间，
            else if(60<x && x<120) rgb[i]=((1-(x-60)/60)*255);
                //如果相差大于120°则为0
            else rgb[i]=0;
        }
        //在调节饱和度s
        for(int i=0;i<3;i++)
            rgb[i]+=(255-rgb[i])*(1-hsb[1]);
        //最后调节亮度b
        for(int i=0;i<3;i++)
            rgb[i]*=hsb[2];
        return rgb;
    }

    //正式-传3个参数返回16进制颜色代码
    public static  String hsv2rgb(int hue, double sat, double val ) {
        String result="";
        float[] hsb = new float[3];
        hsb[0] = (float)hue;
        hsb[1] = (float)sat;
        hsb[2] = (float)val;
//        float[] hsb ={60,1.0f,1.0f };
        float[] rgb = hsb2rgb(hsb);
        int r =(int)rgb[0];
        int g =(int)rgb[1];
        int b =(int)rgb[2];
        result = convertRGBToHex( r,  g,  b);
        return result;
    }

    //16进制转10进制
    public static  String hexto10(String hex_num) {
        String c10 = "16777215";
        long dec_num = Long.parseLong(hex_num, 16);
        c10 = String.valueOf(dec_num);
        if("0".equals(c10)){
            c10 = "1";
        }
        return c10;
    }

    //10进制转16进制
    public static String tentohex(int numb) {
        String hex= Integer.toHexString(numb);
        return hex;
    }

    //hex颜色去掉0x
    public static String cutColor(String hexColor) {
        String[] colorArray = hexColor.split("x");
        return colorArray[1];
    }


    private static final int RED_YELLOW = 1;
    private static final int YELLOW_GREEN = 2;
    private static final int GREEN_CYAN = 3;
    private static final int CYAN_BLUE = 4;
    private static final int BLUE_MAGENTA = 5;
    private static final int MAGENTA_RED = 6;
    private static final float HUE_SPLIT = 60.f;
    private static final int HUE_MAX = 360;

    public static Color transformHSVtoRGB(final int hue, final float sat, final float val) {
        if (hue < 0 || hue > HUE_MAX) {
            throw new IllegalArgumentException("hue needs to be between" + " 0 and 360");
        }
        if (sat < 0.0 || sat > 1.0) {
            throw new IllegalArgumentException("saturation needs to be between" + " 0.0 and 1.0");
        }
        if (val < 0.0 || val > 1.0) {
            throw new IllegalArgumentException("value needs to be between" + " 0.0 and 1.0");
        }
        int h = (int) (hue / HUE_SPLIT);
        float f = (hue / HUE_SPLIT) - h;
        float p = val * (1 - sat);
        float q = val * (1 - sat * f);
        float t = val * (1 - sat * (1 - f));
        switch (h) {
            case RED_YELLOW:
                return new Color(q, val, p);
            case YELLOW_GREEN:
                return new Color(p, val, t);
            case GREEN_CYAN:
                return new Color(p, q, val);
            case CYAN_BLUE:
                return new Color(t, p, val);
            case BLUE_MAGENTA:
                return new Color(val, p, q);
            case MAGENTA_RED:
            default:
                return new Color(val, t, p);
        }
    }

}