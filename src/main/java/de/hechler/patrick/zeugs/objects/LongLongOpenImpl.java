package de.hechler.patrick.zeugs.objects;

import de.hechler.patrick.zeugs.interfaces.LongLong;

public class LongLongOpenImpl implements LongLong {
	
	public long first;
	public long second;
	
	
	
	public LongLongOpenImpl(long first, long second) {
		this.first = first;
		this.second = second;
	}
	
	
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
	
	@Override
	public String toString() {
		return "LongLongOpenImpl [first=" + first + ", second=" + second + "]";
	}
	
}
