package com.hikari.danmaku.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hikari.danmaku.utils.ColorUtil.convertRGBToHex;

//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.bukkit.plugin.java.JavaPlugin;

public class PictureUtil {
    public static void main(String []args) {
//       double number = 12;
//        double number2 = 12.66666;
//        doubleFormat(number);
//        doubleFormat(number2);
//        getCharacter();
                String path="D:\\kbw.png";
        getBlackWhite(path);
//        getGradientColor(path,5);
//        String path="D:\\img2\\像素100.png";
//        getImagePixel(path);
//        test();
//        System.out.println(lineSplit("M 0 0 L 100 0",10));
    }


    public static String  test(){
            /*
            使用图片IO获取像素点
             */
            String path="D:\\img2\\100古.png";
            File file = new File(path);//导入文件
            BufferedImage bufImg = null;
            try {
                bufImg = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int height = bufImg.getHeight();
            int width = bufImg.getWidth();
            System.out.println("高："+ height +" 宽："+ width);

            int arg2=10; //黑白颜色的判断权值, 0~255  越小判断为黑的范围越大
            //获取s的 x,y,z坐标
            int x = 0;
            int y = 0;
            int z = 0;
            /*
            开始绘制图片
             */
            String str = "";
            int point;
            for(int zindx=z;zindx<z+width;zindx++){
                for(int yindx=y;yindx<y+height;yindx++){
                    point=bufImg.getRGB(zindx-z,(height-1)-(yindx-y))&0x0000ff;
//                    Block currentBlock = world.getBlockAt(x+10, yindx,zindx);
                    /*
                    为方便实现，使用黑白两种颜色的羊毛
                    条件判断会根据玩家输入的参数作为阈值
                     */

                    if(point>=arg2){
                        System.out.println("true");
                    }
                    else{
                        System.out.println("false");
                    }
                }
            }


        return "";
    }


    /**
     * 读取一张图片的RGB值
     */
    public  static void getImagePixel(String image) {
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

        String str ="";

        for(int y = minY; y < height; y++) {


            for(int x = minX; x < width; x++) {
                //获取包含这个像素的颜色信息的值, int型
                int pixel = bi.getRGB(x, y);
                //从pixel中获取rgb的值
                rgb[0] = (pixel & 0xff0000) >> 16; //r
                rgb[1] = (pixel & 0xff00) >> 8; //g
                rgb[2] = (pixel & 0xff); //b
                System.out.print("("+rgb[0] + "," + rgb[1] + "," + rgb[2] + ")");
//                System.out.print("16进制颜色："+convertRGBToHex(rgb[0],rgb[1],rgb[2]) );
//                convertRGBToHex(rgb[0],rgb[1],rgb[2]);

            }
            System.out.println();
        }

    }


    public  static List<String> getGradientColor(String imageUrl,int maxk) {
        List<String> colorList = new ArrayList<>();
        int[] rgb = new int[3];
        File file = new File(imageUrl);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = bi.getWidth();
        int height = 1;
//        int height = bi.getHeight();
        int minX = bi.getMinX();

//        int minY = bi.getMinY();
        int minY = 0;
        String str ="";

        for(int y = minY; y < height; y++) {
            for(int x = minX; x < width; x++) {
                //获取包含这个像素的颜色信息的值, int型
                int pixel = bi.getRGB(x, y);
                //从pixel中获取rgb的值
                rgb[0] = (pixel & 0xff0000) >> 16; //r
                rgb[1] = (pixel & 0xff00) >> 8; //g
                rgb[2] = (pixel & 0xff); //b
                int partK = 0;
                if(maxk!=1){
                     partK= (int)Math.floor((width-1)/(maxk-1)) ;
                }else {
                    partK = x ;
                }
                if( x ==0 || x % partK == 0 ){
//                    System.out.print("16进制颜色："+convertRGBToHex(rgb[0],rgb[1],rgb[2]) +"\n" );
                    colorList.add(convertRGBToHex(rgb[0],rgb[1],rgb[2]));
                }
            }
        }
        return colorList;
    }




    public  static void  getCharacter(){
        String content = "一键三连";

//　　◢■■■■■■■█◤

        BufferedImage image = new BufferedImage(content.length()*12, 12, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setFont(new Font("simsun", Font.PLAIN, 12));
        g.drawString(content, 0, image.getHeight() - 2);
        int [] p = image.getRGB(0, 0, image.getWidth(), image.getHeight(), new int[image.getWidth()*image.getHeight()], 0, image.getWidth());
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                System.out.print(p[i*image.getWidth() + j] == -1 ? "█" : "　" + (j == image.getWidth()-1 ? "\n" : ""));
            }
        }
    }




    public  static void getBlackWhite(String image) {
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





//    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//        if (cmd.getName().equalsIgnoreCase("Drawit")) {
//            if (!(sender instanceof Player)) {
//                sender.sendMessage("Only players can use this command!");
//                return true;
//            }
//            // 检测指令执行者是玩家后，我们便可以安全的进行强制转换类操作。
//            Player s = (Player) sender;
//            //获取s的 x,y,z坐标
//            int x = (int)s.getLocation().getX()+70;
//            int y = 4;
//            int z = (int)s.getLocation().getZ();
//            //获得接口
//            World world=s.getWorld();
//            //获得命令后的参数
//            String arg = args[0];
//            int arg2=Integer.parseInt(args[1]); //黑白颜色的判断权值, 0~255  越小判断为黑的范围越大
//            String path="D:\\img2\\"+arg;
//            getLogger().info(path);
//            /*
//            ->未实现功能
//            根据url地址下载网络上的图片,
//            获取它的长和宽,每一个像素点对应MC里的12色羊毛
//             */
//
//
//            /*
//            使用图片IO获取像素点
//             */
//            File file = new File(path);//导入文件
//            BufferedImage bufImg = null;
//            try {
//                bufImg = ImageIO.read(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            int height = bufImg.getHeight();
//            int width = bufImg.getWidth();
//            /*
//            开始绘制图片
//             */
//            int point;
//            for(int zindx=z;zindx<z+width;zindx++){
//                for(int yindx=y;yindx<y+height;yindx++){
//                    point=bufImg.getRGB(zindx-z,(height-1)-(yindx-y))&0x0000ff;
//                    getLogger().info(point+"");
//                    Block currentBlock = world.getBlockAt(x+10, yindx,zindx);
//                    /*
//                    为方便实现，使用黑白两种颜色的羊毛
//                    条件判断会根据玩家输入的参数作为阈值
//                     */
//
//                    if(point>=arg2){
//                        currentBlock.setType(Material.SNOW_BLOCK);
//                    }
//                    else{
//                        currentBlock.setType(Material.COAL_BLOCK);
//                    }
//                }
//            }
//            return true;
//        }
//        return false;
//    }

}