package com.xenoage.zong.core.header

import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.time.TimeSignature.Companion.implicitSenzaMisura
import com.xenoage.zong.core.music.time.TimeType.Companion.time_3_4
import com.xenoage.zong.core.music.time.TimeType.Companion.time_4_4
import com.xenoage.zong.core.score1Staff4Measures
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [ScoreHeader].
 */
class ScoreHeaderTest {

	@Test
	fun getTimeAtOrBeforeTest() {
		//create a little test score, where there is no time signature
		//in the first measure, but a 3/4 time in the second one
		//and a 4/4 in the fourth one
		val score = score1Staff4Measures()
		val header = score.header
		val time1 = TimeSignature(time_3_4)
		header.getColumnHeader(1).setTime(time1)
		val time2 = TimeSignature(time_4_4)
		header.getColumnHeader(3).setTime(time2)
		//test method
		assertEquals(implicitSenzaMisura, header.getTimeAtOrBefore(0))
		assertEquals(time1, header.getTimeAtOrBefore(1))
		assertEquals(time1, header.getTimeAtOrBefore(2))
		assertEquals(time2, header.getTimeAtOrBefore(3))
	}

}
