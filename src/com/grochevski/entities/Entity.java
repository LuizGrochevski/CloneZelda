package com.grochevski.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.grochevski.main.Game;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(180, 0, 20, 20);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(180, 20, 20, 20);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(180, 40, 20, 20);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(120, 0, 20, 20);

	protected double x;
	protected double y;
	protected int width;
	protected int height;

	private BufferedImage sprite;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void tick() {

	}

	public void render(Graphics g) {
		g.drawImage(sprite, this.getX(), this.getY(), null);
	}

}
