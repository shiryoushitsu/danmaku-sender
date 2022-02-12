package com.hikari.danmaku.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class CommonUtil {
    public static void main(String []args)  {
       String txt = "一二三层";
        System.out.println(rowToColumn(txt));
    }

    //double保留2位小数
    public static double  d2(double number){
        BigDecimal bigDecimal = new BigDecimal(number).setScale(2, RoundingMode.HALF_UP);
        double newDouble = bigDecimal.doubleValue();
        return newDouble;
    }

    //double保留3位小数
    public static double  d3(double number){
        BigDecimal bigDecimal = new BigDecimal(number).setScale(3, RoundingMode.HALF_UP);
        double newDouble = bigDecimal.doubleValue();
        return newDouble;
    }


    //double转string 保留2位小数，去.0
    public static String  doubleFormat(double number){
        DecimalFormat decimalFormat = new DecimalFormat("###################.##");
        return decimalFormat.format(number);
    }

    public static String  df(double number){
        DecimalFormat decimalFormat = new DecimalFormat("###################.##");
        return decimalFormat.format(number);
    }

    public static String  df3(double number){
        DecimalFormat decimalFormat = new DecimalFormat("###################.###");
        return decimalFormat.format(number);
    }

    //处理高级弹幕百分比坐标
    public static String  df3m7(double number){
        if(number >= 1){
            number = 0.999;
        }else if (number <=0 ){
            number = 0.001;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###################.###");
        return decimalFormat.format(number);
    }


    public static String  df4(double number){
        DecimalFormat decimalFormat = new DecimalFormat("###################.####");
        return decimalFormat.format(number);
    }

    //高级版处理double数字（大于1时取整，小于1时原版）
    public static String doubleTrans(double number){
        if(Math.round(number)-number==0){
            return String.valueOf((long)number);
        }
        if(number>1){
            return String.valueOf((int)Math.floor(number));
        }
        return String.valueOf(df3(number));
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static Integer getUUIDInOrderId(){
        Integer orderId=UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return orderId;
    }

    //1.int转String
    public static String int2String(int number){
        return Integer.toString(number);
    }

    //2.int转Double
    public static Double int2Double(int number){
        return Double.valueOf(number);
    }

    //.long转int //Integer.valueOf(String.valueOf(timesOffset)
    public static int long2Int(long number){
        return (int)number;
    }

    //解析字符串，分开为
    public static List<String> string2CharKara(String str){
        List<String> stringArray = new ArrayList<>();
         str = "よう312 wqe qeqeqeq";

        int len =  str.getBytes().length;
        try {
            System.out.println(str.getBytes().length);
            System.out.println("GBK=="+str.getBytes("GBK").length);
            System.out.println("UTF-8=="+str.getBytes("UTF-8").length);
            System.out.println("GB2312=="+str.getBytes("GB2312").length);
            System.out.println("ISO-8859-1=="+str.getBytes("ISO-8859-1").length);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return stringArray;
    }


    //将16进制转换为二进制
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String lattice2Hex(String number){

        String string =number;
//        String substring = string.substring(0, string.length() - 1);
        String[] result = string.split(",");
        String aa="";
        for(int i =0 ;i<result.length;i++){
          String a = hex2Two(result[i].toString()) ;
            aa = aa + a;
            if( i!=0 && (i+1) % 2 == 0){
                aa =aa+"\n";
            }
        }

        return aa;
    }
    //0x01 转4
    public static String hex2Two(String number){
        String str = "";
        for(int i=2;i<4;i++){
            char grade = number.charAt(i);

            String str2 = "";
            switch(grade)
            {
                case '0' :
                    str2 = "0000";
                    break;
                case '1' :
                    str2 = "0001";
                    break;
                case '2' :
                    str2 = "0010";
                    break;
                case '3' :
                    str2 = "0011";
                    break;
                case '4' :
                    str2 = "0100";
                    break;
                case '5' :
                    str2 = "0101";
                    break;
                case '6' :
                    str2 = "0110";
                    break;
                case '7' :
                    str2 = "0111";
                    break;
                case '8' :
                    str2 = "1000";
                    break;
                case '9' :
                    str2 = "1001";
                    break;
                case 'a' :
                    str2 = "1010";
                    break;
                case 'b' :
                    str2 = "1011";
                    break;
                case 'c' :
                    str2 = "1100";
                    break;
                case 'd' :
                    str2 = "1101";
                    break;
                case 'e' :
                    str2 = "1110";
                    break;
                case 'f' :
                    str2 = "1111";
                    break;
                default :
                    System.out.println("错误");
            }
            str =str + str2;
        }
//        System.out.println(str);

        return str;
    }


    // 往字前面添加空格（M7逐字用）
    public static String addBlankSpace(String word, int k){
        String str = word;
        String fill = "";
        for(int i = 0; i < k; i++){
            fill = fill + "　";//"　","  "
        }
        str = fill + word;
        return str;
    }

    //添加特殊空格（M1竖排用）
    public static String addSpecialBlankSpace(String word,int k){
        String str = word;
        String fill = "";
        for(int i=0;i<k;i++){
            fill = fill +" ";//"　","  "
        }
        str =  word + fill;
        return str;
    }

    //随机正反范围（-100,-50）（50,100）
    public static int randomPlus(int max,int min,int flag){
        Random rand = new Random();
        int randNumber = rand.nextInt(max - min + 1) + min;
        if(flag ==1 ){
            int x = Math.random() > 0.5 ? 1 : -1;
            randNumber = randNumber * x;
        }
        return randNumber;
    }

    public  static List<String>  getCharacter(String content){
        List<String> contentList = new ArrayList<>();
//        String content = "一键三连";
//　　◢■■■■■■■█◤
        BufferedImage image = new BufferedImage(content.length()*16, 16, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setFont(new Font("方正像素16", Font.PLAIN, 16));
//        g.setFont(new Font("meiryo", Font.PLAIN, 12));
        g.drawString(content, 0, image.getHeight() - 2);
        int [] p = image.getRGB(0, 0, image.getWidth(), image.getHeight(), new int[image.getWidth()*image.getHeight()], 0, image.getWidth());
        for (int i = 0; i < image.getHeight(); i++) {
            String  charStr  ="";
            for (int j = 0; j < image.getWidth(); j++) {
                String oneChar = p[i*image.getWidth() + j] == -1 ? "█" : "　";
                charStr = charStr + oneChar;
                System.out.print(p[i*image.getWidth() + j] == -1 ? "█" : "　" + (j == image.getWidth()-1 ? "\n" : ""));
            }
            contentList.add(charStr);
        }

        return contentList;
    }


    //rad
    public static String sentenceCircle(int centerx,int centery,double angle,  int sentenceR){
        int r = sentenceR;
        int centerX = centerx;
        int centerY = centery;
        double angleNum = angle + 180;
        double xR1 = centerX + Math.cos(angleNum  * Math.PI / 180) * r  ;
        double yR1 = centerY + Math.sin(angleNum * Math.PI / 180) * r ;
//        System.out.println(xR1+ "  " +yR1);
        return doubleTrans(xR1)+","+doubleTrans(yR1);
    }


    //[注]：编程时为了提高效率避免浮点运算，往往把除法放在最后面，这样公式就成了：Gradient = A + (B-A) * N / Step
    public static  List<String>  createGradientsColor(List<String> oldColorList, int maxk,int repeat){
//        int oldR = 10 ,oldG =10,oldB =10;
//        int newR = 100 ,newG =200, newB =75;
//        Color oldColor = new Color(oldR,oldG,oldB);  //初始颜色
//        Color newColor = new Color(newR,newG,newB);  //结束颜色
//        Color decodeColor  = Color.decode("#FFCCEE");
//        System.out.println(decodeColor.getRed()+","+decodeColor.getGreen()+","+decodeColor.getBlue());
//        for(int k =0;k<oldColorList.size();k++){
            Color oldColor  = Color.decode("#"+ oldColorList.get(0));
            Color newColor  = Color.decode("#"+ oldColorList.get(1));
            if(oldColorList.size() == 3  ){
                if("1".equals(oldColorList.get(2)) ){
                    repeat = 1;
                }
            }
//        }
        int oriMaxk = maxk;
        List<String> colorList = new ArrayList<>();
        if(repeat == 1){
            if(maxk%2!=0){//奇数 9
                maxk = (maxk+1)/2;
            }else {
               maxk = maxk/2;
            }
        }

        int step = maxk-1;  //分多少步完成
        for(int i = 0 ; i <= step; i++){
            int r = oldColor.getRed() + (newColor.getRed() - oldColor.getRed())*i/step;
            int g = oldColor.getGreen() + (newColor.getGreen() - oldColor.getGreen())*i/step;
            int b = oldColor.getBlue() + (newColor.getBlue() - oldColor.getBlue())*i/step;
            Color tempColor = new Color(r,g,b);
            String newChangeColor = String.format("#%06x",tempColor.getRGB()& 0x00FFFFFF);
//            System.out.println(newChangeColor);
            colorList.add(newChangeColor.substring(1));
        }

        if(repeat == 1){
            if(oriMaxk%2!=0){//奇数
                List<String> changeList = new ArrayList<>(colorList);
                changeList.remove(changeList.size()-1);
                Collections.reverse(changeList);
                colorList.addAll(changeList);
            }else {
                List<String> changeList = new ArrayList<>(colorList);
                Collections.reverse(changeList);
                colorList.addAll(changeList);
            }

        }

        System.out.println(colorList);
        return colorList;
    }


    public static String sentenceCircle2(int centerx,int centery,double angle,  int sentenceR){
        int r = sentenceR;
        int centerX = centerx;
        int centerY = centery;
        double angleNum = angle + 180;
        double xR1 = centerX + Math.cos(angleNum  * Math.PI / 180) * r ;
        double yR1 = centerY + Math.sin(angleNum * Math.PI / 180) * r*16/9 ;
//        System.out.println(xR1+ "  " +yR1);
        return df(xR1)+","+df(yR1);
    }

    /**
     * 判断文件的编码格式
     * @param fileName :file
     * @return 文件编码格式
     * @throws Exception
     */
    public static String codeString(File fileName) throws Exception{
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        IOUtils.closeQuietly(bin);
        return code;
    }

    public static String rowToColumn(String content){
        int length = content.length();
        String result = "";
        List<String> strList = new ArrayList<>();
        for (int i = 0;i < length;i++) {
            String word = String.valueOf(content.charAt(i));
            if(i < length -1){
                result = result + word +"\\n";
            }else {
                result = result + word;
            }


        }
        return result;
    }
}


