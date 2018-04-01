package com.xenoage.zong.commands.core.music

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.music.MeasureSide
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.barline.BarlineStyle.LightHeavy
import com.xenoage.zong.core.music.barline.barlineRegular
import com.xenoage.zong.core.music.util.get
import com.xenoage.zong.core.score1Staff4Measures
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [ColumnElementWrite].
 */
class ColumnElementWriteTest {

	@Test
	fun test() {
		val score = score1Staff4Measures()
		val cmd = score.commandPerformer
		val column2 = score.getColumnHeader(2)
		//write start barline, middle barline and end barline
		val b1 = barlineRegular()
		val b2 = barlineRegular()
		val b3 = Barline(LightHeavy)
		cmd.execute(ColumnElementWrite(b1, column2, null, MeasureSide.Left))
		cmd.execute(ColumnElementWrite(b2, column2, fr(1, 4)))
		cmd.execute(ColumnElementWrite(b3, column2, null, MeasureSide.Right))
		assertEquals(b1, column2.startBarline)
		assertEquals(b2, column2.middleBarlines[fr(1, 4)])
		assertEquals(b3, column2.endBarline)
		//overwrite middle barline
		val b4 = barlineRegular()
		cmd.execute(ColumnElementWrite(b4, column2, fr(1, 4), null))
		assertEquals(b4, column2.middleBarlines[fr(1, 4)])
		//undo. b2 should be here again
		cmd.undo()
		assertEquals(b2, column2.middleBarlines[fr(1, 4)])
		//undo all steps. the middle barline should not exist any more
		cmd.undoMultipleSteps(3)
		assertEquals(0, column2.middleBarlines.size)
	}

}
