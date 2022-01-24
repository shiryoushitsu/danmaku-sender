package com.hikari.danmaku.utils;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.hikari.danmaku.entity.Ass;
import com.hikari.danmaku.entity.Lyric;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * lrc转exo工具类
 */

public class Lrc2ExoUtil {
    public static void main(String []args) {
        String fileName = "dance-jp";
        String inputLrcPath = "C:\\Users\\hikari\\Desktop\\" + fileName + ".lrc";
        String outputExoPath = "C:\\Users\\hikari\\Desktop\\"+ fileName +".exo";
        List<Ass> assList = lrcToAssListV2(inputLrcPath);
        String lrfExoStr = createLrcExo(assList);
        //输出字符串到文件
//        File file = new File(outputExoPath,"gb2312");
        FileWriter writer = new FileWriter(outputExoPath,"gb2312");
        writer.append(lrfExoStr);
    }


    /**
     * 普通文字转exo编码文字
     */
    public static String textToExoText(String text) {
        //1.String转UTF-16LE
        StringBuilder resultBuilder = new StringBuilder();
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {
                byte dataByte = (byte) b;
                int dataInt = Byte.toUnsignedInt(dataByte);
                String dataString = Integer.toHexString(dataInt);
                if (dataString.length() == 1) dataString = "0" + dataString;
                resultBuilder.append(dataString);
            }
        };
        try {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_16LE);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = resultBuilder.toString();
        int length = 4096 - result.length();
        //2.Aviutl的1.6d版本UTF-16LE补0（text字节共4096）
        for(int i = 0; i < length; i++){
            result = result + "0";
        }

        return result;
    }

    /**
     * 将歌词对象转换为exo歌词文件
     */
    public static String createLrcExo(List<Ass> assList)  {
        int rate = 30;
        String result = "";
        String exeditStr =
                "[exedit]\n" +
                "width=850\n" +
                "height=560\n" +
                "rate="+ rate +"\n" +
                "scale=1\n" +
                "length=10000\n" +
                "audio_rate=44100\n" +
                "audio_ch=2\n";
        result = result + exeditStr;
        for(int i = 0; i < assList.size(); i++){
            Ass ass = assList.get(i);
            int startTime = ass.getStartTime() ;
            int endTime = ass.getEndTime() ;
            int startFlame = startTime * rate / 1000 + 1 ;
            int endFlame = endTime * rate / 1000  ;
            String text = ass.getContent();
            String exoText = textToExoText(text);
            String text1 =
                    "["+ i +"]\n" +
                            "start="+ startFlame+"\n" +
                            "end="+ endFlame +"\n" +
                            "layer=1\n" +
                            "overlay=1\n" +
                            "camera=0\n";
            String text2 =
                    "["+ i +".0]\n" +
                            "_name=文本\n" +
                            "大小=40\n" +
                            "显示速度=0.0\n" +
                            "独立每个文字=0\n" +
                            "在路径上显示=0\n" +
                            "自动滚动=0\n" +
                            "B=0\n" +
                            "I=0\n" +
                            "type=0\n" +
                            "autoadjust=0\n" +
                            "soft=1\n" +
                            "monospace=0\n" +
                            "align=4\n" +
                            "spacing_x=0\n" +
                            "spacing_y=0\n" +
                            "precision=1\n" +
                            "color=ffffff\n" +
                            "color2=000000\n" +
                            "font=MS Mincho\n" +
                            "text=" + exoText + "\n";

            String text3 =
                    "["+ i +".1]\n" +
                            "_name=标准变换\n" +
                            "X=0.0\n" +
                            "Y=0.0\n" +
                            "Z=0.0\n" +
                            "缩放率=100.00\n" +
                            "透明度=0.0\n" +
                            "旋转=0.00\n" +
                            "blend=0\n";
            result = result + text1 + text2 + text3;
        }


        return result;
    }

    //将lrc歌词转化为ass实体类
    public static  List<Ass> lrcToAssListV2(String path)  {
        FileReader reader = new FileReader(path);
        List<String> linesList = reader.readLines();
        //解析歌词
        List<Lyric> Lyrics = new ArrayList<>();//存放歌词

        for (String s : linesList) {
            List<Lyric> list = parseLineWithEmpty(s);
            if (list != null && !list.isEmpty()) {
                Lyrics.addAll(list);
            }
        }
        //按照时间排序
        Collections.sort(Lyrics, (lyrBean, t1) -> (int) (lyrBean.getTime() - t1.getTime()));

        List<Ass> assList = new ArrayList<>();
        //Lyric转换AssEntity
        for (int i = 0;i < Lyrics.size();i++){
            Ass ass= new Ass();
            int startTime = Lyrics.get(i).getTime();
            int endTime = startTime + 4000;//最后一句持续时间是4秒
            if(i < Lyrics.size() - 1 ){
                endTime = Lyrics.get(i + 1).getTime();
            }
            int syl = endTime - startTime ;

            String text = Lyrics.get(i).getText();
            if(text != null && !"".equals(text)){
                ass.setStartTime(startTime);
                ass.setEndTime(endTime);
                ass.setContent(Lyrics.get(i).getText());
                ass.setSyl(syl);
                assList.add(ass);
            }
        }

        return assList;
    }


    //解析每一行的歌词(无文本也返回)
    private static List<Lyric> parseLineWithEmpty(String s) {
        if (s.isEmpty()) { return null; }
        s = s.trim();
        // 正则表达式，判断s中是否有[00:00.60]或[00:00.600]格式的片段
        Matcher lineMatcher = Pattern.compile("((\\[\\d{2}:\\d{2}\\.\\d{2,3}\\])+)(.*)").matcher(s);
        // 如果没有，返回null
        if (!lineMatcher.matches()) { return null; }
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
            entryList.add(new Lyric(time, times, text));
        }
        return entryList;
    }
}