package de.hechler.patrick.zeugs.objects;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class ArrayListImpl <E> implements List <E> {
	
	private int grow;
	private int mod;
	private int size;
	private E[] arr;
	
	
	public ArrayListImpl(Class <E> cls, Collection <E> addAll) {
		this(cls, addAll, 10, 10);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayListImpl(Class <E> cls, Collection <E> addAll, int grow, int additionallInitSize) {
		this.mod = 1;
		this.grow = grow;
		Object[] objs = addAll.toArray();
		int len = objs.length;
		this.arr = (E[]) Array.newInstance(cls, len + additionallInitSize);
		System.arraycopy(objs, 0, arr, 0, len);
		this.size = len;
	}
	
	public ArrayListImpl(Class <E> cls) {
		this(cls, 10, 10);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayListImpl(Class <E> cls, int grow, int initSize) {
		this.mod = this.size = 0;
		this.grow = grow;
		this.arr = (E[]) Array.newInstance(cls, initSize);
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
		for (int i = 0; i < size; i ++ ) {
			if (Objects.equals(o, arr[i])) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Iterator <E> iterator() {
		return new Iter();
	}
	
	public class Iter implements ListIterator <E> {
		
		int i;
		int estMod = mod;
		Boolean next;
		
		
		public Iter() {
			i = 0;
		}
		
		public Iter(int index) {
			i = index;
		}
		
		@Override
		public boolean hasNext() {
			modCheck();
			return i < size;
		}
		
		@Override
		public E next() {
			modCheck();
			if (i >= size) {
				throw new NoSuchElementException("nextI=" + i + " size=" + size);
			}
			next = true;
			return arr[i ++ ];
		}
		
		@Override
		public void remove() {
			modCheck();
			if (next == null) {
				throw new IllegalStateException("did not called next/previus before");
			}
			if (next) {
				ArrayListImpl.this.remove(i - 1);
			} else {
				ArrayListImpl.this.remove(i);
			}
			estMod ++ ;
		}
		
		@Override
		public boolean hasPrevious() {
			modCheck();
			return i > 0;
		}
		
		@Override
		public E previous() {
			modCheck();
			if (i <= 0) {
				throw new NoSuchElementException("prevI=" + (i - 1));
			}
			next = false;
			return arr[ -- i];
		}
		
		@Override
		public int nextIndex() {
			modCheck();
			return i;
		}
		
		@Override
		public int previousIndex() {
			modCheck();
			return i - 1;
		}
		
		@Override
		public void set(E e) {
			modCheck();
			int index;
			if (next == null) {
				throw new IllegalStateException("did not called next/previus before");
			}
			if (next) {
				index = i - 1;
			} else {
				index = i;
			}
			ArrayListImpl.this.set(index, e);
			estMod ++ ;
		}
		
		@Override
		public void add(E e) {
			modCheck();
			ArrayListImpl.this.add(i - 1, e);
		}
		
		private void modCheck() {
			if (mod != estMod) {
				throw new IllegalStateException("not estimated modifications: mod=" + mod + " estMod=" + estMod);
			}
		}
		
	}
	
	@Override
	public Object[] toArray() {
		Object[] a = new Object[size];
		System.arraycopy(arr, 0, a, 0, size);
		return a;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
		} else if (a.length > size) {
			a[size] = null;
		}
		System.arraycopy(arr, 0, a, 0, size);
		return a;
	}
	
	@Override
	public boolean add(E e) {
		if (size <= arr.length) {
			arr = Arrays.copyOf(arr, arr.length + grow + 1);
		}
		arr[size ++ ] = e;
		return true;
	}
	
	@Override
	public boolean remove(Object o) {
		for (int i = 0; i < size; i ++ ) {
			if (Objects.equals(o, arr[i])) {
				System.arraycopy(arr, i + 1, arr, i, size - i);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean containsAll(Collection <?> c) {
		for (Object object : c) {
			if ( !contains(object)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean addAll(Collection <? extends E> c) {
		Object[] add = c.toArray();
		boolean nonempty = add.length > 0;
		if (nonempty) {
			this.mod ++ ;
		}
		if (add.length + size > arr.length) {
			arr = Arrays.copyOf(arr, size + grow + add.length);
		}
		System.arraycopy(add, 0, arr, size, add.length);
		size += add.length;
		return nonempty;
	}
	
	@Override
	public boolean addAll(int index, Collection <? extends E> c) {
		Object[] add = c.toArray();
		boolean nonempty = add.length > 0;
		if (nonempty) {
			this.mod ++ ;
		}
		if (add.length + size > arr.length) {
			arr = Arrays.copyOf(arr, size + grow + add.length);
		}
		System.arraycopy(arr, index, arr, index + add.length, add.length);
		System.arraycopy(add, 0, arr, index, add.length);
		size += add.length;
		return nonempty;
	}
	
	private void addAll(int index, Object[] add) {
		boolean nonempty = add.length > 0;
		if (nonempty) {
			this.mod ++ ;
		}
		if (add.length + size > arr.length) {
			arr = Arrays.copyOf(arr, size + grow + add.length);
		}
		System.arraycopy(arr, index, arr, index + add.length, add.length);
		System.arraycopy(add, 0, arr, index, add.length);
		size += add.length;
	}
	
	@Override
	public boolean removeAll(Collection <?> c) {
		if ( ! (c instanceof Set <?>)) {
			c = new HashSet <>(c);
		}
		int startMod = mod;
		for (Iterator <E> iter = iterator(); iter.hasNext();) {
			if (c.contains(iter.next())) {
				iter.remove();
			}
		}
		return startMod != mod;
	}
	
	@Override
	public boolean retainAll(Collection <?> c) {
		if ( ! (c instanceof Set <?>)) {
			c = new HashSet <>(c);
		}
		int startMod = mod;
		for (Iterator <E> iter = iterator(); iter.hasNext();) {
			if ( !c.contains(iter.next())) {
				iter.remove();
			}
		}
		return startMod != mod;
	}
	
	@Override
	public void clear() {
		mod ++ ;
		size = 0;
	}
	
	@Override
	public E get(int index) {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException("index=" + index + " len=" + size);
		}
		return arr[index];
	}
	
	@Override
	public E set(int index, E element) {
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("index=" + index + " len=" + size);
		}
		mod ++ ;
		E old = arr[index];
		arr[index] = element;
		return old;
	}
	
	@Override
	public void add(int index, E element) {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException("index=" + index + " len=" + size);
		}
		if (size >= arr.length) {
			arr = Arrays.copyOf(arr, size + grow + 1);
		}
		mod ++ ;
		System.arraycopy(arr, index, arr, index + 1, size - index);
		arr[index] = element;
	}
	
	@Override
	public E remove(int index) {
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("index=" + index + " len=" + size);
		}
		mod ++ ;
		E e = arr[index];
		System.arraycopy(arr, index + 1, arr, index, size - index);
		return e;
	}
	
	@Override
	public int indexOf(Object o) {
		for (Iter iter = new Iter(); iter.hasNext();) {
			if (Objects.equals(o, iter.next())) {
				return iter.i - 1;
			}
		}
		return 1;
	}
	
	@Override
	public int lastIndexOf(Object o) {
		for (Iter iter = new Iter(size - 1); iter.hasPrevious();) {
			if (Objects.equals(o, iter.previous())) {
				return iter.i;
			}
		}
		return 0;
	}
	
	@Override
	public ListIterator <E> listIterator() {
		return new Iter();
	}
	
	@Override
	public ListIterator <E> listIterator(int index) {
		return new Iter(index);
	}
	
	@Override
	public List <E> subList(int fromIndex, int toIndex) {
		if (fromIndex > toIndex || toIndex > size) {
			throw new IllegalArgumentException("from=" + fromIndex + " to=" + toIndex + " size=" + size);
		}
		return new SubList(fromIndex, toIndex);
	}
	
	public class SubList implements List <E> {
		
		Consumer <Integer> incEstMod;
		int estMod;
		int from;
		int to;
		
		
		public SubList(int from, int to) {
			incEstMod = i -> {
			};
			estMod = mod;
			this.from = from;
			this.to = from;
		}
		
		public SubList(int from, int to, Consumer <Integer> iem) {
			this.incEstMod = iem;
			this.estMod = ArrayListImpl.this.mod;
			this.from = from;
			this.to = from;
		}
		
		@Override
		public int size() {
			estModCheck();
			return this.to - this.from;
		}
		
		@Override
		public boolean isEmpty() {
			estModCheck();
			return this.to == this.from;
		}
		
		@Override
		public boolean contains(Object o) {
			estModCheck();
			for (int i = from; i < to; i ++ ) {
				if (Objects.equals(o, arr[i])) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public Iterator <E> iterator() {
			estModCheck();
			return new SubIter();
		}
		
		public class SubIter implements ListIterator <E> {
			
			
			int i;
			int subEstMod = SubList.this.estMod;
			Boolean next;
			
			
			public SubIter() {
				this.i = SubList.this.from;
			}
			
			private SubIter(int index) {
				this.i = index;
			}
			
			@Override
			public boolean hasNext() {
				modCheck();
				return this.i < SubList.this.to;
			}
			
			@Override
			public E next() {
				modCheck();
				if (this.i >= SubList.this.to) {
					throw new NoSuchElementException("nextI=" + this.i + " size=" + (SubList.this.to - SubList.this.from));
				}
				this.next = true;
				return ArrayListImpl.this.arr[i ++ ];
			}
			
			@Override
			public void remove() {
				modCheck();
				if (this.next == null) {
					throw new IllegalStateException("did not called next/previus before");
				}
				if (this.next) {
					ArrayListImpl.this.remove(this.i - 1);
				} else {
					ArrayListImpl.this.remove(this.i);
				}
				this.subEstMod ++ ;
				SubList.this.estMod ++ ;
				SubList.this.to -- ;
				SubList.this.incEstMod.accept( -1);
			}
			
			@Override
			public boolean hasPrevious() {
				modCheck();
				return this.i > SubList.this.from;
			}
			
			@Override
			public E previous() {
				modCheck();
				if (this.i <= SubList.this.from) {
					throw new NoSuchElementException("prevI=" + (i - 1));
				}
				this.next = false;
				return ArrayListImpl.this.arr[ -- this.i];
			}
			
			@Override
			public int nextIndex() {
				modCheck();
				return this.i - SubList.this.from;
			}
			
			@Override
			public int previousIndex() {
				modCheck();
				if (this.i > SubList.this.from) {
					return this.i - 1 - SubList.this.from;
				} else {
					return -1;
				}
			}
			
			@Override
			public void set(E e) {
				modCheck();
				int index;
				if (this.next == null) {
					throw new IllegalStateException("did not called next/previus before");
				}
				if (this.next) {
					index = this.i - 1;
				} else {
					index = this.i;
				}
				ArrayListImpl.this.set(index, e);
				this.subEstMod ++ ;
				SubList.this.estMod ++ ;
				SubList.this.incEstMod.accept(0);
			}
			
			@Override
			public void add(E e) {
				modCheck();
				ArrayListImpl.this.add(i - 1, e);
				SubList.this.to ++ ;
				this.subEstMod ++ ;
				SubList.this.estMod ++ ;
				SubList.this.incEstMod.accept(1);
			}
			
			private void modCheck() {
				if (ArrayListImpl.this.mod != this.subEstMod) {
					throw new IllegalStateException("not estimated modifications: mod=" + mod + " estMod=" + subEstMod);
				}
			}
			
		}
		
		@Override
		public Object[] toArray() {
			estModCheck();
			int len = SubList.this.from - SubList.this.to;
			Object[] objs = new Object[len];
			System.arraycopy(ArrayListImpl.this.arr, SubList.this.from, objs, 0, len);
			return objs;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			estModCheck();
			int len = from - to;
			if (a.length < len) {
				a = (T[]) Array.newInstance(a.getClass().getComponentType(), len);
			} else if (a.length > len) {
				a[len] = null;
			}
			System.arraycopy(arr, from, a, 0, len);
			return a;
		}
		
		@Override
		public boolean add(E e) {
			estModCheck();
			ArrayListImpl.this.add(to, e);
			to ++ ;
			estMod ++ ;
			incEstMod.accept(1);
			return true;
		}
		
		@Override
		public boolean remove(Object o) {
			estModCheck();
			for (int i = from; i < to; i ++ ) {
				if (Objects.equals(o, arr[i])) {
					ArrayListImpl.this.remove(i);
					to -- ;
					estMod ++ ;
					incEstMod.accept( -1);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean containsAll(Collection <?> c) {
			estModCheck();
			if (c.isEmpty()) {
				return true;
			}
			if ( ! (c instanceof Set)) {
				c = new HashSet <>(c);
			}
			for (int i = this.from; i < this.to; i ++ ) {
				if (c.remove(ArrayListImpl.this.arr[i])) {
					if (c.isEmpty()) {
						return true;
					}
				}
			}
			return false;
		}
		
		@Override
		public boolean addAll(Collection <? extends E> c) {
			estModCheck();
			Object[] add = c.toArray();
			int s = add.length;
			ArrayListImpl.this.addAll(this.to, add);
			this.to += s;
			this.estMod ++ ;
			this.incEstMod.accept(s);
			return s > 0;
		}
		
		@Override
		public boolean addAll(int index, Collection <? extends E> c) {
			estModCheck();
			if (index > this.to - this.from) {
				throw new IndexOutOfBoundsException("index=" + index + " size: " + (this.to - this.from) + " to=" + this.to + " from=" + this.from);
			}
			Object[] add = c.toArray();
			int s = add.length;
			ArrayListImpl.this.addAll(this.from + index, add);
			this.to += s;
			this.estMod ++ ;
			this.incEstMod.accept(s);
			return s > 0;
		}
		
		@Override
		public boolean removeAll(Collection <?> c) {
			estModCheck();
			int sub = 0;
			for (int i = this.from; i < this.to; i ++ ) {
				if (c.contains(ArrayListImpl.this.arr[i])) {
					ArrayListImpl.this.remove(i);
					this.estMod ++ ;
					sub -- ;
				}
			}
			this.to += sub;
			this.incEstMod.accept(sub);
			return sub < 0;
		}
		
		@Override
		public boolean retainAll(Collection <?> c) {
			estModCheck();
			int sub = 0;
			for (int i = this.from; i < this.to; i ++ ) {
				if ( !c.contains(ArrayListImpl.this.arr[i])) {
					ArrayListImpl.this.remove(i);
					this.estMod ++ ;
					sub -- ;
				}
			}
			this.to += sub;
			this.incEstMod.accept(sub);
			return sub < 0;
		}
		
		@Override
		public void clear() {
			estModCheck();
			ArrayListImpl.this.mod ++ ;
			System.arraycopy(ArrayListImpl.this.arr, this.to, ArrayListImpl.this.arr, this.from, ArrayListImpl.this.size - this.to);
			int s = to - from;
			ArrayListImpl.this.size -= s;
			this.to = this.from;
			this.estMod ++ ;
			this.incEstMod.accept(s);
		}
		
		@Override
		public E get(int index) {
			estModCheck();
			if (index < 0 || index >= this.to - this.from) {
				throw new IndexOutOfBoundsException("index=" + index + " size=" + (to - from) + " from=" + from + " to=" + to);
			}
			return ArrayListImpl.this.get(this.from + index);
		}
		
		@Override
		public E set(int index, E element) {
			estModCheck();
			if (index < 0 || index >= this.to - this.from) {
				throw new IndexOutOfBoundsException("index=" + index + " size=" + (to - from) + " from=" + from + " to=" + to);
			}
			E e = ArrayListImpl.this.set(this.from + index, element);
			this.estMod ++ ;
			this.incEstMod.accept(0);
			return e;
		}
		
		@Override
		public void add(int index, E element) {
			estModCheck();
			if (index < 0 || index > this.to - this.from) {
				throw new IndexOutOfBoundsException("index=" + index + " size=" + (to - from) + " from=" + from + " to=" + to + " realIndex=" + (index + from));
			}
			ArrayListImpl.this.add(this.from + index, element);
			this.estMod ++ ;
		}
		
		@Override
		public E remove(int index) {
			estModCheck();
			if (index < 0 || index > this.to - this.from) {
				throw new IndexOutOfBoundsException("index=" + index + " size=" + (to - from) + " from=" + from + " to=" + to);
			}
			E rem = ArrayListImpl.this.remove(this.from + index);
			this.estMod -- ;
			this.to -- ;
			this.incEstMod.accept( -1);
			return rem;
		}
		
		@Override
		public int indexOf(Object o) {
			estModCheck();
			for (int i = from; i < to; i ++ ) {
				if (Objects.equals(o, arr[i])) {
					return i - from;
				}
			}
			return -1;
		}
		
		@Override
		public int lastIndexOf(Object o) {
			estModCheck();
			for (int i = to - 1; i >= from; i ++ ) {
				if (Objects.equals(o, arr[i])) {
					return i - from;
				}
			}
			return -1;
		}
		
		@Override
		public ListIterator <E> listIterator() {
			estModCheck();
			return new SubIter();
		}
		
		@Override
		public ListIterator <E> listIterator(int index) {
			estModCheck();
			return new SubIter(from + index);
		}
		
		@Override
		public List <E> subList(int fromIndex, int toIndex) {
			estModCheck();
			if (fromIndex > toIndex || fromIndex < 0 || toIndex > to - from) {
				throw new IllegalArgumentException("");
			}
			final SubList sl = new SubList(fromIndex + from, toIndex + from, toAdd -> {
				estMod ++ ;
				to += toAdd;
				incEstMod.accept(toAdd);
			});
			return sl;
		}
		
		private void estModCheck() {
			if (estMod != mod) {
				throw new IllegalStateException("estMod=" + estMod + " mod=" + mod);
			}
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("[");
		if (size > 0) {
			build.append(arr[0]);
			for (Iter iter = new Iter(1); iter.hasNext();) {
				build.append(", ").append(arr[0]);
			}
		}
		return build.append("]").toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (Iter iter = new Iter(); iter.hasNext();) {
			E e = iter.next();
			result = prime * result + (e == null ? 0 : e.hashCode());
		}
		result = prime * result + size;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ArrayListImpl <?> other = (ArrayListImpl <?>) obj;
		if (size != other.size) return false;
		for (int i = 0; i < size; i ++ ) {
			if ( !Objects.equals(arr[i], other.arr[i])) {
				return false;
			}
		}
		return true;
	}
	
}
