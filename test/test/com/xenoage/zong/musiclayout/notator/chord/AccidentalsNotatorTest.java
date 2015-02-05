package com.xenoage.zong.musiclayout.notator.chord;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notations.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notator.chord.AccidentalsNotator.accidentalsNotator;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notator.chord.AccidentalsNotator;
import com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData;

/**
 * Tests for {@link AccidentalsNotator}.
 *
 * @author Andreas Wenger
 */
public class AccidentalsNotatorTest
	extends TestData {

	private AccidentalsNotator testee = accidentalsNotator;

	/**
	 * Tests some chords with no accidentals.
	 */
	@Test public void testNoAcc() {
		//C5
		AccidentalsNotation accs = testee.compute(alist(pi(0, 0, 5)),
			new NoteDisplacement[] { note(5) }, cw, contextC);
		assertEmpty(accs);
		//C4, D4, G4
		accs = testee.compute(alist(pi(0, 0, 4), pi(1, 0, 4), pi(4, 0, 4)),
			new NoteDisplacement[] { note(-2), note(-1, noteOffset, susRight), note(2) }, cw, contextC);
		assertEmpty(accs);
		//Eb4, Ab4, G##5 with contextEb
		accs = testee.compute(alist(pi(2, -1, 4), pi(5, -1, 4), pi(4, 2, 5)),
			new NoteDisplacement[] { note(0), note(3), note(9) }, cw, contextEb);
		assertEmpty(accs);
	}
	
	private void assertEmpty(AccidentalsNotation caa) {
		assertEquals(0, caa.accidentals.length);
		assertEquals(0, caa.widthIs, df);
	}

}
