package de.hechler.patrick.zeugs.objects;

import de.hechler.patrick.zeugs.interfaces.Position2D;

public class UnchangeablePosition2D implements Position2D {
	
	public final int x;
	public final int y;
	
	
	
	public UnchangeablePosition2D(int x, int y) {
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
		throw new UnsupportedOperationException("setXY");
	}
	
	@Override
	public Position2D unchangeable() {
		return this;
	}
	
	@Override
	public boolean isChangeable() {
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if ( ! (obj instanceof Position2D)) return false;
		Position2D other = (Position2D) obj;
		if (x != other.getX()) return false;
		if (y != other.getY()) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "UnchangeablePosition2D[" + x + "|" + y + "]";
	}
	
}
