package de.hechler.patrick.zeugs.interfaces;

import java.util.NoSuchElementException;

public interface Stack <E> {
	
	/**
	 * returns the highest element of this {@link Stack} without removing it
	 * 
	 * @return the highest element of this {@link Stack} without removing it
	 * @throws NoSuchElementException
	 *             if this {@link Stack} has no elements (if {@link #size()} would return {@code 0})
	 */
	E peek() throws NoSuchElementException;
	
	/**
	 * returns the highest element of this {@link Stack} and removes it
	 * 
	 * @return the highest element of this {@link Stack}
	 * @throws NoSuchElementException
	 *             if this {@link Stack} has no elements (if {@link #size()} would return {@code 0})
	 */
	E pop() throws NoSuchElementException;
	
	/**
	 * adds the Element to the top of this {@link Stack}
	 * 
	 * @param e
	 *            the element to be added
	 */
	void push(E e);
	
	/**
	 * returns the number of elements in this stack
	 * 
	 * @return the number of elements in this stack
	 */
	int size();
	
	/**
	 * returns <code>true</code> if {@link #size()} is {@code 0} and <code>false</code> if not
	 * 
	 * @return <code>true</code> if {@link #size()} is {@code 0} and <code>false</code> if not
	 */
	boolean isEmpty();
	
}
