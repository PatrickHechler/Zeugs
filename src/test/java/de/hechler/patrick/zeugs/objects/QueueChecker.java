package de.hechler.patrick.zeugs.objects;

import java.util.Queue;

import de.hechler.patrick.zeugs.check.Checker;
import de.hechler.patrick.zeugs.check.anotations.Check;
import de.hechler.patrick.zeugs.check.anotations.CheckClass;


/**
 * checks the {@link Queue} implementations: {@link QueueArrayImpl} and {@link QueueImpl}
 * 
 */
@CheckClass
public class QueueChecker extends Checker {
	
	@Check
	private void check() {
		Queue <String> qai = new QueueArrayImpl <>(String.class);
		Queue <String> qi = new QueueImpl <>();
		assertEquals(qi, qai);
		String s0 = "hello world";
		assertEquals(0, qi.size());
		assertEquals(0, qai.size());
		assertTrue(qi.add(s0));
		assertEquals(1, qi.size());
		assertEquals(0, qai.size());
		assertNotEquals(qi, qai);
		assertTrue(qai.add(s0));
		assertEquals(qi, qai);
		assertEquals(1, qi.size());
		assertEquals(1, qai.size());
		assertSame(s0, qai.peek());
		assertSame(s0, qi.peek());
		assertSame(s0, qai.peek());
		assertSame(s0, qi.peek());
		assertEquals(1, qi.size());
		assertEquals(1, qai.size());
		assertSame(s0, qai.remove());
		assertSame(s0, qi.remove());
		assertEquals(0, qi.size());
		assertEquals(0, qai.size());
		s0 = "string0";
		String s1 = "string1";
		String s2 = "string2";
		assertTrue(qi.add(s0));
		assertTrue(qai.add(s0));
		assertEquals(1, qi.size());
		assertEquals(1, qai.size());
		assertTrue(qi.add(s1));
		assertTrue(qai.add(s1));
		assertEquals(2, qi.size());
		assertEquals(2, qai.size());
		assertTrue(qi.add(s2));
		assertTrue(qai.add(s2));
		assertEquals(3, qi.size());
		assertEquals(3, qai.size());
		assertArrayEquals(new String[] {s0, s1, s2 }, qi.toArray(new String[3]));
		assertArrayEquals(new String[] {s0, s1, s2 }, qai.toArray(new String[3]));
		assertEquals(qi, qai);
		qai.clear();
		qi.clear();
		assertTrue(qai.isEmpty());
		assertTrue(qi.isEmpty());
		s0 = "0";
		s1 = "1";
		s2 = "2";
		String s3 = "3";
		String s4 = "4";
		String s5 = "5";
		String s6 = "6";
		String s7 = "7";
		String s8 = "8";
		String s9 = "9";
		String s10 = "10";
		String s11 = "11";
		assertTrue(qi.add(s0));
		assertTrue(qai.add(s0));
		assertTrue(qi.add(s1));
		assertTrue(qai.add(s1));
		assertTrue(qi.add(s2));
		assertTrue(qai.add(s2));
		assertTrue(qi.add(s3));
		assertTrue(qai.add(s3));
		assertTrue(qi.add(s4));
		assertTrue(qai.add(s4));
		assertTrue(qi.add(s5));
		assertTrue(qai.add(s5));
		assertTrue(qi.add(s6));
		assertTrue(qai.add(s6));
		assertTrue(qi.add(s7));
		assertTrue(qai.add(s7));
		assertTrue(qi.add(s8));
		assertTrue(qai.add(s8));
		assertTrue(qi.add(s9));
		assertTrue(qai.add(s9));
		assertTrue(qi.add(s10));
		assertTrue(qai.add(s10));
		assertTrue(qi.add(s11));
		assertTrue(qai.add(s11));
		assertArrayEquals(new String[] {s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11 }, qai.toArray());
		assertArrayEquals(new String[] {s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11 }, qi.toArray());
	}
	
}
