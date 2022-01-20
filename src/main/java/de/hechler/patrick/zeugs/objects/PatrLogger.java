package de.hechler.patrick.zeugs.objects;

import java.io.Closeable;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PatrLogger extends Logger implements Closeable, Serializable {
	
	/** UID */
	private static final long serialVersionUID = -7744521531573940412L;
	
	private final PrintStream out;
	private final String name;
	private final String resourceBundleName;
	private final String lineSep;
	private final PatrLogger parent;
	private List <PatrLogger> childs;
	
	public PatrLogger(PrintStream out, String name) {
		this(out, name, null, "\n", null);
	}
	
	public PatrLogger(PrintStream out, String name, String resourceBundleName, String lineSep) {
		this(out, name, resourceBundleName, lineSep, null);
	}
	
	private PatrLogger(PrintStream out, String name, String resourceBundleName, String lineSep, PatrLogger parent) {
		super(name, resourceBundleName);
		this.out = out;
		this.name = name;
		this.resourceBundleName = resourceBundleName;
		this.lineSep = (lineSep.contains("\r") || lineSep.contains("\n")) ? lineSep : (lineSep.concat(System.lineSeparator()));
		this.parent = parent;
		this.childs = new ArrayListImpl <>(PatrLogger.class);
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	// @Override
	// public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
	// if (!isLoggable(level)) {
	// return;
	// }
	// if (thrown != null) {
	// log(new String[] {"error:" });
	// log(thrown);
	// }
	// if (bundle != null) {
	// Object obj = bundle.getObject(msg);
	// if (obj instanceof String) {
	// log((String) obj);
	// } else if (obj instanceof String[]) {
	// for (String potline : (String[]) obj) {
	// log(potline);
	// }
	// } else {
	// throw new InternalError("the ResourceBundle did not return a String or a String[]: class: " +
	// obj.getClass() + " tos: '" + obj + "'");
	// }
	// } else {
	// log(msg);
	// }
	// }
	//
	// @Override
	// public void log(Level level, ResourceBundle bundle, String format, Object... params) {
	// if (!isLoggable(level)) {
	// return;
	// }
	// String msg = String.format(format, params);
	// if (bundle != null) {
	// Object obj = bundle.getObject(msg);
	// if (obj instanceof String) {
	// log((String) obj);
	// } else if (obj instanceof String[]) {
	// for (String potline : (String[]) obj) {
	// log(potline);
	// }
	// } else {
	// throw new InternalError("the ResourceBundle did not return a String or a String[]: class: " +
	// obj.getClass() + " tos: '" + obj + "'");
	// }
	// } else {
	// log(msg);
	// }
	// }
	
	@Override
	public void setParent(Logger parent) {
		// only allow init
		if (this.parent != super.getParent() && parent == this.parent) {
			super.setParent(parent);
		}
		throw new UnsupportedOperationException("setParent(parent) parent=" + parent);
	}
	
	@Override
	public PatrLogger getParent() {
		return parent;
	}
	
	@Override
	public void log(LogRecord record) {
		if ( !isLoggable(record.getLevel())) {
			return;
		}
		Filter f = super.getFilter();
		if (f != null && !f.isLoggable(record)) {
			return;
		}
		String msg = record.getMessage();
		Object[] params = record.getParameters();
		ResourceBundle rb = record.getResourceBundle();
		log(rb, msg, params);
		Throwable error = record.getThrown();
		if (error != null) {
			log("  error:");
			log(error, "  ");
		}
		super.log(record);
	}
	
	public void log(ResourceBundle rb, String msg, Object[] params) throws ClassCastException {
		msg = msg == null ? "" : msg;
		params = params == null ? new Object[0] : params;
		Object replacingmsg = null;
		if (rb != null) {
			try {
				replacingmsg = rb.getObject(msg);
			} catch (MissingResourceException e) {
			}
		}
		if (replacingmsg != null) {
			if (replacingmsg instanceof String) {
				msg = (String) replacingmsg;
			} else if (replacingmsg instanceof String[]) {
				String[] arr = (String[]) replacingmsg;
				StringBuilder build = new StringBuilder();
				for (String str : arr) {
					build.append(str).append(lineSep);
				}
				msg = build.toString();
			} else {
				throw new ClassCastException("cannt cas from " + replacingmsg.getClass().getName() + " to String or String[]");
			}
		}
		msg = String.format(msg, params);
		log(msg);
	}
	
	public void log(Throwable error) {
		log(error, "");
	}
	
	public void log(Throwable error, String indention) {
		log(indention + "cls: " + error.getClass().getCanonicalName());
		log(indention + "msg: " + error.getMessage());
		String subIndention = indention + "  ";
		for (StackTraceElement traceElement : error.getStackTrace()) {
			log(subIndention + "at " + traceElement);
		}
		for (Throwable suppressed : error.getSuppressed()) {
			log(indention + "suppressed:");
			log(suppressed, subIndention);
		}
		Throwable cause = error.getCause();
		if (cause != null) {
			log(indention + "cause:");
			log(cause, subIndention);
		}
	}
	
	public void log(String msg) {
		log(msg.split("\r\n?|\n"));
	}
	
	private void log(String[] lines) {
		for (String line : lines) {
			out.print("[" + name + "]:" + line + lineSep);
		}
	}
	
	
	public List <PatrLogger> getChilds() {
		return Collections.unmodifiableList(childs);
	}
	
	public PatrLogger addSibling(String sname) {
		if (parent == null) {
			return new PatrLogger(out, sname);
		} else {
			return parent.addChild(out, sname, lineSep);
		}
	}
	
	public PatrLogger addChild(String cname) {
		return addChild(out, cname, lineSep);
	}
	
	public PatrLogger addChild(PrintStream out, String name, String lineSep) {
		PatrLogger c = new PatrLogger(out, this.name + '.' + name, resourceBundleName, lineSep);
		c.setParent(this);
		this.childs.add(c);
		return c;
	}
	
	public void closeAll() {
		close();
		for (PatrLogger child : childs) {
			child.closeAll();
		}
	}
	
	@Override
	public void close() {
		out.close();
	}
	
	@Override
	public String toString() {
		return "PatrLogger [name=" + name + ", postfix=" + lineSep + ", childs=" + childs + ", out=" + out + "]";
	}
	
}
