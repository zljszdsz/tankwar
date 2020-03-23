package com.jsyunsi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hudun {
	public static final int WIDTH = 30;
	public static final int LENGTH = 30;
	private int x, y;
	private boolean printable = false, pb = false;
	List<MetalWall> mw = new ArrayList<MetalWall>();
	TankClient tc;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static {
		image = tk.getImage(Blood.class.getResource("../../images/hudun.jpg"));
	}

	public Hudun(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private int step = 0;

	public void draw(Graphics g) {
		if (pb) {
			for (int i = 0; i < this.mw.size(); i++) {
				mw.get(i).draw(g);
			}
		}
		if (printable) {
			if (step == 150) {
				step = 0;
				printable = false;
				return;
			}
			g.drawImage(image, x, y, WIDTH, LENGTH, null);
		} else {
			if (step == 80) {
				printable = true;
				this.x = (int) (Math.random() * 750);
				this.y = (int) (Math.random() * 550);
			} else if (step > 80) {
				step = 0;
			}
		}
		step++;
	}

	public void superWall(List<CommonWall> cw) {
		for (int i = 0; i < cw.size(); i++) {
			int t = cw.get(i).getX();
			cw.get(i).setX(t - 450);
		}
		for (int i = 0; i < 2; i++) {
			mw.add(new MetalWall(360, 560 + i * MetalWall.LENGTH));
			mw.add(new MetalWall(420, 560 + i * MetalWall.LENGTH));
			mw.add(new MetalWall(360 + i * MetalWall.WIDTH, 540));
			mw.add(new MetalWall(400 + i * MetalWall.WIDTH, 540));
		}
		pb = true;
	}

	public void reWall(List<CommonWall> hw) {
		for (int i = 0; i < mw.size(); i++) {
			mw.remove(mw.get(i));
		}
		for (int i = 0; i < hw.size(); i++) {
			int t = hw.get(i).getX();
			hw.get(i).setX(t + 450);
		}
		pb = false;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, LENGTH);
	}
}