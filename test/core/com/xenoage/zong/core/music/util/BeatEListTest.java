package com.xenoage.zong.core.music.util;

import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Test cases for {@link BeatE}.
 * 
 * @author Andreas Wenger
 */
public class BeatEListTest {

	@Test public void addTest() {
		BeatEList<Integer> list = new BeatEList<Integer>();
		list.add(5, fr(5));
		list.add(3, fr(3));
		list.add(2, fr(2));
		list.add(4, fr(4));
		list.add(3, fr(3));
		list.add(3, fr(3));
		list.add(8, fr(8));
		list.add(6, fr(6));
		assertEquals(8, list.getElements().size());
		assertEquals(2, (int) list.getElements().get(0).element);
		assertEquals(3, (int) list.getElements().get(1).element);
		assertEquals(3, (int) list.getElements().get(2).element);
		assertEquals(3, (int) list.getElements().get(3).element);
		assertEquals(4, (int) list.getElements().get(4).element);
		assertEquals(5, (int) list.getElements().get(5).element);
		assertEquals(6, (int) list.getElements().get(6).element);
		assertEquals(8, (int) list.getElements().get(7).element);
	}


	@Test public void setTest() {
		BeatEList<Integer> list = new BeatEList<Integer>();
		list.set(5, fr(5));
		list.set(3, fr(3));
		list.set(2, fr(2));
		list.set(4, fr(4));
		list.set(3, fr(3));
		list.set(3, fr(3));
		list.set(8, fr(8));
		list.set(6, fr(6));
		assertEquals(6, list.getElements().size());
		assertEquals(2, (int) list.getElements().get(0).element);
		assertEquals(3, (int) list.getElements().get(1).element);
		assertEquals(4, (int) list.getElements().get(2).element);
		assertEquals(5, (int) list.getElements().get(3).element);
		assertEquals(6, (int) list.getElements().get(4).element);
		assertEquals(8, (int) list.getElements().get(5).element);
	}

}
