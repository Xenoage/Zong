package com.xenoage.utils.collections;

import org.junit.Test;

import static com.xenoage.utils.collections.BiMap.biMap;
import static org.junit.Assert.*;

/**
 * Tests for {@link BiMap}.
 * 
 * @author Andreas Wenger
 */
public class BiMapTest {
	
	@Test public void testDifferentTypes() {
		BiMap<String, Integer> map = biMap();
		assertEquals(0, map.size());
		map.put("old", 5);
		map.put("other", 3);
		map.put("other", 8);
		map.put("new", 5);
		assertEquals(2, map.size());
		assertEquals("new", map.getBy2(5));
		assertEquals("other", map.getBy2(8));
		assertEquals((Integer) 5, map.getBy1("new"));
		assertEquals((Integer) 8, map.getBy1("other"));
	}
	
	@Test public void testSameTypes() {
		BiMap<String, String> map = biMap();
		assertEquals(0, map.size());
		map.put("1", "1");
		map.put("2", "1");
		map.put("1", "3");
		assertEquals(2, map.size());
		assertEquals("2", map.getBy2("1"));
		assertEquals("1", map.getBy1("2"));
		assertEquals("1", map.getBy2("3"));
		assertEquals("3", map.getBy1("1"));
	}

	@Test public void testDeadReferences() {
		BiMap<String, Integer> map = biMap();
		map.put("A", 1);
		map.put("B", 2);
		//now, when 2 is overwritten, the reference of B must be dead
		map.put("C", 2);
		assertEquals(null, map.getBy1("B"));
		//and when A is overwritten, the reference to 1 must be dead
		map.put("A", 3);
		assertEquals(null, map.getBy2(1));
	}

}
