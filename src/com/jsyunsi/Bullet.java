package com.jsyunsi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

import com.jsyunsi.TankClient.Play0;

public class Bullet {
	public static final int WIDTH = 10;
	public static final int LENGTH = 10;
	private int x, y;
	private Direction direction; // 方向
	private static int speedX = 10;
	private static int speedY = 10;
	private boolean good; // 子弹好坏
	TankClient tc;

	public static int getSpeedX() {
		return speedX;
	}

	public static void setSpeedX(int speedX) {
		Bullet.speedX = speedX;
	}

	public static int getSpeedY() {
		return speedY;
	}

	public static void setSpeedY(int speedY) {
		Bullet.speedY = speedY;
	}

	private boolean live = true; // 子弹是否存活
	private int tanknum;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = null;
	static {
		images = new Image[] { tk.getImage(Bullet.class.getResource("../../images/bulletL.gif")),
				tk.getImage(Bullet.class.getResource("../../images/bulletU.gif")),
				tk.getImage(Bullet.class.getResource("../../images/bulletR.gif")),
				tk.getImage(Bullet.class.getResource("../../images/bulletD.gif")),
				tk.getImage(Bullet.class.getResource("../../images/bulletLU.gif")),
				tk.getImage(Bullet.class.getResource("../../images/bulletLD.gif")),
				tk.getImage(Bullet.class.getResource("../../images/bulletRU.gif")),
				tk.getImage(Bullet.class.getResource("../../images/bulletRD.gif")), };
	}

	public Bullet(Direction direction, boolean good, int x, int y, TankClient tc, int tanknum) {
		super();
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.tanknum = tanknum;
		this.good = good;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		if (!live) {
			// 子弹死了
			return;
		}
		switch (direction) {
		case L:
			g.drawImage(images[0], x, y, null);
			break;
		case U:
			g.drawImage(images[1], x, y, null);
			break;
		case R:
			g.drawImage(images[2], x, y, null);
			break;
		case D:
			g.drawImage(images[3], x, y, null);
			break;
		case LU:
			g.drawImage(images[4], x, y, null);
			break;
		case LD:
			g.drawImage(images[5], x, y, null);
			break;
		case RU:
			g.drawImage(images[6], x, y, null);
			break;
		case RD:
			g.drawImage(images[7], x, y, null);
			break;
		default:
			break;
		}
		move();
	}

	private void move() {
		// TODO Auto-generated method stub
		switch (direction) {
		case L:
			x -= speedX;
			break;
		case U:
			y -= speedY;
			break;
		case R:
			x += speedX;
			break;
		case D:
			y += speedY;
			break;
		case LD:
			x -= speedX;
			y += speedY;
			break;
		case LU:
			x -= speedX;
			y -= speedY;
			break;
		case RD:
			x += speedX;
			y += speedY;
			break;
		case RU:
			x += speedX;
			y -= speedY;
			break;
		default:
			break;
		}
		if (x < 0 || y < 0 || x > TankClient.FRAME_WIDTH || y > TankClient.FRAME_LENGTH) {
			live = false;
			this.x = -200;
			this.y = -200;
		}
	}

	// 子弹和普通墙碰撞
	public void hitCommonWall(CommonWall cw) {
		if (this.getRect().intersects(cw.getRect())) {
			this.tc.bullets.remove(this);
			this.tc.otherWalls.remove(cw);
			this.tc.homeWalls.remove(cw);
		}
	}

	// 子弹和金属墙碰撞
	public void hitMetalWall(MetalWall mw) {
		if (this.getRect().intersects(mw.getRect())) {
			this.tc.bullets.remove(this);
			// this.live = false;
		}
	}

	// 子弹和家碰撞
	public boolean hitHome(Home hm, int i) {
		if (this.getRect().intersects(hm.getRect())) {
			if (this.tc.bullets.get(i).good == false) {
				this.tc.home.setX(-200);
				this.tc.home.setY(-200);
				return true;
			}
			this.tc.bullets.remove(this);
		}
		return false;
	}

	// 子弹和坦克碰撞
	public void hitTank(Tank tank) {
		if ((this.good != tank.isGood()) && (this.getRect().intersects(tank.getRect()))) {
			BombTank bt = new BombTank(this.x, this.y, tc);
			this.tc.bombTanks.add(bt);
			if (this.good) { // 我方子弹
				this.live = false; // 子弹消失
				this.x = -100;
				this.y = -100;
				this.tc.tanks.remove(tank);

				tc.new Play0().start();
			} else { // 敌方子弹
				tc.new Play0().start();

				this.live = false;
				this.tc.bullets.remove(this);
				if (tank.getLife() <= 0) {
					tank.setLife(0);
					tank.setLive(false);
					tank.setX(-100);
					tank.setY(-100);
				}
			}
		}
		if (this.good == tank.isGood() && (this.getRect().intersects(tank.getRect()))) {
			if (this.tanknum != tank.getTanknum()) {
				BombTank bt = new BombTank(this.x, this.y, tc);
				this.tc.bombTanks.add(bt);
				tank.setLife(tank.getLife() - 50);
				this.tc.bullets.remove(this);
				System.out.println(tank.getLife());
				if (tank.getLife() <= 0) {
					tank.setLife(0);
					tank.setLive(false);
					tank.setX(-500);
					tank.setY(-500);
				}
			}
		}
	}

	// 子弹和子弹碰撞
	public void hitBullet(List<Bullet> list) {
		for (int i = 0; i < list.size(); i++) {
			Bullet bullet = list.get(i);
			if ((this.good != bullet.good) && this.getRect().intersects(bullet.getRect())) {
				this.tc.bullets.remove(this);
				this.tc.bullets.remove(bullet);
			}
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, LENGTH);
	}

	// 子弹和金属墙碰撞（吃了星星后）
	public void hitmetalwall(List<MetalWall> mw) {
		for (int i = 0; i < mw.size(); i++) {
			if (this.getRect().intersects(mw.get(i).getRect()) && this.good == true) {
				this.tc.bullets.remove(this);
				mw.remove(mw.get(i));
			} else if (this.getRect().intersects(mw.get(i).getRect()) && this.good == false) {
				this.tc.bullets.remove(this);
			}
		}
	}

	// 子弹和家金属墙碰撞
	public void hithomemetalwall(Hudun hudun) {
		for (int i = 0; i < hudun.mw.size(); i++) {
			if (this.getRect().intersects(hudun.mw.get(i).getRect()) && this.good == true) {
				this.tc.bullets.remove(this);
				hudun.mw.remove(hudun.mw.get(i));
			} else if (this.getRect().intersects(hudun.mw.get(i).getRect()) && this.good == false) {
				this.tc.bullets.remove(this);
			}
		}
	}
}
