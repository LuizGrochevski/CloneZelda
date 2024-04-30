package com.grochevski.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.grochevski.main.Game;
import com.grochevski.world.Camera;
import com.grochevski.world.World;

public class Enemy extends Entity {

	private double speed = 0.6;

	private int maskX = 5, maskY = 5, maskWidth = 10, maskHeight = 10;
	private int zoneX = -40, zoneY = -40, zoneWidth = 100, zoneHeight = 100;

	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] upEnemy;
	private BufferedImage[] downEnemy;
	private BufferedImage[] enemyFeedback;

	private int life = 10;

	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);

		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		upEnemy = new BufferedImage[4];
		downEnemy = new BufferedImage[4];
		enemyFeedback = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(120 + (i * 20), 0, 20, 20);
		}
		for (int i = 0; i < 4; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(120 + (i * 20), 20, 20, 20);
		}
		for (int i = 0; i < 4; i++) {
			downEnemy[i] = Game.spritesheet.getSprite(120 + (i * 20), 60, 20, 20);
		}
		for (int i = 0; i < 4; i++) {
			upEnemy[i] = Game.spritesheet.getSprite(120 + (i * 20), 40, 20, 20);
		}
		for (int i = 0; i < 4; i++) {
			enemyFeedback[i] = Game.spritesheet.getSprite(120 + (i * 20), 80, 20, 20);
		}
	}

	public void tick() {
//		if (Game.rand.nextInt(100) > 30) {
		if (isZone() == true) {
			if (isColiddingWithPlayer() == false) {
				if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
						&& !isColidding((int) (x + speed), this.getY())) {
					moved = true;
					dir = right_dir;
					x += speed;
				} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
						&& !isColidding((int) (x - speed), this.getY())) {
					moved = true;
					dir = left_dir;
					x -= speed;
				}

				if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
						&& !isColidding(this.getX(), (int) (y + speed))) {
					moved = true;
					dir = up_dir;
					y += speed;
				} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
						&& !isColidding(this.getX(), (int) (y - speed))) {
					moved = true;
					dir = down_dir;
					y -= speed;
				}
//		}

				if (moved) {
					frames++;
					if (frames == maxFrames) {
						frames = 0;
						index++;
						if (index > maxIndex) {
							index = 0;
						}
					}
				}

			} else {
				if (Game.rand.nextInt(100) <= 10) {
					Game.player.life -= Game.rand.nextInt(3);
					Game.player.isDamaged = true;
				}
			}
		}
		
		if (life <= 0) {
			destroySelf();
			return;
		}
		
		if (isDamaged) {
			this.damageCurrent++;
			if (this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}

		collidingSpell();
	}

	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}

	public void collidingSpell() {
		for (int i = 0; i < Game.spell.size(); i++) {
			Entity e = Game.spell.get(i);
			if (Entity.isColidding(this, e)) {
				isDamaged = true;
				life--;
				Game.spell.remove(i);
				return;
			}
		}
	}

	public boolean isZone() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + zoneX, this.getY() + zoneY, zoneWidth, zoneHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), World.TILE_SIZE, World.TILE_SIZE);

		return enemyCurrent.intersects(player);
	}

	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskWidth, maskHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), World.TILE_SIZE, World.TILE_SIZE);

		return enemyCurrent.intersects(player);
	}

	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskX, ynext + maskY, maskWidth, maskHeight);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);

			if (e == this)
				continue;

			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskWidth, maskHeight);

			if (enemyCurrent.intersects(targetEnemy))
				return true;

		}

		return false;
	}

	public void render(Graphics g) {

		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			if (dir == up_dir) {
				g.drawImage(upEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == down_dir) {
				g.drawImage(downEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		} else {
			g.drawImage(enemyFeedback[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

//		g.setColor(Color.red);
//		g.fillRect(this.getX() + zoneX - Camera.x, this.getY() + zoneY - Camera.y, zoneWidth, zoneHeight);
//		g.setColor(Color.BLUE);
//		g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);

	}
}
