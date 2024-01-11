package com.grochevski.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.grochevski.entities.Player;

public class UI {

	public void render(Graphics g) {
		
		g.setColor(Color.RED);
		g.fillRect(15, 15, 70, 7);
		g.setColor(Color.GREEN);
		g.fillRect(15, 15, (int)((Player.life/Player.maxLife)*70), 7);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 7));
		g.drawString((int)Player.life + "/" + (int)Player.maxLife, 37, 21);
	}
	
}
