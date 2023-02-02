package com.felipitogames.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.felipitogames.entities.Player;
import com.felipitogames.main.Game;

public class UI {

	
	
	public void  render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(12, 10, 50, 8);
		g.setColor(Color.green);
		g.fillRect(12, 10, (int) ((Game.player.life/Game.player.maxlife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,10));
		
		
	}
	
}
