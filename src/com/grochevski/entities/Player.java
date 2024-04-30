package com.grochevski.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

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

	public int mana = 100;

	private boolean hasWeapon = false;

	public boolean isDamaged = false;
	private int damegeFrames = 0;

	public boolean spell = false;
	public boolean mouseSpell = false;

	public int mouseX, mouseY;

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

		if (spell) {
			spell = false;
			if (mana > 0) {
				mana--;

				int dx = 0;
				int px = 0;
				int py = 0;
				if (dir == right_dir) {
					px = 4;
					py = +15;
					dx = 1;
				} else {
					px = 14;
					py = +15;
					dx = -1;
				}

				Spell spell = new Spell(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
				Game.spell.add(spell);
			}
		}

		if (mouseSpell) {
			mouseSpell = false;
			if (mana > 0) {
				mana--;

				double angle = Math.atan2(mouseY - (this.getY() + 10 - Camera.y), mouseX - (this.getX() + 10 - Camera.x));
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				int px = 0;
				int py = 0;

				if (dir == right_dir) {
					px = 4;
					py = +15;

				} else {
					px = 14;
					py = +15;

				}

				Spell spell = new Spell(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.spell.add(spell);
			}
		}

		if (life <= 0) {
			life = 0;
			Game.gameState = "GAME_OVER";
		}

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 20 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 20 - Game.HEIGHT);
	}

	public void checkCollisionMana() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Mana) {
				if (Entity.isColidding(this, e)) {
					mana += Game.rand.nextInt(100);
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
					g.drawImage(Entity.WEAPON_RIGHT_EN, this.getX() + 8 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(Entity.WEAPON_LEFT_EN, this.getX() - 6 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			}
			if (dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(Entity.WEAPON_RIGHT_EN, this.getX() + 7 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(Entity.WEAPON_LEFT_EN, this.getX() - 7 - Camera.x, this.getY() + 3 - Camera.y, null);
				}
			}
		} else {
			g.drawImage(playerDamage[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}
