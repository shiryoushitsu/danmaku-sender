package com.hikari.danmaku.utils;

import cn.hutool.core.util.NumberUtil;
import com.hikari.danmaku.entity.CubicBezier;
import com.hikari.danmaku.entity.Position;
import com.hikari.danmaku.entity.Vector;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 贝塞尔曲线相关工具类
 *
 */
public class CubicBezierUtil {
    public static void main(String[] args) {

        System.out.println( parseCubic("cubic-bezier(.72,.07,.91,.22)", 1000,100));

        String content="弹";//内容
        String fontSize="5";
        String strs= "def text tem(x = \"\",y = \"\") {\n" +
                "    content =\""+ content  +"\" \n" +
                "    x = x\n" +
                "    y = y\n" +
                "    fontSize = "+ fontSize +"%\n" +
                "    anchorX = 0.5\n" +
                "    anchorY = 0.5\n" +
                "    alpha = 0\n" +
                "    fontFamily = \"微软雅黑\"\n" +
                "    color =0xffffff\n" +
                "    textShadow = 0\n" +
                "    bord = 1\n" +
                "}\n" ;

        List<CubicBezier> cubicBezierList = parseCubic("cubic-bezier(.72,.07,.91,.22)", 1000,100);
        String  a = "aa",
                percentStr = "%";
        double scale = 3;
        for(int i = 0; i< cubicBezierList.size();i++){
            strs = strs + "let t"+  a + "_" + i + " = tem(" + 100* +cubicBezierList.get(i).getProgression() + ""+ percentStr + "  ,"+ 50 +""+ percentStr + " + sc )" +
                    " set t"+  a + "_" +i+"{  }"+ i * 50 +"ms "+
                    " then set t"+  a + "_" + i +"{ alpha = 1 }0ms "+
                    " then set t"+  a + "_" + i +"{ alpha = 1 }"+  120 +"ms \n";

        }
        System.out.println(strs);
    }

    /**
     * 1.解析贝塞尔曲线字符串
     */
    public static List<Position> parseCubicStr(String cubicStr){
        List<Position> positionList = new ArrayList<>();

        Map<String,Object> map= new HashMap<>();
        String input = cubicStr;
        String regex = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String regCubicStr = "";
        while (matcher.find()) {
            regCubicStr = matcher.group(1);
        }
        String[] as = regCubicStr.split(",");
        Double[] asDouble = new Double[4];
        for (int a = 0;a<as.length;a++){
            if(".".equals(as[a].charAt(0))) {
                asDouble[a] = Double.parseDouble(as[a]);
            }else {
                asDouble[a] = Double.parseDouble(as[a]);
            }
        }

        double x1 = 0;
        double x2 = asDouble[0] ;
        double x3 = asDouble[2] ;
        double x4 = 1;
        double y1 = 0;
        double y2 = asDouble[1] ;
        double y3 = asDouble[3] ;
        double y4 = 1;
        positionList.add(new Position(x2,y2));
        positionList.add(new Position(x3,y3));
        return positionList ;
    }

    /**
     * 2.解析贝塞尔曲线
     */
    public static List<CubicBezier> parseCubic(String cubicStr, Integer duration, Integer frame){
        List<Position> positionList = parseCubicStr(cubicStr);
        List<CubicBezier> cubicBezierList = new ArrayList<>();

//        int frame = 33;
        double p1x = positionList.get(0).getX();
        double p1y = positionList.get(0).getY();
        double p2x = positionList.get(1).getX();
        double p2y = positionList.get(1).getY();

        double  ay = 3 * p1y - 3 * p2y + 1,
                by = 3 * p2y - 6 * p1y,
                cy = 3 * p1y;
        double xTarget = 0.1;

        int n = (int)Math.floor(NumberUtil.div(duration.doubleValue(),frame.doubleValue()));
        for(int i = 0; i <= n; i++){
            xTarget = NumberUtil.div(i,n);
            double t = newtonMethod(xTarget, p1x, p1y, p2x, p2y);
            double y = ((ay * t + by) * t + cy ) * t;
            double progression = NumberUtil.round(y,4).doubleValue();
            cubicBezierList.add(new CubicBezier(xTarget, progression));
        }

        return cubicBezierList;
    }

    public static List<CubicBezier> parseCubicByTimes(String cubicStr, Integer times){
        List<Position> positionList = parseCubicStr(cubicStr);
        List<CubicBezier> cubicBezierList = new ArrayList<>();

//        int frame = 33;
        double p1x = positionList.get(0).getX();
        double p1y = positionList.get(0).getY();
        double p2x = positionList.get(1).getX();
        double p2y = positionList.get(1).getY();

        double  ay = 3 * p1y - 3 * p2y + 1,
                by = 3 * p2y - 6 * p1y,
                cy = 3 * p1y;
        double xTarget = 0.1;

        int n = times;
        for(int i = 0; i <= n; i++){
            xTarget = NumberUtil.div(i,n);
            double t = newtonMethod(xTarget, p1x, p1y, p2x, p2y);
            double y = ((ay * t + by) * t + cy ) * t;
            double progression = NumberUtil.round(y,4).doubleValue();
            cubicBezierList.add(new CubicBezier(xTarget, progression));
        }

        return cubicBezierList;
    }


    /**
     * 在已知 x 的情况下求 t
     *
     */
    public static double newtonMethod(double xTarget, double x1, double y1, double x2, double y2){
        double tolerance = 0.00001,
                t0 = 0.6,
                x = 3*(1-t0)*(1-t0)*t0*x1 + 3*(1-t0)*t0*t0*x2 + t0*t0*t0,
                t = 0l;
        while( Math.abs(x - xTarget) > tolerance ){
            t = t0 - ( 3*(1-t0)*(1-t0)*t0*x1 + 3*(1-t0)*t0*t0*x2 + t0*t0*t0 - xTarget ) / ( 3*(1 - t0)*(1 - t0)*x1 + 6*(1 - t0)*t0*(x2 - x1) + 3*t0*t0*(1 - x2) );
            t0 = t;
            x = 3*(1-t0)*(1-t0)*t0*x1 + 3*(1-t0)*t0*t0*x2 + t0*t0*t0;
        }
        //return 3*(1-t)*(1-t)*t*y1 + 3*(1-t)*t*t*y2 + t*t*t;//这个是返回与x对应的y值

        return t;
    }




    //旧版贝塞尔曲线
    private static void oldCubicBezier(String cubicStr){
        Random random =new Random();

        String input = cubicStr;
        String regex = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String regCubicStr = "";
        while (matcher.find()) {
            regCubicStr = matcher.group(1);
        }
        String[] as = regCubicStr.split(",");

        String lineArray="m 38 50 l 57 39 l 72 42 l 90 45 l 99 60 l 125 51 l 123 32";

        String  [] assStrArray = lineArray.split("\\s+");//根据空格分离
        Double[] asDouble = new Double[8];
        for (int a = 0;a<as.length;a++){
            if(".".equals(as[a].charAt(0)))
            {
                asDouble[a]= Double.parseDouble(as[a]);
            }else {
                asDouble[a]= Double.parseDouble(as[a]);
            }
        }

        String a1 = assStrArray[1];
        String a2 = assStrArray[2];
        String a3 = assStrArray[3];
        String a4 = assStrArray[4];
        String a5 = assStrArray[5];
        String a6 = assStrArray[6];
        String a7 = assStrArray[7];
        String a8 = assStrArray[8];



        List<String> xArray =new ArrayList<>();
        List<String> yArray =new ArrayList<>();
        List<Double> xArrayDouble =new ArrayList<>();
        List<Double> yArrayDouble =new ArrayList<>();

        for (int o = 0; o <assStrArray.length/3; o++) {
            xArray.add(assStrArray[o * 3 + 1]);
            yArray.add(assStrArray[o * 3 + 2]);

            xArrayDouble.add(Double.parseDouble(assStrArray[o * 3 + 1]) / 160 * 100);// 百分比转换
            yArrayDouble.add(Double.parseDouble(assStrArray[o * 3 + 2]) / 90 * 100);//
        }


        int t1= 0 ;
        int t2 = 1000;//第一个粒子到最后一个粒子出现时间
        int accel=1; //加速度
        int afterimage_blur=0;//偏移量
        double afterimage_blur2=0.05; //偏移量 百分比
        double afterimage_dur=2000;
        int density=1;//粒子密度

        double x_blur=100;//x偏移量
        double y_blur=100;//y偏移量
        int frame_dur=33;//一帧时间

        String percentStr="%";

        List<String> x_pos = new ArrayList<>();
        List<String> y_pos = new ArrayList<>();

        String content="·";//内容
        int dur = t2 - t1;
        double frames = Math.floor(dur/(frame_dur/density));//循环次数
        String fontSize="3";
        String strs= "def text tem(x = \"\",y = \"\") {\n" +
                "    content =\""+ content  +"\" \n" +
                "    x = x\n" +
                "    y = y\n" +
                "    fontSize = "+ fontSize +"%\n" +
                "    anchorX = 0.5\n" +
                "    anchorY = 0.5\n" +
                "    alpha = 0\n" +
                "    fontFamily = \"微软雅黑\"\n" +
                "    color =0xffffff\n" +
                "    textShadow = 0\n" +

                "    bord = 1\n" +
//                "    strokeColor = 0xffffff\n" +
                "}\n" ;


        int jiange = frame_dur/density;

        String colorArray[]={"FE0302","FFFF00"};
        int frame = 33;

        int durations= 0 ;//持续时间积累
//        for (int a = 0; a < xArrayDouble.size()-1; a++) {
        for (int a = 0; a < 1; a++) {
//            double x1 = xArrayDouble.get(a);
//            double x2 = x1;
//            double x3 = xArrayDouble.get(a+1);
//            double x4 = x3;
//            double y1 = yArrayDouble.get(a);
//            double y2 = y1;
//            double y3 = yArrayDouble.get(a+1);
//            double y4 = y3;
            double x1 = 0;
            double x2 = asDouble[0]  * 100;
            double x3 = asDouble[2]  * 100;
            double x4 = 1* 100;
            double y1 = 0;
            double y2 = asDouble[1] * 100;
            double y3 = asDouble[3] * 100;
            double y4 = 1* 100;

            for(int i=0;i<frames;i++){
                if( afterimage_blur == 0 ) { x_blur = 0 ;y_blur = 0; } else
                {
                    x_blur =(double) random.nextInt(afterimage_blur) ;
                    y_blur = (double) random.nextInt(afterimage_blur);
                }
                x_pos.add(String.valueOf(Math.pow(1-Math.pow(i/frames,accel),3)* x1 + 3*Math.pow(1-Math.pow(i/frames,accel),2) * (Math.pow(i/frames,accel)) * x2 + 3*(1-Math.pow(i/frames,accel)) * Math.pow(Math.pow(i/frames,accel),2) * x3 + Math.pow(Math.pow(i/frames,accel),3) * x4))  ;
                y_pos.add(String.valueOf( Math.pow(1-Math.pow(i/frames,accel),3) * y1 + 3*Math.pow(1-Math.pow(i/frames,accel),2) * (Math.pow(i/frames,accel)) * y2 + 3*(1-Math.pow(i/frames,accel)) * Math.pow(Math.pow(i/frames,accel),2) * y3 + Math.pow(Math.pow(i/frames,accel),3)* y4));

                double xxRo = Math.pow(1-Math.pow(i/frames,accel),3)* x1 + 3*Math.pow(1-Math.pow(i/frames,accel),2) * (Math.pow(i/frames,accel)) * x2 + 3*(1-Math.pow(i/frames,accel)) * Math.pow(Math.pow(i/frames,accel),2) * x3 + Math.pow(Math.pow(i/frames,accel),3) * x4;
                double yRo = Math.pow(1-Math.pow(i/frames,accel),3) * y1 + 3*Math.pow(1-Math.pow(i/frames,accel),2) * (Math.pow(i/frames,accel)) * y2 + 3*(1-Math.pow(i/frames,accel)) * Math.pow(Math.pow(i/frames,accel),2) * y3 + Math.pow(Math.pow(i/frames,accel),3)* y4;

//                double xbu = xxRo/ 160 * 100+ x_blur;
//                double ybu = yRo/ 90 * 100 + y_blur;

                double xbu = xxRo;
                double ybu = yRo;

                int startTimes= durations + jiange*i;
                strs = strs + "let t"+  a + "_" + i + " = tem(" + xbu + ""+ percentStr + "  ,"+ ybu +""+ percentStr + " )" +
                        " set t"+  a + "_" +i+"{  }"+ startTimes +"ms "+
                        " then set t"+  a + "_" + i +"{alpha = 1  }0ms "+
                        " then set t"+  a + "_" + i +"{ x="+ xbu  +""+ percentStr + "   y="+ ybu +""+ percentStr + "     }"+  afterimage_dur +"ms \n";
            }
            durations =durations + 286;
        }
//        System.out.println(strs);
//        System.out.println(x_pos);
//        System.out.println(y_pos);
    }


//        var ax = 3 * p1x - 3 * p2x + 1,
//                bx = 3 * p2x - 6 * p1x,
//                cx = 3 * p1x;
//        x= ((ax * t + bx) * t + cx ) * t
//    double  ax =3 * p1x - 3 * p2x + 1,
//            bx =3 * p1x - 3 * p2x + 1,
//            cx =3 * p1x - 3 * p2x + 1;

}
