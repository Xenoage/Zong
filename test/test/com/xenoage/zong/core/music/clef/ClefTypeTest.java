package com.xenoage.zong.core.music.clef;

import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.music.Pitch;

/**
 * Tests for {@link ClefType}.
 * 
 * @author Andreas Wenger
 */
public class ClefTypeTest {

	@Test public void getLpTest() {
		//test center pitches
		assertEquals(2, ClefType.clefTreble.getLp(pi('G', 0, 4)));
		assertEquals(2, ClefType.clefTreble15vb.getLp(pi('G', 0, 2)));
		assertEquals(2, ClefType.clefTreble8vb.getLp(pi('G', 0, 3)));
		assertEquals(2, ClefType.clefTreble8va.getLp(pi('G', 0, 5)));
		assertEquals(2, ClefType.clefTreble15va.getLp(pi('G', 0, 6)));
		assertEquals(6, ClefType.clefBass.getLp(pi('F', 0, 3)));
		assertEquals(6, ClefType.clefBass15vb.getLp(pi('F', 0, 1)));
		assertEquals(6, ClefType.clefBass8vb.getLp(pi('F', 0, 2)));
		assertEquals(6, ClefType.clefBass8va.getLp(pi('F', 0, 4)));
		assertEquals(6, ClefType.clefBass15va.getLp(pi('F', 0, 5)));
		assertEquals(4, ClefType.clefAlto.getLp(pi('C', 0, 4)));
		assertEquals(6, ClefType.clefTenor.getLp(pi('C', 0, 4)));
		assertEquals(4, ClefType.clefTab.getLp(pi('B', 0, 4)));
		assertEquals(4, ClefType.clefTabSmall.getLp(pi('B', 0, 4)));
		assertEquals(4, ClefType.clefPercTwoRects.getLp(pi('B', 0, 4)));
		assertEquals(4, ClefType.clefPercEmptyRect.getLp(pi('B', 0, 4)));
		//test e5
		Pitch e5 = pi('E', 0, 5);
		assertEquals(7, ClefType.clefTreble.getLp(e5));
		assertEquals(21, ClefType.clefTreble15vb.getLp(e5));
		assertEquals(14, ClefType.clefTreble8vb.getLp(e5));
		assertEquals(0, ClefType.clefTreble8va.getLp(e5));
		assertEquals(-7, ClefType.clefTreble15va.getLp(e5));
		assertEquals(19, ClefType.clefBass.getLp(e5));
		assertEquals(33, ClefType.clefBass15vb.getLp(e5));
		assertEquals(26, ClefType.clefBass8vb.getLp(e5));
		assertEquals(12, ClefType.clefBass8va.getLp(e5));
		assertEquals(5, ClefType.clefBass15va.getLp(e5));
		assertEquals(13, ClefType.clefAlto.getLp(e5));
		assertEquals(15, ClefType.clefTenor.getLp(e5));
		assertEquals(7, ClefType.clefTab.getLp(e5));
		assertEquals(7, ClefType.clefTabSmall.getLp(e5));
		assertEquals(7, ClefType.clefPercTwoRects.getLp(e5));
		assertEquals(7, ClefType.clefPercEmptyRect.getLp(e5));
	}

}
