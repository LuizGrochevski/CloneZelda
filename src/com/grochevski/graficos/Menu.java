package com.grochevski.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.grochevski.main.Game;

public class Menu {

	public String[] options = { "novo jogo", "carregar", "sair", "reiniciar"};

	public int currentOption = 0;
	public int maxOption = options.length - 1;

	public boolean up, down, enter, sair;

	public boolean pause = false;

	public void tick() {
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}

		if (enter) {
			enter = false;
			if (options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			} else if  (options[currentOption] == "sair") {
				System.exit(1);
			}
		}
		
	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		g.setColor(new Color(200, 150, 0));
		g.setFont(new Font("Arial", Font.BOLD, 60));
		g.drawString("GROCHEVSKI", (Game.WIDTH * Game.SCALE) / 2 - 200, (Game.HEIGHT) - 120);

		if (pause == false) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 65, (Game.HEIGHT) - 40);
		} else {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("Continuar", (Game.WIDTH * Game.SCALE) / 2 - 60, (Game.HEIGHT) - 40);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 85, (Game.HEIGHT) + 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 25, (Game.HEIGHT) + 60);

		if (options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 90, (Game.HEIGHT) - 40);
		} else if (options[currentOption] == "carregar") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 110, (Game.HEIGHT) + 10);
		} else if (options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT) + 60);
		}

	}

}
