package com.grochevski.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.grochevski.main.Game;
import com.grochevski.world.Camera;

public class Spell extends Entity {
	
	private double dx;
	private double dy;
	private double speed = 3;
	
	private int time = 80, currentTime = 0;

	public Spell(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x+=dx*speed;
		y+=dy*speed;
		currentTime++;
		if(currentTime == time) {
			Game.spell.remove(this);
			return;
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
	
}
