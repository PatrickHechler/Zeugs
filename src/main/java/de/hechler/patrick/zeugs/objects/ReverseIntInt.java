package de.hechler.patrick.zeugs.objects;

import de.hechler.patrick.zeugs.interfaces.IntInt;

public class ReverseIntInt extends AbstractIntInt {
	
	/** UID */
	private static final long serialVersionUID = 1516325567062312472L;
	
	
	
	private final IntInt reverse;
	
	
	
	public ReverseIntInt(IntInt reverse) {
		this.reverse = reverse;
	}
	
	
	
	@Override
	public int getFirst() {
		return reverse.getSecond();
	}
	
	@Override
	public int getSecond() {
		return reverse.getFirst();
	}
	
	@Override
	public void setFirst(int newFirst) {
		reverse.setSecond(newFirst);
	}
	
	@Override
	public void setSecond(int newsecond) {
		reverse.setFirst(newsecond);
	}
	
	@Override
	public void addFirst(int add) {
		reverse.addSecond(add);
	}
	
	@Override
	public void addSecond(int add) {
		reverse.addFirst(add);
	}
	
	@Override
	public void subFirst(int sub) {
		reverse.subSecond(sub);
	}
	
	@Override
	public void subSecond(int sub) {
		reverse.subFirst(sub);
	}
	
	@Override
	public void incFirst() {
		reverse.incSecond();
	}
	
	@Override
	public void incSecond() {
		reverse.incFirst();
	}
	
	@Override
	public void decFirst() {
		reverse.decSecond();
	}
	
	@Override
	public void decSecond() {
		reverse.decFirst();
	}

	@Override
	public boolean isFirst(int val) {
		return reverse.isSecond(val);
	}

	@Override
	public boolean isSecond(int val) {
		return reverse.isFirst(val);
	}

	@Override
	public boolean bothSame() {
		return reverse.bothSame();
	}
	
}
