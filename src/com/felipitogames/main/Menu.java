package com.felipitogames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"novo jogo", "carregar jogo", "sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public void tick() {
		if(up){
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				
			}else if(options [currentOption] == "sair") {
				System.exit(1);
			}
		}
		
	}
	 
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color (0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("arial", Font.BOLD,30));
		g.drawString(">Bob o ADM: Behind The Company<", (Game.WIDTH*Game.SCALE) / 2 - 258, (Game.HEIGHT*Game.SCALE) / 2 - 130);
		
		// opçoes
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,23));
		if(pause == false)
		g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) / 2 - 51 , (Game.HEIGHT*Game.SCALE) / 2 - 40 );
		else
			g.drawString("Continuar", (Game.WIDTH*Game.SCALE) / 2 - 51 , (Game.HEIGHT*Game.SCALE) / 2 - 40 );
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) / 2 - 51, (Game.HEIGHT*Game.SCALE) / 2 - 10 );
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) / 2 - 51, (Game.HEIGHT*Game.SCALE) / 2 + 20 );
		
		if(options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 65, (Game.HEIGHT*Game.SCALE) / 2 - 40);
		}else if(options[currentOption] == "carregar jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 65, (Game.HEIGHT*Game.SCALE) / 2 - 10);
		}else if(options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 65, (Game.HEIGHT*Game.SCALE) / 2 + 20);
	}
	}
}
