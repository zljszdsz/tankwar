package com.jsyunsi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

//import com.jsyunsi.Client.PaintThread;

import javazoom.jl.player.Player;
import java.io.BufferedInputStream;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;

import javazoom.jl.player.*;

public class TankClient extends Frame implements ActionListener {
	/**
	 * 
	 */
	private boolean pb = false;
	// 定义框的大小
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_LENGTH = 600;
	private int restart;
	private long start, end;

	private int n = 0, m = 0;

	// 获取电脑屏幕大小
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screenSize = tk.getScreenSize();
	int screenWidth = screenSize.width;
	int screenLength = screenSize.height;
	// 定义一个控制暂停/继续的变量
	private boolean printable = true;
	// 定义一个画布
	Image image = null;
	// 定义菜单
	MenuBar jmb = null; // 吧台
	Menu jm1, jm2, jm3, jm4 = null; // 只定义了jm4 菜单
	MenuItem jmi1, jmi2, jmi3, jmi4, jmi5, jmi6, jmi7, jmi8, jmi9; // 子菜单
	// 定义河流类
	River river = new River(100, 100);
	// 定义树林
	List<Tree> trees = new ArrayList<Tree>(); // 集合
	// 定义金属墙
	List<MetalWall> metalWalls = new ArrayList<MetalWall>();
	// 定义普通墙
	List<CommonWall> otherWalls = new ArrayList<CommonWall>();
	// 定义家周围的墙
	List<CommonWall> homeWalls = new ArrayList<CommonWall>();
	// 定义家
	Home home = new Home(383, 562);
	// 定义我方坦克
	Tank hometank = new Tank(Direction.STOP, true, 310, 565, 1, this);
	Tank hometank2 = new Tank(Direction.STOP, true, 460, 565, 2, this);

	// 定义敌方坦克
	List<Tank> tanks = new ArrayList<Tank>();
	// 定义子弹
	List<Bullet> bullets = new ArrayList<Bullet>();
	// 定义爆炸效果集合
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	// 定义血包
	Blood blood = new Blood((int) (Math.random() * 800), (int) (Math.random() * 600));
	// 定义盾牌
	Hudun hudun = new Hudun(400, 400);
	// 定义星星
	Star star = new Star(400, 400);
	// 定义炸弹
	Bomb bomb = new Bomb(400, 400);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankClient();
	}

	public void update(Graphics g) { // g是框的画笔
		image = this.createImage(FRAME_WIDTH, FRAME_LENGTH); // 设置画布的画笔和框的大小相同
		Graphics gps = image.getGraphics(); // gps是画布的画笔
		framepaint(gps);
		g.drawImage(image, 0, 0, null); // 设置画布位置，覆盖整个框
	}

	private void framepaint(Graphics g) {
		// TODO Auto-generated method stub
		// 绘制河流
		river.draw(g);
		hometank.collideWithRiver(river); // 我方坦克和河流碰撞
		hometank2.collideWithRiver(river); // 我方坦克2和河流碰撞
		for (int i = 0; i < tanks.size(); i++) { // 敌方坦克和河流碰撞
			tanks.get(i).collideWithRiver(river);
		}

		// 绘制其他普通墙
		for (int i = 0; i < otherWalls.size(); i++) {
			otherWalls.get(i).draw(g);
			hometank.collideWithCommonWall(otherWalls.get(i)); // 我方坦克和普通墙碰撞
			hometank2.collideWithCommonWall(otherWalls.get(i)); // 我方坦克2和普通墙碰撞
			for (int j = 0; j < tanks.size(); j++) { // 敌方坦克和普通碰撞
				tanks.get(j).collideWithCommonWall(otherWalls.get(i));
			}
		}
		// 绘制家周围的墙
		for (int i = 0; i < homeWalls.size(); i++) {
			homeWalls.get(i).draw(g);
			hometank.collideWithHomeWall(homeWalls.get(i)); // 我方坦克和家墙碰撞
			hometank2.collideWithHomeWall(homeWalls.get(i)); // 我方坦克2和家墙碰撞
			for (int j = 0; j < tanks.size(); j++) { // 敌方坦克和家墙碰撞
				tanks.get(j).collideWithCommonWall(homeWalls.get(i));
			}
		}
		// 绘制金属墙
		for (int i = 0; i < metalWalls.size(); i++) {
			metalWalls.get(i).draw(g);
			hometank.collideWithMetalWall(metalWalls.get(i)); // 我方坦克和金属墙碰撞
			hometank2.collideWithMetalWall(metalWalls.get(i)); // 我方坦克和金属墙碰撞
			for (int j = 0; j < tanks.size(); j++) { // 敌方坦克和金属墙碰撞
				tanks.get(j).collideWithMetalWall(metalWalls.get(i));
			}
		}
		// 进入Boss战时家墙变成金属墙
		if (n == 1) {
			for (int i = 0; i < 4; i++) {
				metalWalls.add(new MetalWall(360, 560 + i * MetalWall.LENGTH));
				metalWalls.add(new MetalWall(420, 560 + i * MetalWall.LENGTH));
				metalWalls.add(new MetalWall(360 + i * MetalWall.WIDTH, 540));
			}
		}
		// 绘制家
		home.draw(g);
		hometank.collideWithHome(home); // 我方坦克和家碰撞
		hometank2.collideWithHome(home); // 我方坦克2和家碰撞

		for (int j = 0; j < tanks.size(); j++) { // 敌方坦克和家碰撞
			tanks.get(j).collideWithHome(home);
		}
		//
		for (int i = 0; i < hudun.mw.size(); i++) {
			hometank.collideWithMetalWall(hudun.mw.get(i));
			hometank2.collideWithMetalWall(hudun.mw.get(i));
			for (int j = 0; j < tanks.size(); j++) {
				tanks.get(j).collideWithMetalWall(hudun.mw.get(i));
			}
		}
		// 绘制我方坦克,
		hometank.draw(g);
		hometank2.draw(g);
		// 绘制敌方坦克
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).draw(g);
		}

		if (tanks.size() == 0) {
			printable = false;
			Object[] options = { "确定" };
			int response = JOptionPane.showOptionDialog(this, "！获胜！", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.exit(0);
			}
			g.setFont(new Font("", Font.BOLD, 20));
			g.setColor(Color.red);

		}

		// 坦克和坦克之间碰撞
		hometank.collideWithTanks(tanks);
		hometank.collideWithhomeTank(hometank2);
		hometank2.collideWithTanks(tanks);
		hometank2.collideWithhomeTank(hometank);

		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).collideWithTanks(tanks);
		}
		// 绘制子弹,子弹的初始化在Tank类中实现
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			bullet.draw(g);
			// 子弹和其他普通墙碰撞
			for (int j = 0; j < otherWalls.size(); j++) {
				bullet.hitCommonWall(otherWalls.get(j));
			}
			// 子弹和家墙碰撞
			for (int j = 0; j < homeWalls.size(); j++) {
				bullet.hitCommonWall(homeWalls.get(j));
			}
			// 子弹和金属墙碰撞
			if (!hometank.isPb()) {
				for (int j = 0; j < metalWalls.size(); j++) {
					bullet.hitMetalWall(metalWalls.get(j));
				}
			}

			// 我方子弹和敌方坦克碰撞
			for (int j = 0; j < tanks.size(); j++) {
				bullet.hitTank(tanks.get(j));
			}
			for (int j = 0; j < hudun.mw.size(); j++) {
				bullet.hitMetalWall(hudun.mw.get(j));
			}

			// 敌方子弹和我方坦克碰撞
			bullet.hitTank(hometank);
			bullet.hitTank(hometank2);

			// 子弹和家碰撞
			pb = bullet.hitHome(home, i);
			if (pb) {
				printable = false;
				Object[] options = { "确定" };
				int response = JOptionPane.showOptionDialog(this, "！失败！", "", JOptionPane.YES_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (response == 0) {
					System.exit(0);
				}
			}
			// 子弹和子弹碰撞
			bullet.hitBullet(bullets);
			//
			if (hometank.isPb()) {
				bullet.hitmetalwall(metalWalls);
				bullet.hithomemetalwall(hudun);
			}
		}
		// 绘制树林
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).draw(g);
		}
		// 坦克状态
		g.setFont(new Font("", Font.BOLD, 20));
		g.setColor(Color.green);
		g.drawString("剩余敌方坦克数量" + tanks.size(), 50, 100);
		g.drawString("我方坦克生命值" + hometank.getLife(), 500, 100);
		g.drawString("友方坦克生命值" + hometank2.getLife(), 300, 100);
		if ((hometank.getLife() <= 0) && (hometank2.getLife() <= 0)) {

			printable = false;

			Object[] options = { "确定" };
			int response = JOptionPane.showOptionDialog(this, "！失败!", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.exit(0);
			}
		}
		// 绘制爆炸效果
		for (int i = 0; i < bombTanks.size(); i++) {
			bombTanks.get(i).draw(g);
		}
		// 绘制血包
		blood.draw(g);
		// 坦克和血包碰撞
		hometank.collideWithBlood(blood);
		hometank2.collideWithBlood(blood);
		// 绘制盾牌
		hudun.draw(g);
		// 坦克和盾牌碰撞
		hometank.collideWithHudun(hudun);
		hometank2.collideWithHudun(hudun);
		//
		hometank.reMetalWall(hudun);
		hometank2.reMetalWall(hudun);
		// 绘制星星
		star.draw(g);
		// 我方坦克和星星碰撞
		hometank.collideWithStar(star);
		hometank2.collideWithStar(star);
		// 绘制炸弹
		bomb.draw(g);

		// 我方坦克和炸弹碰撞
		hometank.collideWithBomb(bomb);
		hometank2.collideWithBomb(bomb);
		//
		hometank.BombTank(tanks);
		hometank2.BombTank(tanks);
	}

	// 无参构造
	public TankClient() {

		new Play0().start();

		bombTanks.add(new BombTank(-100, -100, this));
		// 初始化20辆敌方坦克
		for (int i = 0; i < 20; i++) {
			if (i < 5) {
				tanks.add(new Tank(Direction.D, false, 10, 150 + i * 50, 1, this));
			} else if (i < 14) {
				tanks.add(new Tank(Direction.D, false, 200 + (i - 5) * 45, 45, 1, this));
			} else {
				tanks.add(new Tank(Direction.D, false, 710, 300 + (i - 14) * 50, 1, this));
			}
		}
		// 初始化树林信息（集合）
		for (int i = 0; i < 4; i++) {
			trees.add(new Tree(0 + i * Tree.WIDTH, 400));
			trees.add(new Tree(240 + i * Tree.WIDTH, 400));
			trees.add(new Tree(460 + i * Tree.WIDTH, 400));
			trees.add(new Tree(680 + i * Tree.WIDTH, 400));
		}
		// 初始化金属墙
		for (int i = 0; i < 10; i++) {
			metalWalls.add(new MetalWall(140 + i * MetalWall.WIDTH, 120));
			metalWalls.add(new MetalWall(140 + i * MetalWall.WIDTH, 150));
			metalWalls.add(new MetalWall(620, 430 + i * MetalWall.LENGTH));
		}
		// 初始化其他普通墙
		for (int i = 0; i < 16; i++) {
			otherWalls.add(new CommonWall(220, 430 + i * CommonWall.LENGTH));
			otherWalls.add(new CommonWall(240, 430 + i * CommonWall.LENGTH));
			otherWalls.add(new CommonWall(520, 430 + i * CommonWall.LENGTH));
			otherWalls.add(new CommonWall(540, 430 + i * CommonWall.LENGTH));
			otherWalls.add(new CommonWall(240 + i * CommonWall.WIDTH, 315));
			otherWalls.add(new CommonWall(240 + i * CommonWall.WIDTH, 335));
			otherWalls.add(new CommonWall(520 + i * CommonWall.WIDTH, 150));
			otherWalls.add(new CommonWall(520 + i * CommonWall.WIDTH, 190));
		}
		// 初始化家周围的墙
		for (int i = 0; i < 4; i++) {
			homeWalls.add(new CommonWall(360, 560 + i * CommonWall.LENGTH));
			homeWalls.add(new CommonWall(420, 560 + i * CommonWall.LENGTH));
			homeWalls.add(new CommonWall(360 + i * CommonWall.WIDTH, 540));
		}
		// 初始化菜单
		jmb = new MenuBar();
		jm1 = new Menu("游戏");
		jm2 = new Menu("暂停/继续");
		jm3 = new Menu("游戏级别");
		jm4 = new Menu("帮助");
		jmi1 = new MenuItem("开始新游戏");
		jmi1.addActionListener(this); // 给jmi1绑定当前对象
		jmi1.setActionCommand("Start"); // 给该事件起名
		jmi2 = new MenuItem("退出");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3 = new MenuItem("暂停");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4 = new MenuItem("继续");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5 = new MenuItem("游戏级别1");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("Level1");
		jmi6 = new MenuItem("游戏级别2");
		jmi6.addActionListener(this);
		jmi6.setActionCommand("Level2");
		jmi7 = new MenuItem("游戏级别3");
		jmi7.addActionListener(this);
		jmi7.setActionCommand("Level3");
		jmi8 = new MenuItem("游戏级别4");
		jmi8.addActionListener(this);
		jmi8.setActionCommand("Level4");
		jmi9 = new MenuItem("游戏说明");
		jmi9.addActionListener(this);
		jmi9.setActionCommand("Help");
		// 设置字体格式
		jm1.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jm2.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jm3.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jm4.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi1.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi2.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi3.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi4.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi5.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi6.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi7.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi8.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi9.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmb.add(jm1); // 菜单加到吧台上
		jmb.add(jm2);
		jmb.add(jm3);
		jmb.add(jm4);
		jm1.add(jmi1); // 把各子菜单加到相应的菜单下
		jm1.add(jmi2);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jm3.add(jmi5);
		jm3.add(jmi6);
		jm3.add(jmi7);
		jm3.add(jmi8);
		jm4.add(jmi9);
		this.setMenuBar(jmb);
		// 设置窗口可见
		this.setVisible(true);
		// 设置窗口大小不可变
		this.setResizable(false);
		// 设置窗口大小
		this.setSize(FRAME_WIDTH, FRAME_LENGTH);
		// 设置窗口背景为灰色
		this.setBackground(Color.GRAY);
		// 设置窗口位置居中
		this.setLocation(screenWidth / 2 - FRAME_WIDTH / 2, screenLength / 2 - FRAME_LENGTH / 2);
		// 设置窗口关闭模式
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// 给窗口加一个标题
		this.setTitle("坦克大战");
		// 添加键盘监听事件
		this.addKeyListener(new KeyMonitor());
		// 启动线程
		new Thread(new PaintThread()).start();
	}

	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			hometank.keyPressed(e);
			hometank2.keyPressed1(e);
		}

		public void keyReleased(KeyEvent e) {
			hometank.keyReleased(e);
			hometank2.ketReleased1(e);
		}
	}

	//
	class Play0 extends Thread {

		Player player;

		// String music;

		public void run() {

			try {

				play();

			} catch (FileNotFoundException | JavaLayerException e) {

				e.printStackTrace();

			}

		}

		public void play() throws FileNotFoundException, JavaLayerException {

		}

	}

//
	// 构造线程内部类
	public class PaintThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (printable) {
				repaint();
				try { // 睡眠
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("Start")) { // 获取事件名，相比较，执行指令
			System.out.println("开始新游戏");
			printable = false; // 停止画图
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "你确定要重新开始游戏吗？", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				printable = true;
				this.dispose(); // 标框消失
				new TankClient();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 重新启动线程
			}
		} else if (e.getActionCommand().equals("Exit")) {
			System.out.println("退出");
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "你确定要退出游戏吗？", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Stop")) {
			System.out.println("暂停");
			printable = false;
		} else if (e.getActionCommand().equals("Continue")) {
			System.out.println("继续");
			if (!printable) { // 先判断printable是否为false，若为true，执行下列语句会再开启一个进程
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Level1")) {
			System.out.println("游戏级别1");
		} else if (e.getActionCommand().equals("Level2")) {
			System.out.println("游戏级别2");

			for (int i = 0; i < tanks.size(); i++) {
				tanks.get(i).speedX = 8;
				tanks.get(i).speedY = 8;
				tanks.get(i).setLevel(37);

			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).setSpeedX(12);
				bullets.get(i).setSpeedY(12);
			}
		} else if (e.getActionCommand().equals("Level3")) {
			System.out.println("游戏级别3");
			for (int i = 0; i < tanks.size(); i++) {
				tanks.get(i).speedX = 10;
				tanks.get(i).speedY = 10;
				tanks.get(i).setLevel(36);

			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).setSpeedX(14);
			}
		} else if (e.getActionCommand().equals("Level4")) {
			System.out.println("游戏级别4");
			for (int i = 0; i < tanks.size(); i++) {
				tanks.get(i).speedX = 12;
				tanks.get(i).speedY = 12;
				tanks.get(i).setLevel(35);

			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).setSpeedX(14);
				bullets.get(i).setSpeedY(14);
			}
		} else if (e.getActionCommand().equals("Help")) {

			System.out.println("Help");
			printable = false;
			JOptionPane.showMessageDialog(null, "游戏介绍\n经典游戏《坦克大战》\n" + "带你重温童年的欢乐~\n" + "操作方式：\n"
					+ "Player1:WSAD移动	J键射击\n" + "Player2:↑↓←→移动	.键射击\n", "提示", JOptionPane.INFORMATION_MESSAGE);
			printable = true;
			new Thread(new PaintThread()).start();
		}
	}
}