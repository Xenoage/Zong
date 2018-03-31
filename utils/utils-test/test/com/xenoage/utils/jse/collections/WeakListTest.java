package com.xenoage.utils.jse.collections;

import org.junit.Test;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static org.junit.Assert.*;

/**
 * Test cases for the {@link WeakList}.
 * 
 * @author Andreas Wenger
 */
public class WeakListTest {

	@Test public void test()
		throws InterruptedException {
		//create list
		WeakList<Object> list = new WeakList<>();
		Object o1 = new Object();
		Object o2Temp = new Object();
		Object o3 = new Object();
		list.add(o1);
		list.add(o2Temp);
		list.add(o3);
		//check list
		assertEquals(alist(o1, o2Temp, o3), list.getAll());
		//remove an element
		list.remove(o1);
		//check list
		assertEquals(alist(o2Temp, o3), list.getAll());
		//garbage-collect an element
		o2Temp = null;
		System.gc();
		assertEquals(alist(o3), list.getAll());
	}

}
