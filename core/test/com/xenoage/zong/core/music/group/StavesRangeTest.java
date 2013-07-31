package com.xenoage.zong.core.music.group;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Tests for {@link StavesRange}.
 * 
 * @author Andreas Wenger
 */
public class StavesRangeTest {
	
	@Test public void insertTest() {
		StavesRange r;
		//insert before
		r = new StavesRange(5, 10);
		r.insert(4, 10);
		assertEquals(new StavesRange(15, 20), r);
		//insert within
		r = new StavesRange(5, 10);
		r.insert(5, 10);
		assertEquals(new StavesRange(5, 20), r);
		//insert within
		r = new StavesRange(5, 10);
		r.insert(10, 10);
		assertEquals(new StavesRange(5, 20), r);
		//insert after
		r = new StavesRange(5, 10);
		r.insert(11, 10);
		assertEquals(new StavesRange(5, 10), r);
	}
	
	
	@Test public void removeTest() {
		StavesRange r;
		//remove completely
		r = new StavesRange(5, 10);
		assertTrue(r.remove(5, 6));
		//remove completely
		r = new StavesRange(5, 10);
		assertTrue(r.remove(4, 7));
		//remove completely
		r = new StavesRange(5, 10);
		assertTrue(r.remove(5, 7));
		//remove completely
		r = new StavesRange(5, 10);
		assertTrue(r.remove(4, 8));
		//remove before
		r = new StavesRange(5, 10);
		assertFalse(r.remove(2, 2));
		assertEquals(new StavesRange(3, 8), r);
		//remove around start
		r = new StavesRange(5, 10);
		assertFalse(r.remove(2, 4));
		assertEquals(new StavesRange(2, 5), r);
		//remove within
		r = new StavesRange(5, 10);
		assertFalse(r.remove(5, 5));
		assertEquals(new StavesRange(5, 5), r);
		//remove within
		r = new StavesRange(5, 10);
		assertFalse(r.remove(6, 5));
		assertEquals(new StavesRange(5, 5), r);
		//remove around end
		r = new StavesRange(5, 10);
		assertFalse(r.remove(8, 4));
		assertEquals(new StavesRange(5, 7), r);
		//remove after
		r = new StavesRange(5, 10);
		assertFalse(r.remove(11, 4));
		assertEquals(new StavesRange(5, 10), r);
	}

}
