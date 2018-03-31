package com.xenoage.utils.math;

import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.*;

import org.junit.Test;

import com.xenoage.utils.math.Fraction;

/**
 * Test cases for the Fraction class.
 *
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class FractionTest {

	/**
	 * Tests some fractions.
	 */
	@Test public void testFractions() {
		Fraction f1, f2;
		//0 in the denominator must throw an IllegalArgumentException
		try {
			f1 = fr(5, 0);
			fail();
		} catch (IllegalArgumentException ex) {
		}
		//compare two 0 fractions
		f1 = fr(0, 1);
		f2 = fr(0, 1);
		assertTrue(f1.equals(f2));
		assertTrue(f1.compareTo(f2) == 0);
		//compare two equal fractions
		f1 = fr(13, 39);
		f2 = fr(2, 6);
		assertTrue(f1.equals(f2));
		assertTrue(f1.compareTo(f2) == 0);
		//compare two different fractions
		f1 = fr(12, 24);
		f2 = fr(5, 6);
		assertFalse(f1.equals(f2));
		assertTrue(f1.compareTo(f2) < 0);
	}

	/**
	 * Tests the fromString method.
	 */
	@Test public void testFromString() {
		assertEquals(fr(5), Fraction.fromString("5"));
		assertEquals(fr(-2), Fraction.fromString("-2"));
		assertEquals(fr(3, 4), Fraction.fromString("3/4"));
		assertEquals(fr(-3, 4), Fraction.fromString("-3/4"));
		assertEquals(fr(-3, 4), Fraction.fromString("3/-4"));
		assertEquals(fr(3, 4), Fraction.fromString("-3/-4"));
		assertEquals(fr(20, 8), Fraction.fromString("2+4/8"));
		assertEquals(fr(12, 8), Fraction.fromString("2+-4/8"));
		assertEquals(fr(12, 8), Fraction.fromString("2+4/-8"));
	}

	/**
	 * Tests the add method.
	 */
	@Test public void add() {
		Fraction sum, f1, f2, f3;
		f1 = fr(0, 46);
		f2 = fr(13, 69);
		f3 = fr(71255, 927);
		//add 0
		sum = f1.add(f2);
		assertEquals(sum, f2);
		//add more complicated fractions
		sum = f2.add(f3);
		assertEquals(1642882, sum.getNumerator());
		assertEquals(21321, sum.getDenominator());
	}

	/**
	 * Tests the sub method.
	 */
	@Test public void sub() {
		Fraction sum, f1, f2, f3;
		f1 = fr(0, 46);
		f2 = fr(13, 69);
		f3 = fr(71255, 927);
		//subtract 0
		sum = f2.sub(f1);
		assertEquals(sum, f2);
		//subtract more complicated fractions
		sum = f2.sub(f3);
		assertEquals(-1634848, sum.getNumerator());
		assertEquals(21321, sum.getDenominator());
	}

	@Test public void divide() {
		Fraction f1, f2;
		f1 = fr(4, 5);
		f2 = fr(7, 3);
		assertEquals(fr(12, 35), f1.divideBy(f2));
		assertEquals(fr(1), f1.divideBy(f1));
	}

	@Test public void testCancel() {
		assertEquals(fr(1, 2), fr(4, 8));
		assertEquals(fr(-2, 7), fr(4, -14));
	}

}
