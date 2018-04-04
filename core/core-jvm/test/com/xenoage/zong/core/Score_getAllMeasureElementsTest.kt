package com.xenoage.zong.core

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.music.direction.Words
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.position.MP.Companion.atBeat
import com.xenoage.zong.core.position.MP.Companion.atElement
import com.xenoage.zong.core.position.MP.Companion.unknown
import com.xenoage.zong.core.text.UnformattedText
import com.xenoage.zong.io.selection.Cursor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [Score.getAllMeasureElements].
 */
class Score_getAllMeasureElementsTest {

	@Test
	fun test() {
		val it = createTestScore().getAllMeasureElements().iterator()
		for (staff in listOf(1, 3)) {
			for (measure in listOf(1, 3)) {
				for (element in 0..1) {
					assertTrue(it.hasNext())
					val e = it.next()
					assertEquals("staff $staff, measure $measure, element $element",
							(e.element as Words).text.toString())
					assertEquals(atBeat(staff, measure, unknown, fr(element + 1, 4)), e.mp)
				}
			}
		}
		assertFalse(it.hasNext())
	}

	/**
	 * Test score with 4 staves and 4 measures. In staves 1 and 3, the measures 1 and 3 have each
	 * three words with text "staff x, measure y, element z" on beats 1/4 and 2/4
	 */
	private fun createTestScore(): Score {
		val score = Score()
		val cursor = Cursor(score)
		for (staff in listOf(1, 3)) {
			for (measure in listOf(1, 3)) {
				cursor.mp = atElement(staff, measure, 0, 0)
				for (element in 0..1) {
					cursor.write(Rest(fr(1, 4)))
					cursor.write(Words(UnformattedText(
							"staff $staff, measure $measure, element $element")))
				}
			}
		}
		return score
	}

}
