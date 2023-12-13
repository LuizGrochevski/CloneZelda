package com.grochevski.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {

	private Tile[] tiles;
	public static int WIDTH, HEIGHT;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH * HEIGHT];
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					int pixelAtual = pixels[xx + (yy * WIDTH)];

					if (pixelAtual == 0xFF000000) {
						// Floor/Chao
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 20, yy * 20, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFFFFF) {
						// Parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 20, yy * 20, Tile.TILE_WALL);
					} else if (pixelAtual == 0xFF0026FF) {
						// Player
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 20, yy * 20, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFF0000) {
						// Enemy
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 20, yy * 20, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFF6A00) {
						// Weapon
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 20, yy * 20, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFD800) {
						// Bullet
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 20, yy * 20, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFF7F7F) {
						// LifePack
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 20, yy * 20, Tile.TILE_FLOOR);
					} else {
						// Floor/Chao
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 20, yy * 20, Tile.TILE_FLOOR);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}

}
