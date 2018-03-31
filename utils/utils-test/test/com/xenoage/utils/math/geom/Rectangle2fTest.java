package com.xenoage.utils.math.geom;

import static com.xenoage.utils.math.Delta.Df;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for the Rectangle2f class.
 *
 * @author Andreas Wenger
 */
public class Rectangle2fTest {

	@Test public void extend() {
		//test 1
		Rectangle2f r1 = new Rectangle2f(2, 4, 6, 8);
		Rectangle2f r2 = new Rectangle2f(1, -4, 12, 20);
		r1 = r1.extend(r2);
		assertEquals(r1.position.x, 1, Df);
		assertEquals(r1.position.y, -4, Df);
		assertEquals(r1.size.width, 12, Df);
		assertEquals(r1.size.height, 20, Df);
		//test 2
		r1 = new Rectangle2f(2, 4, 6, 8);
		r2 = new Rectangle2f(3, 8, 4, 1);
		r1 = r1.extend(r2);
		assertEquals(r1.position.x, 2, Df);
		assertEquals(r1.position.y, 4, Df);
		assertEquals(r1.size.width, 6, Df);
		assertEquals(r1.size.height, 8, Df);
		//test 3
		r1 = new Rectangle2f(2, 2, 4, 4);
		r2 = new Rectangle2f(0, 0, 4, 4);
		r1 = r1.extend(r2);
		assertEquals(r1.position.x, 0, Df);
		assertEquals(r1.position.y, 0, Df);
		assertEquals(r1.size.width, 6, Df);
		assertEquals(r1.size.height, 6, Df);
	}

}
