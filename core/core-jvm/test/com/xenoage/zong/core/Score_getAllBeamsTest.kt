package com.xenoage.zong.core

import com.xenoage.utils.math._0
import com.xenoage.utils.math._1_8
import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.C
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atElement
import com.xenoage.zong.io.selection.Cursor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [Score.getAllBeams].
 */
class Score_getAllBeamsTest {

	@Test
	fun test() {
		val it = createTestScore().getAllBeams().iterator()
		//first beam
		assertTrue(it.hasNext())
		var beam = it.next()
		assertEquals(MP(0, 0, 1, _0, 0), beam.mp)
		//second beam (two staves)
		assertTrue(it.hasNext())
		beam = it.next()
		assertEquals(MP(0, 2, 0, _0, 0), beam.mp)
		assertEquals(MP(0, 2, 0, _0, 0), beam.element[0].chord.mp)
		assertEquals(MP(1, 2, 0, _1_8, 1), beam.element[1].chord.mp)
		//third chord
		assertTrue(it.hasNext())
		beam = it.next()
		assertEquals(MP(1, 3, 0, _0, 0), beam.mp)
		//finished
		assertFalse(it.hasNext())
	}

	/**
	 * Test score with 2 staves and 4 measures. In measure 0, there is a beam in
	 * staff 0 in voice 1. In measure 2, there is a beam spanning over two staves.
	 * In measure 3, there is a beam in staff 1 in voice 0.
	 */
	fun createTestScore(): Score {
		val score = Score()
		val cursor = Cursor(score)
		//first beam
		cursor.mp = MP.atElement(0, 0, 1, 0)
		cursor.openBeam()
		repeat(4) { cursor.write(Chord(_1_8, pi(C, 4))) }
		cursor.closeBeam()
		//second beam
		cursor.mp = atElement(0, 2, 0, 0)
		cursor.openBeam()
		cursor.write(Chord(_1_8, pi(C, 4)))
		cursor.mp = atElement(1, 2, 0, 0)
		cursor.write(Rest(_1_8))
		cursor.write(Chord(_1_8, pi(C, 4)))
		cursor.closeBeam()
		//third beam
		cursor.mp = atElement(1, 3, 0, 0)
		cursor.openBeam()
		repeat(2) { cursor.write(Chord(_1_8, pi(C, 4))) }
		cursor.closeBeam()
		return score
	}

}
