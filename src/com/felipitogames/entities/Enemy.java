package com.felipitogames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.felipitogames.main.Game;
import com.felipitogames.main.Sound;
import com.felipitogames.world.Camera;
import com.felipitogames.world.World;

public class Enemy extends Entity {
	private double speed = 2.4;

	private int maskx = 8, masky = 10, maskw = 17, maskh = 14;

	private int frames = 0, maxFrames = 12, index = 0, maxIndex = 3;

	private BufferedImage[] sprites;

	private int life = 3;

	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, null);
		sprites = new BufferedImage[4];
		sprites[0] = Game.spritesheet.getSprite(224, 32, 32, 32);
		sprites[1] = Game.spritesheet.getSprite(256, 32, 32, 32);
		sprites[2] = Game.spritesheet.getSprite(256 + 32, 32, 32, 32);
		sprites[3] = Game.spritesheet.getSprite(256, 32, 32, 32);
	}

	public void tick() {
		maskx = 8;
		masky = 10;
		maskw = 17;
		maskh = 14;

		if (isColiddingWithPlayer() == false) {
			if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
					&& !isColliding((int) (x + speed), this.getY())) {
				x += speed;
			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
					&& !isColliding((int) (x - speed), this.getY())) {
				x -= speed;
			}

			if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
					&& !isColliding(this.getX(), (int) (y + speed))) {
				y += speed;
			} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
					&& !isColliding(this.getX(), (int) (y - speed))) {
				y -= speed;
			}
		} else {
			// estamos colidindo
			if (Game.rand.nextInt(100) < 30) {
				Sound.damage.play();
				Game.player.life -= Game.rand.nextInt(5);
				Game.player.isDamaged = true;

			}
		}

		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex)
				index = 0;
		}
		collidingBullet();
		if (life <= 0) {
			destroySelf();
			return;
		}

		if (isDamaged) {

			this.damageCurrent++;
			if (this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;

			}
		}

	}

	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);

	}

	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (Entity.isColidding(this, e)) {
				Sound.shoot.play();
				isDamaged = true;
				life--;
				Game.bullets.remove(i);
				return;
			}

		}
	}

	public boolean isColiddingWithPlayer() {

		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 32, 32);

		return enemyCurrent.intersects(player);
	}

	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}

		return false;
	}

	public void render(Graphics g) {
		if (!isDamaged)

			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		else
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);

		// renderização da máscara

		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y,
		// maskw, maskh);
	}
}
