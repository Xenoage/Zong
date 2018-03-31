package com.xenoage.utils.math;

import com.xenoage.utils.math.geom.Point2f;
import org.junit.Test;

import java.math.BigInteger;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.DRf;
import static com.xenoage.utils.math.Delta.Df;
import static com.xenoage.utils.math.MathUtils.*;
import static org.junit.Assert.*;

/**
 * Test cases for {@link MathUtils}.
 *
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class MathUtilsTest {

	@Test public void modTest() {
		//mod 2
		for (int i = -20; i <= 20; i+=2)
			assertEquals(0, mod(i, 2));
		for (int i = -19; i <= 19; i+=2)
			assertEquals(1, mod(i, 2));
		//mod 4
		for (int i = -20; i <= 20; i+=4)
			assertEquals(0, mod(i, 4));
		for (int i = -19; i <= 17; i+=4)
			assertEquals(1, mod(i, 4));
		for (int i = -18; i <= 18; i+=4)
			assertEquals(2, mod(i, 4));
		for (int i = -17; i <= 19; i+=4)
			assertEquals(3, mod(i, 4));
	}
	
	@Test public void modMinTest() {
		assertEquals(2, modMin(5, 3, 0));
		assertEquals(-8, modMin(1, 3, -10));
		assertEquals(2, modMin(-1, 3, 0));
		assertEquals(5, modMin(5, 3, 4));
		assertEquals(20, modMin(5, 3, 20));
		assertEquals(30, modMin(8, 1, 30));
		assertEquals(0, modMin(4, 4, 0));
		assertEquals(-6, modMin(-2, 4, -6));
	}

	/**
	 * Tests the rotate-method.
	 */
	@Test public void rotateTest() {
		Point2f p = new Point2f(10, 5);
		Point2f res;
		//angle 0
		res = rotate(p, 0);
		assertEquals(10, res.x, DRf);
		assertEquals(5, res.y, DRf);
		//angle 90
		res = rotate(p, 90);
		assertEquals(5, res.x, DRf);
		assertEquals(-10, res.y, DRf);
		//angle 122
		res = rotate(p, 122);
		double cos = Math.cos(122 * Math.PI / 180f);
		double sin = Math.sin(122 * Math.PI / 180f);
		assertEquals(10 * cos + 5 * sin, res.x, DRf);
		assertEquals(10 * -sin + 5 * cos, res.y, DRf);
	}

	@Test public void lowestPrimeNumberTest() {
		assertEquals(2, lowestPrimeNumber(8));
		assertEquals(3, lowestPrimeNumber(21));
		assertEquals(5, lowestPrimeNumber(65));
		assertEquals(11, lowestPrimeNumber(209));
	}

	@Test public void interpolateLinearTest() {
		assertEquals(5, interpolateLinear(5, 10, 100, 200, 100), Df);
		assertEquals(10, interpolateLinear(5, 10, 100, 200, 200), Df);
		assertEquals(6, interpolateLinear(5, 10, 100, 200, 120), Df);
	}
	
	@Test public void minTest() {
		assertNull(min());
		assertNull(min((Integer) null));
		assertEquals((Integer) 1, min(2, null, 1, 5));
		//test, if first candidate is returned
		BigInteger ref1 = new BigInteger("1");
		BigInteger ref2 = new BigInteger("1");
		assertSame(ref1, min(ref1, ref2));
		assertNotSame(ref1, min(ref2, ref1));
	}
	
	@Test public void maxTest() {
		assertNull(max());
		assertNull(max((Integer) null));
		assertEquals((Integer) 5, max(2, null, 5, 1));
		//test, if first candidate is returned
		BigInteger ref1 = new BigInteger("1");
		BigInteger ref2 = new BigInteger("1");
		assertSame(ref1, max(ref1, ref2));
		assertNotSame(ref1, max(ref2, ref1));
	}

	@Test public void log2Test() {
		//one single computation
		assertEquals(10, log2(1024));
		//exact result
		for (int i : range(10)) {
			long number = (long) Math.pow(2, i);
			assertEquals("log2(" + number + ")", i, log2(number));
		}
		//lower result
		for (int i : range(2, 10)) {
			long number = (long) Math.pow(2, i);
			assertEquals("log2(" + number + ")", i, log2(number + 1 + i));
		}
		//exception
		try {
			log2(0);
			log2(-5);
			fail();
		} catch (Exception ex) {
		}
	}

}
