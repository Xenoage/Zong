package com.xenoage.zong.musicxml;

import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.zong.core.music.Pitch;

/**
 * Some expected results from the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * This values may be helpful for any test class, which uses the
 * MusicXML test suite.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLTestSuite {
	
	public static final String dir = "data/test/scores/MusicXML-TestSuite-0.1/";
	

	/**
	 * All pitches from G to c”” in ascending steps; First without accidentals,
	 * then with a sharp and then with a flat accidental.
	 * Double alterations and cautionary accidentals are tested at the end. 
	 */
	public static Pitch[] get_01a_Pitches_Pitches() {
		Pitch[] expectedPitches = new Pitch[24 * 4 + 6];
		int iPitch = 0;
		for (int alter : new int[] { 0, 1, -1 }) {
			Pitch nextPitch = pi('G', alter, 2);
			for (int i = 0; i < 8 * 4; i++) {
				expectedPitches[iPitch++] = nextPitch;
				int newStep = nextPitch.getStep() + 1;
				int newOctave = nextPitch.getOctave();
				if (newStep > 6) {
					newStep = 0;
					newOctave += 1;
				}
				nextPitch = pi(newStep, alter, newOctave);
			}
		}
		expectedPitches[iPitch++] = pi(0, 2, 5);
		expectedPitches[iPitch++] = pi(0, -2, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		expectedPitches[iPitch++] = pi(0, 1, 5);
		return expectedPitches;
	}
	
	
	/**
	 * All pitch intervals in ascending jump size. 
	 */
	public static Pitch[] get_01b_Pitches_Intervals() {
		Pitch[] expectedPitches = new Pitch[41 * 2];
		Pitch pi1 = pi(0, 0, 5);
		Pitch pi2 = pi(0, 0, 5);
		for (int i = 0; i < expectedPitches.length / 2; i++) {
			expectedPitches[i * 2 + 0] = pi1;
			expectedPitches[i * 2 + 1] = pi2;
			pi1 = incHalftoneWithEnharmonicChange(pi1);
			pi2 = decHalftoneWithEnharmonicChange(pi2);
		}
		return expectedPitches;
	}
	
	//not supported yet: 01d-Pitches-Microtones.xml
	
	
	private static Pitch incHalftoneWithEnharmonicChange(Pitch p) {
		int step = p.getStep();
		int alter = p.getAlter();
		int octave = p.getOctave();
		if (alter == 1) {
			//next step
			alter = -1;
			step += 1;
			if (step > 6) {
				step = 0;
				octave += 1;
			}
		}
		else {
			alter += 1;
		}
		return pi(step, alter, octave);
	}
	
	private static Pitch decHalftoneWithEnharmonicChange(Pitch p) {
		int step = p.getStep();
		int alter = p.getAlter();
		int octave = p.getOctave();
		if (alter == -1) {
			//next step
			alter = 1;
			step -= 1;
			if (step < 0) {
				step = 6;
				octave -= 1;
			}
		}
		else {
			alter -= 1;
		}
		return pi(step, alter, octave);
	}
	
}
