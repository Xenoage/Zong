package com.xenoage.zong.core.util;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1$8;
import static com.xenoage.zong.core.music.Pitch.C;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.selection.Cursor;

/**
 * Tests for {@link BeamIterator}.
 * 
 * @author Andreas Wenger
 */
public class BeamIteratorTest {
	
	
	@Test public void test() {
		BeamIterator it = new BeamIterator(createTestScore());
		//first beam
		assertTrue(it.hasNext());
		Beam beam = it.next();
		assertEquals(atBeat(0, 0, 1, Companion.get_0()), beam.getMP());
		//second beam (two staves)
		assertTrue(it.hasNext());
		beam = it.next();
		assertEquals(atBeat(0, 2, 0, Companion.get_0()), beam.getMP());
		assertEquals(atBeat(0, 2, 0, Companion.get_0()), beam.getChord(0).getMP());
		assertEquals(atBeat(1, 2, 0, Companion.get_1$8()), beam.getChord(1).getMP());
		//third chord
		assertTrue(it.hasNext());
		beam = it.next();
		assertEquals(atBeat(1, 3, 0, Companion.get_0()), beam.getMP());
		//finished
		assertFalse(it.hasNext());
	}
	
	/**
	 * Test score with 2 staves and 4 measures. In measure 0, there is a beam in
	 * staff 0 in voice 1. In measure 2, there is a beam spanning over two staves.
	 * In measure 3, there is a beam in staff 1 in voice 0.
	 */
	public static Score createTestScore() {
		Score score = new Score();
		Cursor cursor = new Cursor(score, MP.mp0, true);
		//first beam
		cursor.setMp(atElement(0, 0, 1, 0));
		cursor.openBeam();
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.closeBeam();
		//second beam
		cursor.setMp(atElement(0, 2, 0, 0));
		cursor.openBeam();
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.setMp(atElement(1, 2, 0, 0));
		cursor.write(new Rest(Companion.get_1$8()));
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.closeBeam();
		//third beam
		cursor.setMp(atElement(1, 3, 0, 0));
		cursor.openBeam();
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.write(chord(Companion.pi(Companion.getC(), 4), Companion.get_1$8()));
		cursor.closeBeam();
		return score;
	}

}
