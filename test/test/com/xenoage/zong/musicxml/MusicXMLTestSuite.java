package com.xenoage.zong.musicxml;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.mp0;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.UnformattedText;

/**
 * Abstract tests for the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * For specific projects (core, musicxml-in, layout, ...), subclasses of this
 * class can be created.
 * 
 * @param <T> musicxml-in tests may test a {@link MusicXMLDocument}, core tests
 *            may test a {@link Score}, and so on.
 * 
 * @author Andreas Wenger
 */
public abstract class MusicXMLTestSuite<T> {

	public final String dir = "data/test/scores/MusicXML-TestSuite-0.1/";


	/**
	 * Loads the given file or fails in JUnit.
	 */
	public abstract T load(String file);

	/**
	 * All pitches from G to c”” in ascending steps; First without accidentals,
	 * then with a sharp and then with a flat accidental.
	 * Double alterations and cautionary accidentals are tested at the end. 
	 */
	@Test public void test_01a_Pitches_Pitches() {
		T data = load("01a-Pitches-Pitches.xml");
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
		test_01a_Pitches_Pitches(data, expectedPitches);
	}

	public abstract void test_01a_Pitches_Pitches(T data, Pitch[] expectedPitches);

	/**
	 * All pitch intervals in ascending jump size. 
	 */
	@Test public void test_01b_Pitches_Intervals() {
		T data = load("01b-Pitches-Intervals.xml");
		Pitch[] expectedPitches = new Pitch[41 * 2];
		Pitch pi1 = pi(0, 0, 5);
		Pitch pi2 = pi(0, 0, 5);
		for (int i = 0; i < expectedPitches.length / 2; i++) {
			expectedPitches[i * 2 + 0] = pi1;
			expectedPitches[i * 2 + 1] = pi2;
			pi1 = incHalftoneWithEnharmonicChange(pi1);
			pi2 = decHalftoneWithEnharmonicChange(pi2);
		}
		test_01b_Pitches_Intervals(data, expectedPitches);
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

	public abstract void test_01b_Pitches_Intervals(T data, Pitch[] expectedPitches);

	/**
	 * The {@literal <voice>} element of notes is optional in MusicXML (although Dolet always writes it out).
	 * Here, there is one note with lyrics, but without a voice assigned.
	 * It should still be correctly converted.  
	 */
	@Test public void test_01c_Pitches_NoVoiceElement() {
		T data = load("01c-Pitches-NoVoiceElement.xml");
		test_01c_Pitches_NoVoiceElement(data);
	}

	public abstract void test_01c_Pitches_NoVoiceElement(T data);

	//TODO: not supported yet: 01d-Pitches-Microtones.xml
	//TODO: not supported yet: 01e-Pitches-ParenthesizedAccidentals.xml
	//TODO: not supported yet: 01f-Pitches-ParenthesizedMicrotoneAccidentals.xml

	/**
	 * All pitch intervals in ascending jump size. 
	 */
	@Test public void test_02a_Rests_Durations() {
		//TODO: <multiple-rest> not yet supported
		T data = load("02a-Rests-Durations.xml");
		Fraction[] expectedDurations = new Fraction[] {
			//undotted
			fr(1), fr(1), fr(1), fr(1, 2), fr(1, 4), fr(1, 8), fr(1, 16), fr(1, 32), fr(1, 64),
			fr(1, 128), fr(1, 128),
			//dotted
			fr(3, 4), fr(1, 4), fr(1, 4), fr(3, 8), fr(3, 16), fr(3, 32), fr(3, 64), fr(3, 128),
			fr(3, 256), fr(3, 256) };
		test_02a_Rests_Durations(data, expectedDurations);
	}
	
	public abstract void test_02a_Rests_Durations(T data, Fraction[] expectedDurations); 

	//TODO: not supported yet: 02b-Rests-PitchedRests.xml
	//TODO: not supported yet: 02c-Rests-MultiMeasureRests.xml
	//not tested: 02e-Rests-NoType.xml - we ignore the type attribute anyway

	/**
	 * All note durations, from long, brevis, whole until 128th;
	 * First with their plain values, then dotted and finally doubly-dotted. 
	 */
	@Test public void test_03a_Rhythm_Durations() {
		//TODO: <multiple-rest> not yet supported
		T data = load("03a-Rhythm-Durations.xml");
		Fraction[] expectedDurations = new Fraction[] {
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
		test_03a_Rhythm_Durations(data, expectedDurations);
	}
	
	public abstract void test_03a_Rhythm_Durations(T data, Fraction[] expectedDurations);

	/**
	 * Two voices with a backup, that does not jump to the beginning for the measure for voice 2,
	 * but somewhere in the middle. Voice 2 thus won’t have any notes or rests for the first beat
	 * of the measures. 
	 */
	@Test public void test_03b_Rhythm_Backup() {
		T data = load("03b-Rhythm-Backup.xml");
		test_03b_Rhythm_Backup(data);
	}
	
	public abstract void test_03b_Rhythm_Backup(T data);

	/**
	 * Although uncommon, the divisions of a quarter note can change somewhere in the middle of
	 * a MusicXML file. Here, the first half measure uses a division of 1, which then changes
	 * to 8 in the middle of the first measure and to 38 in the middle of the second measure. 
	 */
	@Test public void test_03c_Rhythm_DivisionChange() {
		T data = load("03c-Rhythm-DivisionChange.xml");
		Fraction[] expectedDurations = new Fraction[] {
			fr(1, 4), fr(1, 4), fr(1, 4), fr(1, 4), fr(1, 2), fr(1, 2) };
		test_03c_Rhythm_DivisionChange(data, expectedDurations);
	}

	public abstract void test_03c_Rhythm_DivisionChange(T data, Fraction[] expectedDurations);

	//TODO: not supported yet: 03d-Rhythm-DottedDurations-Factors.xml

	/**
	 * Various time signatures: 2/2 (alla breve), 4/4 (C), 2/2, 3/2, 2/4,
	 * 3/4, 4/4, 5/4, 3/8, 6/8, 12/8.
	 */
	@Test public void test_11a_TimeSignatures() {
		T data = load("11a-TimeSignatures.xml");
		TimeType[] expectedTimes = new TimeType[] {
			TimeType.timeAllaBreve, TimeType.timeCommon, TimeType.time_2_2,
			TimeType.timeType(3, 2), TimeType.time_2_4, TimeType.time_3_4, TimeType.time_4_4,
			TimeType.timeType(5, 4), TimeType.timeType(3, 8), TimeType.time_6_8, TimeType.timeType(12, 8) };
		test_11a_TimeSignatures(data, expectedTimes);
	}
	
	public abstract void test_11a_TimeSignatures(T data, TimeType[] expectedTimes);

	/**
	 * A score without a time signature (but with a key and clefs).
	 */
	@Test public void test_11b_TimeSignatures_NoTime() {
		T data = load("11b-TimeSignatures-NoTime.xml");
		test_11b_TimeSignatures_NoTime(data);
	}
	
	public abstract void test_11b_TimeSignatures_NoTime(T data);

	//TODO: not supported yet: 11c-TimeSignatures-CompoundSimple.xml
	//TODO: not supported yet: 11d-TimeSignatures-CompoundMultiple.xml
	//TODO: not supported yet: 11e-TimeSignatures-CompoundMixed.xml
	//not tested: 11f-TimeSignatures-SymbolMeaning.xml - 3/8 time with cut symbol is not supported
	//TODO: not supported yet: 11g-TimeSignatures-SingleNumber.xml

	/**
	 * Senza-misura time signature.
	 */
	@Test public void test_11h_TimeSignatures_SenzaMisura() {
		T data = load("11h-TimeSignatures-SenzaMisura.xml");
		test_11h_TimeSignatures_SenzaMisura(data);
	}
	
	public abstract void test_11h_TimeSignatures_SenzaMisura(T data);

	/**
	 * Various clefs: G, C, F, percussion, TAB and none; some are also possible
	 * with transposition and on other staff lines than their default (e.g.
	 * soprano/alto/tenor/baritone C clefs); Each measure shows a different clef
	 * (measure 17 has the "none" clef), only measure 18 has the same treble clef
	 * as measure 1. 
	 */
	@Test public void test_12a_Clefs() {
		T data = load("12a-Clefs.xml");
		test_12a_Clefs(data);
	}
	
	public abstract void test_12a_Clefs(T data);

	/**
	 * A score without any key or clef defined. The default (4/4 in treble clef) should be used. 
	 */
	@Test public void test_12b_Clefs_NoKeyOrClef() {
		T data = load("12b-Clefs-NoKeyOrClef.xml");
		test_12b_Clefs_NoKeyOrClef(data);
	}
	
	public abstract void test_12b_Clefs_NoKeyOrClef(T data);

	/**
	 * Various key signature: from 11 flats to 11 sharps (each one first one measure in major,
	 * then one measure in minor).
	 */
	@Test public void test_13a_KeySignatures() {
		//TODO: Zong! supports only -7 to +7
		T data = load("13a-KeySignatures.xml");
		TraditionalKey[] expectedKeys = new TraditionalKey[15 * 2];
		for (int fifths = -7; fifths <= 7; fifths++) {
			expectedKeys[(fifths + 7) * 2 + 0] = new TraditionalKey(fifths, Mode.Major);
			expectedKeys[(fifths + 7) * 2 + 1] = new TraditionalKey(fifths, Mode.Minor);
		}
		test_13a_KeySignatures(data, expectedKeys);
	}
	
	protected abstract void test_13a_KeySignatures(T data, TraditionalKey[] expectedKeys);

	/**
	 * All different modes: major, minor, ionian, dorian, phrygian, lydian, mixolydian,
	 * aeolian, and locrian; All modes are given with 2 sharps.
	 */
	@Test public void test_13b_KeySignatures_ChurchModes() {
		T data = load("13b-KeySignatures-ChurchModes.xml");
		TraditionalKey[] expectedKeys = new TraditionalKey[9];
		for (int i : range(expectedKeys)) {
			expectedKeys[i] = new TraditionalKey(2, Mode.values()[i]);
		}
		test_13b_KeySignatures_ChurchModes(data, expectedKeys);
	}
	
	protected abstract void test_13b_KeySignatures_ChurchModes(T data, TraditionalKey[] expectedKeys);

	//TODO: not supported yet: 13c-KeySignatures-NonTraditional.xml
	//TODO: not supported yet: 13d-KeySignatures-Microtones.xml
	//TODO: not supported yet: 14a-StaffDetails-LineChanges.xml

	/**
	 * One simple chord consisting of two notes.
	 */
	@Test public void test_21a_Chord_Basic() {
		T data = load("21a-Chord-Basic.xml");
		Chord expectedChord = ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4));
		test_21a_Chord_Basic(data, expectedChord);
	}
	
	private Chord ch(Fraction duration, Pitch... pitches) {
		ArrayList<Note> notes = alist();
		for (Pitch pitch : pitches)
			notes.add(new Note(pitch));
		return new Chord(notes, duration);
	}
	
	protected abstract void test_21a_Chord_Basic(T data, Chord expectedChord);
	
	/**
	 * Some subsequent (identical) two-note chords.
	 */
	@Test public void test_21b_Chords_TwoNotes() {
		T data = load("21b-Chords-TwoNotes.xml");
		Chord expectedChord = ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4));
		test_21b_Chords_TwoNotes(data, 8, expectedChord);
	}
	
	protected abstract void test_21b_Chords_TwoNotes(T data, int expectedChordsCount, Chord expectedChord);

	/**
	 * Some three-note chords, with various durations.
	 */
	@Test public void test_21c_Chords_ThreeNotesDuration() {
		T data = load("21c-Chords-ThreeNotesDuration.xml");
		Chord[] expectedChords = new Chord[] {
			ch(fr(3, 8), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
			ch(fr(1, 8), pi('A', 0, 4), pi('G', 0, 5)),
			ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
			ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
			ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('E', 0, 5)),
			ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('F', 0, 5)),
			ch(fr(1, 2), pi('F', 0, 4), pi('A', 0, 4), pi('D', 0, 5))
		};
		test_21c_Chords_ThreeNotesDuration(data, expectedChords);
	}
	
	public abstract void test_21c_Chords_ThreeNotesDuration(T data, Chord[] expectedChords);
	
	/**
	 * Chords in the second measure, after several ornaments in the first measure
	 * and a p at the beginning of the second measure. 
	 */
	@Test public void test_21d_Chords_SchubertStabatMater() {
		T data = load("21d-Chords-SchubertStabatMater.xml");
		//chords
		Chord[] expectedChords = new Chord[] {
			ch(fr(1, 1), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
			ch(fr(3, 8), pi('F', 0, 4), pi('A', -1, 4)),
			ch(fr(1, 8), pi('F', 0, 4), pi('A', -1, 4)),
			ch(fr(1, 4), pi('G', 0, 4), pi('B', -1, 4)),
			ch(fr(1, 4), pi('G', 0, 4), pi('B', -1, 4))
		};
		addAnnotation(expectedChords[0], new Articulation(ArticulationType.Accent, Placement.Below));
		addAnnotation(expectedChords[0], new Fermata(Placement.Above));
		//directions
		List<Tuple2<MP, ? extends Direction>> expectedDirections = alist();
		Words largo = new Words(new UnformattedText("Largo"));
		largo.setPositioning(Placement.Above);
		expectedDirections.add(t(mp0, largo));
		Dynamics fp = new Dynamics(DynamicsType.fp);
		fp.setPositioning(Placement.Below);
		expectedDirections.add(t(mp0, fp));
		Dynamics p = new Dynamics(DynamicsType.p);
		fp.setPositioning(Placement.Below);
		expectedDirections.add(t(mp0.withMeasure(1), p));
		//test
		test_21d_Chords_SchubertStabatMater(data, expectedChords, expectedDirections);
	}
	
	private void addAnnotation(Chord chord, Annotation a) {
		if (chord.getAnnotations() == null)
			chord.setAnnotations(new ArrayList<Annotation>(1));
		chord.getAnnotations().add(a);
	}
	
	public abstract void test_21d_Chords_SchubertStabatMater(T data, Chord[] expectedChords,
		List<Tuple2<MP, ? extends Direction>> expectedDirections);
	
	/**
	 * Check for proper chord detection after a pickup measure (i.e. the first beat of the measure is
	 * not aligned with multiples of the time signature)! 
	 */
	@Test public void test_21e_Chords_PickupMeasures() {
		T data = load("21e-Chords-PickupMeasures.xml");
		test_21e_Chords_PickupMeasures(data);
	}
	
	public abstract void test_21e_Chords_PickupMeasures(T data);
	
	/**
	 * Between the individual notes of a chord there can be direction or harmony elements,
	 * which should be properly assigned to the chord (or the position of the chord). 
	 */
	@Test public void test_21f_Chord_ElementInBetween() {
		T data = load("21f-Chord-ElementInBetween.xml");
		test_21f_Chord_ElementInBetween(data);
	}
	
	public abstract void test_21f_Chord_ElementInBetween(T data);
	
	//TODO: not supported yet: 22a-Noteheads.xml
	//TODO: not supported yet: 22b-Staff-Notestyles.xml
	//TODO: not supported yet: 22c-Noteheads-Chords.xml
	//TODO: not supported yet: 22d-Parenthesized-Noteheads.xml
	
	/**
	 * Some tuplets (3:2, 3:2, 3:2, 4:2, 4:1, 7:3, 6:2) with the default tuplet bracket displaying the
	 * number of actual notes played. The second tuplet does not have a number attribute set. 
	 */
	@Test public void test_23a_Tuplets() {
		T data = load("23a-Tuplets.xml");
		Fraction[] expectedDurations = new Fraction[] {
			fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6),
			fr(1, 8), fr(1, 8), fr(1, 8), fr(1, 8), fr(1, 16), fr(1, 16), fr(1, 16), fr(1, 16),
			fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28),
			fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12)
		};
		test_23a_Tuplets(data, expectedDurations);
	}
	
	public abstract void test_23a_Tuplets(T data, Fraction[] expectedDurations);
	
	//TODO: not yet supported: 23b-Tuplets-Styles.xml
	//TODO: not yet supported: 23c-Tuplet-Display-NonStandard.xml 
	//TODO: not supported yet: 23d-Tuplets-Nested.xml
	//TODO: not supported yet: 23e-Tuplets-Tremolo.xml
	
	/**
	 * Some "triplets" on the end of the first and in the second staff, using only
	 * {@literal <time-modification>}, but not explicit tuplet bracket. Thus, the duration of the
	 * notes in the second staff should be scaled properly in comparison to staff 1,
	 * but no visual indication about the tuplets is given. 
	 */
	@Test public void test_23f_Tuplets_DurationButNoBracket() {
		T data = load("23f-Tuplets-DurationButNoBracket.xml");
		Fraction[] expectedDurationsStaff1 = new Fraction[] {
			fr(1, 4), fr(1, 4), fr(1, 6), fr(1, 6), fr(1, 6)
		};
		Fraction[] expectedDurationsStaff2 = new Fraction[] {
			fr(1, 8), fr(1, 8), fr(1, 12), fr(1, 12), fr(1, 12),
			fr(1, 16), fr(1, 16), fr(1, 16), fr(1, 16),
			fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24)
		};
		test_23f_Tuplets_DurationButNoBracket(data, expectedDurationsStaff1, expectedDurationsStaff2);
	}
	
	//TODO: add layout test for this case. notes must be properly aligned.
	public abstract void test_23f_Tuplets_DurationButNoBracket(T data,
		Fraction[] expectedDurationsStaff1, Fraction[] expectedDurationsStaff2);
	
	//TODO: 24a-GraceNotes.xml (60 min) (partly supported) 
	//TODO: 24b-ChordAsGraceNote.xml (30 min) 
	//TODO: 24c-GraceNote-MeasureEnd.xml (30 min) 
	//TODO: 24d-AfterGrace.xml (30 min) 
	
	//TODO: not supported yet: 24e-GraceNote-StaffChange.xml
	
	//TODO: 24f-GraceNote-Slur.xml (30 min) 
	//TODO: 31a-Directions.xml (60 min) (partly supported) 
	
	//TODO: not supported yet: 31c-MetronomeMarks.xml
	
	//TODO: 32a-Notations.xml (60 min) (partly supported) 
	//TODO: 32b-Articulations-Texts.xml (30 min) (partly supported) 
	//TODO: 32c-MultipleNotationChildren.xml (30 min) 
	
	//TODO: not supported yet: 32d-Arpeggio.xml
	//TODO: not supported yet: 33a-Spanners.xml
	
	//TODO: 33b-Spanners-Tie.xml (15 min) 
	//TODO: 33c-Spanners-Slurs.xml (30 min)
	
	//TODO: not supported yet: 33d-Spanners-OctaveShifts.xml
	//TODO: not supported yet: 33e-Spanners-OctaveShifts-InvalidSize.xml
	//TODO: not supported yet: 33f-Trill-EndingOnGraceNote.xml
	
	//TODO: 33g-Slur-ChordedNotes.xml (30 min) 
	
	//TODO: not supported yet: 33h-Spanners-Glissando.xml
	
	//TODO: 33i-Ties-NotEnded.xml (45 min) 
	//TODO: 41a-MultiParts-Partorder.xml (30 min) 
	//TODO: 41b-MultiParts-MoreThan10.xml (15 min) 
	//TODO: 41c-StaffGroups.xml (60 min) 
	//TODO: 41d-StaffGroups-Nested.xml (15 min) 
	//TODO: 41e-StaffGroups-InstrumentNames-Linebroken.xml (30 min) 
	//TODO: 41f-StaffGroups-Overlapping.xml (30 min) 
	//TODO: 41g-PartNoId.xml (15 min) 
	//TODO: 41h-TooManyParts.xml (15 min) 
	//TODO: 41i-PartNameDisplay-Override.xml (30 min) 
	//TODO: 42a-MultiVoice-TwoVoicesOnStaff-Lyrics.xml (60 min) 
	//TODO: 42b-MultiVoice-MidMeasureClefChange.xml (60 min) 
	//TODO: 43a-PianoStaff.xml (15 min) 
	
	//TODO: not supported yet: 43b-MultiStaff-DifferentKeys.xml
	//TODO: not supported yet: 43c-MultiStaff-DifferentKeysAfterBackup.xml
	
	//TODO: 43d-MultiStaff-StaffChange.xml (60 min) 
	//TODO: 43e-Multistaff-ClefDynamics.xml (60 min) 
	//TODO: 45a-SimpleRepeat.xml (30 min) 
	//TODO: 45b-RepeatWithAlternatives.xml (30 min) 
	//TODO: 45c-RepeatMultipleTimes.xml (30 min) 
	//TODO: 45d-Repeats-Nested-Alternatives.xml (30 min) 
	//TODO: 45e-Repeats-Nested-Alternatives.xml (45 min) 
	//TODO: 45f-Repeats-InvalidEndings.xml (30 min) 
	//TODO: 45g-Repeats-NotEnded.xml (15 min) 
	//TODO: 46a-Barlines.xml (30 min) 
	//TODO: 46b-MidmeasureBarline.xml (30 min) 
	//TODO: 46c-Midmeasure-Clef.xml (15 min) 
	//TODO: 46d-PickupMeasure-ImplicitMeasures.xml (15 min) 
	//TODO: 46e-PickupMeasure-SecondVoiceStartsLater.xml (15 min) 
	//TODO: 46f-IncompleteMeasures.xml (15 min)
	
	//TODO: not supported yet: 46g-PickupMeasure-Chordnames-FiguredBass.xml
	
	//TODO: 51b-Header-Quotes.xml (15 min) 
	//TODO: 51c-MultipleRights.xml (15 min) 
	//TODO: 51d-EmptyTitle.xml (15 min) 
	//TODO: 52a-PageLayout.xml (30 min) 
	//TODO: 52b-Breaks.xml (15 min) 
	//TODO: 61a-Lyrics.xml (30 min) 
	//TODO: 61b-MultipleLyrics.xml (30 min)
	//TODO: 61c-Lyrics-Pianostaff.xml (30 min) 
	//TODO: 61d-Lyrics-Melisma.xml (30 min) 
	//TODO: 61e-Lyrics-Chords.xml (15 min) 
	//TODO: 61f-Lyrics-GracedNotes.xml (30 min) 
	//TODO: 61g-Lyrics-NameNumber.xml (30 min) 
	//TODO: 61h-Lyrics-BeamsMelismata.xml (30 min) 
	//TODO: 61i-Lyrics-Chords.xml (15 min) 
	//TODO: 61j-Lyrics-Elisions.xml (30 min) 
	//TODO: 61k-Lyrics-SpannersExtenders.xml (30 min)
	
	//TODO: not supported yet: 71a-Chordnames.xml
	//TODO: not supported yet: 71c-ChordsFrets.xml
	//TODO: not supported yet: 71d-ChordsFrets-Multistaff.xml
	//TODO: not supported yet: 71e-TabStaves.xml
	//TODO: not supported yet: 71f-AllChordTypes.xml
	//TODO: not supported yet: 71g-MultipleChordnames.xml
	
	//TODO: 72a-TransposingInstruments.xml (60 min)
	//TODO: 72b-TransposingInstruments-Full.xml (30 min)
	//TODO: 72c-TransposingInstruments-Change.xml (60 min)
	//TODO: 73a-Percussion.xml (60 min)
	
	//TODO: not supported yet: 74a-FiguredBass.xml
	//TODO: not supported yet: 75a-AccordionRegistrations.xml
	
	//TODO: 90a-Compressed-MusicXML.mxl (30 min)
	
	//irrelevant: 99a-Sibelius5-IgnoreBeaming.xml
	//irrelevant: 99b-Lyrics-BeamsMelismata-IgnoreBeams.xml
	
	
}
