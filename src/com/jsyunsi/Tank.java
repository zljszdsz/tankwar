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
	private int x, y, oldx, oldy; // oldx,oldy��ʾ�ϴ�̹�˵�����
	private long start, end;
	private boolean live = true; // ��ʾ̹���Ƿ���
	private boolean printable = false, pb = false, pc = false;
	private int life = 200; // �ҷ�̹�˵�����ֵ
	public static int speedX = 6;
	public static int speedY = 6;
	private Direction direction; // ��ʾ̹�˷���
	private Direction oldDirection = Direction.U; // ��¼̹���ϴεķ���,��ʼ����Ϊ��
	TankClient tc; // ��������
	private int level = 38;

	private boolean good; // ���ֵ��ҷ�̹�� trueΪ�ҷ�̹�ˣ�falseΪ�з�̹��
	private static Toolkit tk = Toolkit.getDefaultToolkit(); // toolkit�����ڵõ�ͼƬ
	private static Image[] images = null;
	private boolean bL, bU, bR, bD; // �������
	private int tanknum; // tanknum=1 Ϊ p1 tanknum=2 Ϊ p2
	private int n = (int) (Math.random() * 10) + 5; // ��ʾ5~15��һ�������
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
	} // ������ͼƬ����images��

	// ���캯��������̹�˵ķ��򣬵��ң�λ�ã�ʣ��̹������
	public Tank(Direction direction, boolean good, int x, int y, int tanknum, TankClient tc) {
		this.direction = direction;
		this.good = good;
		this.x = x;
		this.tanknum = tanknum;
		this.y = y;
		this.tc = tc;
	}

	// ������get set����
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

	// ����̹��
	public void draw(Graphics g) {
		if (good) {
			if (!live) {
				// ��Ϸʧ��
				return;
			}
		}
		// ���ݴ���ķ�����������Ʋ�ͬ�ķ���
		switch (direction) {
		// ��
		case L:
			if (!good)
				g.drawImage(images[0], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[4], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[8], x, y, WIDTH, LENGTH, null);
			break;
		// ��
		case U:
			if (!good)
				g.drawImage(images[1], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[5], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[9], x, y, WIDTH, LENGTH, null);
			break;
		// ��
		case R:
			if (!good)
				g.drawImage(images[2], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[6], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[10], x, y, WIDTH, LENGTH, null);
			break;
		// ��
		case D:
			if (!good)
				g.drawImage(images[3], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 1)
				g.drawImage(images[7], x, y, WIDTH, LENGTH, null);
			if (good && tanknum == 2)
				g.drawImage(images[11], x, y, WIDTH, LENGTH, null);
			break;
		// ��ͣ״̬��������һ�ε�λ�÷���
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

	// λ���ƶ�,�˴��б߽���ײ���ж�
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
		if (!good) { // �з�̹������˶�
			Direction[] directions = Direction.values(); // ��Direction������ݶ�����directions��
			// ʹ̹�˱任�����Ƶ�ʱ����ں���ķ�Χ��
			if (n == 0) {
				int rn = (int) (Math.random() * 5);
				direction = directions[rn];
				n = (int) (Math.random() * 10) + 5;
			}
			n--;
			if ((int) (Math.random() * 40) > level) { // �з�̹���������
				fire();
			}
		}
	}

	// KeyEvent��ʾ���̷����¼� Pressed���¼������¼�
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		switch (KeyCode) {
		case KeyEvent.VK_A:
//			System.out.println("����A");
			bL = true;
			break;
		case KeyEvent.VK_S:
//			System.out.println("����S");
			bD = true;
			break;
		case KeyEvent.VK_D:
//			System.out.println("����D");
			bR = true;
			break;
		case KeyEvent.VK_W:
//			System.out.println("����W");
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

	// fire�¼�
	private void fire() {
		// TODO Auto-generated method stub
		if (!live) { // ̹�����������ڷ����ӵ�
			return;
		}

		// ����ǵз�̹��
		if (good) {
			tc.new Play0().start();
		}

		int x = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
		int y = this.y + Tank.LENGTH / 2 - Bullet.LENGTH / 2;
		if (direction == Direction.STOP) // ʹ̹����STOP״̬�¿��Է����ӵ�
			direction = oldDirection;
		Bullet bullet = new Bullet(direction, good, x, y, tc, tanknum);
		tc.bullets.add(bullet); // ��ʼ���ӵ�
	}

	public void keyReleased(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		switch (KeyCode) {
		case KeyEvent.VK_A:
//			System.out.println("�ſ�A");
			bL = false;
			break;
		case KeyEvent.VK_S:
//			System.out.println("�ſ�S");
			bD = false;
			break;
		case KeyEvent.VK_D:
//			System.out.println("�ſ�D");
			bR = false;
			break;
		case KeyEvent.VK_W:
//			System.out.println("�ſ�W");
			bU = false;
			break;
		default:
			break;
		}
		decideDirection();
	}

	public void decideDirection() { // ����̹�˱����˶�״̬
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

	// ̹�˺ͺ�����ײ
	public void collideWithRiver(River river) {
		Rectangle rect = river.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("����");
			changeToOldDir(); // ����̹��λ�ò���
		}
	}

	private void changeToOldDir() {
		// TODO Auto-generated method stub
		x = oldx;
		y = oldy;
	}

	// ̹�˺ͽ���ǽ��ײ
	public void collideWithMetalWall(MetalWall metalWall) {
		Rectangle rect = metalWall.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("����");
			changeToOldDir();
		}
	}

	// ̹�˺���ͨǽ��ײ
	public void collideWithCommonWall(CommonWall commonWall) {
		Rectangle rect = commonWall.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("����");
			changeToOldDir();
		}
	}

	// ̹�˺ͼ�ǽ��ײ
	public void collideWithHomeWall(CommonWall homeWall) {
		Rectangle rect = homeWall.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("����");
			changeToOldDir();
		}
	}

	// ̹�˺ͼ���ײ
	public void collideWithHome(Home home) {
		Rectangle rect = home.getRect();
		if (this.getRect().intersects(rect)) {
//			System.out.println("����");
			changeToOldDir();
		}
	}

	

	// ̹�˺�̹����ײ
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

	
	// ̹�˺�Ѫ����ײ
	public void collideWithBlood(Blood blood) {
		Rectangle rect = blood.getRect();
		if (this.getRect().intersects(rect) && this.good == true) {
			this.tc.blood.setX(-50);
			this.tc.blood.setY(-50);
			this.life = 200;
		}
	}

	// ̹�˺Ͷ�����ײ
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

	// ����ǽ�ָ�
	public void reMetalWall(Hudun hudun) {
		end = System.currentTimeMillis();
		if (end - start >= 3000 && printable) {
			hudun.reWall(this.tc.homeWalls);
			printable = false;
		}
	}

	// ̹�˺�������ײ
	public void collideWithStar(Star star) {
		Rectangle rect = star.getRect();
		if (this.getRect().intersects(rect) && this.good == true) {
			star.setX(-100);
			star.setY(-100);
			pb = true;
		}
	}

	// ̹�˺�ը����ײ
	public void collideWithBomb(Bomb bomb) {
		Rectangle rect = bomb.getRect();
		if (this.getRect().intersects(rect)) {
			bomb.setX(-100);
			bomb.setY(-100);
			pc = true;
		}
	}

	
	// ̹�������ը
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
		// С�������
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
