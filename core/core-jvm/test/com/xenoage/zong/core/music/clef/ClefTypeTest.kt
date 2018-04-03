package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.Pitch.pi
import org.junit.Assert.assertEquals

import org.junit.Test

import com.xenoage.zong.core.music.Pitch

/**
 * Tests for [ClefType].
 *
 * @author Andreas Wenger
 */
class ClefTypeTest {

	@Test
	fun getLpTest() {
		//test center pitches
		assertEquals(2, ClefType.Companion.getClefTreble().getLp(Companion.pi('G', 0, 4)))
		assertEquals(2, ClefType.Companion.getClefTreble15vb().getLp(Companion.pi('G', 0, 2)))
		assertEquals(2, ClefType.Companion.getClefTreble8vb().getLp(Companion.pi('G', 0, 3)))
		assertEquals(2, ClefType.Companion.getClefTreble8va().getLp(Companion.pi('G', 0, 5)))
		assertEquals(2, ClefType.Companion.getClefTreble15va().getLp(Companion.pi('G', 0, 6)))
		assertEquals(6, ClefType.Companion.getClefBass().getLp(Companion.pi('F', 0, 3)))
		assertEquals(6, ClefType.Companion.getClefBass15vb().getLp(Companion.pi('F', 0, 1)))
		assertEquals(6, ClefType.Companion.getClefBass8vb().getLp(Companion.pi('F', 0, 2)))
		assertEquals(6, ClefType.Companion.getClefBass8va().getLp(Companion.pi('F', 0, 4)))
		assertEquals(6, ClefType.Companion.getClefBass15va().getLp(Companion.pi('F', 0, 5)))
		assertEquals(4, ClefType.Companion.getClefAlto().getLp(Companion.pi('C', 0, 4)))
		assertEquals(6, ClefType.Companion.getClefTenor().getLp(Companion.pi('C', 0, 4)))
		assertEquals(4, ClefType.Companion.getClefTab().getLp(Companion.pi('B', 0, 4)))
		assertEquals(4, ClefType.Companion.getClefTabSmall().getLp(Companion.pi('B', 0, 4)))
		assertEquals(4, ClefType.Companion.getClefPercTwoRects().getLp(Companion.pi('B', 0, 4)))
		assertEquals(4, ClefType.Companion.getClefPercEmptyRect().getLp(Companion.pi('B', 0, 4)))
		//test e5
		val e5 = Companion.pi('E', 0, 5)
		assertEquals(7, ClefType.Companion.getClefTreble().getLp(e5))
		assertEquals(21, ClefType.Companion.getClefTreble15vb().getLp(e5))
		assertEquals(14, ClefType.Companion.getClefTreble8vb().getLp(e5))
		assertEquals(0, ClefType.Companion.getClefTreble8va().getLp(e5))
		assertEquals(-7, ClefType.Companion.getClefTreble15va().getLp(e5))
		assertEquals(19, ClefType.Companion.getClefBass().getLp(e5))
		assertEquals(33, ClefType.Companion.getClefBass15vb().getLp(e5))
		assertEquals(26, ClefType.Companion.getClefBass8vb().getLp(e5))
		assertEquals(12, ClefType.Companion.getClefBass8va().getLp(e5))
		assertEquals(5, ClefType.Companion.getClefBass15va().getLp(e5))
		assertEquals(13, ClefType.Companion.getClefAlto().getLp(e5))
		assertEquals(15, ClefType.Companion.getClefTenor().getLp(e5))
		assertEquals(7, ClefType.Companion.getClefTab().getLp(e5))
		assertEquals(7, ClefType.Companion.getClefTabSmall().getLp(e5))
		assertEquals(7, ClefType.Companion.getClefPercTwoRects().getLp(e5))
		assertEquals(7, ClefType.Companion.getClefPercEmptyRect().getLp(e5))
	}

}
