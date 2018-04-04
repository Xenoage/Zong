package com.xenoage.zong.core

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.utils.math._0
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atElement
import com.xenoage.zong.io.selection.Cursor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [Score.getAllVoiceElements].
 */
class Score_getAllVoiceElementsTest {

	@Test
	fun test() {
		val it = createTestScore().getAllVoiceElements().iterator()
		for (staff in 0..3) {
			for (measure in 0..3) {
				if ((staff == 1 || staff == 3) && (measure == 1 || measure == 3)) {
					//expect two voices with each 4 quarter rests
					for (voice in 0..1) {
						for (element in 0..3) {
							assertTrue(it.hasNext())
							val e = it.next()
							assertEquals(MP(staff, measure, voice, fr(element, 4), element), e.mp)
							assertEquals(fr(1, 4), e.element.duration)
						}
					}
				} else {
					//expect a full rest
					assertTrue(it.hasNext())
					val e = it.next()
					assertEquals(MP(staff, measure, 0, _0, 0), e.mp)
					assertEquals(fr(1), e.element.duration)
				}
			}
		}
		assertFalse(it.hasNext())
	}

	/**
	 * Test score with 4 staves and 4 measures. In staves 1 and 3, the measures 1 and 3 have each
	 * two voices with 4 quarter rests. The other measures have a single voice and
	 * a full rest.
	 */
	fun createTestScore(): Score {
		val score = Score()
		val cursor = Cursor(score)
		for (staff in 0..3) {
			for (measure in 0..3) {
				if ((staff == 1 || staff == 3) && (measure == 1 || measure == 3)) {
					//2 voices with each 4 quarter notes
					for (voice in 0..1) {
						cursor.mp = atElement(staff, measure, voice, 0)
						for (i in 0..3)
							cursor.write(Rest(fr(1, 4)))
					}
				} else {
					//full rest
					cursor.mp = atElement(staff, measure, 0, 0)
					cursor.write(Rest(fr(1)))
				}
			}
		}
		return score
	}

}
