package de.hechler.patrick.zeugs.objects;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;


public class EnumSet <E extends Enum <E>> implements NavigableSet <E> {
	
	private final Class <E> cls;
	private final E[] enums;
	private int size;
	
	@SuppressWarnings("unchecked")
	public EnumSet(Class <E> cls) {
		this.cls = cls;
		this.enums = (E[]) Array.newInstance(cls, cls.getEnumConstants().length);
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
		try {
			E e = cls.cast(o);
			return enums[e.ordinal()] != null;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	@Override
	public Iterator <E> iterator() {
		return new Iterator <E>() {
			
			private int index = -1;
			private int len = 0;
			
			@Override
			public boolean hasNext() {
				return this.len < EnumSet.this.size;
			}
			
			@Override
			public E next() {
				for (this.index ++ ; this.index < EnumSet.this.enums.length; this.index ++ ) {
					E e = EnumSet.this.enums[this.index];
					if (e != null) {
						return e;
					}
				}
				throw new NoSuchElementException("no more elements!");
			}
			
			@Override
			public void remove() {
				if (this.index < 0 || null == EnumSet.this.enums[this.index]) {
					throw new IllegalStateException("my last element has been already removed! (or I have no previusly index) this.index=" + this.index);
				}
				EnumSet.this.size -- ;
				this.len -- ;
				EnumSet.this.enums[this.index] = null;
			}
			
		};
	}
	
	@Override
	public Object[] toArray() {
		Object[] objs = new Object[this.size];
		fillArray(objs);
		return objs;
	}
	
	public E[] toEnumArray() {
		@SuppressWarnings("unchecked")
		E[] es = (E[]) Array.newInstance(this.cls, this.size);
		fillArray(es);
		return es;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length != this.size) {
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), this.size);
		}
		fillArray(a);
		return a;
	}
	
	@SuppressWarnings("unchecked")
	private <T> void fillArray(T[] ts) {
		for (int i = 0, ii = 0; i < this.size; ii ++ ) {
			for (; i < this.enums.length; i ++ ) {
				E e = this.enums[i];
				if (e != null) {
					ts[ii] = (T) e;
					break;
				}
			}
		}
	}
	
	@Override
	public boolean add(E e) {
		int o = e.ordinal();
		if (enums[o] != null) {
			return false;
		}
		enums[o] = e;
		return true;
	}
	
	@Override
	public boolean remove(Object obj) {
		try {
			E e = cls.cast(obj);
			int o = e.ordinal();
			if (this.enums[o] == null) {
				return false;
			}
			this.enums[o] = null;
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	@Override
	public boolean containsAll(Collection <?> c) {
		if (c instanceof EnumSet) {
			EnumSet <?> s = (EnumSet <?>) c;
			if (s.cls != this.cls) {
				return s.size == 0;
			}
			int more = this.size - s.size;
			int i = 0;
			int rem = s.size;
			while (more >= 0) {
				if (rem <= 0) {
					return true;
				}
				E my = this.enums[i];
				Object other = s.enums[i];
				if (my != null) {
					if (other != null) {
						i ++ ;
						rem -- ;
						continue;
					} else {
						more -- ;
						i ++ ;
						continue;
					}
				} else {
					if (other != null) {
						return false;
					} else {
						i ++ ;
						continue;
					}
				}
			}
			return false;
		}
		for (Object object : c) {
			if ( !contains(object)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean addAll(Collection <? extends E> c) {
		boolean m = false;
		if (c instanceof EnumSet) {
			EnumSet <? extends E> s = (EnumSet <? extends E>) c;
			for (int i = 0, remain = s.size; i < this.enums.length && remain > 0; i ++ ) {
				if (this.enums[i] != null) {
					continue;
				}
				if (s.enums[i] != null) {
					remain -- ;
					m = true;
					this.enums[i] = s.enums[i];
				}
			}
		} else {
			for (E e : c) {
				m |= add(e);
			}
		}
		return m;
	}
	
	@Override
	public boolean retainAll(Collection <?> c) {
		boolean m = false;
		if (c instanceof EnumSet) {
			EnumSet <?> s = (EnumSet <?>) c;
			int index = 0, remain;
			if (s.cls == this.cls) {
				int r0;
				for (remain = s.size, r0 = size; remain > 0 && r0 > 0; index ++ ) {
					if (this.enums[index] == null) {
						if (s.enums[index] != null) {
							remain -- ;
						}
						continue;
					}
					r0 -- ;
					if (s.enums[index] == null) {
						m = true;
						this.enums[index] = null;
					} else {
						remain -- ;
					}
				}
				remain = size - s.size;
			} else {
				remain = size;// enum set of an other class can't contain any enums of my class
			}
			for (; remain > 0; index ++ ) {
				if (this.enums[index] != null) {
					remain -- ;
					m = true;
					this.enums[index] = null;
				}
			}
		} else {
			if ( ! (c instanceof Set <?>)) {
				c = new HashSet <>(c);
			}
			for (int i = 0, remain = this.size; remain > 0; i ++ ) {
				if (this.enums[i] == null) {
					continue;
				}
				remain -- ;
				if ( !c.contains(this.enums[i])) {
					this.enums[i] = null;
					m = true;
				}
			}
		}
		return m;
	}
	
	@Override
	public boolean removeAll(Collection <?> c) {
		boolean m = false;
		if (c instanceof EnumSet) {
			EnumSet <?> s = (EnumSet <?>) c;
			if (s.cls != this.cls) {
				return false;
			}
			for (int i = 0, remain = s.size; i < this.enums.length && remain > 0; i ++ ) {
				if (this.enums[i] == null) {
					continue;
				}
				if (s.enums[i] != null) {
					remain -- ;
					m = true;
					this.enums[i] = null;
				}
			}
		} else {
			for (Object e : c) {
				m |= remove(e);
			}
		}
		return m;
	}
	
	@Override
	public void clear() {
		for (int i = 0; this.size > 0; i ++ ) {
			if (this.enums[i] != null) {
				this.size -- ;
				this.enums[i] = null;
			}
		}
	}
	
	@Override
	public Comparator <? super E> comparator() {
		return (a, b) -> Integer.compare(a.ordinal(), b.ordinal());
	}
	
	@Override
	public E first() throws NoSuchElementException {
		for (int i = 0; i < enums.length; i ++ ) {
			E e = enums[i];
			if (e != null) {
				return e;
			}
		}
		throw new NoSuchElementException("empty!");
	}
	
	@Override
	public E last() throws NoSuchElementException {
		for (int i = enums.length - 1; i >= 0; i -- ) {
			E e = enums[i];
			if (e != null) {
				return e;
			}
		}
		throw new NoSuchElementException("empty!");
	}
	
	@Override
	public E lower(E e) throws NoSuchElementException {
		for (int i = e.ordinal() - 1; i >= 0; i -- ) {
			e = enums[i];
			if (e != null) {
				return e;
			}
		}
		throw new NoSuchElementException("no lower found!");
	}
	
	@Override
	public E floor(E e) throws NoSuchElementException {
		for (int i = e.ordinal(); i >= 0; i -- ) {
			e = enums[i];
			if (e != null) {
				return e;
			}
		}
		throw new NoSuchElementException("no floor found!");
	}
	
	@Override
	public E ceiling(E e) throws NoSuchElementException {
		for (int i = e.ordinal(); i < enums.length; i ++ ) {
			e = enums[i];
			if (e != null) {
				return e;
			}
		}
		throw new NoSuchElementException("no ceiling found!");
	}
	
	@Override
	public E higher(E e) throws NoSuchElementException {
		for (int i = e.ordinal(); i < enums.length; i ++ ) {
			e = enums[i];
			if (e != null) {
				return e;
			}
		}
		throw new NoSuchElementException("no ceiling found!");
	}
	
	@Override
	public E pollFirst() {
		for (int i = 0; i < enums.length; i ++ ) {
			E e = enums[i];
			if (e != null) {
				size -- ;
				enums[i] = null;
				return e;
			}
		}
		throw new NoSuchElementException("empty!");
	}
	
	@Override
	public E pollLast() {
		for (int i = enums.length - 1; i >= 0; i -- ) {
			E e = enums[i];
			if (e != null) {
				size -- ;
				enums[i] = null;
				return e;
			}
		}
		throw new NoSuchElementException("empty!");
	}
	
	@Override
	public CustomEnumSet <E> descendingSet() {
		return subSet(0, enums.length).descendingSet();
	}
	
	@Override
	public Iterator <E> descendingIterator() {
		return subSet(0, enums.length).descendingIterator();
	}
	
	/**
	 * used for by the sub set methods
	 * 
	 * @param fromIndex
	 *            inclusive
	 * @param toIndex
	 *            exclusive
	 * @return the sub set
	 */
	private CustomEnumSet <E> subSet(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > this.enums.length || toIndex < fromIndex) {
			throw new IllegalArgumentException("illegal arguments: from=" + fromIndex + ", fromMin=0 to=" + toIndex + " toMax=" + this.enums.length);
		}
		return new CustomEnumSet <E>(new EnumArrayView <E>(this.enums, fromIndex, toIndex, () -> this.size ++ , () -> this.size -- ), this.cls, false);
	}
	
	@Override
	public CustomEnumSet <E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
		int from = fromElement.ordinal();
		if ( !fromInclusive) {
			from ++ ;
		}
		int to = toElement.ordinal();
		if (toInclusive) {
			to ++ ;
		}
		return subSet(from, to);
	}
	
	@Override
	public CustomEnumSet <E> headSet(E toElement, boolean inclusive) {
		int to = toElement.ordinal();
		if (inclusive) {
			to ++ ;
		}
		return subSet(0, to);
	}
	
	@Override
	public CustomEnumSet <E> tailSet(E fromElement, boolean inclusive) {
		int from = fromElement.ordinal();
		if ( !inclusive) {
			from ++ ;
		}
		return subSet(from, enums.length);
	}
	
	@Override
	public CustomEnumSet <E> subSet(E fromElement, E toElement) {
		return subSet(fromElement.ordinal(), toElement.ordinal());
	}
	
	@Override
	public CustomEnumSet <E> headSet(E toElement) {
		return subSet(0, toElement.ordinal());
	}
	
	@Override
	public CustomEnumSet <E> tailSet(E fromElement) {
		return subSet(fromElement.ordinal(), 0);
	}
	
	public static class CustomEnumSet <E extends Enum <E>> implements NavigableSet <E> {
		
		private final EnumArrayView <E> arr;
		private final Class <E> cls;
		private final boolean reversed;
		
		public CustomEnumSet(EnumArrayView <E> arr, Class <E> cls, boolean reversed) {
			this.arr = arr;
			this.cls = cls;
			this.reversed = reversed;
		}
		
		@Override
		public Comparator <? super E> comparator() {
			if (reversed) {
				return (a, b) -> Integer.compare(b.ordinal(), a.ordinal());
			} else {
				return (a, b) -> Integer.compare(a.ordinal(), b.ordinal());
			}
		}
		
		@Override
		public E first() {
			for (int i = arr.start; i < arr.end; i ++ ) {
				E e = arr.get(i);
				if (e != null) {
					return e;
				}
			}
			throw new UnsupportedOperationException("empty!");
		}
		
		@Override
		public E last() {
			for (int i = arr.end - 1; i >= arr.start; i -- ) {
				E e = arr.get(i);
				if (e != null) {
					return e;
				}
			}
			throw new UnsupportedOperationException("empty!");
		}
		
		@Override
		public int size() {
			int s = 0;
			for (int i = arr.start; i < arr.end; i ++ ) {
				E e = arr.get(i);
				if (e != null) {
					s ++ ;
				}
			}
			return s;
		}
		
		@Override
		public boolean isEmpty() {
			for (int i = arr.start; i < arr.end; i ++ ) {
				E e = arr.get(i);
				if (e != null) {
					return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean contains(Object o) {
			try {
				E e = cls.cast(o);
				e = arr.get(e.ordinal());
				return e != null;
			} catch (ClassCastException e) {
				return false;
			}
		}
		
		@Override
		public Object[] toArray() {
			int s = size();
			Object[] a = new Object[s];
			fillArray(a, s);
			return a;
		}
		
		@SuppressWarnings("unchecked")
		public E[] toEnumArray() {
			int s = size();
			E[] a = (E[]) Array.newInstance(cls, s);
			fillArray(a, s);
			return a;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			int s = size();
			if (a.length != s) {
				a = (T[]) Array.newInstance(a.getClass().getComponentType(), s);
			}
			fillArray(a, s);
			return a;
		}
		
		@SuppressWarnings("unchecked")
		private <T> void fillArray(T[] a, int s) {
			for (int i = arr.start, ai = 0; s > 0; i ++ ) {
				E e = arr.get(i);
				if (e != null) {
					s -- ;
					a[ai ++ ] = (T) e;
				}
			}
		}
		
		@Override
		public boolean add(E e) {
			int o = e.ordinal();
			E m = arr.get(o);
			if (m != null) {
				return false;
			} else {
				arr.set(o, e);
				return true;
			}
		}
		
		@Override
		public boolean remove(Object o) {
			try {
				E e = cls.cast(o);
				int i = e.ordinal();
				E m = arr.get(i);
				if (m == null) {
					return false;
				} else {
					arr.set(i, null);
					return true;
				}
			} catch (ClassCastException e) {
				return false;
			}
		}
		
		@Override
		public boolean containsAll(Collection <?> c) {
			if (c instanceof EnumSet <?>) {
				EnumSet <?> e = (EnumSet <?>) c;
				if (e.cls != cls) {
					return e.size == 0;
				} else {
					int remain = e.size;
					int i;
					for (i = 0; i < arr.start; i ++ ) {
						if (e.enums[i] != null) {
							return false;
						}
					}
					if (e.enums != arr.arr) {
						for (; i < arr.end; i ++ ) {
							if (e.enums[i] != null) {
								if (arr.get(i) == null) {
									return false;
								} else {
									remain -- ;
									if (remain <= 0) {
										return true;
									}
								}
							}
						}
					} else {
						i = arr.end;
					}
					for (; i < e.enums.length; i ++ ) {
						if (e.enums[i] != null) {
							return false;
						}
					}
					return true;
				}
			} else if (c instanceof CustomEnumSet <?>) {
				CustomEnumSet <?> e = (CustomEnumSet <?>) c;
				if (e.cls != cls) {
					return !c.isEmpty();
				}
				if (e.arr.arr != arr.arr) {
					for (int i = e.arr.start; i < e.arr.end; i ++ ) {
						Enum <?> ee = e.arr.get(i);
						if (ee != null) {
							if (arr.get(i) == null) {
								return false;
							}
						}
					}
					return true;
				} else {
					int ms = arr.start, me = arr.end, es = e.arr.start, ee = e.arr.end;
					if (ms < es) {
						for (int i = Math.max(me, es); i < ee; i ++ ) {
							if (e.arr.get(i) != null) {
								return false;
							}
						}
						return true;
					} else if (ms > ee) {
						for (int i = es; i < ee; i ++ ) {
							if (e.arr.get(i) != null) {
								return false;
							}
						}
						return true;
					} else {
						for (int i = es; i < ms; i ++ ) {
							if (e.arr.get(i) != null) {
								return false;
							}
						}
						for (int i = me; i < ee; i ++ ) {
							if (e.arr.get(i) != null) {
								return false;
							}
						}
						return true;
					}
				}
			} else {
				for (Object object : c) {
					if ( !contains(object)) {
						return false;
					}
				}
				return true;
			}
		}
		
		@Override
		public boolean addAll(Collection <? extends E> c) {
			boolean m = false;
			for (E e : c) {
				m |= add(e);
			}
			return m;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean retainAll(Collection <?> c) {
			if ( ! (c instanceof Set)) {
				EnumSet <E> zw = new EnumSet <>(cls);
				for (Object object : c) {
					if (cls.isInstance(object)) {
						zw.add((E) object);
					}
				}
				c = zw;
			}
			boolean mod = false;
			for (int i = arr.start; i < arr.end; i ++ ) {
				E e = arr.get(i);
				if ( !c.contains(e)) {
					arr.set(i, null);
					mod = true;
				}
			}
			return mod;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean removeAll(Collection <?> c) {
			if ( ! (c instanceof Set)) {
				EnumSet <E> zw = new EnumSet <>(cls);
				for (Object object : c) {
					if (cls.isInstance(object)) {
						zw.add((E) object);
					}
				}
				c = zw;
			}
			boolean mod = false;
			for (int i = arr.start; i < arr.end; i ++ ) {
				E e = arr.get(i);
				if (c.contains(e)) {
					arr.set(i, null);
					mod = true;
				}
			}
			return mod;
		}
		
		@Override
		public void clear() {
			for (int i = arr.start; i < arr.end; i ++ ) {
				arr.set(i, null);
			}
		}
		
		@Override
		public E lower(E e) {
			int i = e.ordinal();
			if (reversed) {
				if (i < arr.start) {
					i = arr.start;
				} else {
					i ++ ;
				}
				for (; i < arr.end; i ++ ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			} else {
				if (i > arr.end) {
					i = arr.end;
				}
				for (i -- ; i >= arr.start; i -- ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			}
			throw new NoSuchElementException("no lower found!");
		}
		
		@Override
		public E floor(E e) {
			int i = e.ordinal();
			if (reversed) {
				if (i < arr.start) {
					i = arr.start;
				}
				for (; i < arr.end; i ++ ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			} else {
				if (i > arr.end) {
					i = arr.end - 1;
				}
				for (; i >= arr.start; i -- ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			}
			throw new NoSuchElementException("no floor found!");
		}
		
		@Override
		public E ceiling(E e) {
			int i = e.ordinal();
			if ( !reversed) {
				if (i < arr.start) {
					i = arr.start;
				}
				for (; i < arr.end; i ++ ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			} else {
				if (i > arr.end) {
					i = arr.end - 1;
				}
				for (; i >= arr.start; i -- ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			}
			throw new NoSuchElementException("no floor found!");
		}
		
		@Override
		public E higher(E e) {
			int i = e.ordinal();
			if ( !reversed) {
				if (i < arr.start) {
					i = arr.start;
				} else {
					i ++ ;
				}
				for (; i < arr.end; i ++ ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			} else {
				if (i > arr.end) {
					i = arr.end;
				}
				for (i -- ; i >= arr.start; i -- ) {
					e = arr.get(i);
					if (e != null) {
						return e;
					}
				}
			}
			throw new NoSuchElementException("no lower found!");
		}
		
		@Override
		public E pollFirst() {
			if ( !reversed) {
				for (int i = arr.start; i < arr.end; i ++ ) {
					E e = arr.get(i);
					if (e != null) {
						arr.set(i, null);
						return e;
					}
				}
			} else {
				for (int i = arr.end - 1; i >= arr.start; i -- ) {
					E e = arr.get(i);
					if (e != null) {
						arr.set(i, null);
						return e;
					}
				}
			}
			throw new NoSuchElementException("no first found!");
		}
		
		@Override
		public E pollLast() {
			if (reversed) {
				for (int i = arr.start; i < arr.end; i ++ ) {
					E e = arr.get(i);
					if (e != null) {
						arr.set(i, null);
						return e;
					}
				}
			} else {
				for (int i = arr.end - 1; i >= arr.start; i -- ) {
					E e = arr.get(i);
					if (e != null) {
						arr.set(i, null);
						return e;
					}
				}
			}
			throw new NoSuchElementException("no first found!");
		}
		
		@Override
		public Iterator <E> iterator() {
			if (reversed) {
				return new Iterator <E>() {
					
					int last = -1;
					int index = arr.end;
					
					@Override
					public boolean hasNext() {
						for (index -- ; index >= arr.start; index -- ) {
							E e = arr.get(index);
							if (e != null) {
								index ++ ;
								return true;
							}
						}
						return false;
					}
					
					@Override
					public E next() {
						for (index -- ; index >= arr.start; index -- ) {
							E e = arr.get(index);
							if (e != null) {
								last = index;
								return e;
							}
						}
						throw new NoSuchElementException("finished iterating");
					}
					
					@Override
					public void remove() {
						if (last == -1) {
							throw new IllegalStateException("I don't have a last element!");
						}
						if (arr.get(last) == null) {
							throw new IllegalStateException("the last element does not exist anymore!");
						}
						arr.set(last, null);
					}
					
				};
			} else {
				return new Iterator <E>() {
					
					int last = -1;
					int index = arr.start;
					
					@Override
					public boolean hasNext() {
						for (index ++ ; index < arr.end; index ++ ) {
							E e = arr.get(index);
							if (e != null) {
								index -- ;
								return true;
							}
						}
						return false;
					}
					
					@Override
					public E next() {
						for (index ++ ; index < arr.end; index ++ ) {
							E e = arr.get(index);
							if (e != null) {
								last = index;
								return e;
							}
						}
						throw new NoSuchElementException("finished iterating");
					}
					
					@Override
					public void remove() {
						if (last == -1) {
							throw new IllegalStateException("I don't have a last element!");
						}
						if (arr.get(last) == null) {
							throw new IllegalStateException("the last element does not exist anymore!");
						}
						arr.set(last, null);
					}
					
				};
			}
		}
		
		@Override
		public CustomEnumSet <E> descendingSet() {
			return new CustomEnumSet <>(arr, cls, !reversed);
		}
		
		@Override
		public Iterator <E> descendingIterator() {
			return descendingSet().iterator();
		}
		
		/**
		 * creates a sub set if start is smaller then this {@link EnumArrayView#start start} or end is greater then this {@link EnumArrayView#end end} an
		 * {@link IllegalArgumentException} will be thrown
		 * 
		 * @param start
		 *            inclusive
		 * @param end
		 *            exclusive
		 * @return the subSet
		 */
		private CustomEnumSet <E> subSet(int start, int end) throws IllegalArgumentException {
			if (end > arr.end || start < arr.start || start > end) {
				throw new IllegalArgumentException("myRange: [min=" + arr.start + ", end=" + arr.end + "] wantedRange: [min=" + start + ", end=" + end + "]");
			}
			return new CustomEnumSet <>(new EnumArrayView <>(arr.arr, start, end, arr.incSize, arr.decSize), cls, reversed);
		}
		
		@Override
		public CustomEnumSet <E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) throws IllegalArgumentException {
			int start = fromElement.ordinal();
			if ( !fromInclusive) {
				start ++ ;
			}
			int end = toElement.ordinal();
			if (toInclusive) {
				end ++ ;
			}
			return subSet(start, end);
		}
		
		@Override
		public CustomEnumSet <E> headSet(E toElement, boolean inclusive) throws IllegalArgumentException {
			int end = toElement.ordinal();
			if (inclusive) {
				end ++ ;
			}
			return subSet(arr.start, end);
		}
		
		@Override
		public CustomEnumSet <E> tailSet(E fromElement, boolean inclusive) throws IllegalArgumentException {
			int start = fromElement.ordinal();
			if ( !inclusive) {
				start ++ ;
			}
			return subSet(start, arr.end);
		}
		
		@Override
		public CustomEnumSet <E> subSet(E fromElement, E toElement) throws IllegalArgumentException {
			int start = fromElement.ordinal();
			int end = toElement.ordinal();
			return subSet(start, end);
		}
		
		@Override
		public CustomEnumSet <E> headSet(E toElement) throws IllegalArgumentException {
			int end = toElement.ordinal();
			return subSet(arr.start, end);
		}
		
		@Override
		public CustomEnumSet <E> tailSet(E fromElement) throws IllegalArgumentException {
			int start = fromElement.ordinal();
			return subSet(start, arr.end);
		}
		
	}
	
	public static class EnumArrayView <T extends Enum <T>> {
		
		private final T[] arr;
		public final int start;
		public final int end;
		private final Runnable incSize;
		private final Runnable decSize;
		
		/**
		 * creates a view to the array.
		 * 
		 * when trying to get elements outside of the bounds <code>null</code> will be returned
		 * 
		 * @param arr
		 *            array
		 * @param startIndex
		 *            inclusive
		 * @param endIndex
		 *            exclusive
		 */
		public EnumArrayView(T[] arr, int start, int end, Runnable incSize, Runnable decSize) {
			this.arr = arr;
			this.start = start;
			this.end = end;
			this.incSize = incSize;
			this.decSize = decSize;
		}
		
		
		
		public T get(int i) {
			if (i < start) {
				return null;
			} else if (i >= end) {
				return null;
			} else {
				return arr[i];
			}
		}
		
		public void set(int i, T e) {
			if (i < start) {
				throw new IndexOutOfBoundsException("this ranged array starts at " + start + " but index is " + i);
			} else if (i >= end) {
				throw new IndexOutOfBoundsException("this ranged array is only up to " + end + " but index is " + i);
			} else {
				if (arr[i] != e) {
					if (arr[i] == null) {
						incSize.run();
					} else {
						decSize.run();
					}
				}
				arr[i] = e;
			}
		}
		
	}
	
}
