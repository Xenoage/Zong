package musicxmltestsuite;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChords;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.mp0;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.beam.BeamAdd;
import com.xenoage.zong.commands.core.music.slur.SlurAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.direction.Coda;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.direction.Pedal;
import com.xenoage.zong.core.music.direction.Pedal.Type;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeEnd;
import com.xenoage.zong.core.music.direction.WedgeType;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.UnformattedText;
import com.xenoage.zong.musicxml.MusicXMLDocument;

/**
 * Abstract tests for the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * For specific projects (core, musicxml-in, layout, ...), subclasses of this
 * class can be created. Tests can be overwritten, but if not, just the loading of
 * the file is checked.
 * 
 * @param <T> musicxml-in tests may test a {@link MusicXMLDocument}, core tests
 *            may test a {@link Score}, and so on.
 * 
 * @author Andreas Wenger
 */
public abstract class MusicXMLTestSuiteBase<T> {

	public final String dir = "data/test/scores/MusicXML-TestSuite-0.1/";

	/**
	 * The filename of the current test.
	 */
	@Getter private String file;
	
	/**
	 * The loaded data of the current test.
	 */
	@Getter private T data;

	/**
	 * Loads the given file or fails in JUnit.
	 */
	public abstract T loadData(String file);
	
	/**
	 * Prepares the given test.
	 * Call this method at the beginning of each test to
	 * load the required data.
	 */
	private void loadTest(String file) {
		this.file = file;
		this.data = loadData(file);
	}

	/**
	 * The {@literal <voice>} element of notes is optional in MusicXML (although Dolet always writes it out).
	 * Here, there is one note with lyrics, but without a voice assigned.
	 * It should still be correctly converted.  
	 */
	@Test public void test_01c() {
		loadTest("01c-Pitches-NoVoiceElement.xml");
	}

	//TODO: not supported yet: 01d-Pitches-Microtones.xml
	//TODO: not supported yet: 01e-Pitches-ParenthesizedAccidentals.xml
	//TODO: not supported yet: 01f-Pitches-ParenthesizedMicrotoneAccidentals.xml

	/**
	 * All pitch intervals in ascending jump size. 
	 */
	@Test public void test_02a() {
		//TODO: <multiple-rest> not yet supported
		loadTest("02a-Rests-Durations.xml");
	}
	
	@Getter private Fraction[] _02a_Durations = {
		//undotted
		fr(1), fr(1), fr(1), fr(1, 2), fr(1, 4), fr(1, 8), fr(1, 16), fr(1, 32), fr(1, 64),
		fr(1, 128), fr(1, 128),
		//dotted
		fr(3, 4), fr(1, 4), fr(1, 4), fr(3, 8), fr(3, 16), fr(3, 32), fr(3, 64), fr(3, 128),
		fr(3, 256), fr(3, 256) };

	//TODO: not supported yet: 02b-Rests-PitchedRests.xml
	//TODO: not supported yet: 02c-Rests-MultiMeasureRests.xml
	//not tested: 02e-Rests-NoType.xml - we ignore the type attribute anyway

	/**
	 * All note durations, from long, brevis, whole until 128th;
	 * First with their plain values, then dotted and finally doubly-dotted. 
	 */
	@Test public void test_03a() {
		//TODO: <multiple-rest> not yet supported
		loadTest("03a-Rhythm-Durations.xml");
	}
	
	@Getter private Fraction[] _03a_Durations = {
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

	/**
	 * Two voices with a backup, that does not jump to the beginning for the measure for voice 2,
	 * but somewhere in the middle. Voice 2 thus won’t have any notes or rests for the first beat
	 * of the measures. 
	 */
	@Test public void test_03b() {
		loadTest("03b-Rhythm-Backup.xml");
	}

	/**
	 * Although uncommon, the divisions of a quarter note can change somewhere in the middle of
	 * a MusicXML file. Here, the first half measure uses a division of 1, which then changes
	 * to 8 in the middle of the first measure and to 38 in the middle of the second measure. 
	 */
	@Test public void test_03c() {
		loadTest("03c-Rhythm-DivisionChange.xml");
	}
	
	@Getter private Fraction[] _03c_Durations = new Fraction[] {
		fr(1, 4), fr(1, 4), fr(1, 4), fr(1, 4), fr(1, 2), fr(1, 2) };

	//TODO: not supported yet: 03d-Rhythm-DottedDurations-Factors.xml

	/**
	 * Various time signatures: 2/2 (alla breve), 4/4 (C), 2/2, 3/2, 2/4,
	 * 3/4, 4/4, 5/4, 3/8, 6/8, 12/8.
	 */
	@Test public void test_11a() {
		loadTest("11a-TimeSignatures.xml");
	}
	
	@Getter private TimeType[] _11a_Times = {
		TimeType.timeAllaBreve, TimeType.timeCommon, TimeType.time_2_2,
		TimeType.timeType(3, 2), TimeType.time_2_4, TimeType.time_3_4, TimeType.time_4_4,
		TimeType.timeType(5, 4), TimeType.timeType(3, 8), TimeType.time_6_8, TimeType.timeType(12, 8) };

	/**
	 * A score without a time signature (but with a key and clefs).
	 */
	@Test public void test_11b() {
		loadTest("11b-TimeSignatures-NoTime.xml");
	}

	//TODO: not supported yet: 11c-TimeSignatures-CompoundSimple.xml
	//TODO: not supported yet: 11d-TimeSignatures-CompoundMultiple.xml
	//TODO: not supported yet: 11e-TimeSignatures-CompoundMixed.xml
	//not tested: 11f-TimeSignatures-SymbolMeaning.xml - 3/8 time with cut symbol is not supported
	//TODO: not supported yet: 11g-TimeSignatures-SingleNumber.xml

	/**
	 * Senza-misura time signature.
	 */
	@Test public void test_11h() {
		loadTest("11h-TimeSignatures-SenzaMisura.xml");
	}

	/**
	 * Various clefs: G, C, F, percussion, TAB and none; some are also possible
	 * with transposition and on other staff lines than their default (e.g.
	 * soprano/alto/tenor/baritone C clefs); Each measure shows a different clef
	 * (measure 17 has the "none" clef), only measure 18 has the same treble clef
	 * as measure 1. 
	 */
	@Test public void test_12a() {
		loadTest("12a-Clefs.xml");
	}

	/**
	 * A score without any key or clef defined. The default (4/4 in treble clef) should be used. 
	 */
	@Test public void test_12b() {
		loadTest("12b-Clefs-NoKeyOrClef.xml");
	}

	/**
	 * Various key signature: from 11 flats to 11 sharps (each one first one measure in major,
	 * then one measure in minor).
	 */
	@Test public void test_13a() {
		//TODO: Zong! supports only -7 to +7
		loadTest("13a-KeySignatures.xml");
	}
	
	public TraditionalKey[] get_13a_Keys() {
		TraditionalKey[] expectedKeys = new TraditionalKey[15 * 2];
		for (int fifths = -7; fifths <= 7; fifths++) {
			expectedKeys[(fifths + 7) * 2 + 0] = new TraditionalKey(fifths, Mode.Major);
			expectedKeys[(fifths + 7) * 2 + 1] = new TraditionalKey(fifths, Mode.Minor);
		}
		return expectedKeys;
	}

	/**
	 * All different modes: major, minor, ionian, dorian, phrygian, lydian, mixolydian,
	 * aeolian, and locrian; All modes are given with 2 sharps.
	 */
	@Test public void test_13b() {
		loadTest("13b-KeySignatures-ChurchModes.xml");
	}
	
	public TraditionalKey[] get_13b_Keys() {
		TraditionalKey[] expectedKeys = new TraditionalKey[9];
		for (int i : range(expectedKeys)) {
			expectedKeys[i] = new TraditionalKey(2, Mode.values()[i]);
		}
		return expectedKeys;
	}

	//TODO: not supported yet: 13c-KeySignatures-NonTraditional.xml
	//TODO: not supported yet: 13d-KeySignatures-Microtones.xml
	//TODO: not supported yet: 14a-StaffDetails-LineChanges.xml

	/**
	 * One simple chord consisting of two notes.
	 */
	@Test public void test_21a() {
		loadTest("21a-Chord-Basic.xml");
	}
	
	@Getter private Chord _21a_Chord = ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4));
	
	private Chord ch(Fraction duration, Pitch... pitches) {
		ArrayList<Note> notes = alist();
		for (Pitch pitch : pitches)
			notes.add(new Note(pitch));
		return new Chord(notes, duration);
	}
	
	/**
	 * Some subsequent (identical) two-note chords.
	 */
	@Test public void test_21b() {
		loadTest("21b-Chords-TwoNotes.xml");
	}
	
	@Getter private int _21b_ChordsCount = 8;
	@Getter private Chord _21b_Chord = ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4));

	/**
	 * Some three-note chords, with various durations.
	 */
	@Test public void test_21c() {
		loadTest("21c-Chords-ThreeNotesDuration.xml");
	}
	
	@Getter private Chord[] _21c_Chords = {
		ch(fr(3, 8), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
		ch(fr(1, 8), pi('A', 0, 4), pi('G', 0, 5)),
		ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
		ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
		ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('E', 0, 5)),
		ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('F', 0, 5)),
		ch(fr(1, 2), pi('F', 0, 4), pi('A', 0, 4), pi('D', 0, 5))
	};
	
	/**
	 * Chords in the second measure, after several ornaments in the first measure
	 * and a p at the beginning of the second measure. 
	 */
	@Test public void test_21d() {
		loadTest("21d-Chords-SchubertStabatMater.xml");
	}
	
	public Chord[] get_21d_Chords() {
		Chord[] expectedChords = {
			ch(fr(1, 1), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
			ch(fr(3, 8), pi('F', 0, 4), pi('A', -1, 4)),
			ch(fr(1, 8), pi('F', 0, 4), pi('A', -1, 4)),
			ch(fr(1, 4), pi('G', 0, 4), pi('B', -1, 4)),
			ch(fr(1, 4), pi('G', 0, 4), pi('B', -1, 4))
		};
		addAnnotation(expectedChords[0], new Articulation(ArticulationType.Accent, Placement.Below));
		addAnnotation(expectedChords[0], new Fermata(Placement.Above));
		return expectedChords;
	}
	
	public List<Tuple2<MP, ? extends Direction>> get_21d_Directions() {
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
		return expectedDirections;
	}
	
	private void addAnnotation(Chord chord, Annotation a) {
		if (chord.getAnnotations() == null)
			chord.setAnnotations(new ArrayList<Annotation>(1));
		chord.getAnnotations().add(a);
	}
	
	/**
	 * Check for proper chord detection after a pickup measure (i.e. the first beat of the measure is
	 * not aligned with multiples of the time signature)! 
	 */
	@Test public void test_21e() {
		loadTest("21e-Chords-PickupMeasures.xml");
	}
	
	/**
	 * Between the individual notes of a chord there can be direction or harmony elements,
	 * which should be properly assigned to the chord (or the position of the chord). 
	 */
	@Test public void test_21f() {
		loadTest("21f-Chord-ElementInBetween.xml");
	}
	
	//TODO: not supported yet: 22a-Noteheads.xml
	//TODO: not supported yet: 22b-Staff-Notestyles.xml
	//TODO: not supported yet: 22c-Noteheads-Chords.xml
	//TODO: not supported yet: 22d-Parenthesized-Noteheads.xml
	
	/**
	 * Some tuplets (3:2, 3:2, 3:2, 4:2, 4:1, 7:3, 6:2) with the default tuplet bracket displaying the
	 * number of actual notes played. The second tuplet does not have a number attribute set. 
	 */
	@Test public void test_23a() {
		loadTest("23a-Tuplets.xml");
	}
	
	@Getter private Fraction[] _23a_Durations = {
		fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6), fr(1, 6),
		fr(1, 8), fr(1, 8), fr(1, 8), fr(1, 8), fr(1, 16), fr(1, 16), fr(1, 16), fr(1, 16),
		fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28), fr(3, 28),
		fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12), fr(1, 12)
	};
	
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
	@Test public void test_23f() {
		loadTest("23f-Tuplets-DurationButNoBracket.xml");
		//TODO: add layout test for this case. notes must be properly aligned.
	}
	
	@Getter private Fraction[][] _23f_Durations = {
		{ //staff 0
			fr(1, 4), fr(1, 4), fr(1, 6), fr(1, 6), fr(1, 6)
		},
		{ //staff 1
			fr(1, 8), fr(1, 8), fr(1, 12), fr(1, 12), fr(1, 12),
			fr(1, 16), fr(1, 16), fr(1, 16), fr(1, 16),
			fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24), fr(1, 24)
		}
	};
	
	/**
	 * Different kinds of grace notes: acciaccatura, appoggiatura; beamed grace notes;
	 * grace notes with accidentals; different durations of the grace notes. 
	 */
	@Test public void test_24a() {
		loadTest("24a-GraceNotes.xml");
	}
	
	public Chord[] get_24a_Chords() {
		//[b]eamed chords [s]tart and [e]nd
		Chord b1s, b1e, b2s, b2e;
		Chord[] expectedChords = new Chord[] {
			//measure 0
			gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			b1s = gr(fr(1, 16), false, pi('E', 0, 5)),
			b1e = gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 8), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			//measure 1
			gr(fr(1, 16), true, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			b2s = gr(fr(1, 16), false, pi('E', 0, 5)),
			b2e = gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(2, 4), pi('C', 0, 5)),
			gr(fr(1, 16), true, pi('D', 0, 5)),
			ch(fr(1, 8), pi('C', 0, 5)),
			gr(fr(1, 16), true, pi('D', 0, 5)),
			ch(fr(1, 8), pi('C', 0, 5)),
			gr(fr(1, 16), false, pi('E', 0, 5)),
			//measure 2
			gr(fr(1, 16), false, pi('E', 0, 5)),
			ch(fr(1, 4), pi('F', 0, 4), pi('C', 0, 5)),
			gr(fr(1, 4), false, pi('D', 1, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 4), false, pi('D', -1, 5)),
			gr(fr(1, 4), false, pi('A', -1, 4)),
			ch(fr(1, 4), pi('C', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5))
		};
		new BeamAdd(beamFromChords(alist(b1s, b1e))).execute();
		new BeamAdd(beamFromChords(alist(b2s, b2e))).execute();
		return expectedChords;
	}
	
	/**
	 * Sets the given chord to a grace chord.
	 */
	private Chord gr(Fraction graceDuration, boolean slash, Pitch... pitches) {
		Grace grace = new Grace(slash, graceDuration);
		ArrayList<Note> notes = alist();
		for (Pitch pitch : pitches)
			notes.add(new Note(pitch));
		return new Chord(notes, grace);
	}
	
	/**
	 * Chords as grace notes.
	 */
	@Test public void test_24b() {
		loadTest("24b-ChordAsGraceNote.xml");
	}
	
	public Chord[] get_24b_Chords() {
		return new Chord[] {
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 8), true, pi('D', 0, 5), pi('F', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 8), true, pi('B', 0, 4), pi('D', 0, 5)),
			ch(fr(1, 4), pi('A', 0, 4), pi('C', 0, 5))};
	}
	
	/**
	 * A grace note that appears at the measure end (without any steal-from-* attribute set).
	 * Some applications need to convert this into an after-grace. 
	 */
	@Test public void test_24c() {
		loadTest("24c-GraceNote-MeasureEnd.xml");
	}
	
	public Chord[] get_24c_Chords() {
		Chord[] ret = {
			ch(fr(2, 4), pi('E', 0, 5)),
			ch(fr(2, 4), pi('E', 0, 5)),
			gr(fr(1, 16), false, pi('G', 0, 5)),
			gr(fr(1, 16), false, pi('A', 0, 5))};
		new BeamAdd(beamFromChords(alist(ret[2], ret[3]))).execute();
		return ret;
	}
	
	/**
	 * Some grace notes and after-graces (indicated by steal-time-previous
	 * and steal-time-following). 
	 */
	@Test public void test_24d() {
		loadTest("24d-AfterGrace.xml");
		//TODO: concept of steal-time-previous and steal-time-following is not implemented yet
	}
	
	public Chord[] get_24d_Chords() {
		Chord[] ret = {
			ch(fr(2, 4), pi('E', 0, 5)),
			gr(fr(1, 16), false, pi('G', 0, 5)),
			gr(fr(1, 16), false, pi('A', 0, 5)),
			gr(fr(1, 16), false, pi('A', 0, 5)),
			ch(fr(2, 4), pi('E', 0, 5)),
			gr(fr(1, 16), false, pi('G', 0, 5)),
			gr(fr(1, 16), false, pi('A', 0, 5))};
		new BeamAdd(beamFromChords(alist(ret[4], ret[5]))).execute();
		return ret;
	}
	
	/**
	 * A grace note on a different staff than the actual note.
	 */
	@Test public void test_24e() {
		loadTest("24e-GraceNote-StaffChange.xml");
	}
	
	public Chord[][] get_24e_StavesChords() {
		Chord[][] ret = {
			{ //staff 0
				ch(fr(2, 4), pi('E', 0, 5)),
				ch(fr(2, 4), pi('E', 0, 5))
			},
			{ //staff 1
				gr(fr(1, 16), false, pi('G', 0, 5)),
				gr(fr(1, 16), false, pi('A', 0, 5))
			}};
		new BeamAdd(beamFromChords(alist(ret[0][0], ret[0][1]))).execute();
		return ret;
	}
	
	public Fraction[][] get_24e_StavesBeats() {
		return new Fraction[][] {
			{ _0, fr(2, 4) }, //staff 0
			{ fr(2, 4), fr(2, 4) }, //staff 1
		};
	}
	
	/**
	 * A grace note with a slur to the actual note. This can be interpreted
	 * as acciaccatura or appoggiatura, depending on the existence of a slash. 
	 */
	@Test public void test_24f() {
		loadTest("24f-GraceNote-Slur.xml");
	}
	
	public Chord[] get_24f_Chords() {
		Chord[] ret = {
			ch(fr(2, 4), pi('E', 0, 5)),
			gr(fr(1, 16), false, pi('G', 0, 5)),
			ch(fr(2, 4), pi('E', 0, 5))};
		new SlurAdd(slur(ret[1], ret[2])).execute();
		return ret;
	}
		
	/**
	 * Creates a slur for the given chords (to the first notes).
	 */
	private Slur slur(Chord start, Chord end) {
		return new Slur(SlurType.Slur, new SlurWaypoint(start, 0, null),
			new SlurWaypoint(end, 0, null), null);
	}
	
	/**
	 * All {@literal <direction>} elements defined in MusicXML.
	 * The lyrics for each note describes the direction element assigned to that note. 
	 */
	@Test public void test_31a() {
		loadTest("31a-Directions.xml");
	}
	
	public List<Tuple2<MP, ?>> get_31a_Directions() {
		//TODO: not all directions are supported yet. return only those which are supported now
		Wedge w1, w2;
		return alist(
			//TODO: 4 rehearsal marks
			t(atBeat(0, 1, 0, fr(0, 4)), new Segno()),
			t(atBeat(0, 1, 0, fr(1, 4)), new Coda()),
			t(atBeat(0, 1, 0, fr(2, 4)), new Words(new UnformattedText("words"))),
			//TODO: eyeglasses
			t(atBeat(0, 2, 0, fr(0, 4)), new Dynamics(DynamicsType.p)),
			t(atBeat(0, 2, 0, fr(1, 4)), new Dynamics(DynamicsType.pp)),
			t(atBeat(0, 2, 0, fr(2, 4)), new Dynamics(DynamicsType.ppp)),
			t(atBeat(0, 2, 0, fr(3, 4)), new Dynamics(DynamicsType.pppp)),
			t(atBeat(0, 3, 0, fr(0, 4)), new Dynamics(DynamicsType.ppppp)),
			t(atBeat(0, 3, 0, fr(1, 4)), new Dynamics(DynamicsType.pppppp)),
			t(atBeat(0, 3, 0, fr(2, 4)), new Dynamics(DynamicsType.f)),
			t(atBeat(0, 3, 0, fr(3, 4)), new Dynamics(DynamicsType.ff)),
			t(atBeat(0, 4, 0, fr(0, 4)), new Dynamics(DynamicsType.fff)),
			t(atBeat(0, 4, 0, fr(1, 4)), new Dynamics(DynamicsType.ffff)),
			t(atBeat(0, 4, 0, fr(2, 4)), new Dynamics(DynamicsType.fffff)),
			t(atBeat(0, 4, 0, fr(3, 4)), new Dynamics(DynamicsType.ffffff)),
			t(atBeat(0, 5, 0, fr(0, 4)), new Dynamics(DynamicsType.mp)),
			t(atBeat(0, 5, 0, fr(1, 4)), new Dynamics(DynamicsType.mf)),
			t(atBeat(0, 5, 0, fr(2, 4)), new Dynamics(DynamicsType.sf)),
			t(atBeat(0, 5, 0, fr(3, 4)), new Dynamics(DynamicsType.sfp)),
			t(atBeat(0, 6, 0, fr(0, 4)), new Dynamics(DynamicsType.sfpp)),
			t(atBeat(0, 6, 0, fr(1, 4)), new Dynamics(DynamicsType.fp)),
			t(atBeat(0, 6, 0, fr(2, 4)), new Dynamics(DynamicsType.rf)),
			t(atBeat(0, 6, 0, fr(3, 4)), new Dynamics(DynamicsType.rfz)),
			t(atBeat(0, 7, 0, fr(0, 4)), new Dynamics(DynamicsType.sfz)),
			t(atBeat(0, 7, 0, fr(1, 4)), new Dynamics(DynamicsType.sffz)),
			t(atBeat(0, 7, 0, fr(2, 4)), new Dynamics(DynamicsType.fz)),
			//TODO: dynamics with additional text
			t(atBeat(0, 8, 0, fr(0, 4)), w1 = new Wedge(WedgeType.Crescendo)),
			t(atBeat(0, 8, 0, fr(1, 4)), new WedgeEnd(w1)),
			//TODO: dashes
			//TODO: bracket
			//TODO: octave shift
			t(atBeat(0, 10, 0, fr(0, 4)), new Pedal(Type.Start)),
			//TODO: pedal change
			t(atBeat(0, 10, 0, fr(3, 4)), new Pedal(Type.Stop)),
			t(atBeat(0, 11, 0, fr(0, 4)), new Tempo(fr(1, 4), 60)),
			//TODO: harp-pedals, damp, damp-all, scordatura, accordion-registration
			t(atBeat(0, 13, 0, fr(0, 4)), alist(
				new Words(new UnformattedText("subito")), //we do not test formatting here
				//new Words(new UnformattedText(" ")), //TODO: currently this second element is ignored
				new Dynamics(DynamicsType.p))),
			t(atBeat(0, 13, 0, fr(1, 4)), alist(
				new Dynamics(DynamicsType.ppp),
				w2 = new Wedge(WedgeType.Crescendo))),
			t(atBeat(0, 13, 0, fr(2, 4)), alist(
				new WedgeEnd(w2),
				new Dynamics(DynamicsType.ffff))));
	}
	
	/**
	 * Tempo Markings: note=bpm, text (note=bpm), note=note, (note=note), (note=bpm)  
	 */
	@Test public void test_31c() {
		loadTest("31c-MetronomeMarks.xml");
	}
	
	public List<Tuple2<MP, Tempo>> get_31c_Tempos() {
		//TODO: not all tempos are supported yet. return only those which are supported now
		return alist(
			t(atBeat(0, 0, 0, fr(0, 4)), new Tempo(fr(3, 8), 100))
			//TODO: atBeat(0, 0, 0, fr(3, 4)): longa = 100
			//TODO: atBeat(0, 1, 0, fr(0, 4)): fr(3, 8) = fr(3, 4)
			//TODO: atBeat(0, 1, 0, fr(3, 4)): longa = fr(3, 64)
			//TODO: atBeat(0, 2, 0, fr(0, 4)): fr(3, 8) = fr(3, 4) in parens
			//TODO: atBeat(0, 2, 0, fr(3, 4)): fr(3, 8) = 77 in parens
			);
	}
	
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
