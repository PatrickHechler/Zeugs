package de.hechler.patrick.zeugs.objects;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Queue;


public class QuedReaderable implements Readable, Appendable {
	
	private final Queue <char[]> buffer;
	private int timeout;
	
	
	
	public QuedReaderable() {
		this(new QueueImpl <>(), 0);
	}
	
	public QuedReaderable(Queue <char[]> que, int timeout) {
		this.buffer = que;
		this.timeout = timeout;
	}
	
	
	
	public int getTimeout() {
		return timeout;
	}
	
	/**
	 * set the timeout in ms to wait at most when there
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		if (timeout < 0 && timeout != -1) {
			throw new IllegalArgumentException("negative timeout!");
		}
		this.timeout = timeout;
	}
	
	@Override
	public QuedReaderable append(CharSequence csq) {
		append(csq == null ? "null" : csq.toString());
		return this;
	}
	
	@Override
	public QuedReaderable append(char c) {
		append(new char[] {c });
		return this;
	}
	
	@Override
	public Appendable append(CharSequence csq, int start, int end) throws IOException {
		append(csq.subSequence(start, end).toString());
		return this;
	}
	
	public boolean append(String str) {
		if (str == null) {
			return false;
		}
		return append(str.toCharArray());
	}
	
	public boolean append(char[] chars) {
		if (timeout == -2) {
			throw new IllegalStateException("end was already appended!");
		}
		if (chars == null) {
			return false;
		}
		if (chars.length == 0) {
			return false;
		}
		synchronized (this) {
			buffer.add(chars);
			this.notifyAll();
		}
		return true;
	}
	
	public void appendEnd() {
		synchronized (this) {
			this.timeout = -2;
			buffer.add(null);
		}
	}
	
	@Override
	public int read(CharBuffer cb) throws IOException {
		char[] chars;
		synchronized (this) {
			if (timeout >= 0 && buffer.isEmpty()) {
				try {
					this.wait(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			chars = buffer.remove();
		}
		if (chars == null) {
			return -1;
		}
		cb.put(chars);
		return chars.length;
	}
	
}
