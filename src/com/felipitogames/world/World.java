package com.felipitogames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.felipitogames.entities.Bullet;
import com.felipitogames.entities.Enemy;
import com.felipitogames.entities.Entity;
import com.felipitogames.entities.LifePack;
import com.felipitogames.entities.Npc;
import com.felipitogames.entities.Player;
import com.felipitogames.entities.Weapon;
import com.felipitogames.graficos.Spritesheet;
import com.felipitogames.main.Game;

public class World {

	public static boolean npc_lvl = false;
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 32;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 32, yy * 32, Tile.TILE_FLOOR);

					if (pixelAtual == 0xFF000000) {
						// ch�o
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 32, yy * 32, Tile.TILE_FLOOR);

					} else if (pixelAtual == 0xFFFFFFFF) {
						// parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 32, yy * 32, Tile.TILE_WALL);

					} else if (pixelAtual == 0xFF0026FF) {
						// player
						Game.player.setX(xx * 32);
						Game.player.setY(yy * 32);

					} else if (pixelAtual == 0xFFFF0000) {
						// enemy
						Enemy en = new Enemy(xx * 32, yy * 32, 32, 32, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);

					} else if (pixelAtual == 0xFFFF6A00) {
						// weapon
						Weapon weapon = new Weapon(xx * 32, yy * 32, 32, 32, Entity.WEAPON_EN);
						weapon.setMask(7, 15, 16, 9);
						Game.entities.add(weapon);

					} else if (pixelAtual == 0xff4CFF00) {
						// lifepack
						LifePack pack = new LifePack(xx * 32, yy * 32, 32, 32, Entity.LIFEPACK_EN);
						pack.setMask(5, 16, 20, 16);
						;
						Game.entities.add(pack);

					} else if (pixelAtual == 0xFFFFD800) {
						// bullet
						Bullet bullet = new Bullet(xx * 32, yy * 32, 32, 32, Entity.BULLET_EN);
						bullet.setMask(8, 16, 16, 15);
						Game.entities.add(bullet);
					} else if (pixelAtual == 0xFFFF00DC) {
						Npc npc = new Npc(xx * 32, yy * 32, 32, 32, Entity.NPC_EN);
						Game.entities.add(npc);

					}

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFreeDynamic(int xnext, int ynext, int width, int height) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + width - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + height - 1) / TILE_SIZE;

		int x4 = (xnext + width - 1) / TILE_SIZE;
		int y4 = (ynext + height - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

	}

	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

	}

	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 32, 32, Game.spritesheet.getSprite(64, 0, 32, 32));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;

	}

	public void render(Graphics g) {
		int xstart = Camera.x / 32;
		int ystart = Camera.y / 32;

		int xfinal = xstart + (Game.WIDTH / 30);
		int yfinal = ystart + (Game.HEIGHT / 28);

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}

}
