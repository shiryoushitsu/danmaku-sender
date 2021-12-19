package com.hikari.danmaku.utils;

import cn.hutool.core.util.NumberUtil;
import com.hikari.danmaku.entity.Position;
import com.hikari.danmaku.entity.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 坐标相关工具类
 *
 */
public class CoordinateUtil {
    public static void main(String[] args) {
        List<Vector> vector = path2Vector("m 800 105 l 499 102 l 652 104 ");
        String vPath = "";
        for(Vector v : vector){
            vPath =  vPath + v.getStr().toUpperCase() + "" +  Math.round(v.getPosition().get(0).getX()) +"," +Math.round(v.getPosition().get(0).getY());
        }
        System.out.println( vPath );
    }

    /**
     * 通过分隔符拆分句子,并保留分隔符
     */
    public static List<Vector> path2Vector(String sentence){
        List<Vector>  vector= new ArrayList<>();
        List<Position>  positions= new ArrayList<>();

        //1. 定义匹配模式
        Pattern p = Pattern.compile("[a-zA-z]");
        Matcher m = p.matcher(sentence);

        //2. 拆分句子[拆分后的句子符号也没了]
        String[] words = p.split(sentence);

        //3. 保留原来的分隔符
        if(words.length > 0){
            int count = 1;
            while(count < words.length){
                if(m.find()){
                    words[count] =  m.group() + words[count] ;
                }
                count++;
            }
        }

        // 分隔每段命令
        for(int i = 1 ;i<words.length ;i++){
            Vector vec = new Vector();

            String word =  words[i];
            //去除逗号
            String wordFilter = word.replaceAll(","," ");

            String[] lineStrArray = wordFilter.split("\\s+");//根据空格分离

            String method = lineStrArray[0];
            vec.setStr(method);

            //命令判断
            if("v".equals(method)||"h".equals(method)||"V".equals(method)||"H".equals(method)){
                List<Position> listPos = new ArrayList<>();
                Position position = new Position(Double.parseDouble(lineStrArray[1]),Double.parseDouble(lineStrArray[1]));
//                position.setX();
//                position.setY();
                listPos.add(position);
                vec.setPosition(listPos);
            }else {
                List<Position> listPos = new ArrayList<>();
                for (int j = 1 ; j <=(lineStrArray.length-1)/2 ;j++){
                    Position pos = new Position(Double.parseDouble(lineStrArray[2*(j-1) + 1]),Double.parseDouble(lineStrArray[2*(j-1) + 1 + 1]));
//                    double doubleX =  ;
//                    double doubleY =  ;
//                    pos.setX(doubleX);
//                    pos.setY(doubleY);
                    listPos.add(pos);
                }
                vec.setPosition(listPos);
            }

            vector.add(vec);
        }

        //将首命令小m改为绝对位置M
        vector.get(0).setStr("M");

        return vector;
    }


    /**
     * 解析Vector】将path坐标解析转为转为List<Vector>【带转换】
     */
    public static String vector2Path(List<Vector> vec){
        String newPath = "";
        for(int i = 0;i<vec.size();i++){
            List<Position>  posiList =  vec.get(i).getPosition();
            String method = vec.get(i).getStr();

            newPath = newPath +  method + " ";
            //命令判断
            if("v".equals(method)||"h".equals(method)||"V".equals(method)||"H".equals(method)){
                for(int j = 0;j<posiList.size();j++){
                    double x= posiList.get(j).getX();
//                    int power = 1;
//                    if("v".equals(method)){
//                        x = x *  power ;
//                    }else  if("h".equals(method)){
//                        x = x *  power ;
//                    }

                    newPath = newPath +  x + " " ;
                }
            }
            else if ("z".equals(method)){

            }
            else {
                for(int j = 0;j<posiList.size();j++){
                    double x= posiList.get(j).getX();
                    double y= posiList.get(j).getY();


//                    // 转换
//                   String xyStr =  scaleFilter(x,y,0,0,2,2);
//                   String[] xyStrArray = xyStr.split(",");
//
//                    double xFilter = Double.valueOf( xyStrArray[0]);
//                    double yFilter = Double.valueOf( xyStrArray[1]);
//
//                    x = xFilter;
//                    y = yFilter;

                    x = x;
                    y = y;
                    posiList.get(j).setX(x);
                    posiList.get(j).setY(y);

                    newPath = newPath +  x + " " + y + " ";
                }
            }
        }
        // z闭合
        newPath = newPath +  "z" + " ";
//        System.out.println(newPath);
        return  newPath ;
    }

    /**
     * 1.将path平移（path为相对路径时）
     */
    public static String pathMoveFirst(String path,double endX,double endY){
        List<Vector> vec =new ArrayList<>();
        vec = path2Vector(path);

        double  StartX = vec.get(0).getPosition().get(0).getX();
        double  StartY = vec.get(0).getPosition().get(0).getY();

        vec.get(0).getPosition().get(0).setX(endX);
        vec.get(0).getPosition().get(0).setY(endY);
        String newPath = vector2Path(vec);

        return  newPath ;
    }

    /**
     * 2.将path围绕点（a，b）旋转（path为相对路径时）（平移转）
     */
    public static String pathRotateFirst(String path,double a,double b,double rad){
        List<Vector> vec =new ArrayList<>();
        vec = path2Vector(path);

        double  StartX = vec.get(0).getPosition().get(0).getX();
        double  StartY = vec.get(0).getPosition().get(0).getY();

        String xyStr = rotateFilter(StartX,StartY,a,b,rad);
        String[] xyStrArray = xyStr.split(",");
        double endX =Double.valueOf( xyStrArray[0]);
        double endY =Double.valueOf( xyStrArray[1]);

        vec.get(0).getPosition().get(0).setX(endX);
        vec.get(0).getPosition().get(0).setY(endY);
        String newPath = vector2Path(vec);

        return  newPath ;
    }

    /**
     * 2.5将path围绕点（a，b）旋转（path为绝对路径时）（每个坐标转）（中心转）
     */
    public static String pathRotateSecond(String path,double a,double b,double rad){
        List<Vector> vec =new ArrayList<>();
        vec = path2Vector(path);

        for(int i = 0 ;i<vec.size();i++){
            double  StartX = vec.get(i).getPosition().get(0).getX();
            double  StartY = vec.get(i).getPosition().get(0).getY();
            String xyStr = rotateFilter(StartX,StartY,a,b,rad);
            String[] xyStrArray = xyStr.split(",");
            double endX =Double.valueOf( xyStrArray[0]);
            double endY =Double.valueOf( xyStrArray[1]);

            vec.get(i).getPosition().get(0).setX(endX);
            vec.get(i).getPosition().get(0).setY(endY);

        }

        String newPath = vector2Path(vec);

        return  newPath ;
    }

    /**
     * 3.将path围绕点（a，b）放大c倍（path为绝对路径时）（每个坐标转）（中心转）
     */
    public static String pathScaleSecond(String path,double a,double b,double Scale){
        List<Vector> vec =new ArrayList<>();
        vec = path2Vector(path);

        for(int i = 0 ;i<vec.size();i++){
            double  StartX = vec.get(i).getPosition().get(0).getX();
            double  StartY = vec.get(i).getPosition().get(0).getY();
            String xyStr = scaleFilter(StartX,StartY,a,b,Scale,Scale);
            String[] xyStrArray = xyStr.split(",");
            double endX =Double.valueOf( xyStrArray[0]);
            double endY =Double.valueOf( xyStrArray[1]);

            vec.get(i).getPosition().get(0).setX(endX);
            vec.get(i).getPosition().get(0).setY(endY);

        }

        String newPath = vector2Path(vec);

        return  newPath ;
    }

    /**
     * 转换1，以任意点（a,b）进行x方向缩放c倍、有方向缩放d倍   Scale
     */
    public static String scaleFilter(double x,double y,double a,double b,double c,double d){
        //a+(x-a)*c ,b+(y-b)*d
        String path = "";
        double changeX;
        double changeY;
        String changeXY = "";

        //转换
        changeX = a+(x-a)*c;
        changeY = b+(y-b)*d;

        //保留两位小数
        changeX = Double.valueOf(String.format("%.2f", changeX ));
        changeY = Double.valueOf(String.format("%.2f", changeY ));

        changeXY = changeX +","+ changeY;
        return  changeXY ;
    }

    /**
     * 转换2：平面中围绕任意点旋转(x,y 值，中心a b值， rad是角度 ) 返回100,100 这样的值
     */
    public static String rotateFilter(double x,double y,double a,double b,double rad){
        String path = "";
        double changeX;
        double changeY;
        String changeXY = "";
        rad=Math.toRadians(rad);

        //转换
        changeX =(x-a)*Math.cos(rad) +(y-b)*Math.sin(rad)+a;
        changeY =b-(x-a)*Math.sin(rad)+(y-b)*Math.cos(rad);

        //保留两位小数
        changeX = Double.valueOf(String.format("%.2f", changeX ));
        changeY = Double.valueOf(String.format("%.2f", changeY ));

        changeXY = changeX +","+ changeY;
//        System.out.println(changeXY);

        return  changeXY ;
    }

    /**
     * 转换2：平面中围绕任意点旋转(x,y 值，中心a b值， rad是角度 )
     * V2
     */
    public static Position rotateTransform(double x,double y,double a,double b,double rad){
        double changeX,changeY;
        rad = Math.toRadians(rad);

        //转换
        changeX =(x-a)*Math.cos(rad) + (y-b)*Math.sin(rad)+a;
        changeY =b-(x-a)*Math.sin(rad) + (y-b) *Math.cos(rad);

        //保留两位小数
        changeX = NumberUtil.round(changeX,2).doubleValue();
        changeY = NumberUtil.round(changeY,2).doubleValue();

        return new Position(changeX,changeY) ;
    }


    /**
     * 丝线：转换过滤
     */
    public static String  curviFiller(String path,Integer k,Integer maxk){
        List<Vector> vec = path2Vector(path);

        for(int i = 0;i<vec.size();i++){
            List<Position>  posiList =  vec.get(i).getPosition();
            for(int j = 0;j<posiList.size();j++){
                double x= posiList.get(j).getX();
                double y= posiList.get(j).getY();
//                x = Math.round(x);
//                y = Math.round(y+(x+400)*40*Math.sin(k*Math.PI*4/maxk+(x+400)*Math.PI*4/1600)/1600);
//                y = y+(x+1800)*80*Math.sin(k*Math.PI*4/maxk+(x+800)*Math.PI*4/1600)/3200;
                x = x;
                y = y+(x+800)*40*Math.sin( k*Math.PI*4/maxk + (x+500)*Math.PI*4/3200 )/6400;
                posiList.get(j).setX(x);
                posiList.get(j).setY(y);
            }
        }
        String newPath = "";
        for(int ii = 0;ii<vec.size();ii++){
            String strL = vec.get(ii).getStr();
            List<Position>  posiList =  vec.get(ii).getPosition();
            newPath = newPath + strL + " ";
            for(int kk = 0 ; kk<posiList.size();kk++){
                posiList.get(kk).getX();
                posiList.get(kk).getY();
                newPath = newPath +  CommonUtil.df( posiList.get(kk).getX() )  + " "+ CommonUtil.df( posiList.get(kk).getY() )   + " ";
            }

        }
//        System.out.println(newPath);
        return  newPath ;
    }

}
