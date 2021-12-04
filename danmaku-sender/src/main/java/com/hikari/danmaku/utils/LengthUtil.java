package com.hikari.danmaku.utils;

import java.io.UnsupportedEncodingException;

public class LengthUtil {

    // 编码方式
    private static final String ENCODE_UTF = "UTF-8";

    public static void main(String[] args) {
        String str = "测试";
        System.out.println(getLength(str));
        System.out.println(getStrLength(str));
    }

    /**
     * 计算中英文字符串的字节长度 <br/>
     * 一个中文占3个字节
     *
     * @param str
     * @return int 字符串的字节长度
     */
    public static int getLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        try {
            return str.getBytes(ENCODE_UTF).length;
        } catch (UnsupportedEncodingException e) {
        }
        return 0;
    }


    /**
     * 计算中英文字符串的字节长度
     *
     * @param str
     * @return int
     */
    public static int getStrLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int len = 0;
        for (int i = 0, j = str.length(); i < j; i++) {
            //UTF-8编码格式中文占三个字节，GBK编码格式 中文占两个字节 ;
            len += (str.charAt(i) > 255 ? 3 : 1);
        }
        return len;
    }



    
}