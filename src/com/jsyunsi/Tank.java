package com.jsyunsi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.List;

public class Tank {
	public static final int WIDTH = 35;
	public static final int LENGTH = 35;
	private int x, y, oldx, oldy; // oldx,oldy表示上次坦克的坐标
	private long start, end;
	private boolean live = true; // 表示坦克是否存活
	private boolean printable = false, pb = false, pc = false;
	private int life = 200; // 我方坦克的生命值
	public static int speedX = 6;
	public static int speedY = 6;
	private Direction direction; // 表示坦克方向
	private Direction oldDirection = Direction.U; // 记录坦克上次的方向,初始方向为上
	TankClient tc; // 定义主类
	private int level = 38;

	private boolean good; // 区分敌我方坦克 true为我方坦克，false为敌方坦克
	private static Toolkit tk = Toolkit.getDefaultToolkit(); // toolkit类用于得到图片
	private static Image[] images = null;
	private boolean bL, bU, bR, bD; // 方向参数
	private int tanknum; // tanknum=1 为 p1 tanknum=2 为 p2
	private int n = (int) (Math.random() * 10) + 5; // 表示5~15的一个随机数
	static {
		images = new Image[] { tk.getImage(Tank.class.getResource("../../images/tankL.gif")),
				tk.getImage(Tank.class.getResource("../../images/tankU.gif")),
				tk.getImage(Tank.class.getResource("../../images/tankR.gif")),
				tk.getImage(Tank.class.getResource("../../images/tankD.gif")),
				tk.getImage(Tank.class.getResource("../../images/1L.gif")),
				tk.getImage(Tank.class.getResource("../../images/1U.gif")),
				tk.getImage(Tank.class.getResource("../../images/1R.gif")),
				tk.getImage(Tank.class.getResource("../../images/1D.gif")),
				tk.getImage(Tank.class.getResource("../../images/2L.gif")),
				tk.getImage(Tank.class.getResource("../../images/2U.gif")),
				tk.getImage(Tank.class.getResource("../../images/2R.gif")),
				tk.getImage(Tank.class.getResource("../../images/2D.gif")), };
	} // 将所有图片放入images中

	// 构造函数，传入坦克的方向，敌我，位置，剩余坦克数量
	public Tank(Direction direction, boolean good, int x, int y, int tanknum, TankClient tc) {
		this.direction = direction;
		this.good = good;
		this.x = x;
		this.tanknum = tanknum;
		this.y = y;
		this.tc = tc;
	}

	// 各属性get set方法
	public int getTanknum() {
		return tanknum;
	}

	public void setTanknum(int tanknum) {
		this.tanknum = tanknum;
	}

	public boolean isPb() {
		return pb;
	}

	public void setPb(boolean pb) {
		this.pb = pb;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	// 绘制坦克
	public void draw(Graphics g) {
		if (good) {
			if (!live) {
				// 游戏失败
				return;
			}
		}
		// 根据传入的方向参数，绘制不同的方向
		switch (direction) {
		// ←
		case L:
			if (!good)
				g.drawImage(images[0], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[4], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[8], x, y, WIDTH, LENGTH, null);
			break;
		// ↑
		case U:
			if (!good)
				g.drawImage(images[1], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[5], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[9], x, y, WIDTH, LENGTH, null);
			break;
		// →
		case R:
			if (!good)
				g.drawImage(images[2], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[6], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[10], x, y, WIDTH, LENGTH, null);
			break;
		// ↓
		case D:
			if (!good)
				g.drawImage(images[3], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[7], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[11], x, y, WIDTH, LENGTH, null);
			break;
		// 暂停状态，保持上一次的位置方向
		case STOP:
			if (oldDirection == Direction.L) {
				if (!good)
					g.drawImage(images[0], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 1)
					g.drawImage(images[4], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 2)
					g.drawImage(images[8], x, y, WIDTH, LENGTH, null);
			} else if (oldDirection == Direction.U) {
				if (!good)
					g.drawImage(images[1], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 1)
					g.drawImage(images[5], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 2)
					g.drawImage(images[9], x, y, WIDTH, LENGTH, null);
			} else if (oldDirection == Direction.R) {
				if (!good)
					g.drawImage(images[2], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 1)
					g.drawImage(images[6], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 2)
					g.drawImage(images[10], x, y, WIDTH, LENGTH, null);
			} else if (oldDirection == Direction.D) {
				if (!good)
					g.drawImage(images[3], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 1)
					g.drawImage(images[7], x, y, WIDTH, LENGTH, null);
				if (good && tanknum == 2)
					g.drawImage(images[11], x, y, WIDTH, LENGTH, null);
			}
			break;
		default:
			break;
		}
		move();
	}

	// 位置移动,此处有边界碰撞的判定
	private void move() {
		// TODO Auto-generated method stub
		oldx = x;
		oldy = y;
		switch (direction) {
		case L:
			if (x <= 5)
				break;
			x -= speedX;
			break;
		case D:
			if (y >= 565)
				break;
			y += speedY;
			break;
		case R:
			if (x >= 756)
				break;
			x += speedX;
			break;
		case U:
			if (y <= 50)
				break;
			y -= speedY;
			break;
		case STOP:
			break;
		default:
			break;
		}
		if (!good) { // 敌方坦克随机运动
			Direction[] directions = Direction.values(); // 把Direction类的内容都赋到directions上
			// 使坦克变换方向的频率保持在合理的范围内
			if (n == 0) {
				int rn = (int) (Math.random() * 5);
				direction = directions[rn];
				n = (int) (Math.random() * 10) + 5;
			}
			n--;
			if ((int) (Math.random() * 40) > level) { // 敌方坦克随机发射
				fire();
			}
		}
	}

	// KeyEvent表示键盘发生事件 Pressed按下键发生事件
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		switch (KeyCode) {
		case KeyEvent.VK_A:
//			System.out.println("按下A");
			bL = true;
			break;
		case KeyEvent.VK_S:
//			System.out.println("按下S");
			bD = true;
			break;
		case KeyEvent.VK_D:
//			System.out.println("按下D");
			bR = true;
			break;
		case KeyEvent.VK_W:
//			System.out.println("按下W");
			bU = true;
			break;
		case KeyEvent.VK_J:
			fire();
			break;
		default:
			break;
		}
		decideDirection();
	}

	// fire事件
	private void fire() {
		// TODO Auto-generated method stub
		if (!live) { // 坦克死亡不会在发射子弹
			return;
		}

		// 如果是敌方坦克
		if (good) {
			tc.new Play0().start();
		}

		int x = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
		int y = this.y + Tank.LENGTH / 2 - Bullet.LENGTH / 2;
		if (direction == Direction.STOP) // 使坦克在STOP状态下可以发射子弹
			direction = oldDirection;
		Bullet bullet = new Bullet(direction, good, x, y, tc, tanknum);
		tc.bullets.add(bullet); // 初始化子弹
	}

	public void keyReleased(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		switch (KeyCode) {
		case KeyEvent.VK_A:
//			System.out.println("放开A");
			bL = false;
			break;
		case KeyEvent.VK_S:
//			System.out.println("放开S");
			bD = false;
			break;
		case KeyEvent.VK_D:
//			System.out.println("放开D");
			bR = false;
			break;
		case KeyEvent.VK_W:
//			System.out.println("放开W");
			bU = false;
			break;
		default:
			break;
		}
		decideDirection();
	}

	public void decideDirection() { // 决定坦克本次运动状态
		if (bL && !bU && !bR && !bD) {
			direction = direction.L;
			oldDirection = direction;
		} else if (!bL && bU && !bR && !bD) {
			direction = direction.U;
			oldDirection = direction;
		} else if (!bL && !bU && bR && !bD) {
			direction = direction.R;
			oldDirection = direction;
		} else if (!bL && !bU && !bR && bD) {
			direction = direction.D;
			oldDirection = direction;
		} else if (!bL && !bU && !bR && !bD) {
			direction = direction.STOP;
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, LENGTH);
	}

	// 坦克和河流碰撞
	public void collideWithRiver(River river) {
		Rectangle rect = river.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("碰到");
			changeToOldDir(); // 保持坦克位置不变
		}
	}

	private void changeToOldDir() {
		// TODO Auto-generated method stub
		x = oldx;
		y = oldy;
	}

	// 坦克和金属墙碰撞
	public void collideWithMetalWall(MetalWall metalWall) {
		Rectangle rect = metalWall.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("碰到");
			changeToOldDir();
		}
	}

	// 坦克和普通墙碰撞
	public void collideWithCommonWall(CommonWall commonWall) {
		Rectangle rect = commonWall.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("碰到");
			changeToOldDir();
		}
	}

	// 坦克和家墙碰撞
	public void collideWithHomeWall(CommonWall homeWall) {
		Rectangle rect = homeWall.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("碰到");
			changeToOldDir();
		}
	}

	// 坦克和家碰撞
	public void collideWithHome(Home home) {
		Rectangle rect = home.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("碰到");
			changeToOldDir();
		}
	}

	

	// 坦克和坦克碰撞
	public void collideWithTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if ((this != tank) && this.getRect().intersects(tank.getRect())) {
				this.changeToOldDir();
				tank.changeToOldDir();
			}
		}
	}

	public void collideWithhomeTank(Tank tank) {
		if ((this != tank) && this.getRect().intersects(tank.getRect())) {
			this.changeToOldDir();
			tank.changeToOldDir();
		}
	}

	
	// 坦克和血包碰撞
	public void collideWithBlood(Blood blood) {
		Rectangle rect = blood.getRect();
		if (this.getRect().intersects(rect) && this.good == true) {
			this.tc.blood.setX(-50);
			this.tc.blood.setY(-50);
			this.life = 200;
		}
	}

	// 坦克和盾牌碰撞
	public void collideWithHudun(Hudun hudun) {
		Rectangle rect = hudun.getRect();
		if (this.getRect().intersects(rect) && this.good == true) {
			this.tc.hudun.setX(-100);
			this.tc.hudun.setY(-100);
			hudun.superWall(this.tc.homeWalls);
			start = System.currentTimeMillis();
			printable = true;
		}
	}

	// 金属墙恢复
	public void reMetalWall(Hudun hudun) {
		end = System.currentTimeMillis();
		if (end - start >= 3000 && printable) {
			hudun.reWall(this.tc.homeWalls);
			printable = false;
		}
	}

	// 坦克和星星碰撞
	public void collideWithStar(Star star) {
		Rectangle rect = star.getRect();
		if (this.getRect().intersects(rect) && this.good == true) {
			star.setX(-100);
			star.setY(-100);
			pb = true;
		}
	}

	// 坦克和炸弹碰撞
	public void collideWithBomb(Bomb bomb) {
		Rectangle rect = bomb.getRect();
		if (this.getRect().intersects(rect)) {
			bomb.setX(-100);
			bomb.setY(-100);
			pc = true;
		}
	}

	
	// 坦克随机爆炸
	public void BombTank(List<Tank> tank) {
		if (pc) {
			int t = (int) (Math.random() * tank.size());
			for (int i = 0; i < tank.size(); i++) {
				if (t == i) {
					BombTank bt = new BombTank(tank.get(i).getX(), tank.get(i).getY(), tc);
					this.tc.bombTanks.add(bt);
					tank.remove(tank.get(i));
					pc = false;
				}
			}
		}
	}

	public void keyPressed1(KeyEvent e) {// keyevent
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		// 小数点射击
		case KeyEvent.VK_DECIMAL:
			break;
		default:
			break;
		}
		decideDirection();
	}

	public void ketReleased1(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_DECIMAL:
			fire();
			break;
		default:
			break;
		}
		decideDirection();
	}

	public static int getSpeedX() {
		return speedX;
	}

	public static void setSpeedX(int speedX) {
		Tank.speedX = speedX;
	}

	public static int getSpeedY() {
		return speedY;
	}

	public static void setSpeedY(int speedY) {
		Tank.speedY = speedY;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
