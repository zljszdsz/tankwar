package com.jsyunsi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

public class Bomb {
	public static final int WIDTH = 30;
	public static final int LENGTH = 30;
	private int x, y;
	private boolean printable = false;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static {
		image = tk.getImage(Blood.class.getResource("../../images/bomb.jpg"));
	}
	public Bomb(int x,int y) {
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
	
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, LENGTH);
	}

}
