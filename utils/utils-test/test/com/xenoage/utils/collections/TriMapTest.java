package com.xenoage.utils.collections;

import com.xenoage.utils.kernel.Tuple3;
import org.junit.Test;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.TriMap.triMap;
import static com.xenoage.utils.kernel.Tuple3.t3;
import static org.junit.Assert.*;

/**
 * Tests for {@link TriMap}.
 * 
 * @author Andreas Wenger
 */
public class TriMapTest {
	
	@Test public void testDifferentTypes() {
		TriMap<String, Integer, Float> map = triMap();
		assertEquals(0, map.size());
		map.put("A", 1, 1f);
		map.put("A", 2, 2f);
		map.put("B", 3, 3f);
		map.put("C", 4, 2f);
		assertEquals(2, map.size());
		for (Tuple3<String, Integer, Float> expected : alist(t3("B", 3, 3f), t3("C", 4, 2f))) {
			assertEquals(expected, map.getBy1(expected.get1()));
			assertEquals(expected, map.getBy2(expected.get2()));
			assertEquals(expected, map.getBy3(expected.get3()));
		}
	}

	@Test public void testSameTypes() {
		TriMap<Integer, Integer, Integer> map = triMap();
		assertEquals(0, map.size());
		map.put(1, 1, 1);
		map.put(1, 2, 2);
		map.put(2, 3, 3);
		map.put(3, 4, 2);
		assertEquals(2, map.size());
		for (Tuple3<Integer, Integer, Integer> expected : alist(t3(2, 3, 3), t3(3, 4, 2))) {
			assertEquals(expected, map.getBy1(expected.get1()));
			assertEquals(expected, map.getBy2(expected.get2()));
			assertEquals(expected, map.getBy3(expected.get3()));
		}
	}

	@Test public void testDeadReferences() {
		TriMap<String, Integer, Float> map = triMap();
		map.put("A", 1, 1f);
		map.put("B", 2, 2f);
		map.put("C", 3, 3f);
		//now, when "A" is overwritten, the references of 1 and 1f must be dead
		map.put("A", 10, 10f);
		assertEquals(null, map.getBy2(1));
		assertEquals(null, map.getBy3(1f));
		//and when 2 is overwritten, the references of "B" and 2f must be dead
		map.put("BB", 2, 20f);
		assertEquals(null, map.getBy1("B"));
		assertEquals(null, map.getBy3(2f));
		//and when 3f is overwritten, the references of "C" and 3 must be dead
		map.put("CC", 30, 3f);
		assertEquals(null, map.getBy1("C"));
		assertEquals(null, map.getBy2(3));
	}

}
