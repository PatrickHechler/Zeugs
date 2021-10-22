package de.hechler.patrick.zeugs.objects;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class IntList extends ArrayListImpl <Integer> {
	
	public IntList() {
		super(Integer.class);
	}
	
	public IntList(Collection <Integer> addAll) {
		super(Integer.class, addAll);
	}
	
	@Override
	public void add(int index, Integer element) {
		if (element == null) {
			throw new NullPointerException("only non null elements are allowed");
		}
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(Collection <? extends Integer> c) {
		boolean mod = false;
		for (Integer integer : c) {
			add(integer);
			mod = true;
		}
		return mod;
	}
	
	@Override
	public boolean addAll(int index, Collection <? extends Integer> c) {
		boolean mod = false;
		for (Integer integer : c) {
			add(index ++ , integer);
			mod = true;
		}
		return mod;
	}
	
	@Override
	public List <Integer> subList(int fromIndex, int toIndex) {
		return new SubIntList(fromIndex, toIndex);
	}
	
	public class SubIntList extends SubList {
		
		public SubIntList(int from, int to) {
			super(from, to);
		}
		
		public SubIntList(int from, int to, Consumer <Integer> iem) {
			super(from, to, iem);
		}
		
		@Override
		public void add(int index, Integer element) {
			if (element == null) {
				throw new NullPointerException("only non null elements are allowed");
			}
			super.add(index, element);
		}
		
		@Override
		public boolean add(Integer e) {
			if (e == null) {
				throw new NullPointerException("only non null elements are allowed");
			}
			return super.add(e);
		}
		
		@Override
		public boolean addAll(Collection <? extends Integer> c) {
			boolean mod = false;
			for (Integer e : c) {
				if (e == null) {
					throw new NullPointerException("only non null elements are allowed");
				}
				super.add(e);
				mod = true;
			}
			return mod;
		}
		
		@Override
		public boolean addAll(int index, Collection <? extends Integer> c) {
			boolean mod = false;
			for (Integer e : c) {
				if (e == null) {
					throw new NullPointerException("only non null elements are allowed");
				}
				super.add(index ++ , e);
				mod = true;
			}
			return mod;
		}
		
		@Override
		public List <Integer> subList(int fromIndex, int toIndex) {
			if (fromIndex > toIndex || fromIndex < 0 || toIndex > super.to - super.from) {
				throw new IllegalArgumentException("fromIndex=" + fromIndex + " toIndex=" + toIndex + " myFrom=" + super.from + " myTo=" + super.to + " mySize: " + (super.to - super.from));
			}
			return new SubIntList(fromIndex + super.from, toIndex + super.from, toAdd -> {
				super.estMod ++ ;
				super.to += toAdd;
				super.incEstMod.accept(toAdd);
			});
		}
		
	}
	
}
