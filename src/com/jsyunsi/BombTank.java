package com.jsyunsi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class BombTank {
	private int x, y;
	TankClient tc;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = null;
	static {
		images = new Image[] {
				tk.getImage(BombTank.class.getResource("../../images/1.gif")),
				tk.getImage(BombTank.class.getResource("../../images/2.gif")),
				tk.getImage(BombTank.class.getResource("../../images/3.gif")),
				tk.getImage(BombTank.class.getResource("../../images/4.gif")),
				tk.getImage(BombTank.class.getResource("../../images/5.gif")),
				tk.getImage(BombTank.class.getResource("../../images/6.gif")),
				tk.getImage(BombTank.class.getResource("../../images/7.gif")),
				tk.getImage(BombTank.class.getResource("../../images/8.gif")),
				tk.getImage(BombTank.class.getResource("../../images/9.gif")),
				tk.getImage(BombTank.class.getResource("../../images/10.gif")) };
	}

	public BombTank(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	int step = 0;

	public void draw(Graphics g) {
		if (step == 10) {
			this.tc.bombTanks.remove(this);
			step = 0;
			return;
		}
		g.drawImage(images[step], x, y, null);
		step++;
	}
}