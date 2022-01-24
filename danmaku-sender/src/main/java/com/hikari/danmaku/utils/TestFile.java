package com.hikari.danmaku.utils;

import java.util.Random;

public class TestFile {
 
	public static void main(String[] args) {
		int[][] arr={{1,2,3,4},{4,5,6,8},{7,8,9,0}};
		int[][] toarr=new int[3][3];


//		Random random =new Random();
		int start = 60;
		int end = 60;
		int y = 50;
		for(int i=0;i<100;i++){
			int random1 = (int)(Math.random()*30+1);
			int random11 = (int)(Math.random()*10+1);
			int random2 = (int)(Math.random()*8);
			int random3 = (int)(Math.random()*8+1);
			int random33 = (int)(Math.random()*8+1);
			int random4 = (int)(Math.random()*35+1);

			int randomStart =start + random1 ;
			int randomEnd = end +random11;
			int randomY = y +random4;
			String t = "let t"+ i +"=tTem("+randomStart +"%,"+ randomY +"%) " +
					"set t"+ i +" {alpha = 1 }"+ random2 +"."+ random33 +"s,\"step-end\" " +
					"then set t"+ i + " {x = "+ "60"+"%}0."+ random3 +"s";
			System.out.println(t);


		}






		System.out.println("反转前");
		
		int k=0;		
		for(int[] x:arr){
			for(int i=0;i<x.length;i++){
				System.out.print(x[i]+" ");
			    toarr[i][k]= arr[k][i];
			}
			k++;
			System.out.println("");
		}
		
		System.out.println("反转后");
		
		for(int[] x:toarr){
			for(int i=0;i<x.length;i++){				
				System.out.print(x[i]+" ");
			}
			k++;
			System.out.println("");
		}
	}
	
	static void Print(String outinfo){
		System.out.println(outinfo); 
	}
}