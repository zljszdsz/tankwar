package com.jsyunsi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Tree {
	public static final int WIDTH = 30;
	public static final int LENGTH = 30;
	private int x, y;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image image = null;
	static {
		image = tk.getImage(Tree.class.getResource("../../images/tree.gif"));
	}

	public Tree(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, WIDTH, LENGTH, null);
	}

}
