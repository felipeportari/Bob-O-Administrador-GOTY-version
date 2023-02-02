package com.felipitogames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.felipitogames.main.Game;
import com.felipitogames.world.Camera;
import com.felipitogames.world.World;

public class BulletShoot extends Entity {

	private double dx;
	private double dy;
	private double spd = 9;

	// private int life = 300, curLife = 0;

	public BulletShoot(int x, int y, int width, int heigth, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, heigth, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		if (World.isFreeDynamic((int) (x + (dx * spd)), (int) (y + (dy * spd)), 3, 3)) {
			x += dx * spd;
			y += dy * spd;
		} else {
			Game.bullets.remove(this);
			return;
		}
		/*
		 * curLife++; if (curLife == life) { Game.bullets.remove(this); return; }
		 */
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}

}
