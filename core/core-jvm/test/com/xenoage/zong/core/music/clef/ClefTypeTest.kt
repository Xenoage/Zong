package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.Pitch.Companion.pi
import com.xenoage.zong.core.music.Step.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [ClefType].
 */
class ClefTypeTest {

	@Test
	fun getLpTest() {
		//test center pitches
		assertEquals(2, clefTreble.getLP(pi(G, 4)))
		assertEquals(2, clefTreble15vb.getLP(pi(G, 2)))
		assertEquals(2, clefTreble8vb.getLP(pi(G, 3)))
		assertEquals(2, clefTreble8va.getLP(pi(G, 5)))
		assertEquals(2, clefTreble15va.getLP(pi(G, 6)))
		assertEquals(6, clefBass.getLP(pi(F, 3)))
		assertEquals(6, clefBass15vb.getLP(pi(F, 1)))
		assertEquals(6, clefBass8vb.getLP(pi(F, 2)))
		assertEquals(6, clefBass8va.getLP(pi(F, 4)))
		assertEquals(6, clefBass15va.getLP(pi(F, 5)))
		assertEquals(4, clefAlto.getLP(pi(C, 4)))
		assertEquals(6, clefTenor.getLP(pi(C, 4)))
		assertEquals(4, clefTab.getLP(pi(B, 4)))
		assertEquals(4, clefTabSmall.getLP(pi(B, 4)))
		assertEquals(4, clefPercTwoRects.getLP(pi(B, 4)))
		assertEquals(4, clefPercEmptyRect.getLP(pi(B, 4)))
		//test e5
		val e5 = pi(E, 5)
		assertEquals(7, clefTreble.getLP(e5))
		assertEquals(21, clefTreble15vb.getLP(e5))
		assertEquals(14, clefTreble8vb.getLP(e5))
		assertEquals(0, clefTreble8va.getLP(e5))
		assertEquals(-7, clefTreble15va.getLP(e5))
		assertEquals(19, clefBass.getLP(e5))
		assertEquals(33, clefBass15vb.getLP(e5))
		assertEquals(26, clefBass8vb.getLP(e5))
		assertEquals(12, clefBass8va.getLP(e5))
		assertEquals(5, clefBass15va.getLP(e5))
		assertEquals(13, clefAlto.getLP(e5))
		assertEquals(15, clefTenor.getLP(e5))
		assertEquals(7, clefTab.getLP(e5))
		assertEquals(7, clefTabSmall.getLP(e5))
		assertEquals(7, clefPercTwoRects.getLP(e5))
		assertEquals(7, clefPercEmptyRect.getLP(e5))
	}

}
