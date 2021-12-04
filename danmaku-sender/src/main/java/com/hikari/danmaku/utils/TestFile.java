package com.hikari.danmaku.utils;

public class TestFile {
 
	public static void main(String[] args) {
		int[][] arr={{1,2,3,4},{4,5,6,8},{7,8,9,0}};
		int[][] toarr=new int[3][3]; 
		
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