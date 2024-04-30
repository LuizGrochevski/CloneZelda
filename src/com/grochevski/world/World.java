package com.grochevski.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.grochevski.entities.Mana;
import com.grochevski.entities.Player;
import com.grochevski.entities.Enemy;
import com.grochevski.entities.Entity;
import com.grochevski.entities.Lifepack;
import com.grochevski.entities.Weapon;
import com.grochevski.graficos.Spritesheet;
import com.grochevski.graficos.UI;
import com.grochevski.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 20;

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

					switch (pixelAtual) {
					default:
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
					}

					if (pixelAtual == 0xFF000000) {
						// Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFFFFF) {
						// Wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
					} else if (pixelAtual == 0xFF0026FF) {
						// Player
						Game.player.setX(xx * TILE_SIZE);
						Game.player.setY(yy * TILE_SIZE);
					} else if (pixelAtual == 0xFFFF0000) {
						// Enemy
//						Game.entities.add(new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, 20, 20, Entity.ENEMY_EN));
						Enemy en = new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, 20, 20, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if (pixelAtual == 0xFFFF6A00) {
						// Weapon
						Game.entities.add(new Weapon(xx * TILE_SIZE, yy * TILE_SIZE, 20, 20, Entity.WEAPON_EN));
					} else if (pixelAtual == 0xFFFFD800) {
						// Bullet
						Game.entities.add(new Mana(xx * TILE_SIZE, yy * TILE_SIZE, 20, 20, Entity.MANA_EN));
					} else if (pixelAtual == 0xFFFF7F7F) {
						// LifePack
						Lifepack pack = new Lifepack(xx * TILE_SIZE, yy * TILE_SIZE, 20, 20, Entity.LIFEPACK_EN);
						pack.setMask(8, 8, 8, 8);
						Game.entities.add(pack);
					} else {
						// Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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

		return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile
				|| tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile
				|| tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile
				|| tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);

	}
	
	public static void restartGame(String level) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 20, 20, Game.spritesheet.getSprite(0, 0, 20, 20));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		Game.ui = new UI();
		return;
	}

	public void render(Graphics g) {
		int xstart = Camera.x / 20;
		int ystart = Camera.y / 20;

		int xfinal = xstart + (Game.WIDTH / 20);
		int yfinal = ystart + (Game.HEIGHT / 20);

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
