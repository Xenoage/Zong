package com.xenoage.utils.iterators;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Tests for {@link MultiIt}.
 * 
 * @author Andreas Wenger
 */
public class MultiItTest {

	@Test public void test() {
		Iterator<Integer> list1 = Arrays.asList(1, 2, 3).iterator();
		Iterator<Integer> list2 = Arrays.<Integer>asList().iterator();
		Iterator<Integer> list3 = Arrays.asList(4).iterator();
		Iterator<Integer> list4 = Arrays.<Integer>asList().iterator();
		MultiIt<Integer> it = new MultiIt<>(list1, list2, list3, list4);
		for (int i = 1; i <= 4; i++) {
			Assert.assertTrue(it.hasNext());
			Assert.assertEquals(i, it.next().intValue());
		}
		Assert.assertFalse(it.hasNext());
	}

}
