package de.hechler.patrick.zeugs;

import de.hechler.patrick.zeugs.check.BigCheckResult;
import de.hechler.patrick.zeugs.check.Checker;
import de.hechler.patrick.zeugs.objects.ArrayListImplChecker;
import de.hechler.patrick.zeugs.objects.QueueChecker;

public class Test {
	
	public void testname() throws Exception {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		BigCheckResult cr = Checker.checkAll(true, QueueChecker.class, ArrayListImplChecker.class);
		cr.print();
		if (cr.wentUnexpected()) {
			System.out.flush();
			cr.detailedPrintUnexpected(System.err);
			throw new Error("unecpected check result: " + cr);
		}
	}
	
}
