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
	// �����Ĵ�С
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_LENGTH = 600;
	private int restart;
	private long start, end;

	private int n = 0, m = 0;

	// ��ȡ������Ļ��С
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screenSize = tk.getScreenSize();
	int screenWidth = screenSize.width;
	int screenLength = screenSize.height;
	// ����һ��������ͣ/�����ı���
	private boolean printable = true;
	// ����һ������
	Image image = null;
	// ����˵�
	MenuBar jmb = null; // ��̨
	Menu jm1, jm2, jm3, jm4 = null; // ֻ������jm4 �˵�
	MenuItem jmi1, jmi2, jmi3, jmi4, jmi5, jmi6, jmi7, jmi8, jmi9; // �Ӳ˵�
	// ���������
	River river = new River(100, 100);
	// ��������
	List<Tree> trees = new ArrayList<Tree>(); // ����
	// �������ǽ
	List<MetalWall> metalWalls = new ArrayList<MetalWall>();
	// ������ͨǽ
	List<CommonWall> otherWalls = new ArrayList<CommonWall>();
	// �������Χ��ǽ
	List<CommonWall> homeWalls = new ArrayList<CommonWall>();
	// �����
	Home home = new Home(383, 562);
	// �����ҷ�̹��
	Tank hometank = new Tank(Direction.STOP, true, 310, 565, 1, this);
	Tank hometank2 = new Tank(Direction.STOP, true, 460, 565, 2, this);

	// ����з�̹��
	List<Tank> tanks = new ArrayList<Tank>();
	// �����ӵ�
	List<Bullet> bullets = new ArrayList<Bullet>();
	// ���屬ըЧ������
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	// ����Ѫ��
	Blood blood = new Blood((int) (Math.random() * 800), (int) (Math.random() * 600));
	// �������
	Hudun hudun = new Hudun(400, 400);
	// ��������
	Star star = new Star(400, 400);
	// ����ը��
	Bomb bomb = new Bomb(400, 400);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankClient();
	}

	public void update(Graphics g) { // g�ǿ�Ļ���
		image = this.createImage(FRAME_WIDTH, FRAME_LENGTH); // ���û����Ļ��ʺͿ�Ĵ�С��ͬ
		Graphics gps = image.getGraphics(); // gps�ǻ����Ļ���
		framepaint(gps);
		g.drawImage(image, 0, 0, null); // ���û���λ�ã�����������
	}

	private void framepaint(Graphics g) {
		// TODO Auto-generated method stub
		// ���ƺ���
		river.draw(g);
		hometank.collideWithRiver(river); // �ҷ�̹�˺ͺ�����ײ
		hometank2.collideWithRiver(river); // �ҷ�̹��2�ͺ�����ײ
		for (int i = 0; i < tanks.size(); i++) { // �з�̹�˺ͺ�����ײ
			tanks.get(i).collideWithRiver(river);
		}

		// ����������ͨǽ
		for (int i = 0; i < otherWalls.size(); i++) {
			otherWalls.get(i).draw(g);
			hometank.collideWithCommonWall(otherWalls.get(i)); // �ҷ�̹�˺���ͨǽ��ײ
			hometank2.collideWithCommonWall(otherWalls.get(i)); // �ҷ�̹��2����ͨǽ��ײ
			for (int j = 0; j < tanks.size(); j++) { // �з�̹�˺���ͨ��ײ
				tanks.get(j).collideWithCommonWall(otherWalls.get(i));
			}
		}
		// ���Ƽ���Χ��ǽ
		for (int i = 0; i < homeWalls.size(); i++) {
			homeWalls.get(i).draw(g);
			hometank.collideWithHomeWall(homeWalls.get(i)); // �ҷ�̹�˺ͼ�ǽ��ײ
			hometank2.collideWithHomeWall(homeWalls.get(i)); // �ҷ�̹��2�ͼ�ǽ��ײ
			for (int j = 0; j < tanks.size(); j++) { // �з�̹�˺ͼ�ǽ��ײ
				tanks.get(j).collideWithCommonWall(homeWalls.get(i));
			}
		}
		// ���ƽ���ǽ
		for (int i = 0; i < metalWalls.size(); i++) {
			metalWalls.get(i).draw(g);
			hometank.collideWithMetalWall(metalWalls.get(i)); // �ҷ�̹�˺ͽ���ǽ��ײ
			hometank2.collideWithMetalWall(metalWalls.get(i)); // �ҷ�̹�˺ͽ���ǽ��ײ
			for (int j = 0; j < tanks.size(); j++) { // �з�̹�˺ͽ���ǽ��ײ
				tanks.get(j).collideWithMetalWall(metalWalls.get(i));
			}
		}
		// ����Bossսʱ��ǽ��ɽ���ǽ
		if (n == 1) {
			for (int i = 0; i < 4; i++) {
				metalWalls.add(new MetalWall(360, 560 + i * MetalWall.LENGTH));
				metalWalls.add(new MetalWall(420, 560 + i * MetalWall.LENGTH));
				metalWalls.add(new MetalWall(360 + i * MetalWall.WIDTH, 540));
			}
		}
		// ���Ƽ�
		home.draw(g);
		hometank.collideWithHome(home); // �ҷ�̹�˺ͼ���ײ
		hometank2.collideWithHome(home); // �ҷ�̹��2�ͼ���ײ

		for (int j = 0; j < tanks.size(); j++) { // �з�̹�˺ͼ���ײ
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
		// �����ҷ�̹��,
		hometank.draw(g);
		hometank2.draw(g);
		// ���Ƶз�̹��
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).draw(g);
		}

		if (tanks.size() == 0) {
			printable = false;
			Object[] options = { "ȷ��" };
			int response = JOptionPane.showOptionDialog(this, "����ʤ��", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.exit(0);
			}
			g.setFont(new Font("", Font.BOLD, 20));
			g.setColor(Color.red);

		}

		// ̹�˺�̹��֮����ײ
		hometank.collideWithTanks(tanks);
		hometank.collideWithhomeTank(hometank2);
		hometank2.collideWithTanks(tanks);
		hometank2.collideWithhomeTank(hometank);

		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).collideWithTanks(tanks);
		}
		// �����ӵ�,�ӵ��ĳ�ʼ����Tank����ʵ��
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			bullet.draw(g);
			// �ӵ���������ͨǽ��ײ
			for (int j = 0; j < otherWalls.size(); j++) {
				bullet.hitCommonWall(otherWalls.get(j));
			}
			// �ӵ��ͼ�ǽ��ײ
			for (int j = 0; j < homeWalls.size(); j++) {
				bullet.hitCommonWall(homeWalls.get(j));
			}
			// �ӵ��ͽ���ǽ��ײ
			if (!hometank.isPb()) {
				for (int j = 0; j < metalWalls.size(); j++) {
					bullet.hitMetalWall(metalWalls.get(j));
				}
			}

			// �ҷ��ӵ��͵з�̹����ײ
			for (int j = 0; j < tanks.size(); j++) {
				bullet.hitTank(tanks.get(j));
			}
			for (int j = 0; j < hudun.mw.size(); j++) {
				bullet.hitMetalWall(hudun.mw.get(j));
			}

			// �з��ӵ����ҷ�̹����ײ
			bullet.hitTank(hometank);
			bullet.hitTank(hometank2);

			// �ӵ��ͼ���ײ
			pb = bullet.hitHome(home, i);
			if (pb) {
				printable = false;
				Object[] options = { "ȷ��" };
				int response = JOptionPane.showOptionDialog(this, "��ʧ�ܣ�", "", JOptionPane.YES_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (response == 0) {
					System.exit(0);
				}
			}
			// �ӵ����ӵ���ײ
			bullet.hitBullet(bullets);
			//
			if (hometank.isPb()) {
				bullet.hitmetalwall(metalWalls);
				bullet.hithomemetalwall(hudun);
			}
		}
		// ��������
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).draw(g);
		}
		// ̹��״̬
		g.setFont(new Font("", Font.BOLD, 20));
		g.setColor(Color.green);
		g.drawString("ʣ��з�̹������" + tanks.size(), 50, 100);
		g.drawString("�ҷ�̹������ֵ" + hometank.getLife(), 500, 100);
		g.drawString("�ѷ�̹������ֵ" + hometank2.getLife(), 300, 100);
		if ((hometank.getLife() <= 0) && (hometank2.getLife() <= 0)) {

			printable = false;

			Object[] options = { "ȷ��" };
			int response = JOptionPane.showOptionDialog(this, "��ʧ��!", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.exit(0);
			}
		}
		// ���Ʊ�ըЧ��
		for (int i = 0; i < bombTanks.size(); i++) {
			bombTanks.get(i).draw(g);
		}
		// ����Ѫ��
		blood.draw(g);
		// ̹�˺�Ѫ����ײ
		hometank.collideWithBlood(blood);
		hometank2.collideWithBlood(blood);
		// ���ƶ���
		hudun.draw(g);
		// ̹�˺Ͷ�����ײ
		hometank.collideWithHudun(hudun);
		hometank2.collideWithHudun(hudun);
		//
		hometank.reMetalWall(hudun);
		hometank2.reMetalWall(hudun);
		// ��������
		star.draw(g);
		// �ҷ�̹�˺�������ײ
		hometank.collideWithStar(star);
		hometank2.collideWithStar(star);
		// ����ը��
		bomb.draw(g);

		// �ҷ�̹�˺�ը����ײ
		hometank.collideWithBomb(bomb);
		hometank2.collideWithBomb(bomb);
		//
		hometank.BombTank(tanks);
		hometank2.BombTank(tanks);
	}

	// �޲ι���
	public TankClient() {

		new Play0().start();

		bombTanks.add(new BombTank(-100, -100, this));
		// ��ʼ��20���з�̹��
		for (int i = 0; i < 20; i++) {
			if (i < 5) {
				tanks.add(new Tank(Direction.D, false, 10, 150 + i * 50, 1, this));
			} else if (i < 14) {
				tanks.add(new Tank(Direction.D, false, 200 + (i - 5) * 45, 45, 1, this));
			} else {
				tanks.add(new Tank(Direction.D, false, 710, 300 + (i - 14) * 50, 1, this));
			}
		}
		// ��ʼ��������Ϣ�����ϣ�
		for (int i = 0; i < 4; i++) {
			trees.add(new Tree(0 + i * Tree.WIDTH, 400));
			trees.add(new Tree(240 + i * Tree.WIDTH, 400));
			trees.add(new Tree(460 + i * Tree.WIDTH, 400));
			trees.add(new Tree(680 + i * Tree.WIDTH, 400));
		}
		// ��ʼ������ǽ
		for (int i = 0; i < 10; i++) {
			metalWalls.add(new MetalWall(140 + i * MetalWall.WIDTH, 120));
			metalWalls.add(new MetalWall(140 + i * MetalWall.WIDTH, 150));
			metalWalls.add(new MetalWall(620, 430 + i * MetalWall.LENGTH));
		}
		// ��ʼ��������ͨǽ
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
		// ��ʼ������Χ��ǽ
		for (int i = 0; i < 4; i++) {
			homeWalls.add(new CommonWall(360, 560 + i * CommonWall.LENGTH));
			homeWalls.add(new CommonWall(420, 560 + i * CommonWall.LENGTH));
			homeWalls.add(new CommonWall(360 + i * CommonWall.WIDTH, 540));
		}
		// ��ʼ���˵�
		jmb = new MenuBar();
		jm1 = new Menu("��Ϸ");
		jm2 = new Menu("��ͣ/����");
		jm3 = new Menu("��Ϸ����");
		jm4 = new Menu("����");
		jmi1 = new MenuItem("��ʼ����Ϸ");
		jmi1.addActionListener(this); // ��jmi1�󶨵�ǰ����
		jmi1.setActionCommand("Start"); // �����¼�����
		jmi2 = new MenuItem("�˳�");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3 = new MenuItem("��ͣ");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4 = new MenuItem("����");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5 = new MenuItem("��Ϸ����1");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("Level1");
		jmi6 = new MenuItem("��Ϸ����2");
		jmi6.addActionListener(this);
		jmi6.setActionCommand("Level2");
		jmi7 = new MenuItem("��Ϸ����3");
		jmi7.addActionListener(this);
		jmi7.setActionCommand("Level3");
		jmi8 = new MenuItem("��Ϸ����4");
		jmi8.addActionListener(this);
		jmi8.setActionCommand("Level4");
		jmi9 = new MenuItem("��Ϸ˵��");
		jmi9.addActionListener(this);
		jmi9.setActionCommand("Help");
		// ���������ʽ
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
		jmb.add(jm1); // �˵��ӵ���̨��
		jmb.add(jm2);
		jmb.add(jm3);
		jmb.add(jm4);
		jm1.add(jmi1); // �Ѹ��Ӳ˵��ӵ���Ӧ�Ĳ˵���
		jm1.add(jmi2);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jm3.add(jmi5);
		jm3.add(jmi6);
		jm3.add(jmi7);
		jm3.add(jmi8);
		jm4.add(jmi9);
		this.setMenuBar(jmb);
		// ���ô��ڿɼ�
		this.setVisible(true);
		// ���ô��ڴ�С���ɱ�
		this.setResizable(false);
		// ���ô��ڴ�С
		this.setSize(FRAME_WIDTH, FRAME_LENGTH);
		// ���ô��ڱ���Ϊ��ɫ
		this.setBackground(Color.GRAY);
		// ���ô���λ�þ���
		this.setLocation(screenWidth / 2 - FRAME_WIDTH / 2, screenLength / 2 - FRAME_LENGTH / 2);
		// ���ô��ڹر�ģʽ
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// �����ڼ�һ������
		this.setTitle("̹�˴�ս");
		// ��Ӽ��̼����¼�
		this.addKeyListener(new KeyMonitor());
		// �����߳�
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
	// �����߳��ڲ���
	public class PaintThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (printable) {
				repaint();
				try { // ˯��
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
		if (e.getActionCommand().equals("Start")) { // ��ȡ�¼�������Ƚϣ�ִ��ָ��
			System.out.println("��ʼ����Ϸ");
			printable = false; // ֹͣ��ͼ
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ���¿�ʼ��Ϸ��", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				printable = true;
				this.dispose(); // �����ʧ
				new TankClient();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // ���������߳�
			}
		} else if (e.getActionCommand().equals("Exit")) {
			System.out.println("�˳�");
			printable = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ�˳���Ϸ��", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Stop")) {
			System.out.println("��ͣ");
			printable = false;
		} else if (e.getActionCommand().equals("Continue")) {
			System.out.println("����");
			if (!printable) { // ���ж�printable�Ƿ�Ϊfalse����Ϊtrue��ִ�����������ٿ���һ������
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Level1")) {
			System.out.println("��Ϸ����1");
		} else if (e.getActionCommand().equals("Level2")) {
			System.out.println("��Ϸ����2");

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
			System.out.println("��Ϸ����3");
			for (int i = 0; i < tanks.size(); i++) {
				tanks.get(i).speedX = 10;
				tanks.get(i).speedY = 10;
				tanks.get(i).setLevel(36);

			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).setSpeedX(14);
			}
		} else if (e.getActionCommand().equals("Level4")) {
			System.out.println("��Ϸ����4");
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
			JOptionPane.showMessageDialog(null, "��Ϸ����\n������Ϸ��̹�˴�ս��\n" + "��������ͯ��Ļ���~\n" + "������ʽ��\n"
					+ "Player1:WSAD�ƶ�	J�����\n" + "Player2:���������ƶ�	.�����\n", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			printable = true;
			new Thread(new PaintThread()).start();
		}
	}
}