package com.xenoage.zong.musiclayout.notator.chord;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notator.chord.NotesNotator.notesNotator;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemNotator.stemNotator;
import static com.xenoage.zong.musiclayout.notator.chord.stem.single.SingleStemDirector.singleStemDirector;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.ChordFactory;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.notator.chord.stem.StemNotator;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Tests for {@link StemNotator}.
 * 
 * @author Andreas Wenger
 */
public class StemNotatorTest {

	StemNotator testee = stemNotator;


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
		NotesNotation chordNotesAlignment;
		StemNotation chordStemAlignment;

		Fraction fraction = fr(1, 1);
		chord = ChordFactory.chord(pitches, fraction);
		ChordLps linepositions = new ChordLps(chord, context);
		StemDirection stemDirection = singleStemDirector.compute(linepositions, 5);
		chordNotesAlignment = notesNotator.compute(chord, stemDirection,
			ChordWidths.defaultValue, context);
		chordStemAlignment = testee.compute(Stem.defaultStem, chordNotesAlignment, stemDirection, 5,
			1);
		assertEquals(start, chordStemAlignment.startLp, Delta.DELTA_FLOAT);
		assertEquals(end, chordStemAlignment.endLp, Delta.DELTA_FLOAT);
	}

}
