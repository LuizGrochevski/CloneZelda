package com.grochevski.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.grochevski.graficos.Spritesheet;
import com.grochevski.graficos.UI;
import com.grochevski.main.Game;
import com.grochevski.world.Camera;
import com.grochevski.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public double speed = 0.8;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;

	private BufferedImage[] playerDamage;

	public double life = 100, maxLife = 100;

	public int mana = 0;

	private boolean hasWeapon = false;

	public boolean isDamaged = false;
	private int damegeFrames = 0;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];

		playerDamage = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(40 + (i * 20), 0, 20, 20);
		}
		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(40 + (i * 20), 20, 20, 20);
		}
		for (int i = 0; i < 4; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(40 + (i * 20), 60, 20, 20);
		}
		for (int i = 0; i < 4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(40 + (i * 20), 40, 20, 20);
		}

		for (int i = 0; i < 4; i++) {
			playerDamage[i] = Game.spritesheet.getSprite(40 + (i * 20), 80, 20, 20);
		}
	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			dir = up_dir;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			dir = down_dir;
			y += speed;
		}
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

		checkCollisionLifePack();
		checkCollisionMana();
		checkCollisionWeapon();

		if (isDamaged) {
			damegeFrames++;
			if (damegeFrames == 10) {
				damegeFrames = 0;
				isDamaged = false;
			}
		}

		if (life <= 0) {
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0, 0, 20, 20, Game.spritesheet.getSprite(0, 0, 20, 20));
			Game.entities.add(Game.player);
			Game.world = new World("/map.png");
			Game.ui = new UI();
			return;
		}

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 20 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 20 - Game.HEIGHT);
	}

	public void checkCollisionMana() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Mana) {
				if (Entity.isColidding(this, e)) {
					mana += Game.rand.nextInt(5);;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Lifepack) {
				if (life < maxLife) {
					if (Entity.isColidding(this, e)) {
						life += 8;
						if (life >= maxLife)
							life = maxLife;
						Game.entities.remove(i);
						return;
					}
				}
			}
		}
	}

	public void checkCollisionWeapon() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Weapon) {
				if (Entity.isColidding(this, e)) {
					hasWeapon = true;

					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(Entity.WEAPON_RIGHT_EN, this.getX() + 7 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(Entity.WEAPON_LEFT_EN, this.getX() - 5 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			}
			if (dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(Entity.WEAPON_RIGHT_EN, this.getX() + 6 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(Entity.WEAPON_LEFT_EN, this.getX() - 6 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			}
		} else {
			g.drawImage(playerDamage[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}
