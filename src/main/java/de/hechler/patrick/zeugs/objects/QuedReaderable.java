package de.hechler.patrick.zeugs.objects;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Queue;


public class QuedReaderable implements Readable, Appendable {
	
	private final Queue <Object> buffer;
	private int                  timeout;
	
	
	
	public QuedReaderable() {
		this(new QueueImpl <>(), 0);
	}
	
	public QuedReaderable(int timeout) {
		this(new QueueImpl <>(), timeout);
	}
	
	public QuedReaderable(int timeout, String... startelements) {
		this(new QueueImpl <>(), timeout, (Object[]) startelements);
	}
	
	public QuedReaderable(int timeout, char[]... startelements) {
		this(new QueueImpl <>(), timeout, (Object[]) startelements);
	}
	
	public QuedReaderable(int timeout, Readable... startelements) {
		this(new QueueImpl <>(), timeout, (Object[]) startelements);
	}
	
	private QuedReaderable(Queue <Object> que, int timeout, Object... startelements) {
		this.buffer = que;
		this.timeout = timeout;
		for (Object element : startelements) {
			if (element.getClass() == String.class) {
				element = ((String) element).toCharArray();
			}
			this.buffer.add(element);
		}
	}
	
	
	
	public int getTimeout() {
		return timeout;
	}
	
	/**
	 * set the timeout in ms to wait at most when there<br>
	 * 
	 * set to zero to wait without timeout and -1 to not wait any time
	 * 
	 * @param timeout
	 *            the timeout in ms
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
	
	public boolean append(Readable reader) {
		if (timeout == -2) {
			throw new IllegalStateException("end was already appended!");
		}
		synchronized (this) {
			buffer.add(reader);
			this.notifyAll();
		}
		return true;
	}
	
	public void appendAtStart(CharSequence element) {
		if (element == null) {
			throw new NullPointerException("String element");
		}
		if (element.length() == 0) {
			return;
		}
		appendAtStart0(element.toString().toCharArray());
	}
	
	public void appendAtStart(String element) {
		if (element == null) {
			throw new NullPointerException("String element");
		}
		if (element.length() == 0) {
			return;
		}
		appendAtStart0(element.toCharArray());
	}
	
	public void appendAtStart(char[] element) {
		if (element == null) {
			throw new NullPointerException("char[] element");
		}
		if (element.length == 0) {
			return;
		}
		appendAtStart0(element);
	}
	
	public void appendAtStart(Readable element) {
		if (element == null) {
			throw new NullPointerException("Readable element");
		}
		appendAtStart0(element);
	}
	
	private void appendAtStart0(Object element) {
		if (this.timeout == -2 && this.buffer.isEmpty()) {
			throw new IllegalStateException("already read the end!");
		}
		synchronized (this) {
			Queue <Object> objs = new QueueImpl <>();
			while ( !this.buffer.isEmpty()) {
				Object rem = this.buffer.remove();
				objs.add(rem);
			}
			this.buffer.add(element);
			while ( !objs.isEmpty()) {
				Object add = objs.remove();
				this.buffer.add(add);
			}
		}
	}
	
	public void appendEnd() {
		synchronized (this) {
			this.timeout = -2;
			buffer.add(null);
		}
	}
	
	public boolean hasEndAppended() {
		return timeout == -2;
	}
	
	public boolean finished() {
		return timeout == -2 && buffer.isEmpty();
	}
	
	public boolean canRead() {
		return !buffer.isEmpty();
	}
	
	@Override
	public int read(CharBuffer cb) throws IOException {
		long now, start;
		start = now = System.currentTimeMillis();
		synchronized (this) {
			while (true) {
				Object element;
				if (timeout >= 0 && buffer.isEmpty()) {
					try {
						now = System.currentTimeMillis();
						if (timeout == 0) {
							this.wait(0);
						} else {
							long realTimeout = timeout - (now - start);
							if (realTimeout <= 0) {
								throw new IOException("reached timeout");
							}
							this.wait(realTimeout);
						}
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (buffer.isEmpty()) {
					throw new IOException("reached timeout");
				}
				element = buffer.peek();
				if (element == null) {
					return -1;
				}
				if (element.getClass() == char[].class) {
					buffer.remove();
					cb.put((char[]) element);
					return ((char[]) element).length;
				} else if (element instanceof Readable) {
					Readable r = (Readable) element;
					int read = r.read(cb);
					if (read == -1) {
						buffer.remove();
						continue;
					} else {
						return read;
					}
				} else {
					throw new InternalError("unknown object in que: class: " + element.getClass().getCanonicalName() + " toString()->'" + element.toString() + "'");
				}
			}
		}
	}
	
}
