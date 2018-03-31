package com.xenoage.utils.pdlib;

import static com.xenoage.utils.pdlib.PMap.pmap;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.pdlib.PMap;

/**
 * Test cases for the {@link PMap} class.
 * 
 * @author Andreas Wenger
 */
public class PMapTest {

	@Test public void minusValueTest() {
		PMap<Integer, Integer> map = pmap();
		map = map.plus(1, 10);
		map = map.plus(2, 1);
		map = map.plus(3, 10);
		map = map.plus(4, 5);
		map = map.minusValue(10);
		assertEquals(2, map.size());
		assertEquals(1, (int) map.get(2));
		assertEquals(5, (int) map.get(4));
	}

	@Test public void replaceValueTest() {
		PMap<Integer, Integer> map = pmap();
		map = map.plus(1, 10);
		map = map.plus(2, 1);
		map = map.plus(3, 10);
		map = map.plus(4, 5);
		map = map.replaceValue(10, 11);
		assertEquals(4, map.size());
		assertEquals(11, (int) map.get(1));
		assertEquals(1, (int) map.get(2));
		assertEquals(11, (int) map.get(3));
		assertEquals(5, (int) map.get(4));
	}

}
