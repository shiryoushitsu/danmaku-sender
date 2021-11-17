package com.hikari.danmaku.utils;

import com.hikari.danmaku.entity.Ass;
import com.hikari.danmaku.entity.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * create by AwaiHikari on 2020-07-01 20:50
 *
 */

public class LrcUtil {
    public static void main(String []args) {
        String s ="Sat Jan 01 2000 00:00:05 GMT+0800 (中国标准时间)";
//         String s ="00:00:03 35345354";
//        System.out.println(parseOffset("2 00:00:03 2"));
        System.out.println(parseOffset(s));

    }

    public static String createLrc(String path )  {
        String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
        String lyr2 = "";
        try{
            File file = new File(path);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineStr = null; // 每次读取一行字符串
                while ((lineStr = bufferedReader.readLine()) != null) {
                    lyr2 += lineStr +"\n";
                }
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            e.printStackTrace();
        }

        //获取歌词文本
        String lyr = lyr2;
        //[offset:时间补偿值]其单位是毫秒，正值表示延迟，负值表示提前。用于整体调整歌词显示慢（快）于音乐播放。
        String offsetText = getCenterText("[offset", "]", lyr);
        //解析歌词
        List<Lyric> Lyrics = new ArrayList<>();//存放歌词
        String[] split = lyr.split("\\n");//分割
        if (split.length == 1) split = lyr.split("\\\\n");
        for (String s : split) {
            List<Lyric> list = parseLine(s);
            if (list != null && !list.isEmpty()) {
                Lyrics.addAll(list);
            }
        }
        //按照时间排序
        Collections.sort(Lyrics, (lyrBean, t1) -> (int) (lyrBean.getTime() - t1.getTime()));
        //输出看看歌词效果
        String contentLrc = "";
        for (Lyric lyrBean : Lyrics) {
            SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS");
//            System.out.println(format.format(lyrBean.getLongtime()) + "----" + lyrBean.getText());
            contentLrc += format.format(lyrBean.getTime()) + "----" + lyrBean.getText() + "\n";
        }

        return contentLrc;
    }

    //解析offset时间偏移量(首句时间)
    public static int parseOffset(String s) {
        int time = 0;
        if (s.isEmpty()) {
            return 0;
        }
        // 去除空格
        s = s.trim();
        System.out.println(s);
        Matcher timeMatcher1 = Pattern.compile("(\\d{1,3}):(\\d{2})").matcher(s);
        Matcher timeMatcher2 = Pattern.compile("(\\d{1,2}):(\\d{2}):(\\d{2})").matcher(s);
        Matcher timeMatcher3 = Pattern.compile("(\\d{1,3}):(\\d{2})\\.(\\d{2,3})").matcher(s);
        Matcher timeMatcher4 = Pattern.compile("(\\d{1,2}):(\\d{2}):(\\d{2})\\.(\\d{2,3})").matcher(s);
        //elementui时间选择器
        Matcher timeMatcher5 = Pattern.compile(".*(\\d{2}):(\\d{2}):(\\d{2}).*").matcher(s);

        if (timeMatcher1.matches()) {
            int min = Integer.valueOf(timeMatcher1.group(1));// 分
            int second = Integer.valueOf(timeMatcher1.group(2));// 秒
            int intTime = min*1000*60 +second*1000;
            time = intTime;
            return time;

        }else if(timeMatcher2.matches()){
            int hour = Integer.valueOf(timeMatcher2.group(1));// 时
            int min = Integer.valueOf(timeMatcher2.group(2));// 分
            int second = Integer.valueOf(timeMatcher2.group(3));// 秒
            int intTime =hour*1000*60*60 + min*1000*60 +second*1000;
            time = intTime;
            return time;
        }else if(timeMatcher3.matches()){

            int min = Integer.valueOf(timeMatcher3.group(1));// 分
            int second = Integer.valueOf(timeMatcher3.group(2));// 秒
            int mil = Integer.valueOf(timeMatcher3.group(3));// 毫秒

            int scale_mil = 1;//如果毫秒是三位的话（）乘1
            if(timeMatcher3.group(3).length()==3) {
                scale_mil = 1;
            }else {             //两位乘10
                scale_mil = 10;
            }
            int intTime = min * 60000 + second * 1000 + mil * scale_mil;
            time = intTime;
            return time;
        }else if(timeMatcher4.matches()){
            int hour = Integer.valueOf(timeMatcher4.group(1));// 时
            int min = Integer.valueOf(timeMatcher4.group(2));// 分
            int second = Integer.valueOf(timeMatcher4.group(3));// 秒
            int mil = Integer.valueOf(timeMatcher4.group(4));// 毫秒

            int scale_mil = 1;//如果毫秒是三位的话（）乘1
            if(timeMatcher4.group(4).length()==3) {
                scale_mil = 1;
            }else {             //两位乘10
                scale_mil = 10;
            }
            int intTime = min * 60000 + second * 1000 + mil * scale_mil;
            time = intTime;
            return time;
        }else if(timeMatcher5.matches()){
            int hour = Integer.valueOf(timeMatcher5.group(1));// 时
            int min = Integer.valueOf(timeMatcher5.group(2));// 分
            int second = Integer.valueOf(timeMatcher5.group(3));// 秒
            int intTime =hour*1000*60*60 + min*1000*60 +second*1000;
            time = intTime;
            return time;
        }else {
            System.out.println("时间格式不对");
        }

        return time;
    }

    //解析offset时间偏移量(首句时间)
//    public static Long parseOffset(String s) {
//        long time = 0;
//        if (s.isEmpty()) {
//            return null;
//        }
//        // 去除空格
//        s = s.trim();
//
//        Matcher timeMatcher1 = Pattern.compile("(\\d{1,3}):(\\d{2})").matcher(s);
//        Matcher timeMatcher2 = Pattern.compile("(\\d{1,2}):(\\d{2}):(\\d{2})").matcher(s);
//        Matcher timeMatcher3 = Pattern.compile("(\\d{1,3}):(\\d{2})\\.(\\d{2,3})").matcher(s);
//        Matcher timeMatcher4 = Pattern.compile("(\\d{1,2}):(\\d{2}):(\\d{2})\\.(\\d{2,3})").matcher(s);
//
//        if (timeMatcher1.matches()) {
//            int min = Integer.valueOf(timeMatcher1.group(1));// 分
//            int second = Integer.valueOf(timeMatcher1.group(2));// 秒
//            int intTime = min*1000*60 +second*1000;
//            time = (long)intTime;
//            return time;
//
//        }else if(timeMatcher2.matches()){
//            int hour = Integer.valueOf(timeMatcher2.group(1));// 时
//            int min = Integer.valueOf(timeMatcher2.group(2));// 分
//            int second = Integer.valueOf(timeMatcher2.group(3));// 秒
//            int intTime =hour*1000*60*60 + min*1000*60 +second*1000;
//            time = (long)intTime;
//            return time;
//        }else if(timeMatcher3.matches()){
//
//            int min = Integer.valueOf(timeMatcher3.group(1));// 分
//            int second = Integer.valueOf(timeMatcher3.group(2));// 秒
//            int mil = Integer.valueOf(timeMatcher3.group(3));// 毫秒
//
//            int scale_mil = 1;//如果毫秒是三位的话（）乘1
//            if(timeMatcher3.group(3).length()==3) {
//                scale_mil = 1;
//            }else {             //两位乘10
//                scale_mil = 10;
//            }
//            int intTime = min * 60000 + second * 1000 + mil * scale_mil;
//            time = (long)intTime;
//            return time;
//        }else if(timeMatcher4.matches()){
//            int hour = Integer.valueOf(timeMatcher2.group(1));// 时
//            int min = Integer.valueOf(timeMatcher4.group(2));// 分
//            int second = Integer.valueOf(timeMatcher4.group(3));// 秒
//            int mil = Integer.valueOf(timeMatcher4.group(4));// 毫秒
//
//            int scale_mil = 1;//如果毫秒是三位的话（）乘1
//            if(timeMatcher4.group(4).length()==3) {
//                scale_mil = 1;
//            }else {             //两位乘10
//                scale_mil = 10;
//            }
//            int intTime = min * 60000 + second * 1000 + mil * scale_mil;
//            time = (long)intTime;
//            return time;
//        }else {
//            System.out.println("时间格式不对");
//        }
//
//        return time;
//    }

    //获取中间文本，主要是获取歌词中的歌名专辑名这些
    private static String getCenterText(String start, String end, String s) {
        if (start.isEmpty() || end.isEmpty() || s.isEmpty()) {
            return "";
        }
        int i = s.indexOf(start);
        int i1 = s.indexOf(end, i);
        if (i == -1 || i1 == -1) return "";
        String s1 = s.substring(i + start.length(), i1);
        return s1;
    }

    //将lrc歌词转化为lrc实体类
    public static  List<Lyric>  lrcList(String path )  {
        String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
        String lyr2 = "";
        try{
            File file = new File(path);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineStr = null; // 每次读取一行字符串
                while ((lineStr = bufferedReader.readLine()) != null) {
                    if (lineStr != null && !lineStr.isEmpty()) {
                        lyr2 += lineStr +"\n";
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            e.printStackTrace();
        }
        //获取歌词文本
        String lyr = lyr2;

        //解析歌词
        List<Lyric> Lyrics = new ArrayList<>();//存放歌词
        String[] split = lyr.split("\\n");//分割
        if (split.length == 1) split = lyr.split("\\\\n");

        for (String s : split) {
            List<Lyric> list = parseLine(s);
            if (list != null && !list.isEmpty()) {
                Lyrics.addAll(list);
            }
        }
        //按照时间排序
        Collections.sort(Lyrics, (lyrBean, t1) -> (int) (lyrBean.getTime() - t1.getTime()));

        //输出看看歌词效果
        String contentLrc = "";
        for (Lyric lyrBean : Lyrics) {
            SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS");
            contentLrc += format.format(lyrBean.getTime()) + "----" + lyrBean.getText() + "\n";
        }

        return Lyrics;
    }

    //将lrc歌词转化为ass实体类
    public static  List<Ass>  lrcToAssList(String path)  {
        String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
        String lyr2 = "";
        try{
            File file = new File(path);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineStr = null; // 每次读取一行字符串
                while ((lineStr = bufferedReader.readLine()) != null) {
                    if (lineStr != null && !lineStr.isEmpty()) {
                        lyr2 += lineStr +"\n";
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            e.printStackTrace();
        }
        //获取歌词文本
        String lyr = lyr2;

        //解析歌词
        List<Lyric> Lyrics = new ArrayList<>();//存放歌词
        String[] split = lyr.split("\\n");//分割
        if (split.length == 1) split = lyr.split("\\\\n");

        for (String s : split) {
            List<Lyric> list = parseLineWithEmpty(s);
            if (list != null && !list.isEmpty()) {
                Lyrics.addAll(list);
            }
        }
        //按照时间排序
        Collections.sort(Lyrics, (lyrBean, t1) -> (int) (lyrBean.getTime() - t1.getTime()));

        List<Ass> assList = new ArrayList<>();
        //Lyric转换AssEntity
        for (int i=0;i<Lyrics.size();i++){
            Ass ass= new Ass();
            int startTime = (int)Lyrics.get(i).getTime();
            int endTime = startTime + 4000;//最后一句持续时间是4秒
            if(i<Lyrics.size()-1){
                endTime = (int)Lyrics.get(i+1).getTime();
            }
            int syl =endTime - startTime ;
            if(syl>10000){  //超过10秒的，都改为10秒，主要处理lrc歌词间隔过长而且又没空lrc的情况
                syl = 10000;
            }

            String  text = Lyrics.get(i).getText();
            if(text!=null && !"".equals(text)){
                ass.setStartTime(startTime);
                ass.setEndTime(endTime);
                ass.setContent(Lyrics.get(i).getText());
                ass.setSyl(syl);
                assList.add(ass);
            }
        }


        return assList;
    }

    //1.解析ass返回danmaku实体类列表【1124】
    public static  List<Ass>  danmakuList(String path)  {
        String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
        String lyr2 = "";
        try{
            File file = new File(path);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineStr = null; // 每次读取一行字符串
                while ((lineStr = bufferedReader.readLine()) != null) {
                    if (lineStr != null && !lineStr.isEmpty()) {
                        lyr2 += lineStr +"\n";
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            e.printStackTrace();
        }
        //获取歌词文本
        String lyr = lyr2;

        //解析歌词
        List<Ass> assBeans = new ArrayList<>();//存放歌词
        String[] split = lyr.split("\\n");//分割
        if (split.length == 1) split = lyr.split("\\\\n");

        for (String s : split) {
            Ass ass = parseAssLine(s);  //【解析重点】
            if (ass != null && !"".equals(ass.getContent())) {
                assBeans.add(ass);
            }
        }
        //按照时间排序
//        Collections.sort(assBeans, (assBean, t1) -> (assBean.getStartTime() - t1.getStartTime()));

        //输出看看歌词效果
        String contentLrc = "";
        for (int ii = 0 ; ii< assBeans.size() ; ii++) {
            contentLrc +=  assBeans.get(ii).getStartTimeString() + "--" + assBeans.get(ii).getEndTimeString() + "--"+ assBeans.get(ii).getContent() + "\n";
        }
        System.out.println(contentLrc);
        return assBeans;
    }


    //解析每一行的歌词
    private static List<Lyric> parseLine(String s) {
        if (s.isEmpty()) {
            return null;
        }
        // 去除空格
        s = s.trim();
        // 正则表达式，判断s中是否有[00:00.60]或[00:00.600]格式的片段
        Matcher lineMatcher = Pattern.
                compile("((\\[\\d{2}:\\d{2}\\.\\d{2,3}\\])+)(.+)").matcher(s);
        // 如果没有，返回null
        if (!lineMatcher.matches()) {
            return null;
        }
        // 得到时间标签
        String times = lineMatcher.group(1);
        // 得到歌词文本内容
        String text = lineMatcher.group(3);
        List<Lyric> entryList = new ArrayList<>();
        Matcher timeMatcher = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d{2,3})\\]").matcher(times);
        while (timeMatcher.find()) {
            int min = Integer.valueOf(timeMatcher.group(1));// 分
            int sec = Integer.valueOf(timeMatcher.group(2));// 秒
            int mil = Integer.valueOf(timeMatcher.group(3));// 毫秒

            // 转换为long型时间
//            int scale_mil=mil>=100?1:10;//如果毫秒是3位数则乘以1，反正则乘以10
            int scale_mil = 1;//如果毫秒是三位的话（）乘1
            if(timeMatcher.group(3).length()==3) {
                scale_mil = 1;
            }else {             //两位乘10
                scale_mil = 10;
            }




            // 转换为long型时间
            int time = min * 60000 + sec * 1000 + mil * scale_mil;
            // 最终解析得到一个list
            entryList.add(new Lyric(time, times, text));
        }
        return entryList;
    }

    //解析ass每一行的歌词
    private static Ass parseAssLine(String s) {
        if(s.contains("Dialogue")){
//            System.out.println("包含了Dialogue");
        }else if(s.contains("Comment")){
//            System.out.println("包含了Comment");
            return null;
        }else {
            return null;
        }

        if (s.isEmpty()) {
            return null;
        }
        // 去除空格
        s = s.trim();
        // 正则表达式，判断s中是否有[00:00.60]或[00:00.600]格式的片段  0:00:19.22,0:00:20.78
        //Dialogue: 0,0:00:19.22,0:00:20.78,K2,,0,0,0,karaoke,
        //Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
        Matcher lineMatcher = Pattern.
                compile("Dialogue:(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*)").matcher(s);
        // 如果没有，返回null
        if (!lineMatcher.matches()) {
            return null;
        }
        // 得到时间标签
        String StartTimes = lineMatcher.group(2);
        String endTimes = lineMatcher.group(3);

        // 得到歌词文本内容
        String text = lineMatcher.group(10);

        Ass Ass = new Ass();
        String minute =StartTimes.substring(3,4);
        String secondTens =StartTimes.substring(5,6);
        String secondOnes =StartTimes.substring(6,7);
        String millisecondThou =StartTimes.substring(8,9);
        String millisecondHun =StartTimes.substring(9,10);

        String minuteEnd = endTimes.substring(3,4);
        String secondTensEnd = endTimes.substring(5,6);
        String secondOnesEnd = endTimes.substring(6,7);
        String millisecondThouEnd = endTimes.substring(8,9);
        String millisecondHunEnd = endTimes.substring(9,10);

        int startTimems = Integer.parseInt(minute)*1000*60  + Integer.parseInt(secondTens)*10000 +  Integer.parseInt(secondOnes)*1000  + Integer.parseInt(millisecondThou)*100 + Integer.parseInt(millisecondHun)*10 ;
        int endTimems = Integer.parseInt(minuteEnd)*1000*60  + Integer.parseInt(secondTensEnd)*10000 +  Integer.parseInt(secondOnesEnd)*1000  + Integer.parseInt(millisecondThouEnd)*100 + Integer.parseInt(millisecondHunEnd)*10 ;
        int sylms=endTimems -startTimems;


        Ass.setContent(text);
        Ass.setStartTime(startTimems);
        Ass.setEndTime(endTimems);
        Ass.setStartTimeString(StartTimes);
        Ass.setEndTimeString(endTimes);
        Ass.setSyl(sylms);

        return Ass;
    }

    //解析txt每一行
    private static Ass parseTxtLine(String s) {
        if (s.isEmpty()) {
            return null;
        }

        String text = s;
        Ass Ass = new Ass();

        Ass.setContent(text);
        Ass.setStartTime(0);
        Ass.setEndTime(4000);
        Ass.setStartTimeString("0");
        Ass.setEndTimeString("4000");
        Ass.setSyl(4000);

        return Ass;
    }

    //解析每一行的歌词(无文本也返回)
    private static List<Lyric> parseLineWithEmpty(String s) {
        if (s.isEmpty()) {
            return null;
        }
        // 去除空格
        s = s.trim();
        // 正则表达式，判断s中是否有[00:00.60]或[00:00.600]格式的片段
        Matcher lineMatcher = Pattern.
                compile("((\\[\\d{2}:\\d{2}\\.\\d{2,3}\\])+)(.*)").matcher(s);
        // 如果没有，返回null
        if (!lineMatcher.matches()) {
            return null;
        }
        // 得到时间标签
        String times = lineMatcher.group(1);
        // 得到歌词文本内容
        String text = lineMatcher.group(3);
        List<Lyric> entryList = new ArrayList<>();
        Matcher timeMatcher = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d{2,3})\\]").matcher(times);
        while (timeMatcher.find()) {
            int min = Integer.valueOf(timeMatcher.group(1)) ;// 分
            int sec = Integer.valueOf(timeMatcher.group(2));// 秒
            int mil = Integer.valueOf(timeMatcher.group(3));// 毫秒

            int scale_mil = 1;//如果毫秒是三位的话（）乘1
            if(timeMatcher.group(3).length()==3) {
                scale_mil = 1;
            }else {//两位乘10
                scale_mil = 10;
            }
            int time = min * 60000 + sec * 1000 + mil * scale_mil;
            // 最终解析得到一个list
            entryList.add(new Lyric(time, times, text));
        }
        return entryList;
    }

    public static  List<Ass>   sentenceToAss(String text){
        List<Ass> ass = new ArrayList<>();//存放歌词
        int  startTime = 1000;//开始时间
        int endTime =2200;//结束时间
        int syl = 1200;//间隔
        String content ="爷青回";//歌词内容
//        String content ="明天一定会是晴天";//歌词内容
        for(int i=0;i<content.length();i++){
            String word =String.valueOf(content.charAt(i));
            Ass Ass = new Ass();
            Ass.setContent(word);
            Ass.setStartTime(startTime);
            Ass.setEndTime(endTime);
            Ass.setSyl(syl);
            ass.add(Ass);
        }

        return ass;
    }


    public static String  returnModeName(String mode){
        String modeName = "滚动弹幕";
        if("1".equals(mode)){
            modeName = "滚动弹幕";
        }else if("4".equals(mode)){
            modeName = "底部弹幕";
        }else if("5".equals(mode)){
            modeName = "顶部弹幕";
        }else if("7".equals(mode)){
            modeName = "高级弹幕";
        }
        return modeName;
    }

    public static String  returnEffectName(int effectCode) {
        String modeName = "单句弹幕";
        if (effectCode == 0) {
            modeName = "自定义";
        } else if (effectCode == 1) {
            modeName = "随机四周回归";
        } else if (effectCode == 2){
            modeName = "跳动";
        } else if (effectCode == 3){
            modeName = "逐字显现";
        } else if (effectCode == 4){
            modeName = "";
        } else if (effectCode == 5){
            modeName = "向右扩散";
        } else if (effectCode == 6){
            modeName = "字符位移";
        } else if (effectCode == 8){
            modeName = "逐字淡入淡出";
        } else if (effectCode == 10){
            modeName = "发光";
        }
        else if (effectCode == 11){
            modeName = "螺旋丸";
        } else if (effectCode == 12){
            modeName = "句中心螺旋丸";
        }
        else if (effectCode == 21){
            modeName = "字符字";
        }
        return modeName;
    }

    public static String  returnASCIIEffectName(int effectCode) {
        String modeName = "固定位置显示";
        if (effectCode == 0) {
            modeName = "自定义";
        } else if (effectCode == 1) {
            modeName = "固定位置显示";
        } else if (effectCode == 2){
            modeName = "从上到下发送出现（发送位置为顶部）";
        } else if (effectCode == 3){
            modeName = "从上到下发送出现（发送位置为底部）";
        } else if (effectCode == 4){
            modeName = "固定位置显示（统一结束时间）";
        } else if (effectCode == 5){
            modeName = "从上到下发送出现（发送位置为顶部，统一结束时间";
        } else if (effectCode == 6){
            modeName = "从上到下发送出现（发送位置为底部，统一结束时间）";
        }
        return modeName;
    }

    //解析offset时间偏移量(首句时间)【1204保存】
    public static Long parseOffsetOld(String s) {
        long time = 0;
        if (s.isEmpty()) {
            return null;
        }
        // 去除空格
        s = s.trim();

        //00:00:00样式
        if(!s.contains(".")){
            String hourTens =s.substring(0,1);
            String hourOnes =s.substring(1,2);
            String minuteTens =s.substring(3,4);
            String minuteOnes =s.substring(4,5);
            String secondTens =s.substring(6,7);
            String secondOnes =s.substring(7,8);

            int intTime = Integer.parseInt(hourTens)*10000*60*60  +
                    Integer.parseInt(hourOnes)*1000*60*60  +
                    Integer.parseInt(minuteTens)*10000*60 +
                    Integer.parseInt(minuteOnes)*1000*60   +
                    Integer.parseInt(secondTens)*10000 +
                    Integer.parseInt(secondOnes)*1000 ;
            time = (long)intTime;
            return time;
        }

        //00:00.000样式
        // 正则表达式，判断s中是否有[00:00.60]或[00:00.600]格式的片段
        Matcher lineMatcher = Pattern.compile("((\\d{2}:\\d{2}\\.\\d{2,3})+)(.*)").matcher(s);
        // 如果没有，返回null
        if (!lineMatcher.matches()) {
            return null;
        }
        // 得到时间标签
        String times = lineMatcher.group(1);
        // 得到歌词文本内容
        String text = lineMatcher.group(3);
        List<Lyric> entryList = new ArrayList<>();
        Matcher timeMatcher = Pattern.compile("(\\d\\d):(\\d\\d)\\.(\\d{2,3})").matcher(times);
        while (timeMatcher.find()) {
            long min = Long.parseLong(timeMatcher.group(1));// 分
            long sec = Long.parseLong(timeMatcher.group(2));// 秒
            long mil = Long.parseLong(timeMatcher.group(3));// 毫秒
            // 转换为long型时间
//            int scale_mil=mil>=100?1:10;//如果毫秒是3位数则乘以1，反正则乘以10
            int scale_mil = 1;//如果毫秒是三位的话（）乘1
            if(timeMatcher.group(3).length()==3) {
                scale_mil = 1;
            }else {             //两位乘10
                scale_mil = 10;
            }
            // 转换为long型时间
            time = min * 60000 + sec * 1000 + mil * scale_mil;
        }
        return time;
    }

    //将lrc歌词转化为ass实体类
    public static  List<Ass>  lrcToAssListVer2(String path )  {
        String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
        String lyr2 = "";
        try{
            File file = new File(path);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineStr = null; // 每次读取一行字符串
                while ((lineStr = bufferedReader.readLine()) != null) {
                    if (lineStr != null && !lineStr.isEmpty()) {
                        lyr2 += lineStr +"\n";
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            e.printStackTrace();
        }
        //获取歌词文本
        String lyr = lyr2;

        //解析歌词
        List<Lyric> Lyrics = new ArrayList<>();//存放歌词
        String[] split = lyr.split("\\n");//分割
        if (split.length == 1) split = lyr.split("\\\\n");

        for (String s : split) {
            List<Lyric> list = parseLineWithEmpty(s);
            if (list != null && !list.isEmpty()) {
                Lyrics.addAll(list);
            }
        }
        //按照时间排序
        Collections.sort(Lyrics, (lyrBean, t1) -> (int) (lyrBean.getTime() - t1.getTime()));

        List<Ass> assList = new ArrayList<>();
        //Lyric转换AssEntity
        for (int i=0;i<Lyrics.size();i++){
            Ass ass= new Ass();
            int startTime = (int)Lyrics.get(i).getTime();
            int endTime = startTime + 4000;//最后一句持续时间是4秒
            if(i<Lyrics.size()-1){
                endTime = (int)Lyrics.get(i+1).getTime();
            }
            int syl =endTime - startTime ;
//            if(syl>10000){  //超过10秒的，都改为10秒，主要处理lrc歌词间隔过长而且又没空lrc的情况
//                syl = 10000;
//            }

            String  text = Lyrics.get(i).getText();
            if(text!=null && !"".equals(text)){
                ass.setStartTime(startTime);
                ass.setEndTime(endTime);
                ass.setContent(Lyrics.get(i).getText());
                ass.setSyl(syl);
//            ass.setStartTimeString();
//            ass.setEndTimeString();
                assList.add(ass);
            }
        }


        return assList;
    }


//    //解析txt返回二维数组【20210719】
//    public static  List<List<String>> txtToList(String path)  {
//        String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
//        String lyr2 = "";
//        List<List<String>> arrayList = new ArrayList<>();
//        try{
//            File file = new File(path);
//            if (file.isFile() && file.exists()) { // 判断文件是否存在
//                encoding = codeString(file);
//
//                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineStr = null; // 每次读取一行字符串
//                while ((lineStr = bufferedReader.readLine()) != null) {
//                    if (lineStr != null && !lineStr.isEmpty()) {
//                        List<String> stringList = new ArrayList<>();
//                        for(int i = 0 ;i<lineStr.length();i++){
//                            stringList.add( String.valueOf(lineStr.charAt(i)));
//                        }
//                        arrayList.add(stringList);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("读取文件出错!");
//            e.printStackTrace();
//        }
//
//        return arrayList;
//    }
}