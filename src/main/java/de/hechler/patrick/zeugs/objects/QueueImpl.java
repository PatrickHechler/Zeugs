package de.hechler.patrick.zeugs.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class QueueImpl <E> implements Queue <E> {
	
	/**
	 * for faster adding
	 */
	private QE <E> last = null;
	private QE <E> next = null;
	
	
	private static class QE <E> {
		
		QE <E> n;
		E e;
		
		public QE(E e) {
			this.e = e;
		}
		
	}
	
	
	@Override
	public int size() {
		QE <E> q = this.next;
		int s = 0;
		while (q != null) {
			s ++ ;
			q = q.n;
		}
		return s;
	}
	
	@Override
	public boolean isEmpty() {
		return this.next == null;
	}
	
	@Override
	public boolean contains(Object o) {
		QE <E> q = this.next;
		while (q != null) {
			if (o == null ? q.e == null : o.equals(q.e)) {
				return true;
			}
			q = q.n;
		}
		return false;
	}
	
	@Override
	public Iterator <E> iterator() {
		return new Iter <>(this.next);
	}
	
	private static class Iter <E> implements Iterator <E> {
		
		QE <E> n;
		QE <E> l = null;
		
		
		public Iter(QE <E> q) {
			this.n = q;
		}
		
		@Override
		public boolean hasNext() {
			return this.n != null;
		}
		
		@Override
		public E next() throws NoSuchElementException {
			QE <E> qq = this.n;
			if (qq == null) {
				throw new NoSuchElementException("no next");
			}
			this.l = qq;
			this.n = qq.n;
			return qq.e;
		}
		
		@Override
		public void remove() {
			if (this.l == null) {
				throw new NoSuchElementException("no last");
			}
			this.l.e = n.e;
			this.n = this.n.n;
			this.l.n = this.n;
			this.l = null;
		}
		
	}
	
	@Override
	public Object[] toArray() {
		List <Object> objs = new ArrayList <>();
		QE <E> q = this.next;
		while (q != null) {
			objs.add(q.e);
			q = q.n;
		}
		return objs.toArray(new Object[objs.size()]);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		final T[] orig = a;
		int i = 0;
		final int growSize = 5;
		QE <E> q = this.next;
		while (q != null) {
			if (i >= a.length) {
				a = Arrays.copyOf(a, growSize + a.length);
			}
			a[i ++ ] = (T) q.e;
			q = q.n;
		}
		if (i + 1 < a.length) {
			if (a == orig) {
				a[i ++ ] = null;
			} else {
				a = Arrays.copyOf(a, i);
			}
		}
		return a;
	}
	
	@Override
	public boolean remove(Object o) {
		QE <E> q = this.next, l = null;
		while (q != null) {
			if (o == null ? q.e == null : o.equals(q.e)) {
				if (l != null) {
					l.n = q.n;
				} else {
					// remove first element
					this.next = this.next.n;
				}
				return true;
			}
			l = q;
			q = q.n;
		}
		return false;
	}
	
	@Override
	public boolean containsAll(Collection <?> c) {
		for (Object o : c) {
			if ( !contains(o)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean addAll(Collection <? extends E> c) {
		for (E e : c) {
			QE <E> n = new QE <>(e);
			if (this.next == null) {
				this.next = n;
			}
			this.last.n = n;
			this.last = n;
		}
		return !c.isEmpty();
	}
	
	@Override
	public boolean removeAll(Collection <?> c) {
		if ( ! (c instanceof Set)) {
			c = new HashSet <>(c);
		}
		QE <E> q = this.next;
		QE <E> l = null;
		boolean m = false;
		while (q != null) {
			if (c.contains(q)) {
				if (l == null) {
					this.next = q.n;
				} else {
					l.n = q.n;
				}
				m = true;
			}
			l = q;
			q = q.n;
		}
		return m;
	}
	
	@Override
	public boolean retainAll(Collection <?> c) {
		if ( ! (c instanceof Set)) {
			c = new HashSet <>(c);
		}
		QE <E> q = this.next;
		QE <E> l = null;
		boolean m = false;
		while (q != null) {
			if ( !c.contains(q)) {
				if (l == null) {
					this.next = q.n;
				} else {
					l.n = q.n;
				}
				m = true;
			}
			l = q;
			q = q.n;
		}
		return m;
	}
	
	@Override
	public void clear() {
		this.last = null;
		this.next = null;
	}
	
	@Override
	public boolean add(E e) {
		if (this.next == null) {
			this.last = this.next = new QE <>(e);
		} else {
			this.last.n = new QE <>(e);
			this.last = this.last.n;
		}
		return true;
	}
	
	@Override
	public boolean offer(E e) {
		return add(e);
	}
	
	@Override
	public E remove() throws IllegalStateException {
		if (this.next == null) {
			throw new IllegalStateException("empty");
		}
		QE <E> e = this.next;
		this.next = e.n;
		return e.e;
	}
	
	@Override
	public E poll() {
		try {
			return remove();
		} catch (IllegalStateException e) {
			return null;
		}
	}
	
	@Override
	public E element() {
		if (this.next == null) {
			throw new IllegalStateException("empty");
		}
		return this.next.e;
	}
	
	@Override
	public E peek() {
		try {
			return element();
		} catch (IllegalStateException e) {
			return null;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("[");
		Iterator <E> iter = iterator();
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
		for (E e : this) {
			if (e == null) continue;
			result += prime * e.hashCode();
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if ( ! (obj instanceof Queue <?>)) return false;
		Queue <?> other = (Queue <?>) obj;
		Iterator <E> my = iterator();
		Iterator <?> o = other.iterator();
		while (true) {
			if ( !my.hasNext()) {
				if ( !o.hasNext()) {
					return true;
				} else {
					return false;
				}
			}
			if ( !o.hasNext()) {
				return false;
			}
			if ( !Objects.equals(my.next(), o.next())) {
				return false;
			}
		}
	}
	
}
