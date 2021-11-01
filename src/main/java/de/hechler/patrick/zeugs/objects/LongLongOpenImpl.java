package de.hechler.patrick.zeugs.objects;

import de.hechler.patrick.zeugs.interfaces.LongLong;

public class LongLongOpenImpl implements LongLong {
	
	
	
	public LongLongOpenImpl(long first, long second) {
		this.first = first;
		this.second = second;
	}
	
	public long first;
	public long second;
	
	
	@Override
	public long getFirst() {
		return first;
	}
	
	@Override
	public long getSecond() {
		return second;
	}
	
	@Override
	public void setFirst(long newVal) {
		first = newVal;
	}
	
	@Override
	public void setSecond(long newVal) {
		second = newVal;
	}
	
}
