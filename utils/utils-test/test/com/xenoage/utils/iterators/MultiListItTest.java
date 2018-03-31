package com.xenoage.utils.iterators;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link MultiListIt}.
 * 
 * @author Andreas Wenger
 */
public class MultiListItTest {

	@Test public void test() {
		List<Integer> list1 = Arrays.asList(1, 2, 3);
		List<Integer> list2 = Arrays.asList();
		List<Integer> list3 = Arrays.asList(4);
		List<Integer> list4 = Arrays.asList();
		MultiListIt<Integer> it = new MultiListIt<>(list1, list2, list3, list4);
		for (int i = 1; i <= 4; i++) {
			Assert.assertTrue(it.hasNext());
			Assert.assertEquals(i, it.next().intValue());
		}
		Assert.assertFalse(it.hasNext());
	}

}
