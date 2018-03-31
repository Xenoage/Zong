package com.xenoage.utils.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.*;

/**
 * Tests for {@link NullableList}.
 * 
 * @author Andreas Wenger
 */
@SuppressWarnings("deprecation")
public class NullableListTest {

	
	@Test public void addTest() {
		assertEquals(list(1), NullableList.add(null, 1));
		assertEquals(list(1, 2), NullableList.add(list(1), 2));
		assertEquals(list(1, 2, 3), NullableList.add(list(1, 2), 3));
	}

	@Test public void removeTest() {
		assertNull(NullableList.remove(null, 1));
		assertNull(NullableList.remove(list("1"), "1"));
		assertEquals(list("1"), NullableList.remove(list("1", "2"), "2"));
		assertEquals(list("1", "3"), NullableList.remove(list("1", "2", "3"), "2"));
	}

	@Test public void itTest() {
		Iterator<Integer> it;
		//0 elements
		it = NullableList.it((ArrayList<Integer>) null).iterator();
		assertNotNull(it);
		assertEquals(false, it.hasNext());
		//1 element
		it = NullableList.it(list(1)).iterator();
		assertNotNull(it);
		assertEquals((Integer) 1, it.next());
		assertEquals(false, it.hasNext());
		//2 elements
		it = NullableList.it(list(1, 2, 3)).iterator();
		for (int i : range(1, 3))
			assertEquals((Integer) i, it.next());
		assertEquals(false, it.hasNext());
	}

	@Test public void sizeTest() {
		assertEquals(0, NullableList.size(null));
		assertEquals(1, NullableList.size(list(1)));
		assertEquals(3, NullableList.size(list(1, 2, 3)));
	}

	@Test public void getTest() {
		//get on empty list must fail
		try {
			NullableList.get(null, 0);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
		//get with to small index must fail
		try {
			NullableList.get(list(1, 2, 3), -1);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
		//get with to large index must fail
		try {
			NullableList.get(list(1, 2, 3), 3);
			fail();
		} catch (IndexOutOfBoundsException ex) {
		}
		//otherwise it must work
		for (int i : range(0, 3)) {
			assertEquals((Integer) i, NullableList.get(list(0, 1, 2, 3), i));
		}
	}

	private <T> ArrayList<T> list(@SuppressWarnings("unchecked") T... vals) {
		ArrayList<T> ret = new ArrayList<>();
		for (T val : vals)
			ret.add(val);
		return ret;
	}

}
