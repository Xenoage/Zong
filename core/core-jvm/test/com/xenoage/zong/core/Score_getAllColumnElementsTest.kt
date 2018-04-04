package com.xenoage.zong.core

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.utils.math._0
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.barline.BarlineStyle
import com.xenoage.zong.core.music.barline.barlineRegular
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.time.TimeType
import com.xenoage.zong.core.music.time.TimeType.Companion.time_4_4
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atBeat
import com.xenoage.zong.core.position.MP.Companion.atMeasure
import com.xenoage.zong.core.position.MP.Companion.unknown
import com.xenoage.zong.io.selection.Cursor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [Score.getAllColumnElements].
 */
class Score_getAllColumnElementsTest {

	@Test
	fun test() {
		val it = createTestScore().getAllColumnElements().iterator()
		for (measure in listOf(1, 3)) {
			//middle barline is reported first (known beat)
			assertTrue(it.hasNext())
			var e = it.next()
			assertEquals(atBeat(unknown, measure, unknown, fr(1, 4)), e.mp)
			assertEquals(BarlineStyle.Regular, (e.element as Barline).style)
			//time signature
			assertTrue(it.hasNext())
			e = it.next()
			assertEquals(atMeasure(unknown, measure), e.mp)
			assertEquals(time_4_4, (e.element as TimeSignature).type)
			//end barline
			assertTrue(it.hasNext())
			e = it.next()
			assertEquals(atMeasure(unknown, measure), e.mp)
			assertEquals(BarlineStyle.LightLight, (e.element as Barline).style)
		}
		assertFalse(it.hasNext())
	}

	/**
	 * Test score with 4 measures. The measures 1 and 3 have each
	 * a time signature, a middle barline at 1/4 and an end barline.
	 */
	private fun createTestScore(): Score {
		val score = Score()
		val cursor = Cursor(score)
		for (measure in listOf(1, 3)) {
			cursor.mp = MP(unknown, measure, 0, _0, 0)
			cursor.write(TimeSignature(TimeType.time_4_4))
			cursor.mp = MP(unknown, measure, unknown, fr(1, 4), 0)
			cursor.write(barlineRegular())
			val column = score.getColumnHeader(measure)
			column.setEndBarline(Barline(BarlineStyle.LightLight))
		}
		return score
	}

}
