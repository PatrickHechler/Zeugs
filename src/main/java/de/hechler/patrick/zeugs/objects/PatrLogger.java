package de.hechler.patrick.zeugs.objects;

import java.io.Closeable;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.System.Logger;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class PatrLogger implements Closeable, Logger, Serializable {
	
	/** UID */
	private static final long serialVersionUID = -7744521531573940412L;
	
	private final PrintStream out;
	private final String name;
	private final String postfix;
	private final String childadd;
	private final PatrLogger parent;
	private List <PatrLogger> childs;
	private Level loglevel;
	
	public PatrLogger(PrintStream out, String name) {
		this(out, name, "\n", ">", null, Level.ALL);
	}
	
	public PatrLogger(PrintStream out, String name, String postfix) {
		this(out, name, postfix, ">", null, Level.ALL);
	}
	
	public PatrLogger(PrintStream out, String name, String postfix, String childadd, Level loglevel) {
		this(out, name, postfix, childadd, null, Level.ALL);
	}
	
	private PatrLogger(PrintStream out, String name, String postfix, String childadd, PatrLogger parent, Level loglevel) {
		this.out = out;
		this.name = name;
		this.postfix = (postfix.contains("\r") || postfix.contains("\n")) ? postfix : (postfix.concat(System.lineSeparator()));
		this.childadd = childadd;
		this.parent = parent;
		this.childs = new ArrayListImpl <>(PatrLogger.class);
		this.loglevel = loglevel;
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean isLoggable(Level level) {
		return level.getSeverity() >= this.loglevel.getSeverity();
	}
	
	@Override
	public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
		if (level.getSeverity() < this.loglevel.getSeverity()) {
			return;
		}
		if (thrown != null) {
			log(new String[] {"error:" });
			log(thrown);
		}
		if (bundle != null) {
			Object obj = bundle.getObject(msg);
			if (obj instanceof String) {
				log((String) obj);
			} else if (obj instanceof String[]) {
				for (String potline : (String[]) obj) {
					log(potline);
				}
			} else {
				throw new InternalError("the ResourceBundle did not return a String or a String[]: class: " + obj.getClass() + " tos: '" + obj + "'");
			}
		} else {
			log(msg);
		}
	}
	
	@Override
	public void log(Level level, ResourceBundle bundle, String format, Object... params) {
		if (level.getSeverity() < this.loglevel.getSeverity()) {
			return;
		}
		String msg = String.format(format, params);
		if (bundle != null) {
			Object obj = bundle.getObject(msg);
			if (obj instanceof String) {
				log((String) obj);
			} else if (obj instanceof String[]) {
				for (String potline : (String[]) obj) {
					log(potline);
				}
			} else {
				throw new InternalError("the ResourceBundle did not return a String or a String[]: class: " + obj.getClass() + " tos: '" + obj + "'");
			}
		} else {
			log(msg);
		}
	}
	
	public void log(Throwable error) {
		log("  cls: " + error.getClass().getCanonicalName());
		log("  msg: " + error.getMessage());
		for (StackTraceElement traceElement : error.getStackTrace()) {
			log("    at " + traceElement);
		}
		for (Throwable suppressed : error.getSuppressed()) {
			log("  suppressed:");
			log(suppressed);
		}
		Throwable cause = error.getCause();
		if (cause != null) {
			log("  cause:");
			log(cause);
		}
	}
	
	public void log(String msg) {
		log(msg.split("\r\n?|\n"));
	}
	
	private void log(String[] lines) {
		for (String line : lines) {
			out.print("[" + name + "]:" + line + postfix);
		}
	}
	
	public PatrLogger getParent() {
		return parent;
	}
	
	public List <PatrLogger> getChilds() {
		return Collections.unmodifiableList(childs);
	}
	
	public PatrLogger addSibling(String sname) {
		if (parent == null) {
			return new PatrLogger(out, sname);
		} else {
			return parent.addChild(out, sname, postfix, childadd, loglevel);
		}
	}
	
	public PatrLogger addChild(String cname) {
		return addChild(out, cname, postfix, childadd, loglevel);
	}
	
	public PatrLogger addChild(PrintStream out, String name, String postfix, String childadd, Level loglevel) {
		PatrLogger c = new PatrLogger(out, this.name + this.childadd + name, postfix, childadd, loglevel);
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
		return "PatrLogger [name=" + name + ", postfix=" + postfix + ", childadd=" + childadd + ", childs=" + childs + ", loglevel=" + loglevel + ", out=" + out + "]";
	}
	
}
