package de.hechler.patrick.zeugs.check;

import java.io.Serializable;

/**
 * This interface contains methods to check and modify two {@code int} values
 * 
 * @author Patrick
 *
 */
public interface IntInt extends Serializable, Comparable <IntInt> {
	
	/**
	 * returns the first value of this {@link IntInt}
	 * 
	 * @return the first value of this {@link IntInt}
	 */
	int getFirst();
	
	/**
	 * returns the second value of this {@link IntInt}
	 * 
	 * @return the second value of this {@link IntInt}
	 */
	int getSecond();
	
	/**
	 * sets the first value of this {@link IntInt}
	 * 
	 * @param newFirst
	 *            the new first value of this {@link IntInt}
	 */
	void setFirst(int newFirst);
	
	/**
	 * sets the second value of this {@link IntInt}
	 * 
	 * @param newSecond
	 *            the new second value of this {@link IntInt}
	 */
	void setSecond(int newsecond);
	
	/**
	 * adds the given value to the first value of this {@link IntInt}
	 * 
	 * @param add
	 *            the value to be added
	 * @implNote it behaves like <code>{int zw = {@link #getFirst()};{@link #setFirst(int) setFirst(zw + add)};}</code>
	 */
	void addFirst(int add);
	
	/**
	 * adds the given value to the second value of this {@link IntInt}
	 * 
	 * @param add
	 *            the value to be added
	 * @implNote it behaves like <code>{int zw = {@link #getSecond()};{@link #setSecond(int) setSecond(zw + add)};}</code>
	 */
	void addSecond(int add);
	
	/**
	 * subtracts the given value from the first value of this {@link IntInt}
	 * 
	 * @param sub
	 *            the value to be subtract
	 */
	void subFirst(int sub);
	
	/**
	 * subtracts the given value from the second value of this {@link IntInt}
	 * 
	 * @param sub
	 *            the value to be subtract
	 */
	void subSecond(int sub);
	
	/**
	 * increments the first of this {@link IntInt} by 1
	 */
	void incFirst();
	
	/**
	 * increments the second of this {@link IntInt} by 1
	 */
	void incSecond();
	
	/**
	 * decrements the first of this {@link IntInt} by 1
	 */
	void decFirst();
	
	/**
	 * decrements the second of this {@link IntInt} by 1
	 */
	void decSecond();
	
	/**
	 * compares the given value with the first value of this {@link IntInt}<br>
	 * it returns a positive value if the first value is greater, a negative value if the first value is smaller and zero if it is equal
	 * 
	 * @param val
	 *            the value to be compared
	 * @return the result of the compare
	 */
	int compareFirst(int val);
	
	/**
	 * compares the given value with the first value of this {@link IntInt}<br>
	 * it returns a positive value if the first value is greater, a negative value if the first value is smaller and zero if it is equal
	 * 
	 * @param val
	 *            the value to be compared
	 * @return the result of the compare
	 */
	int compareSecond(int val);
	
	/**
	 * compares the second value with the first value of this {@link IntInt}<br>
	 * it returns a positive value if the first value is greater, a negative value if the first value is smaller and zero if they are equal
	 * 
	 * @return the result of the compare
	 */
	int compareFirstWithSecond();
	
	/**
	 * returns <code>true</code> if the given value is the first value of this {@link IntInt} and <code>false</code> if not
	 * 
	 * @param val
	 *            the value to be checked
	 * @return true if the given value is the first value of this {@link IntInt} and <code>false</code> if not
	 */
	boolean isFirst(int val);
	
	/**
	 * returns <code>true</code> if the given value is the second value of this {@link IntInt} and <code>false</code> if not
	 * 
	 * @param val
	 *            the value to be checked
	 * @return true if the given value is the second value of this {@link IntInt} and <code>false</code> if not
	 */
	boolean isSecond(int val);
	
	/**
	 * returns <code>true</code> if the first value of this {@link IntInt} is the second value of this {@link IntInt} and <code>false</code> if not
	 * 
	 * @param val
	 *            the value to be checked
	 * @return <code>true</code> if the first value of this {@link IntInt} is the second value of this {@link IntInt} and <code>false</code> if not
	 */
	boolean bothSame();
	
	/**
	 * returns a linked reverse {@link IntInt}
	 * 
	 * @return a linked reverse {@link IntInt}
	 */
	IntInt reverse();
	
	
	@Override
	int compareTo(IntInt o);
	
}
