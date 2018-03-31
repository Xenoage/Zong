package com.xenoage.utils.iterators;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests for {@link ReverseIterator}.
 *
 * @author Andreas Wenger
 */
public class ReverseIteratorTest {

	@Test public void test() {
		int count = 5;
		ArrayList<Integer> a = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
			a.add(i);
		for (int i : new ReverseIterator<>(a))
			assertEquals(--count, i);
	}

}
