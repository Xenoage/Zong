package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.MusicXMLTestSuite;

/**
 * Test the {@link MusicXmlScoreFileInput} class for the documents in the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLScoreFileInputTestSuiteTest
	extends MusicXMLTestSuite<Score> {
	
	//the currently tested score
	private Score score;
	
	@Override public Score loadData(String file) {
		try {
			String filepath = dir + file;
			return score = new MusicXmlScoreFileInput().read(jsePlatformUtils().openFile(filepath), filepath);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + file + ": " + ex.toString());
			return null;
		}
	}

	@Override public void test_01a() {
		super.test_01a();
		Pitch[] expectedPitches = get_01a_Pitches();
		Staff staff = score.getStaff(0);
		assertEquals(26, staff.getMeasures().size());
		int iPitch = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Measure measure = staff.getMeasures().get(iM);
			Voice voice = measure.getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Chord) {
					//check note and pitch
					Chord chord = (Chord) e;
					assertEquals(expectedPitches[iPitch++], chord.getNotes().get(0).getPitch());
				}
			}
		}
		assertEquals("not all notes found", expectedPitches.length, iPitch);
		//TODO: the editiorial sharp (sharp in parenthesis) in the last measure
		//is not supported yet
	}

	@Override public void test_01b() {
		super.test_01b();
		Pitch[] expectedPitches = get_01b_Pitches();
		int iPitch = 0;
		Staff staff = score.getStaff(0);
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Measure measure = staff.getMeasures().get(iM);
			Voice voice = measure.getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Chord) {
					//check note and pitch
					Chord chord = (Chord) e;
					assertEquals(expectedPitches[iPitch++], chord.getNotes().get(0).getPitch());
				}
			}
		}
		//TODO - ignore this test, since MusicXML input file has a bug (only a single measure),
		//so currently only the first measure is tested
		//assertEquals("not all notes found", expectedPitches.length, iPitch);
	}

	@Override public void test_01c() {
		super.test_01c();
		Voice voice = score.getVoice(mp0);
		for (VoiceElement e : voice.getElements()) {
			if (e instanceof Chord) {
				Chord chord = (Chord) e;
				//check pitch
				assertEquals(pi('G', 0, 4), chord.getNotes().get(0).getPitch());
				//check lyric
				assertEquals(1, chord.getLyrics().size());
				assertEquals("A", chord.getLyrics().get(0).getText().toString());
				return;
			}
		}
		fail("note not found");
	}

	@Override public void test_02a() {
		super.test_02a();
		//multirests are not supported yet - TODO
		checkDurations(score.getStaff(0), get_02a_Durations());
	}

	@Override public void test_03a() {
		super.test_03a();
		checkDurations(score.getStaff(0), get_03a_Durations());
	}

	@Override public void test_03b() {
		super.test_03b();
		Measure measure = score.getMeasure(mp0);
		//two voices
		assertEquals(2, measure.getVoices().size());
		//check first voice
		Voice voice = measure.getVoice(0);
		assertEquals(2, voice.getElements().size());
		assertEquals(pi(0, 0, 4), ((Chord) voice.getElement(0)).getNotes().get(0).getPitch());
		assertEquals(fr(1, 4), voice.getElement(0).getDuration());
		assertEquals(pi(0, 0, 4), ((Chord) voice.getElement(1)).getNotes().get(0).getPitch());
		assertEquals(fr(1, 4), voice.getElement(1).getDuration());
		//check second voice
		//in Zong!, there is no "empty" space in voices. Instead, an invisible rest is used
		voice = measure.getVoice(1);
		assertTrue(voice.getElement(0) instanceof Rest);
		assertTrue(((Rest) voice.getElement(0)).isHidden());
		assertEquals(fr(1, 4), voice.getElement(0).getDuration());
		assertEquals(pi(5, 0, 3), ((Chord) voice.getElement(1)).getNotes().get(0).getPitch());
		assertEquals(fr(1, 4), voice.getElement(1).getDuration());
		assertEquals(pi(5, 0, 3), ((Chord) voice.getElement(2)).getNotes().get(0).getPitch());
		assertEquals(fr(1, 4), voice.getElement(2).getDuration());
	}

	@Override public void test_03c() {
		super.test_03c();
		checkDurations(score.getStaff(0), get_03c_Durations());
	}
	
	private void checkDurations(Staff staff, Fraction[] expectedDurations) {
		int iDuration = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				//check duration
				assertEquals("element " + iDuration, expectedDurations[iDuration++], e.getDuration());
			}
		}
		assertEquals("not all element found", expectedDurations.length, iDuration);
	}

	@Override public void test_11a() {
		super.test_11a();
		TimeType[] expectedTimes = get_11a_Times();
		int iTime = 0;
		for (int iM = 0; iM < score.getMeasuresCount(); iM++) {
			//TODO: first time is wrong in MusicXML file - ignore
			if (iTime == 0) {
				iTime++;
				continue;
			}
			//check time
			assertEquals("element " + iTime, expectedTimes[iTime++],
				score.getColumnHeader(iM).getTime().getType());
		}
		assertEquals("not all times found", expectedTimes.length, iTime);
	}

	@Override public void test_11h() {
		super.test_11h();
		//time signature must be senza misura
		assertEquals(TimeType.timeSenzaMisura, score.getColumnHeader(0).getTime().getType());
		//measure must contain 3 notes and have a length of 3/8
		assertEquals(3, score.getVoice(mp0).getElements().size());
		assertEquals(fr(3, 8), score.getVoice(mp0).getFilledBeats());
	}
	
	@Override public void test_12a() {
		super.test_12a();
		//check clefs and line position of c4
		int m = 0;
		checkClef(score, m++, ClefSymbol.G, 2, -2);
		checkClef(score, m++, ClefSymbol.C, 4, 4);
		checkClef(score, m++, ClefSymbol.C, 6, 6);
		checkClef(score, m++, ClefSymbol.F, 6, 10);
		checkClef(score, m++, ClefSymbol.PercTwoRects, 4, -2); //in Zong!, we use Perc = Treble
		checkClef(score, m++, ClefSymbol.G8vb, 2, 5);
		checkClef(score, m++, ClefSymbol.F8vb, 6, 17);
		checkClef(score, m++, ClefSymbol.F, 4, 8);
		checkClef(score, m++, ClefSymbol.G, 0, -4);
		checkClef(score, m++, ClefSymbol.C, 8, 8);
		checkClef(score, m++, ClefSymbol.C, 2, 2);
		checkClef(score, m++, ClefSymbol.C, 0, 0);
		checkClef(score, m++, ClefSymbol.PercTwoRects, 4, -2); //in Zong!, we use Perc = Treble
		checkClef(score, m++, ClefSymbol.G8va, 2, -9);
		checkClef(score, m++, ClefSymbol.F8va, 6, 3);
		checkClef(score, m++, ClefSymbol.Tab, 4, -2); //in Zong!, we use Tab = Treble
		checkClef(score, m++, ClefSymbol.None, 4, -2); //in Zong!, we use None = Treble
		checkClef(score, m++, ClefSymbol.G, 2, -2);
	}
	
	private void checkClef(Score score, int measure, ClefSymbol expectedSymbol, int expectedClefLP,
		int expectedC4LP) {
		ClefType clef = score.getMeasure(atMeasure(0, measure)).getClefs().getFirst().getElement().getType();
		assertEquals("measure " + measure, expectedSymbol, clef.getSymbol());
		assertEquals("measure " + measure, expectedClefLP, clef.getLp());
		assertEquals("measure " + measure, expectedC4LP, clef.getLp(pi('C', 0, 4)));
	}
	
	@Override public void test_12b() {
		super.test_12b();
		//musical context must be 4/4, C clef and no accidentals
		MusicContext context = score.getMusicContext(mp0, Interval.At, Interval.At);
		assertEquals(fr(4, 4), score.getMeasureBeats(0));
		assertEquals(ClefType.clefTreble, context.getClef().getType());
		for (int i = 0; i < 7; i++)
			assertEquals(0, context.getKey().getAlterations()[i]);
		//there should be a C clef in the first measure
		assertEquals(ClefType.clefTreble, score.getMeasure(mp0).getClefs().get(_0).getType());
		//there should be a time signature and key signature in the measure column
		ColumnHeader header = score.getHeader().getColumnHeader(0);
		assertEquals(TimeType.time_4_4, header.getTime().getType());
		assertNotNull(header.getKeys().get(_0));
	}

	@Override public void test_13a() {
		super.test_13a();
		TraditionalKey[] expectedKeys = get_13a_Keys();
		//TODO: Zong! supports only -7 to +7, starting in measure 9,
		//ending in measure 38
		int iKey = 0;
		for (int i = 8; i <= 37; i++) {
			ColumnHeader column = score.getColumnHeader(i);
			TraditionalKey key = (TraditionalKey) column.getKeys().get(_0);
			assertEquals("measure " + i, expectedKeys[iKey].getFifths(), key.getFifths());
			assertEquals("measure " + i, expectedKeys[iKey].getMode(), key.getMode());
			iKey++;
		}
	}
	
	@Override public void test_13b() {
		super.test_13b();
		TraditionalKey[] expectedKeys = get_13b_Keys();
		MP mp = mp0;
		for (int iKey : range(expectedKeys)) {
			ColumnHeader column = score.getColumnHeader(mp.measure);
			TraditionalKey key = (TraditionalKey) column.getKeys().get(mp.beat);
			assertNotNull("mp " + mp, key);
			assertEquals("mp " + mp, expectedKeys[iKey].getFifths(), key.getFifths());
			assertEquals("mp " + mp, expectedKeys[iKey].getMode(), key.getMode());
			mp = mp.withBeat(mp.beat.add(fr(1, 4)));
			if (mp.beat.compareTo(_1) >= 0) {
				mp = mp.withMeasure(mp.measure + 1).withBeat(_0);
			}
		}
	}
	
	@Override public void test_21a() {
		super.test_21a();
		Chord expectedChord = get_21a_Chord();
		Chord chord = (Chord) score.getVoice(mp0).getElement(0);
		assertEquals(2, chord.getNotes().size());
		assertEquals(expectedChord.getNotes(), chord.getNotes());
		assertEquals(expectedChord.getDuration(), chord.getDuration());
	}
	
	@Override public void test_21b() {
		super.test_21b();
		int expectedChordsCount = get_21b_ChordsCount();
		Chord expectedChord = get_21b_Chord();
		MP mp = mp0;
		for (int i = 0; i < expectedChordsCount; i++) {
			Chord chord = (Chord) score.getVoice(mp).getElementAt(mp.beat);
			assertEquals(expectedChord.getNotes(), chord.getNotes());
			assertEquals(expectedChord.getDuration(), chord.getDuration());
			mp = mp.withBeat(mp.beat.add(fr(1, 4)));
			if (mp.beat.compareTo(_1) >= 0) {
				mp = mp.withMeasure(mp.measure + 1).withBeat(_0);
			}
		}
	}

	@Override public void test_21c() {
		super.test_21c();
		Chord[] expectedChords = get_21c_Chords();
		MP mp = mp0;
		for (int i = 0; i < expectedChords.length; i++) {
			Chord chord = (Chord) score.getVoice(mp).getElementAt(mp.beat);
			assertEquals("chord " + i, expectedChords[i].getNotes(), chord.getNotes());
			assertEquals("chord " + i, expectedChords[i].getDuration(), chord.getDuration());
			mp = mp.withBeat(mp.beat.add(expectedChords[i].getDuration()));
			if (mp.beat.compareTo(_1) >= 0) {
				mp = mp.withMeasure(mp.measure + 1).withBeat(_0);
			}
		}
	}

	@Override public void test_21d() {
		super.test_21d();
		//first measure
		{
			//F4, whole, with accent and fermata
			Chord chord = (Chord) score.getVoice(mp0).getElementAt(_0);
			assertEquals(1, chord.getNotes().size());
			assertEquals(pi('F', 0, 4), chord.getNotes().get(0).getPitch());
			assertEquals(_1, chord.getDuration());
			assertEquals(2, chord.getAnnotations().size());
			assertEquals(new Articulation(ArticulationType.Accent, Placement.Below),
				chord.getAnnotations().get(0));
			assertEquals(new Fermata(Placement.Above), chord.getAnnotations().get(1));
			//words "Largo"
			List<MeasureElement> directions = score.getMeasure(mp0).getMeasureElements().getAll(_0);
			assertEquals(3, directions.size()); //clef, words, dynamics
			Words words = (Words) directions.get(1);
			assertEquals("Largo", words.getText().toString());
			assertEquals(Placement.Above, words.getPositioning());
			//dynamics "fp"
			Dynamics dynamics = (Dynamics) directions.get(2);
			assertEquals(DynamicsType.fp, dynamics.getType());
			assertEquals(Placement.Below, dynamics.getPositioning());
		}
		//second measure
		{
			MP m2 = mp0.withMeasure(1);
			//chords
			Chord chord = (Chord) score.getVoice(m2).getElementAt(_0);
			assertEquals(2, chord.getNotes().size());
			assertEquals(pi('F', 0, 4), chord.getNotes().get(0).getPitch());
			assertEquals(pi('A', -1, 4), chord.getNotes().get(1).getPitch());
			assertEquals(fr(3, 8), chord.getDuration());
			chord = (Chord) score.getVoice(m2).getElementAt(fr(3, 8));
			assertEquals(2, chord.getNotes().size());
			assertEquals(pi('F', 0, 4), chord.getNotes().get(0).getPitch());
			assertEquals(pi('A', -1, 4), chord.getNotes().get(1).getPitch());
			assertEquals(fr(1, 8), chord.getDuration());
			chord = (Chord) score.getVoice(m2).getElementAt(fr(2, 4));
			assertEquals(2, chord.getNotes().size());
			assertEquals(pi('G', 0, 4), chord.getNotes().get(0).getPitch());
			assertEquals(pi('B', -1, 4), chord.getNotes().get(1).getPitch());
			assertEquals(fr(1, 4), chord.getDuration());
			chord = (Chord) score.getVoice(m2).getElementAt(fr(3, 4));
			assertEquals(2, chord.getNotes().size());
			assertEquals(pi('G', 0, 4), chord.getNotes().get(0).getPitch());
			assertEquals(pi('B', -1, 4), chord.getNotes().get(1).getPitch());
			assertEquals(fr(1, 4), chord.getDuration());
			//dynamics "p"
			Dynamics dynamics = (Dynamics) score.getMeasure(m2).getMeasureElements().get(_0);
			assertEquals(DynamicsType.p, dynamics.getType());
			assertEquals(Placement.Below, dynamics.getPositioning());
		}
	}

	@Override public void test_21e() {
		super.test_21e();
		//4/4 time in first measure (implicit)
		assertEquals(fr(4, 4), score.getColumnHeader(0).getTime().getType().getMeasureBeats());
		//first measure has only a 1/4 chord and a total length of 1/4
		assertEquals(1, score.getVoice(mp0).getElements().size());
		assertEquals(fr(1, 4), score.getVoice(mp0).getElement(0).getDuration());
		assertEquals(fr(1, 4), score.getMeasure(mp0).getFilledBeats());
		//second measure has two 1/4 chords and total length of 2/4
		MP m2 = mp0.withMeasure(1);
		assertEquals(2, score.getVoice(m2).getElements().size());
		assertEquals(fr(1, 4), score.getVoice(m2).getElement(0).getDuration());
		assertEquals(fr(1, 4), score.getVoice(m2).getElement(1).getDuration());
		assertEquals(fr(2, 4), score.getMeasure(m2).getFilledBeats());
	}

	@Override public void test_21f() {
		super.test_21f();
		//1/4 with 3 notes at beat 0
		List<VoiceElement> e = score.getVoice(mp0).getElements();
		Chord chord = (Chord) e.get(0);
		assertEquals(3, chord.getNotes().size());
		assertEquals(fr(1, 4), chord.getDuration());
		//followed by 2 rests, 1/4 and 2/4
		assertEquals(fr(1, 4), ((Rest) e.get(1)).getDuration());
		assertEquals(fr(2, 4), ((Rest) e.get(2)).getDuration());
		//segno at beat 1/4 in column
		Direction segno = (Segno) score.getColumnHeader(0).getOtherDirections().get(fr(1, 4));
		assertNotNull(segno);
		//dynamics p at beat 1/4 in measure
		Dynamics dynamics = (Dynamics) score.getMeasure(mp0).getDirections().get(fr(1, 4));
		assertNotNull(dynamics);
		assertEquals(DynamicsType.p, dynamics.getType());
	}

	@Override public void test_23a() {
		super.test_23a();
		checkDurations(score.getStaff(0), get_23a_Durations());
		//TODO: add support for tuplet notation and test it
	}

	@Override public void test_23f() {
		super.test_23f();
		checkDurations(score.getStaff(0), get_23f_Durations()[0]);
		checkDurations(score.getStaff(1), get_23f_Durations()[1]);
	}

	@Override public void test_24a() {
		super.test_24a();
		Chord[] expectedChords = get_24a_Chords();
		Staff staff = score.getStaff(0);
		//check chords
		checkGraceChords(staff, expectedChords);
		//check beams
		int iChord = 0;
		Beam currentBeam = null;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				Chord expectedChord = expectedChords[iChord];
				//beams between chord 2 and 3 and between 11 and 12
				if (iChord == 2 || iChord == 11) {
					assertNotNull("chord " + iChord + " unbeamed", expectedChord.getBeam());
					assertEquals("chord " + iChord, WaypointPosition.Start,
						expectedChord.getBeam().getWaypointPosition(expectedChord));
					currentBeam = expectedChord.getBeam();
				}
				else if (iChord == 3 || iChord == 12) {
					assertNotNull("chord " + iChord + " unbeamed", expectedChord.getBeam());
					assertEquals("wrong beam", currentBeam, expectedChord.getBeam());
					assertEquals("chord " + iChord, WaypointPosition.Stop,
						expectedChord.getBeam().getWaypointPosition(expectedChord));
					currentBeam = null;
				}
				else {
					assertNull("chord " + iChord + " beamed", expectedChord.getBeam());
				}
				iChord++;
			}
		}
		assertEquals("not all chords found", expectedChords.length, iChord);
	}
	
	private void checkGraceChords(Staff staff, Chord[] expectedChords) {
		checkGraceChords(staff, expectedChords, false);
	}
	
	private void checkGraceChords(Staff staff, Chord[] expectedChords, boolean skipRests) {
		int iChord = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Rest && skipRests)
					continue;
				//check duration, type and notes
				Chord chord = (Chord) e;
				Chord expectedChord = expectedChords[iChord];
				assertEquals("chord " + iChord, expectedChord.getDuration(), chord.getDuration());
				assertEquals("chord " + iChord, expectedChord.getGrace(), chord.getGrace());
				assertEquals("chord " + iChord, expectedChord.getNotes(), chord.getNotes());
				iChord++;
			}
		}
		assertEquals("not all chords found", expectedChords.length, iChord);
	}

	@Override public void test_24b() {
		super.test_24b();
		checkGraceChords(score.getStaff(0), get_24b_Chords());
	}
	
	@Override public void test_24c() {
		super.test_24c();
		checkGraceChords(score.getStaff(0), get_24c_Chords());
	}
	
	@Override public void test_24d() {
		super.test_24d();
		checkGraceChords(score.getStaff(0), get_24d_Chords());
	}
	
	@Override public void test_24e() {
		super.test_24e();
		for (int i : range(2))
			checkGraceChords(score.getStaff(i), get_24e_StavesChords()[i], true);
		//check start beats
		Fraction[][] startBeats = get_24e_StavesBeats();
		assertEquals(startBeats[0][0], MP.getMP(score.getVoice(mp0).getElement(0)).getBeat());
		assertEquals(startBeats[0][1], MP.getMP(score.getVoice(mp0).getElement(1)).getBeat());
		assertEquals(fr(2, 4), score.getVoice(mp0.withStaff(1)).getElement(0).getDuration()); //half rest
		assertEquals(startBeats[1][0], MP.getMP(score.getVoice(mp0.withStaff(1)).getElement(1)).getBeat());
		assertEquals(startBeats[1][1], MP.getMP(score.getVoice(mp0.withStaff(1)).getElement(2)).getBeat());
	}
	
	@Override public void test_24f() {
		super.test_24f();
		Chord[] expectedChords = get_24f_Chords();
		checkGraceChords(score.getStaff(0), expectedChords);
		//slur between chord 1 and 2
		Slur slur = expectedChords[1].getSlurs().get(0);
		assertEquals(expectedChords[1], slur.getStart().getChord());
		assertEquals(expectedChords[2], slur.getStop().getChord());
	}
	
}
