package com.xenoage.zong.commands.core.music

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.barline.BarlineStyle.LightHeavy
import com.xenoage.zong.core.music.util.get
import com.xenoage.zong.core.score1Staff4Measures
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [ColumnElementWrite].
 */
class ColumnElementRemoveTest {

	@Test
	fun test() {
		val score = score1Staff4Measures()
		val cmd = score.commandPerformer
		val column2 = score.getColumnHeader(2)
		//write middle barline
		val b = Barline(LightHeavy)
		cmd.execute(ColumnElementWrite(b, column2, fr(1, 4), null))
		//remove it
		cmd.execute(ColumnElementRemove(column2, b))
		assertEquals(0, column2.middleBarlines.size)
		//undo. should be here again
		cmd.undo()
		assertEquals(b, column2.middleBarlines[fr(1, 4)])
	}

}
