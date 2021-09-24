package de.hechler.patrick.zeugs.objects;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

import de.hechler.patrick.zeugs.interfaces.Stack;


public class StackImpl <E> implements Stack <E> {
	
	private final static int DEFAULT_GROW = 10;
	
	private E[] elements;
	private int size;
	private final int grow;
	
	
	
	public StackImpl(Class <E> cls) {
		this(cls, DEFAULT_GROW);
	}
	
	@SuppressWarnings("unchecked")
	public StackImpl(Class <E> cls, int growSize) {
		this.elements = (E[]) Array.newInstance(cls, growSize);
		this.grow = growSize;
	}
	
	
	
	@Override
	public E peek() throws NoSuchElementException {
		try {
			return elements[size - 1];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new NoSuchElementException("this stack is empty");
		}
	}
	
	@Override
	public E pop() {
		try {
			E e = elements[ -- size];
			return e;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new NoSuchElementException("this stack is empty");
		}
	}
	
	@Override
	public void push(E e) {
		if (size >= elements.length) {
			@SuppressWarnings("unchecked")
			E[] el = (E[]) Array.newInstance(elements.getClass().getComponentType(), grow + size);
			System.arraycopy(elements, 0, el, 0, size);
		}
		elements[size ++ ] = e;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
}
