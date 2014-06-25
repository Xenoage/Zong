package com.xenoage.zong.musicxml;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.utils.math.Fraction;
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
public abstract class MusicXMLTestSuite {

	protected final String dir = "data/test/scores/MusicXML-TestSuite-0.1/";


	/**
	 * All pitches from G to c”” in ascending steps; First without accidentals,
	 * then with a sharp and then with a flat accidental.
	 * Double alterations and cautionary accidentals are tested at the end. 
	 */
	public abstract void test_01a_Pitches_Pitches();

	protected Pitch[] get_01a_Pitches_Pitches() {
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
	public abstract void test_01b_Pitches_Intervals();

	protected Pitch[] get_01b_Pitches_Intervals() {
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

	private Pitch incHalftoneWithEnharmonicChange(Pitch p) {
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

	private Pitch decHalftoneWithEnharmonicChange(Pitch p) {
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

	/**
	 * The {@literal <voice>} element of notes is optional in MusicXML (although Dolet always writes it out).
	 * Here, there is one note with lyrics, but without a voice assigned.
	 * It should still be correctly converted.  
	 */
	public abstract void test_01c_Pitches_NoVoiceElement();

	//TODO: not supported yet: 01d-Pitches-Microtones.xml
	//TODO: not supported yet: 01e-Pitches-ParenthesizedAccidentals.xml
	//TODO: not supported yet: 01f-Pitches-ParenthesizedMicrotoneAccidentals.xml

	/**
	 * All pitch intervals in ascending jump size. 
	 */
	public abstract void test_02a_Rests_Durations();

	protected Fraction[] get_02a_Rests_Durations() {
		//TODO: <multiple-rest> not yet supported
		return new Fraction[] {
			//first measure
			fr(1), fr(1, 2), fr(1, 4), fr(1, 8), fr(1, 16), fr(1, 32), fr(1, 64), fr(1, 128), fr(1, 128),
			//second and third measure
			fr(3, 4), fr(1, 4), fr(3, 8), fr(3, 16), fr(3, 32), fr(3, 64), fr(3, 128), fr(1, 64) /* the last dotted 128th is probably a bug in the test suite */};
	}

	//TODO: not supported yet: 02b-Rests-PitchedRests.xml
	//TODO: not supported yet: 02c-Rests-MultiMeasureRests.xml
	//not tested: 02e-Rests-NoType.xml - we ignore the type attribute anyway

	/**
	 * All note durations, from long, brevis, whole until 128th;
	 * First with their plain values, then dotted and finally doubly-dotted. 
	 */
	public abstract void test_03a_Rhythm_Durations();

	protected Fraction[] get_03a_Rhythm_Durations() {
		//TODO: <multiple-rest> not yet supported
		return new Fraction[] {
			//undotted
			fr(4), fr(2), fr(1), fr(1, 2), fr(1, 4), fr(1, 8), fr(1, 16), fr(1, 32), fr(1, 64),
			fr(1, 128),
			fr(1, 128),
			//single dotted
			fr(6), fr(3), fr(3, 2), fr(3, 4), fr(3, 8), fr(3, 16), fr(3, 32), fr(3, 64), fr(3, 128),
			fr(3, 256), fr(3, 256),
			//double dotted
			fr(7), fr(7, 2), fr(7, 4), fr(7, 8), fr(7, 16), fr(7, 32), fr(7, 64), fr(7, 128), fr(7, 256),
			fr(7, 256) };
	}
	
	/**
	 * Two voices with a backup, that does not jump to the beginning for the measure for voice 2,
	 * but somewhere in the middle. Voice 2 thus won’t have any notes or rests for the first beat
	 * of the measures. 
	 */
	public abstract void test_03b_Rhythm_Backup();

}
