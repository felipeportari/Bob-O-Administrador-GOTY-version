package com.felipitogames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.felipitogames.main.Game;
import com.felipitogames.world.Camera;

public class Entity {
	
	
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*32, 0, 32, 32);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*32, 0, 32, 32);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*32, 32, 32, 32);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*32, 32, 32, 32);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(0, 64, 32, 32);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(256, 0, 32, 32);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(288, 0, 32, 32);
	public static BufferedImage GUN_DAMAGE_RIGHT = Game.spritesheet.getSprite(3*32,32*2 ,32,32);
	public static BufferedImage GUN_DAMAGE_LEFT = Game.spritesheet.getSprite(4*32,32*2,32,32);
	public static BufferedImage NPC_EN = Game.spritesheet.getSprite(64,64, 32, 32);
	protected double x;
	protected double y;
	protected int width;
	protected int height;            

private BufferedImage sprite;                                       

private int maskx, masky, mwidth, mheight;

public Entity(int x, int y, int width, int heigth, BufferedImage sprite) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = heigth;
	this.sprite = sprite;
	
	this.maskx = 0;
	this.masky = 0;
	this.mwidth = width;
	this.mheight = height;
	}

public void setMask(int maskx, int masky, int mwidth, int mheight) {
	this.maskx = maskx;
	this.masky = masky;
	this.mwidth = mwidth;
	this.mheight = mheight;
	}
	



public void setX(int newX) {
	this.x = newX;
}

public void setY(int newY) {
	this.y = newY;
}




    public int getX() {
	return (int)this.x;
}

     public int getY() {
	return (int)this.y;
} 

     public int getWidth() {
	return this.width;
}
     public int getHeigth() {
	return this.height;
   
     }
     
     public void tick() {
    	 
     }
     
     public static boolean isColidding(Entity e1,Entity e2) {
    	 Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
    	 Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
    	 
    	 return e1Mask.intersects(e2Mask);
     }
     
     public void render(Graphics g) {
    	 g.drawImage(sprite, this.getX() - Camera.x, getY() - Camera.y, null);
    	// g.setColor(Color.red);
    	// g.fillRect(this.getX() + maskx - Camera.x,this.getY() +masky - Camera.y, mwidth, mheight);
    	 
     }
     
}
