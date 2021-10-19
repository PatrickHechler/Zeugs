package de.hechler.patrick.zeugs.objects;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class QueueArrayImpl <E> implements Queue <E> {
	
	private final int grow;
	private int mod;
	private int size;
	private int start;
	private int lastI;
	private E[] es;
	
	
	public QueueArrayImpl(Class <E> cls) {
		this(cls, 10, 10);
	}
	
	@SuppressWarnings("unchecked")
	public QueueArrayImpl(Class <E> cls, int grow, int startSize) {
		this.mod = this.size = this.start = 0;
		this.lastI = -1;
		this.es = (E[]) Array.newInstance(cls, grow);
		this.grow = grow;
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
	public boolean contains(Object o) {
		int len = start > lastI ? es.length - 1 : lastI;
		for (int i = start; i <= len; i ++ ) {
			if (Objects.equals(o, es[i])) {
				return true;
			}
		}
		if (len != lastI) {
			for (int i = 0; i <= lastI; i ++ ) {
				if (Objects.equals(o, es[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public Iterator <E> iterator() {
		return new Iter();
	}
	
	@Override
	public Object[] toArray() {
		return toArray(new Object[this.size]);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) throws NullPointerException, ArrayStoreException {
		if (a == null) {
			throw new NullPointerException("the array a was null");
		}
		Class <?> cls = a.getClass().getComponentType();
		E[] earr = this.es;
		final int len = this.size;
		final int esLen = earr.length;
		if (a.length < len) {
			a = (T[]) Array.newInstance(cls, len);
		}
		for (int ai = 0, mi = this.start; ai < len; ai ++ ) {
			a[ai] = (T) earr[mi];
			mi ++ ;
			if (mi == esLen) {
				mi = 0;
			}
		}
		return a;
	}
	
	@Override
	public boolean remove(Object o) {
		for (int i = this.start; i != this.lastI + 1;) {
			if (Objects.equals(o, this.es[i])) {
				this.remove(i);
				return true;
			}
			if ( ++ i == this.es.length) {
				i = 0;
			}
		}
		return false;
	}
	
	/**
	 * removes the element at the index {@code i}.<br>
	 * 
	 * @param i
	 *            the index of the element to be removed
	 */
	private void remove(int i) {
		this.mod ++ ;
		if (i < this.start) {
			// && this.lastI < this.start) {
			System.arraycopy(this.es, i + 1, this.es, i, this.lastI - i + 1);
		} else if (this.lastI < this.start) {
			// && i > this.start) {
			System.arraycopy(this.es, i + 1, this.es, i, this.es.length - start);
			this.es[this.es.length] = this.es[0];
			System.arraycopy(this.es, 0, this.es, 1, this.lastI - i);
		} else {
			// this.start < i && this.lastI >= this.start
			int len = this.lastI - i;
			System.arraycopy(this.es, i + 1, this.es, i, len);
		}
		this.size -- ;
		if ( -- this.lastI == -1) {
			if (this.size > 0) {
				this.lastI = this.es.length - 1;
			}
		}
	}
	
	@Override
	public boolean containsAll(Collection <?> c) {
		for (Object obj : c) {
			if ( !this.contains(obj)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean addAll(Collection <? extends E> c) {
		@SuppressWarnings("unchecked")
		E[] add = c.toArray((E[]) Array.newInstance(this.es.getClass().getComponentType(), c.size()));
		addAll(add);
		return add.length > 0;
	}
	
	@SafeVarargs
	public final void addAll(E... add) {
		if (add.length == 0) {
			this.mod ++ ;
		}
		final int newLen = this.size + add.length;
		if (es.length <= newLen) {
			@SuppressWarnings("unchecked")
			E[] newArr = (E[]) Array.newInstance(this.es.getClass().getComponentType(), newLen + grow);
			if (this.lastI < this.start) {
				int firstLen = this.es.length - this.start;
				System.arraycopy(this.es, this.start, newArr, 0, firstLen);
				System.arraycopy(this.es, 0, newArr, firstLen, this.size - firstLen);
			} else {
				System.arraycopy(this.es, this.start, newArr, 0, this.size);
			}
			System.arraycopy(add, 0, newArr, this.size, add.length);
			this.es = newArr;
			this.start = 0;
			this.lastI = newLen - 1;
		} else {
//			System.err.printf("System.arraycopy(add->%s, 0, this.es->%s, (this.lastI->%d + 1)->%d, add.length->%d);\n", Arrays.deepToString(add), Arrays.deepToString(this.es),
//					this.lastI, this.lastI + 1, add.length);
			System.arraycopy(add, 0, this.es, this.lastI + 1, add.length);
			this.lastI += add.length;
		}
		this.size = newLen;
	}
	
	@Override
	public boolean removeAll(Collection <?> c) {
		if ( ! (c instanceof Set <?>)) {
			c = new HashSet <>(c);
		}
		int m = this.mod;
		for (Iter iter = new Iter(); iter.hasNext();) {
			E e = iter.next();
			if (c.contains(e)) {
				iter.remove();
			}
		}
		return m != this.mod;
	}
	
	@Override
	public boolean retainAll(Collection <?> c) {
		if ( ! (c instanceof Set <?>)) {
			c = new HashSet <>(c);
		}
		int m = this.mod;
		for (Iter iter = new Iter(); iter.hasNext();) {
			E e = iter.next();
			if ( !c.contains(e)) {
				iter.remove();
			}
		}
		return m != this.mod;
	}
	
	@Override
	public void clear() {
		this.size = this.start = 0;
		this.lastI = -1;
		this.mod ++ ;
	}
	
	@Override
	public boolean add(E e) {
		addAll(e);
		return true;
	}
	
	@Override
	public boolean offer(E e) {
		return add(e);
	}
	
	@Override
	public E remove() throws NoSuchElementException {
		if (this.size < 1) {
			throw new NoSuchElementException("empty queue");
		}
		int li = this.lastI;
		E e = this.es[li];
		remove(li);
		return e;
	}
	
	@Override
	public E poll() {
		try {
			return remove();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	@Override
	public E element() throws NoSuchElementException {
		if (this.size < 1) {
			throw new NoSuchElementException("empty queue");
		}
		int li = this.lastI;
		return this.es[li];
	}
	
	@Override
	public E peek() {
		try {
			return element();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public class Iter implements Iterator <E> {
		
		private int index = start;
		private int estMod = mod;
		
		@Override
		public boolean hasNext() {
			return this.index != QueueArrayImpl.this.lastI + 1;
		}
		
		@Override
		public E next() {
			if (QueueArrayImpl.this.mod != this.estMod) {
				throw new IllegalStateException("modified outside of this iterator");
			}
			if (this.index == QueueArrayImpl.this.lastI + 1) {
				throw new NoSuchElementException("no element");
			}
			E e = QueueArrayImpl.this.es[this.index ++ ];
			if (this.index >= QueueArrayImpl.this.es.length) {
				this.index = 0;
			}
			return e;
		}
		
		@Override
		public void remove() {
			if (QueueArrayImpl.this.mod != this.estMod) {
				throw new IllegalStateException("modified outside of this iterator");
			}
			QueueArrayImpl.this.remove(this.index);
			this.estMod ++ ;
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("[");
		Iter iter = new Iter();
		if (iter.hasNext()) {
			build.append(iter.next());
			while (iter.hasNext()) {
				build.append(", ").append(iter.next());
			}
		}
		return build.append("]").toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(this.es);
		result = prime * result + this.size;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if ( ! (obj instanceof Queue <?>)) return false;
		if (obj instanceof QueueArrayImpl <?>) {
			QueueArrayImpl <?> other = (QueueArrayImpl <?>) obj;
			if (other.es.getClass() != this.es.getClass()) return false;
			if (other.size != this.size) return false;
		}
		Queue <?> other = (Queue <?>) obj;
		Iter my = new Iter();
		Iterator <?> o = other.iterator();
		while (true) {
			if ( !my.hasNext()) {
				if (o.hasNext()) {
					return false;
				} else {
					return true;
				}
			}
			if ( !Objects.equals(my.next(), o.next())) {
				return false;
			}
		}
	}
	
}
