package com.grochevski.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.grochevski.main.Game;

public class Tile {

	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 20, 20);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(20, 0, 20, 20);

	private BufferedImage sprite;
	private int x, y;

	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

}
