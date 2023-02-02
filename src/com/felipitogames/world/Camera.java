package com.felipitogames.world;

public class Camera {
	
	public static int x = 0;
	public static int y = 0;

public static int clamp(double d, int Min, int Max ) {
	if(d < Min) {
		d = Min;
	} 
	
	if(d > Max) {
		d = Max;
	}
	
	return  (int) d;
	
  }
}
