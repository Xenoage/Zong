package com.xenoage.utils.math

import com.xenoage.utils.math.Fraction.Companion.fr
import org.junit.Assert.*

import kotlin.test.Test

/**
 * Tests for [Fraction].
 */
class FractionTest {

	/** Tests some fractions. */
	@Test
	fun testFractions() {
		var f1: Fraction
		var f2: Fraction
		//0 in the denominator must throw an IllegalArgumentException
		try {
			f1 = fr(5, 0)
			fail()
		} catch (ex: IllegalArgumentException) {
		}

		//compare two 0 fractions
		f1 = fr(0, 1)
		f2 = fr(0, 1)
		assertTrue(f1 == f2)
		assertTrue(f1.compareTo(f2) == 0)
		//compare two equal fractions
		f1 = fr(13, 39)
		f2 = fr(2, 6)
		assertTrue(f1 == f2)
		assertTrue(f1.compareTo(f2) == 0)
		//compare two different fractions
		f1 = fr(12, 24)
		f2 = fr(5, 6)
		assertFalse(f1 == f2)
		assertTrue(f1 < f2)
	}

	@Test fun toFractionTest() {
		assertEquals(fr(5), "5".toFraction())
		assertEquals(fr(-2), "-2".toFraction())
		assertEquals(fr(3, 4), "3/4".toFraction())
		assertEquals(fr(-3, 4), "-3/4".toFraction())
		assertEquals(fr(-3, 4), "3/-4".toFraction())
		assertEquals(fr(3, 4), "-3/-4".toFraction())
		assertEquals(fr(20, 8), "2+4/8".toFraction())
		assertEquals(fr(12, 8), "2+-4/8".toFraction())
		assertEquals(fr(12, 8), "2+4/-8".toFraction())
	}

	@Test fun plusTest() {
		var sum: Fraction
		val f1: Fraction
		val f2: Fraction
		val f3: Fraction
		f1 = fr(0, 46)
		f2 = fr(13, 69)
		f3 = fr(71255, 927)
		//add 0
		sum = f1 + f2
		assertEquals(sum, f2)
		//add more complicated fractions
		sum = f2 + f3
		assertEquals(1642882, sum.numerator.toLong())
		assertEquals(21321, sum.denominator.toLong())
	}

	@Test fun minus() {
		var sum: Fraction
		val f1: Fraction
		val f2: Fraction
		val f3: Fraction
		f1 = fr(0, 46)
		f2 = fr(13, 69)
		f3 = fr(71255, 927)
		//subtract 0
		sum = f2 - f1
		assertEquals(sum, f2)
		//subtract more complicated fractions
		sum = f2 - f3
		assertEquals(-1634848, sum.numerator.toLong())
		assertEquals(21321, sum.denominator.toLong())
	}

	@Test	fun divide() {
		val f1: Fraction
		val f2: Fraction
		f1 = fr(4, 5)
		f2 = fr(7, 3)
		assertEquals(fr(12, 35), f1 / f2)
		assertEquals(fr(1), f1 / f1)
	}

	@Test fun cancelTest() {
		assertEquals(fr(1, 2), fr(4, 8))
		assertEquals(fr(-2, 7), fr(4, -14))
	}

}
