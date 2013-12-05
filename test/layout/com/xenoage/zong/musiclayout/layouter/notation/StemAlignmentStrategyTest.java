package com.xenoage.zong.musiclayout.layouter.notation;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.*;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.ChordFactory;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.ChordLinePositions;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Tests for {@link StemAlignmentStrategy}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class StemAlignmentStrategyTest {

	StemAlignmentStrategy strategy = new StemAlignmentStrategy();

	StemDirectionStrategy csdStrategy = new StemDirectionStrategy();
	NotesAlignmentStrategy cnaStrategy = new NotesAlignmentStrategy();


	@Test public void computeStemAlignmentTest() {
		Pitch pitch;

		pitch = pi('B', 0, 3);
		testPitch(pitch, -3, 4);

		pitch = pi('C', 0, 4);
		testPitch(pitch, -2, 5);

		pitch = pi('D', 0, 4);
		testPitch(pitch, -1, 6);

		pitch = pi('E', 0, 4);
		testPitch(pitch, 0, 7);

		pitch = pi('F', 0, 4);
		testPitch(pitch, 1, 8);

		pitch = pi('G', 0, 4);
		testPitch(pitch, 2, 9);

		pitch = pi('A', 0, 4);
		testPitch(pitch, 3, 10);

		//Stem down
		pitch = pi('B', 0, 5);
		testPitch(pitch, 11, 4);

		pitch = pi('A', 0, 5);
		testPitch(pitch, 10, 3);

		pitch = pi('G', 0, 5);
		testPitch(pitch, 9, 2);

		pitch = pi('F', 0, 5);
		testPitch(pitch, 8, 1);

		pitch = pi('E', 0, 5);
		testPitch(pitch, 7, 0);

		pitch = pi('D', 0, 5);
		testPitch(pitch, 6, -1);

		pitch = pi('C', 0, 5);
		testPitch(pitch, 5, -2);

		pitch = pi('B', 0, 4);
		testPitch(pitch, 4, -3);

		//longer stems
		pitch = pi('C', 0, 6);
		testPitch(pitch, 12, 4);

		pitch = pi('E', 0, 6);
		testPitch(pitch, 14, 4);

		pitch = pi('F', 0, 6);
		testPitch(pitch, 15, 4);

		pitch = pi('A', 0, 6);
		testPitch(pitch, 17, 4);

		pitch = pi('A', 0, 3);
		testPitch(pitch, -4, 4);

		pitch = pi('F', 0, 3);
		testPitch(pitch, -6, 4);

		pitch = pi('E', 0, 3);
		testPitch(pitch, -7, 4);

		pitch = pi('C', 0, 3);
		testPitch(pitch, -9, 4);

		//Some Chords...
		Pitch[] pitches = new Pitch[2];
		pitches[0] = pi('C', 0, 3);
		pitches[1] = pi('F', 0, 3);
		testPitch(pitches, -9, 4);

		pitches = new Pitch[2];
		pitches[0] = pi('C', 0, 3);
		pitches[1] = pi('F', 0, 4);
		;
		testPitch(pitches, -9, 8);
	}

	private void testPitch(Pitch pitch, float start, float end) {
		Pitch[] pitches = new Pitch[1];
		pitches[0] = pitch;
		testPitch(pitches, start, end);
	}

	private void testPitch(Pitch[] pitches, float start, float end) {
		MusicContext context = MusicContext.simpleInstance;
		Chord chord;
		NotesAlignment chordNotesAlignment;
		StemAlignment chordStemAlignment;

		Fraction fraction = fr(1, 1);
		chord = ChordFactory.chord(pitches, fraction);
		ChordLinePositions linepositions = new ChordLinePositions(chord, context);
		StemDirection stemDirection = csdStrategy.computeStemDirection(linepositions, 5);
		chordNotesAlignment = cnaStrategy.computeNotesAlignment(chord, stemDirection,
			ChordWidths.defaultValue, context);
		chordStemAlignment = strategy.computeStemAlignment(null, chordNotesAlignment, stemDirection, 5,
			1);
		assertEquals(start, chordStemAlignment.getStartLinePosition(), Delta.DELTA_FLOAT);
		assertEquals(end, chordStemAlignment.getEndLinePosition(), Delta.DELTA_FLOAT);
	}

}
