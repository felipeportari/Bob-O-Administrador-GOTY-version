package com.felipitogames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.felipitogames.entities.BulletShoot;
import com.felipitogames.entities.Enemy;
import com.felipitogames.entities.Entity;
import com.felipitogames.entities.Npc;
import com.felipitogames.entities.Player;
import com.felipitogames.graficos.Spritesheet;
import com.felipitogames.graficos.UI;
import com.felipitogames.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 280;
	public static final int HEIGHT = 200;
	public static final int SCALE = 3;

	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	private BufferedImage image;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;

	public static World world;

	public static Player player;

	public static Random rand;

	public UI ui;

	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;

	public Menu menu;

	public Npc npc;

	public Game() {
		Sound.music.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		;
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		// inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		menu = new Menu();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 32, 32, spritesheet.getSprite(64, 0, 32, 32));
		entities.add(player);
		world = new World("/level1.png");

	}

	public void initFrame() {
		frame = new JFrame("Bob o Administrador: Behind the Company"); // nome do game
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}

	public void tick() {
		if (gameState == "NORMAL") {
			this.restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			if (enemies.size() == 0 && CUR_LEVEL != 3) {
				// avan�ar para o pr�ximo level
				CUR_LEVEL++;
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level" + CUR_LEVEL + ".png";
				// System.out.println(newWorld);
				World.restartGame(newWorld);

			}
		} else if (gameState == "WIN") {
			this.framesGameOver++;
			if (this.framesGameOver == 25) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
			if (restartGame) {
				this.restartGame = false;
				this.gameState = "MENU";
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				// System.out.println(newWorld);
				World.restartGame(newWorld);
			}

		} else if (gameState == "GAME_OVER") {
			this.framesGameOver++;
			if (this.framesGameOver == 25) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}

			if (restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				// System.out.println(newWorld);
				World.restartGame(newWorld);
			}

		} else if (gameState == "MENU") {
			menu.tick();
		}

	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;

		}
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		/// Renderiza��o do game
		// Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);
		/***/

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 25));
		g.setColor(Color.white);
		g.drawString((int) Game.player.life + "/" + (int) Game.player.maxlife, 68, 50);
		g.drawString("Muni��o: " + player.ammo, 38, 25);
		if (gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.red);
			g.drawString("Game Over!", (WIDTH * SCALE) / 2 - 84, (HEIGHT * SCALE) / 2);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.white);
			if (showMessageGameOver)
				g.drawString(">Pressione Enter para reiniciar< ", (WIDTH * SCALE) / 2 - 230, (HEIGHT * SCALE) / 2 + 40);
		} else if (gameState == "MENU") {
			menu.render(g);
		} else if (gameState == "WIN") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.green);
			g.drawString("Voc� Venceu!", (WIDTH * SCALE) / 2 - 84, (HEIGHT * SCALE) / 2);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.white);
			if (showMessageGameOver)
				g.drawString(">Pressione Enter para reiniciar< ", (WIDTH * SCALE) / 2 - 230, (HEIGHT * SCALE) / 2 + 40);

		}
		bs.show();

	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;

		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if (gameState == "MENU") {
				menu.up = true;
			}

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if (gameState == "MENU") {
				menu.down = true;
			}

		}
		if (e.getKeyCode() == KeyEvent.VK_L) {
			player.shoot = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if (gameState == "MENU") {
				menu.enter = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}

	}

	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
