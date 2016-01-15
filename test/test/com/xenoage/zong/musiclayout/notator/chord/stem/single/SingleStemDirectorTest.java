package com.xenoage.zong.musiclayout.notator.chord.stem.single;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notator.chord.stem.single.SingleStemDirector.singleStemDirector;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.musiclayout.notation.chord.ChordLps;

/**
 * Tests for {@link SingleStemDirector}.
 * 
 * @author Andreas Wenger
 */
public class SingleStemDirectorTest {

	SingleStemDirector testee = singleStemDirector;


	@Test public void computeStemDirectionTest() {
		int linesCount = 5;
		int[] lps;

		//single notes
		lps = new int[1];
		for (int i = -2; i <= 3; i++) {
			lps[0] = i;
			assertEquals(Up, testee.compute(clp(lps), linesCount));
		}
		for (int i = 4; i <= 13; i++) {
			lps[0] = i;
			assertEquals(Down, testee.compute(clp(lps), linesCount));
		}

		//chords with 2 notes
		//stem down
		lps = new int[2];
		lps[0] = 2;
		lps[1] = 7;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 1;
		lps[1] = 9;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 0;
		lps[1] = 9;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 3;
		lps[1] = 6;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 4;
		lps[1] = 6;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 3;
		lps[1] = 7;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 2;
		lps[1] = 8;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = -1;
		lps[1] = 11;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		//stem up
		lps[0] = -1;
		lps[1] = 6;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps[0] = 1;
		lps[1] = 5;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps[0] = 2;
		lps[1] = 4;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps[0] = 0;
		lps[1] = 7;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps[0] = -1;
		lps[1] = 8;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps[0] = -2;
		lps[1] = 9;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps[0] = -4;
		lps[1] = 9;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		//chords with more than 2 notes
		lps = new int[3];
		lps[0] = 1;
		lps[1] = 3;
		lps[2] = 6;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps[0] = 3;
		lps[1] = 6;
		lps[2] = 8;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 2;
		lps[1] = 5;
		lps[2] = 7;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps[0] = 0;
		lps[1] = 4;
		lps[2] = 7;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps = new int[4];
		lps[0] = -4;
		lps[1] = 1;
		lps[2] = 5;
		lps[3] = 8;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps = new int[5];
		lps[0] = -2;
		lps[1] = 2;
		lps[2] = 5;
		lps[3] = 10;
		lps[4] = 14;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		//chords with the same distance from top and lowest note
		lps = new int[4];
		lps[0] = 0;
		lps[1] = 4;
		lps[2] = 6;
		lps[3] = 8;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps = new int[3];
		lps[0] = 1;
		lps[1] = 5;
		lps[2] = 7;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps = new int[3];
		lps[0] = -1;
		lps[1] = 6;
		lps[2] = 9;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps = new int[5];
		lps[0] = -2;
		lps[1] = 1;
		lps[2] = 5;
		lps[3] = 8;
		lps[4] = 10;
		assertEquals(Down, testee.compute(clp(lps), linesCount));

		lps = new int[4];
		lps[0] = 0;
		lps[1] = 2;
		lps[2] = 4;
		lps[3] = 8;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps = new int[3];
		lps[0] = 1;
		lps[1] = 3;
		lps[2] = 7;
		assertEquals(Up, testee.compute(clp(lps), linesCount));

		lps = new int[5];
		lps[0] = -2;
		lps[1] = 1;
		lps[2] = 3;
		lps[3] = 8;
		lps[4] = 10;
		assertEquals(Up, testee.compute(clp(lps), linesCount));
	}

	private ChordLps clp(int[] lps) {
		return new ChordLps(lps);
	}

}
