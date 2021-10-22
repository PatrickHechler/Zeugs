package de.hechler.patrick.zeugs.objects;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RAStack <E> extends StackImpl <E> {
	
	public RAStack(Class <E> cls) {
		super(cls);
	}
	
	public RAStack(Class <E> cls, int growSize) {
		super(cls, growSize);
	}
	
	@Override
	public boolean addAll(Collection <? extends E> c) {
		super.mod ++ ;
		Object[] objs = c.toArray();
		System.arraycopy(objs, 0, super.elements, super.size, objs.length);
		super.size += objs.length;
		return objs.length > 0;
	}
	
	@Override
	public Iterator <E> iterator() {
		return new Iterator <E>() {
			
			private int i = RAStack.super.size;
			private boolean remAllow = false;
			private int estMod = RAStack.super.mod;
			
			@Override
			public boolean hasNext() {
				assertMod();
				return i > 0;
			}
			
			@Override
			public E next() {
				assertMod();
				if (i <= 0) {
					throw new NoSuchElementException("no more elements");
				}
				remAllow = true;
				return RAStack.super.elements[ -- i];
			}
			
			@Override
			public void remove() {
				assertMod();
				if ( !remAllow) {
					throw new IllegalStateException("did not call next() before remove()!");
				}
				remAllow = false;
				System.arraycopy(RAStack.super.elements, i + 2, RAStack.super.elements, i + 1, RAStack.super.size - (i + 1));
			}
			
			private void assertMod() {
				if (RAStack.super.mod != estMod) {
					throw new IllegalStateException("mod has not the asserted value. Was this stack changed/currupted outside of this Iterator?");
				}
			}
			
		};
	}
	
}

