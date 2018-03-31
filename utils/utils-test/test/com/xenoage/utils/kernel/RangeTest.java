package com.xenoage.utils.kernel;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static org.junit.Assert.*;

/**
 * Test cases for the {@link Range} class.
 * 
 * @author Andreas Wenger
 */
public class RangeTest {

	@Test public void rangeTest1() {
		Range r = range(-1, 2);
		Iterator<Integer> it = r.iterator();
		for (int i = -1; i <= 2; i++) {
			assertTrue(it.hasNext());
			assertEquals(i, (int) it.next());
		}
		assertFalse(it.hasNext());
		assertEquals(4, r.getCount());
	}

	@Test public void rangeTest2() {
		Range r = range(3);
		Iterator<Integer> it = r.iterator();
		for (int i = 0; i <= 2; i++) {
			assertTrue(it.hasNext());
			assertEquals(i, (int) it.next());
		}
		assertFalse(it.hasNext());
		assertEquals(3, r.getCount());
	}

	@Test public void rangeTest3() {
		Range r = range(Arrays.asList(5, 2, 8));
		Iterator<Integer> it = r.iterator();
		for (int i = 0; i <= 2; i++) {
			assertTrue(it.hasNext());
			assertEquals(i, (int) it.next());
		}
		assertFalse(it.hasNext());
		assertEquals(3, r.getCount());
	}

	@Test public void rangeTest4() {
		Range r = range(5, 2, 8);
		Iterator<Integer> it = r.iterator();
		for (int i = 0; i <= 2; i++) {
			assertTrue(it.hasNext());
			assertEquals(i, (int) it.next());
		}
		assertFalse(it.hasNext());
		assertEquals(3, r.getCount());
	}

	@Test public void rangeTest5() {
		Range r = range(Arrays.asList());
		Iterator<Integer> it = r.iterator();
		assertFalse(it.hasNext());
		assertEquals(0, r.getCount());
	}

	@Test public void rangeTest6() {
		Range r = range(2, -1);
		Iterator<Integer> it = r.iterator();
		assertFalse(it.hasNext());
		assertEquals(0, r.getCount());
	}

	@Test public void rangeReverseTest1() {
		Range r = rangeReverse(Arrays.asList(5, 2, 8));
		Iterator<Integer> it = r.iterator();
		for (int i = 2; i >= 0; i--) {
			assertTrue(it.hasNext());
			assertEquals(i, (int) it.next());
		}
		assertFalse(it.hasNext());
		assertEquals(3, r.getCount());
	}

	@Test public void rangeReverseTest2() {
		Range r = rangeReverse(Arrays.asList());
		Iterator<Integer> it = r.iterator();
		assertFalse(it.hasNext());
		assertEquals(0, r.getCount());
	}

	@Test public void rangeReverseTest3() {
		Range r = rangeReverse(5, 3);
		Iterator<Integer> it = r.iterator();
		for (int i = 5; i >= 3; i--) {
			assertTrue(it.hasNext());
			assertEquals(i, (int) it.next());
		}
		assertFalse(it.hasNext());
		assertEquals(3, r.getCount());
	}

}
