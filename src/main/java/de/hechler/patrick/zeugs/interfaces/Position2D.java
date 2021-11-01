package de.hechler.patrick.zeugs.interfaces;

import de.hechler.patrick.zeugs.objects.UnchangeablePosition2D;

public interface Position2D {
	
	/**
	 * returns the {@link #getX() X} value of this {@link Position2D}
	 * 
	 * @return the {@link #getX() X} value of this {@link Position2D}
	 */
	int getX();
	
	/**
	 * returns the {@link #getY() Y} value of this {@link Position2D}
	 * 
	 * @return the {@link #getY() Y} value of this {@link Position2D}
	 */
	int getY();
	
	/**
	 * sets the {@link #getX() X} and {@link #getY() Y} values of this {@link Position2D} (optional operation)
	 * @param x the new {@link #getX() X} value
	 * @param y the new {@link #getY() Y} value
	 * @throws UnsupportedOperationException if this {@link Position2D} is not {@link #isChangeable() changeable}
	 */
	void setXY(int x, int y) throws UnsupportedOperationException;
	
	/**
	 * returns a unchangeable {@link Position2D}.
	 * 
	 * A unchangeable {@link Position2D} can not change it's {@link #getX() X} and {@link #getY() Y} values.
	 * 
	 * @return a unchangeable copy of this {@link Position2D} or this {@link Position2D} if this is unchangeable
	 * @see #isChangeable()
	 */
	default Position2D unchangeable() {
		if (isChangeable()) {
			return new UnchangeablePosition2D(getX(), getY());
		} else {
			return this;
		}
	}
	
	/**
	 * returns <code>true</code> if the {@link #getX() X} and/or {@link #getY() Y} values of this {@link Position2D} may change and <code>false</code> if not
	 * 
	 * @return <code>true</code> if the {@link #getX() X} and/or {@link #getY() Y} values of this {@link Position2D} may change and <code>false</code> if not
	 */
	boolean isChangeable();
	
}
