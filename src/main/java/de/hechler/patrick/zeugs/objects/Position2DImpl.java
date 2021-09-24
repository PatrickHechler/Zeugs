package de.hechler.patrick.zeugs.objects;

import de.hechler.patrick.zeugs.interfaces.Position2D;

public class Position2DImpl implements Position2D {
	
	private int x, y;
	
	
	public Position2DImpl() {
		this(0, 0);
	}
	
	public Position2DImpl(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}
