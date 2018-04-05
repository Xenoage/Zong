package com.xenoage.zong.io.selection

import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.instrument.Instrument
import com.xenoage.zong.core.music.InstrumentChange
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.clef.clefBass
import com.xenoage.zong.core.music.clef.clefTreble
import com.xenoage.zong.core.music.direction.Dynamic
import com.xenoage.zong.core.music.direction.DynamicValue
import com.xenoage.zong.core.music.direction.Words
import com.xenoage.zong.core.music.key.TraditionalKey
import com.xenoage.zong.core.music.key.TraditionalKey.Mode.Major
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.music.util.BeatE
import com.xenoage.zong.core.position.MP.Companion.atElement
import com.xenoage.zong.core.position.MP.Companion.atMeasure
import com.xenoage.zong.core.score1Staff
import com.xenoage.zong.core.text.UnformattedText
import com.xenoage.zong.utils.demo.ScoreRevolutionary
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [Cursor].
 */
class CursorTest {

	/**
	 * Tests the consistency of the [ScoreRevolutionary] demo,
	 * which is written with a [Cursor].
	 */
	@Test
	fun testScoreRevolutionary() {
		ScoreRevolutionary()
	}

	@Test
	fun write_MeasureAndColumnElements_Test() {
		val score = score1Staff()
		val cursor = Cursor(score)
		cursor.write(Rest(fr(1, 4)))
		cursor.write(Rest(fr(1, 4)))
		cursor.write(Rest(fr(1, 4)))
		//write clef at 1/4
		val clef1 = Clef(clefBass)
		cursor.mp = atElement(0, 0, 0, 1)
		cursor.write(clef1)
		var clefs = score.getMeasure(atMeasure(0, 0)).clefs
		assertEquals(1, clefs.size)
		assertEquals(BeatE(clef1, fr(1, 4)), clefs[0])
		//write clef at 2/4
		val clef2 = Clef(clefTreble)
		cursor.mp = atElement(0, 0, 0, 2)
		cursor.write(clef2)
		clefs = score.getMeasure(atMeasure(0, 0)).clefs
		assertEquals(2, clefs.size)
		assertEquals(BeatE(clef1, fr(1, 4)), clefs[0])
		assertEquals(BeatE(clef2, fr(2, 4)), clefs[1])
		//overwrite clef at 1/4
		val clef3 = Clef(clefTreble)
		cursor.mp = atElement(0, 0, 0, 1)
		cursor.write(clef3)
		clefs = score.getMeasure(atMeasure(0, 0)).clefs
		assertEquals(2, clefs.size)
		assertEquals(BeatE(clef3, fr(1, 4)), clefs[0])
		assertEquals(BeatE(clef2, fr(2, 4)), clefs[1])
		//write key at 1/4
		val key = TraditionalKey(5, Major)
		cursor.mp = atElement(0, 0, 0, 1)
		cursor.write(key)
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).clefs.size) //clefs must still be there
		assertEquals(1, score.getColumnHeader(0).keys.size)
		//write direction at 1/4
		val direction1 = Dynamic(DynamicValue.ff)
		cursor.mp = atElement(0, 0, 0, 1)
		cursor.write(direction1)
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).clefs.size) //clefs must still be there
		assertEquals(1, score.getColumnHeader(0).keys.size) //key must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).directions.size)
		//write another direction at 1/4, which does not replace the first one
		val direction2 = Words(UnformattedText(""))
		cursor.mp = atElement(0, 0, 0, 1)
		cursor.write(direction2)
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).clefs.size) //clefs must still be there
		assertEquals(1, score.getColumnHeader(0).keys.size) //key must still be there
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).directions.size) //now two directions
		//write instrument change at 1/4
		val instrChange = InstrumentChange(Instrument.defaultInstrument)
		cursor.mp = atElement(0, 0, 0, 1)
		cursor.write(instrChange)
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).clefs.size) //clefs must still be there
		assertEquals(1, score.getColumnHeader(0).keys.size) //key must still be there
		assertEquals(2, score.getMeasure(atMeasure(0, 0)).directions.size) //directions must still be there
		assertEquals(1, score.getMeasure(atMeasure(0, 0)).instrumentChanges.size)
		//check all added measure elements
		val all = score.getMeasure(atMeasure(0, 0)).measureElements.toList().sortedBy { it.beat }
		assertEquals(5, all.size)
		assertEquals(clef3, all[0].element)
		assertEquals(direction1, all[1].element)
		assertEquals(direction2, all[2].element)
		assertEquals(instrChange, all[3].element)
		assertEquals(clef2, all[4].element)
	}

}
