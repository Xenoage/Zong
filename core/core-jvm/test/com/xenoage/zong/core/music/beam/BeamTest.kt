package com.xenoage.zong.core.music.beam

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.C
import com.xenoage.zong.core.music.chord.Chord.Companion.graceChord
import material.beam.fragments.ChlapikBeamFragments
import org.junit.Assert.assertEquals
import kotlin.test.Test

/**
 * Tests for [Beam].
 */
class BeamTest {

	@Test
	fun getMaxLinesCountTest() {
		val source = ChlapikBeamFragments()
		assertEquals(2, source.exampleRow1Col1().maxLinesCount)
		assertEquals(2, source.exampleRow1Col2().maxLinesCount)
		assertEquals(2, source.exampleRow1Col3().maxLinesCount)
		assertEquals(2, source.exampleRow1Col4().maxLinesCount)
		assertEquals(2, source.exampleRow2Col1().maxLinesCount)
		assertEquals(2, source.exampleRow2Col2().maxLinesCount)
		assertEquals(3, source.exampleRow2Col3().maxLinesCount)
		assertEquals(3, source.exampleRow2Col4().maxLinesCount)
		assertEquals(3, source.exampleRow2Col5().maxLinesCount)
		assertEquals(3, source.exampleRow2Col6().maxLinesCount)
		assertEquals(2, source.exampleRow3Col2().maxLinesCount)
		assertEquals(2, source.exampleRow3Col4().maxLinesCount)
		assertEquals(3, source.exampleRow3Col6().maxLinesCount)
	}

	@Test
	fun getMaxLinesCountTest_Grace() {
		val beam = Beam(listOf(
				graceChord(pi(C, 4), fr(1, 32)),
				graceChord(pi(C, 4), fr(1, 32))
		))
		assertEquals(3, beam.maxLinesCount.toLong())
	}

}
