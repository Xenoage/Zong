package com.xenoage.utils.pdlib;

import org.junit.Test;

import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.pdlib.PList.plist;
import static org.junit.Assert.*;


/**
 * Tests for {@link PList}.
 * 
 * @author Andreas Wenger
 */
public class PListTest
{

	@Test public void withTest()
	{
		PList<Integer> v = new PList<>();
		try {
			v = v.with(5, 10);
			fail("Should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException ex) {
		}
	}


	@Test public void withExtendTest()
	{
		PList<Integer> v = new PList<>();
		v = v.withExtend(3, 5, 1);
		assertEquals(plist(1, 1, 1, 5), v);
	}


	@Test public void splitTest()
	{
		PList<Integer> v = plist(1, 2, 3, 4, 5);
		//split in middle
		assertEquals(t(plist(1, 2), plist(3, 4, 5)), v.split(2));
		assertEquals(t(plist(1, 2, 3, 4), plist(5)), v.split(4));
		//split at start
		assertEquals(t(plist(), v), v.split(0));
		assertEquals(t(plist(), v), v.split(-1));
		assertEquals(t(plist(), v), v.split(-2));
		//split at end
		assertEquals(t(v, plist()), v.split(5));
		assertEquals(t(v, plist()), v.split(7));
		assertEquals(t(v, plist()), v.split(6));
	}

}
