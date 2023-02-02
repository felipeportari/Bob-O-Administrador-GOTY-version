package com.felipitogames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.felipitogames.main.Game;
import com.felipitogames.main.Sound;
import com.felipitogames.world.Camera;
import com.felipitogames.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 2.8;

	private int frames = 0, maxFrames = 9, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;

	private BufferedImage playerDamage;
	private BufferedImage playerDamage2;

	private boolean arma = false;

	public int ammo = 10;

	public boolean isDamaged = false;
	private int damageFrames = 0;

	public boolean shoot = false, mouseShoot = false;

	public double life = 100, maxlife = 100;
	public int mx, my;

	public Player(int x, int y, int width, int heigth, BufferedImage sprite) {
		super(x, y, width, heigth, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 32, 32, 32);
		playerDamage2 = Game.spritesheet.getSprite(32, 32, 32, 32);

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(64 + (i * 32), 0, 32, 32);
		}

		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(64 + (i * 32), 32, 32, 32);
		}

	}

	public void tick() {
		moved = false;

		if (right && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			y += speed;
		}
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex)
					index = 0;
			}
		}

		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();

		if (isDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 5) {
				this.damageFrames = 0;
				isDamaged = false;
			}

		}

		if (shoot) {
			shoot = false;
			if (arma && ammo > 0) {

				ammo--;
				// criar bala e atirar
				int dx = 0;
				int px = 0;
				int py = 0;
				if (dir == right_dir) {
					px = 25;
					py = 13;
					dx = 1;
				} else {
					px = 3;
					py = 13;
					dx = -1;
				}

				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}
		if (mouseShoot) {
			mouseShoot = false;

			if (arma && ammo > 0) {
				ammo--;
				// criar bala e atirar

				int px = 0, py = 13;
				double angle = 0;
				if (dir == right_dir) {
					px = 25;

					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));

				} else {
					px = 3;

					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));

				}

				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}

		if (life <= 0) {
			// gameover
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		updateCamera();

	}

	public void updateCamera() {
		Camera.x = Camera.clamp((int) this.getX() - (Game.WIDTH / 2.2), 0, World.WIDTH * 32 - Game.WIDTH);
		Camera.y = Camera.clamp((int) this.getY() - (Game.HEIGHT / 2.5), 0, World.HEIGHT * 32 - Game.HEIGHT);
	}

	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
				if (Entity.isColidding(this, atual)) {
					arma = true;
					Game.entities.remove(atual);
				}
			}
		}

	}

	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Bullet) {
				if (Entity.isColidding(this, atual)) {
					Sound.bullet.play();
					ammo += 10;
					// System.out.println("Munição atual: "+ ammo);
					Game.entities.remove(atual);
				}
			}
		}

	}

	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof LifePack) {
				if (Entity.isColidding(this, atual)) {
					Sound.powerup.play();
					life += 15;
					if (life > 100)
						life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (arma) {
					// desenhar arma para direita
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 10 - Camera.x, this.getY() - 4 - Camera.y, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (arma) {
					// desenhar arma para a esquerda
					g.drawImage(Entity.GUN_LEFT, this.getX() - 10 - Camera.x, this.getY() - 4 - Camera.y, null);
				}
			}

		} else {
			if (dir == right_dir) {
				g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else {
				g.drawImage(playerDamage2, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			if (arma) {
				if (dir == left_dir) {
					g.drawImage(Entity.GUN_DAMAGE_LEFT, this.getX() - 10 - Camera.x, this.getY() - 4 - Camera.y, null);

				} else
					g.drawImage(Entity.GUN_DAMAGE_RIGHT, this.getX() + 10 - Camera.x, this.getY() - 4 - Camera.y, null);
			}
		}
	}
}
