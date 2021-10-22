package de.hechler.patrick.zeugs.objects;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import de.hechler.patrick.zeugs.interfaces.Stack;


public class StackImpl <E>  extends AbstractCollection <E> implements Stack <E> {
	
	private final static int DEFAULT_GROW = 10;
	
	protected E[] elements;
	protected int size;
	protected final int grow;
	protected int mod = 0;
	
	
	
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
		mod ++;
		try {
			E e = elements[ -- size];
			return e;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new NoSuchElementException("this stack is empty");
		}
	}
	
	@Override
	public void push(E e) {
		mod ++;
		if (size >= elements.length) {
			grow();
		}
		elements[size ++ ] = e;
	}
	
	private void grow() {
		@SuppressWarnings("unchecked")
		E[] el = (E[]) Array.newInstance(elements.getClass().getComponentType(), grow + size);
		System.arraycopy(elements, 0, el, 0, size);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (int i = size - 1; i >= 0; i -- ) {
			E el = this.elements[i];
			result = prime * result + (el == null ? 0 : el.hashCode());
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if ( ! (obj instanceof StackImpl <?>) || ((StackImpl <?>) obj).elements.getClass() != this.elements.getClass()) return false;
		@SuppressWarnings("unchecked")
		StackImpl <E> other = (StackImpl <E>) obj;
		for (int i = size - 1; i >= 0; i -- ) {
			E my = this.elements[i];
			E o = other.elements[i];
			if (my == null) {
				if (o != null) {
					return false;
				}
			} else {
				if ( !my.equals(o)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder e = new StringBuilder("Stack [");
		if (size > 0) {
			e.append(elements[size - 1]);
			for (int i = size - 2; i >= 0; i -- ) {
				e.append(", ").append(elements[i]);
			}
		}
		return e.append(']').toString();
	}
	
	@Override
	public void clear() {
		size = 0;
	}
	
	@Override
	public Iterator <E> iterator() {
		throw new UnsupportedOperationException("iterator");
	}
	
}
