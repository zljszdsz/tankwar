package com.jsyunsi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class MetalWall {
	public static final int WIDTH = 20;
	public static final int LENGTH = 20;
	private int x, y;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static {
		image = tk.getImage(MetalWall.class.getResource("../../images/metalWall.gif"));
	}

	public MetalWall(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, WIDTH, LENGTH, null);
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, LENGTH);
	}
}