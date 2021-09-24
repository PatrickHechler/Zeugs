package de.hechler.patrick.zeugs.objects;


public class IntIntImpl extends AbstractIntInt {
	
	/** UID */
	private static final long serialVersionUID = 5453701937308782579L;
	
	
	
	private int first;
	private int second;
	
	
	
	public IntIntImpl() {
		super();
	}
	
	
	
	public IntIntImpl(int first, int second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	
	
	@Override
	public int getFirst() {
		return first;
	}
	
	@Override
	public int getSecond() {
		return second;
	}
	
	@Override
	public void setFirst(int newFirst) {
		first = newFirst;
	}
	
	@Override
	public void setSecond(int newsecond) {
		second = newsecond;
	}
	
	@Override
	public void addFirst(int add) {
		first += add;
	}
	
	@Override
	public void addSecond(int add) {
		second += add;
	}
	
	@Override
	public void subFirst(int sub) {
		first -= sub;
	}
	
	@Override
	public void subSecond(int sub) {
		second -= sub;
	}
	
	@Override
	public void incFirst() {
		first ++ ;
	}
	
	@Override
	public void incSecond() {
		second ++ ;
	}
	
	@Override
	public void decFirst() {
		first -- ;
	}
	
	@Override
	public void decSecond() {
		second -- ;
	}
	
	@Override
	public boolean isFirst(int val) {
		return first == val;
	}
	
	@Override
	public boolean isSecond(int val) {
		return second == val;
	}
	
	@Override
	public boolean bothSame() {
		return first == second;
	}
	
}
