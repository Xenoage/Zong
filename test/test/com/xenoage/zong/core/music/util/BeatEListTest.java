package com.xenoage.zong.core.music.util;

import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.*;


/**
 * Test cases for {@link BeatE}.
 * 
 * @author Andreas Wenger
 */
public class BeatEListTest {

	@Test public void addTest() {
		val list = new BeatEList<Integer>();
		list.add(50, fr(5));
		list.add(30, fr(3));
		list.add(20, fr(2));
		list.add(40, fr(4));
		list.add(31, fr(3));
		list.add(32, fr(3));
		list.add(80, fr(8));
		list.add(60, fr(6));
		assertEquals(8, list.getElements().size());
		assertEquals(20, (int) list.getElements().get(0).element);
		assertEquals(30, (int) list.getElements().get(1).element);
		assertEquals(31, (int) list.getElements().get(2).element);
		assertEquals(32, (int) list.getElements().get(3).element);
		assertEquals(40, (int) list.getElements().get(4).element);
		assertEquals(50, (int) list.getElements().get(5).element);
		assertEquals(60, (int) list.getElements().get(6).element);
		assertEquals(80, (int) list.getElements().get(7).element);
	}


	@Test public void setTest() {
		val list = new BeatEList<Integer>();
		list.set(50, fr(5));
		list.set(30, fr(3));
		list.set(20, fr(2));
		list.set(40, fr(4));
		list.set(31, fr(3));
		list.set(32, fr(3));
		list.set(80, fr(8));
		list.set(60, fr(6));
		assertEquals(6, list.getElements().size());
		assertEquals(20, (int) list.getElements().get(0).element);
		assertEquals(32, (int) list.getElements().get(1).element);
		assertEquals(40, (int) list.getElements().get(2).element);
		assertEquals(50, (int) list.getElements().get(3).element);
		assertEquals(60, (int) list.getElements().get(4).element);
		assertEquals(80, (int) list.getElements().get(5).element);
	}

	@Test public void filterTest() {
		val list = new BeatEList<Integer>();
		list.add(10, fr(1));
		list.add(20, fr(2));
		list.add(30, fr(3));
		list.add(31, fr(3));
		list.add(40, fr(4));
		//before
		BeatEList<Integer> expected = new BeatEList<Integer>();
		expected.add(10, fr(1));
		expected.add(20, fr(2));
		BeatEList<Integer> actual = list.filter(Interval.Before, fr(3));
		assertEquals(expected, actual);
		//at or after
		expected = new BeatEList<Integer>();
		expected.add(30, fr(3));
		expected.add(31, fr(3));
		expected.add(40, fr(4));
		actual = list.filter(Interval.AtOrAfter, fr(3));
		assertEquals(expected, actual);
		//after
		expected = new BeatEList<Integer>();
		expected.add(40, fr(4));
		actual = list.filter(Interval.After, fr(3));
		assertEquals(expected, actual);
	}

}
