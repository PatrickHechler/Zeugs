package de.hechler.patrick.zeugs.objects;

import java.util.ArrayList;
import java.util.List;

import de.hechler.patrick.zeugs.check.Checker;
import de.hechler.patrick.zeugs.check.anotations.Check;
import de.hechler.patrick.zeugs.check.anotations.CheckClass;
import de.hechler.patrick.zeugs.check.anotations.End;
import de.hechler.patrick.zeugs.check.anotations.Start;

@CheckClass
public class ArrayListImplChecker extends Checker {
	
	List <String>          strList;
	ArrayListImpl <String> strAli;
	
	@Start
	private void start() {
		strList = new ArrayList <>();
		strAli = new ArrayListImpl <>(String.class);
	}
	
	@End
	private void end() {
		strList = null;
		strAli = null;
	}
	
	@Check
	private void check() {
		assertEquals(strList, strAli);
		strList.add("hello");
		strAli.add("hello");
		assertEquals(strList, strAli);
		strList.add("world");
		strAli.add("world");
		assertEquals(strList, strAli);
		strList.add("hello");
		strAli.add("hello");
		strList.add("world2");
		strAli.add("world2");
		assertEquals(strList, strAli);
	}
	
}
