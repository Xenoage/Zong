package com.xenoage.zong.core.position

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.position.MP.Companion.atBeat
import com.xenoage.zong.core.position.MP.Companion.atElement
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [MP].
 */
class MPTest {

	@Test
	fun compareTimeToTest() {
		val otherStaff = 1
		val otherVoice = 1
		//test: earlier, equal time, later
		for (test in listOf(-1, 0, 1)) {
			//equal time (with beats)
			val msg = "First MP should be " + arrayOf("earlier", "equal", "later")[test + 1]
			for (m in 0..3) { //measure
				for (b in 0..3) { //beat
					assertEquals(test, atBeat(0, m, 0, fr(b + test, 4)).compareTimeTo(atBeat(0, m, 0, fr(b, 4))))
					assertEquals(test, atBeat(otherStaff, m, 0, fr(b + test, 4)).compareTimeTo(atBeat(0, m, 0, fr(b, 4))))
					assertEquals(test, atBeat(0, m, 0, fr(b + test, 4)).compareTimeTo(atBeat(0, m, otherVoice, fr(b, 4))))
				}
			}
			//equal time (with element index)
			for (m in 0..3) { //measure
				for (i in 0..3) { //element index
					assertEquals(test, atElement(0, m, 0, i + test).compareTimeTo(atElement(0, m, 0, i)))
					assertEquals(test, atElement(otherStaff, m, 0, i + test).compareTimeTo(atElement(0, m, 0, i)))
					assertEquals(test, atElement(0, m, 0, i + test).compareTimeTo(atElement(0, m, otherVoice, i)))
				}
			}
		}
	}

}
