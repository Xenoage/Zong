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
		assertEquals(2, ClefType.Companion.getClefTreble().getLp(pi('G', 0, 4)));
		assertEquals(2, ClefType.Companion.getClefTreble15vb().getLp(pi('G', 0, 2)));
		assertEquals(2, ClefType.Companion.getClefTreble8vb().getLp(pi('G', 0, 3)));
		assertEquals(2, ClefType.Companion.getClefTreble8va().getLp(pi('G', 0, 5)));
		assertEquals(2, ClefType.Companion.getClefTreble15va().getLp(pi('G', 0, 6)));
		assertEquals(6, ClefType.Companion.getClefBass().getLp(pi('F', 0, 3)));
		assertEquals(6, ClefType.Companion.getClefBass15vb().getLp(pi('F', 0, 1)));
		assertEquals(6, ClefType.Companion.getClefBass8vb().getLp(pi('F', 0, 2)));
		assertEquals(6, ClefType.Companion.getClefBass8va().getLp(pi('F', 0, 4)));
		assertEquals(6, ClefType.Companion.getClefBass15va().getLp(pi('F', 0, 5)));
		assertEquals(4, ClefType.Companion.getClefAlto().getLp(pi('C', 0, 4)));
		assertEquals(6, ClefType.Companion.getClefTenor().getLp(pi('C', 0, 4)));
		assertEquals(4, ClefType.Companion.getClefTab().getLp(pi('B', 0, 4)));
		assertEquals(4, ClefType.Companion.getClefTabSmall().getLp(pi('B', 0, 4)));
		assertEquals(4, ClefType.Companion.getClefPercTwoRects().getLp(pi('B', 0, 4)));
		assertEquals(4, ClefType.Companion.getClefPercEmptyRect().getLp(pi('B', 0, 4)));
		//test e5
		Pitch e5 = pi('E', 0, 5);
		assertEquals(7, ClefType.Companion.getClefTreble().getLp(e5));
		assertEquals(21, ClefType.Companion.getClefTreble15vb().getLp(e5));
		assertEquals(14, ClefType.Companion.getClefTreble8vb().getLp(e5));
		assertEquals(0, ClefType.Companion.getClefTreble8va().getLp(e5));
		assertEquals(-7, ClefType.Companion.getClefTreble15va().getLp(e5));
		assertEquals(19, ClefType.Companion.getClefBass().getLp(e5));
		assertEquals(33, ClefType.Companion.getClefBass15vb().getLp(e5));
		assertEquals(26, ClefType.Companion.getClefBass8vb().getLp(e5));
		assertEquals(12, ClefType.Companion.getClefBass8va().getLp(e5));
		assertEquals(5, ClefType.Companion.getClefBass15va().getLp(e5));
		assertEquals(13, ClefType.Companion.getClefAlto().getLp(e5));
		assertEquals(15, ClefType.Companion.getClefTenor().getLp(e5));
		assertEquals(7, ClefType.Companion.getClefTab().getLp(e5));
		assertEquals(7, ClefType.Companion.getClefTabSmall().getLp(e5));
		assertEquals(7, ClefType.Companion.getClefPercTwoRects().getLp(e5));
		assertEquals(7, ClefType.Companion.getClefPercEmptyRect().getLp(e5));
	}

}
