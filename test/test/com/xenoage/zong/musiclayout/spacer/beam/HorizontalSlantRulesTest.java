package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Tests for {@link HorizontalSlantRules}.
 * 
 * @author Andreas Wenger
 */
public class HorizontalSlantRulesTest {
	
	@Test public void firstAndLastNoteEqualTest() {
		//positive examples from Ross, p. 115, row 1
		assertTrue(rules(5, 6, 4, 5).firstAndLastNoteEqual());
		assertFalse(rules(5, 6, 4, 6).firstAndLastNoteEqual());
	}
	
	@Test public void middleNotesNearestToBeam() {
		//positive examples from Ross, p. 115, row 2 and 3
		assertTrue(rules(Down, 7, 3, 5).middleNotesNearestToBeam());
		assertFalse(rules(Up, 7, 3, 5).middleNotesNearestToBeam());
		assertTrue(rules(Down, 8, 6, 9).middleNotesNearestToBeam());
		assertFalse(rules(Up, 8, 6, 9).middleNotesNearestToBeam());
		assertTrue(rules(Up, 1, 4, 2).middleNotesNearestToBeam());
		assertFalse(rules(Down, 1, 4, 2).middleNotesNearestToBeam());
		assertTrue(rules(Up, 3, 4, 1).middleNotesNearestToBeam());
		assertFalse(rules(Down, 3, 4, 1).middleNotesNearestToBeam());
		//positive examples from Ross, p. 115, row 5
		assertTrue(rules(Down, 10, 6, 7, 9).middleNotesNearestToBeam());
		assertFalse(rules(Up, 10, 6, 7, 9).middleNotesNearestToBeam());
		assertFalse(rules(Down, 10, 6, 11, 9).middleNotesNearestToBeam());
		//positive examples from Ross, p. 116, rows 3-6
		assertTrue(rules(Down, 12, 5, 5, 5).middleNotesNearestToBeam());
		assertTrue(rules(Down, 5, 5, 5, 12).middleNotesNearestToBeam());
		assertTrue(rules(Up, 2, 2, 2, -3).middleNotesNearestToBeam());
		assertTrue(rules(Up, -4, 1, 1, 1).middleNotesNearestToBeam());
	}
	
	@Test public void sameIntervalTest() {
		//positive examples partly from Ross, p. 115, rows 4
		assertTrue(rules(3, 1, 3, 1).sameInterval());
		assertTrue(rules(3, 10, 3, 10).sameInterval());
		assertTrue(rules(3, 10, 3, 10, 3, 10, 3, 10).sameInterval());
		assertFalse(rules(3, 1, 3, 2).sameInterval());
		assertFalse(rules(3, 1, 1, 3).sameInterval());
		assertFalse(rules(3, 10, 3, 10, 3, 10, 10, 3).sameInterval());
	}
	
	private HorizontalSlantRules rules(StemDirection stemDir, int... notesLp) {
		return new HorizontalSlantRules(notesLp, stemDir);
	}
	
	private HorizontalSlantRules rules(int... notesLp) {
		return new HorizontalSlantRules(notesLp, Up);
	}

}
