package com.grochevski.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.grochevski.entities.Enemy;
import com.grochevski.entities.Entity;
import com.grochevski.entities.Player;
import com.grochevski.entities.Spell;
import com.grochevski.graficos.Spritesheet;
import com.grochevski.graficos.UI;
import com.grochevski.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public final static int WIDTH = 320;
	public final static int HEIGHT = 240;
	private final int SCALE = 3;

	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Spell> spell;
	public static Spritesheet spritesheet;

	public static World world;

	public static Player player;

	public static Random rand;

	public static UI ui;

	public static String gameState = "NORMAL";
	private boolean showMassageGameOver;
	private int framesGameOver = 0;
	private boolean restartGame = false;

	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		spell = new ArrayList<Spell>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 20, 20, spritesheet.getSprite(0, 0, 20, 20));
		entities.add(player);
		world = new World("/level1.png");
		ui = new UI();
	}

	public void initFrame() {
		frame = new JFrame("Game");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {

	}

	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}

	public void tick() {
		if (gameState == "NORMAL") {
			this.restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}

			for (int i = 0; i < spell.size(); i++) {
				spell.get(i).tick();
			}

			if (enemies.size() == 0) {
				CUR_LEVEL++;
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		} else if (gameState == "GAME_OVER") {
			framesGameOver++;
			if (framesGameOver == 25) {
				framesGameOver = 0;
				if (showMassageGameOver) {
					showMassageGameOver = false;
				} else {
					showMassageGameOver = true;
				}
			}
			if (restartGame) {
				restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		/* Renderiza��a do jogo */
//		Graphics2D g2 = (Graphics2D) g;
//		g2.drawImage(player[curAnimation], 20, 20, null);

		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < spell.size(); i++) {
			spell.get(i).render(g);
		}
		ui.render(g);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Mana:" + player.mana, 50, 90);

		if (gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 30));
			g2.drawString("GAME OVER", (WIDTH * SCALE) / 2 - 65, (HEIGHT * SCALE) / 2);
			if (showMassageGameOver) {
				g2.drawString(">Pressione 'Enter' para reiniciar<", (WIDTH * SCALE) / 2 - 220,
						(HEIGHT * SCALE) / 2 + 60);
			}
		}

		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 100) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_F) {
			player.spell = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_F) {
			player.spell = false;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseSpell = true;
		player.mouseX = (e.getX() / SCALE);
		player.mouseY = (e.getY() / SCALE);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
