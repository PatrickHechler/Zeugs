package de.hechler.patrick.zeugs.interfaces;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public interface Stack <E> extends Collection <E>, Iterator <E> {
	
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
	 * returns the number of elements in this {@link Stack}
	 * 
	 * @return the number of elements in this {@link Stack}
	 */
	int size();
	
	/**
	 * returns <code>true</code> if {@link #size()} is {@code 0} and <code>false</code> if not
	 * 
	 * @return <code>true</code> if {@link #size()} is {@code 0} and <code>false</code> if not
	 */
	boolean isEmpty();
	
	/**
	 * Returns the next element in the iteration.<br>
	 * 
	 * This method is equivalent to the {@link #pop()} method. So you can get the next Element with {@link #peek()} without consuming it.
	 *
	 * @return the next element in the iteration, which would be the return value of {@link #pop()}.
	 * 
	 * @see #pop()
	 * @see #peek()
	 * @see #push(Object)
	 * @see #add(Object)
	 */
	@Override
	default E next() {
		return pop();
	}
	
	/**
	 * Returns true if the iteration has more elements.(In other words, returns true if next would return an element rather than throwing an exception.)<br>
	 * 
	 * This method returns always <code>true</code>, when {@link #size()} is bigger than {@code 0}.<br>
	 * 
	 * So this method is reversed to the {@link #isEmpty()} method.
	 * 
	 * @return <code>true</code> if the iteration has more elements. In other words the reversed result of {@link #isEmpty()}
	 * 
	 * @see #isEmpty()
	 * @see #size()
	 */
	@Override
	default boolean hasNext() {
		return !isEmpty();
	}
	
	/**
	 * Ensures that this collection contains the specified element (optional operation). Returns <code>true</code> if this collection changed as a result of the call. <i>Returns
	 * never <code>false</code>.</i>
	 * <p>
	 *
	 * Collections that support this operation may place limitations on what elements may be added to this collection. In particular, some collections will refuse to add
	 * <tt>null</tt> elements, and others will impose restrictions on the type of elements that may be added. Collection classes should clearly specify in their documentation any
	 * restrictions on what elements may be added.
	 * <p>
	 *
	 * If a collection refuses to add a particular element for any reason, it <i>must</i> throw an exception (rather than returning <code>false</code>). This preserves the
	 * invariant that a collection always contains the specified element after this call returns.
	 * <p>
	 * 
	 * the add operation is equally to the {@link #push(Object)} operations (except it returns <code>boolean true</code> and not <code>void</code>)
	 * 
	 * @param e
	 *            element whose presence in this collection is to be ensured
	 * @return always <code>true</code>
	 * @throws UnsupportedOperationException
	 *             if the {@link #add(Object)}/{@link #push(Object)} operation is not supported by this collection
	 * @throws ClassCastException
	 *             if the class of the specified element prevents it from being added to this collection
	 * @throws NullPointerException
	 *             if the specified element is null and this collection does not permit null elements
	 * @throws IllegalArgumentException
	 *             if some property of the element prevents it from being added to this collection
	 * @throws IllegalStateException
	 *             if the element cannot be added at this time due to insertion restrictions
	 * @see #push(Object)
	 */
	@Override
	default boolean add(E e) {
		push(e);
		return true;
	}
	
	@Override
	default boolean addAll(Collection <? extends E> c) {
		boolean mod = false;
		for (E e : c) {
			push(e);
			mod = true;
		}
		return mod;
	}
	
}
