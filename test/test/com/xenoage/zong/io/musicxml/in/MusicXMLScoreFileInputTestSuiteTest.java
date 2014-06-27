package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefSymbol;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.MusicXMLTestSuite;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlKey;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.enums.MxlMode;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

/**
 * Test the {@link MusicXmlScoreFileInput} class for the documents in the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLScoreFileInputTestSuiteTest
	extends MusicXMLTestSuite {

	@Test @Override public void test_01a_Pitches_Pitches() {
		Score score = load("01a-Pitches-Pitches.xml");
		Staff staff = score.getStaff(0);
		assertEquals(26, staff.getMeasures().size());
		int iPitch = 0;
		Pitch[] expectedPitches = get_01a_Pitches_Pitches();
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

	@Test @Override public void test_01b_Pitches_Intervals() {
		Score score = load("01b-Pitches-Intervals.xml");
		Pitch[] expectedPitches = get_01b_Pitches_Intervals();
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

	@Test @Override public void test_01c_Pitches_NoVoiceElement() {
		Score score = load("01c-Pitches-NoVoiceElement.xml");
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

	@Test @Override public void test_02a_Rests_Durations() {
		//multirests are not supported yet - TODO
		Score score = load("02a-Rests-Durations.xml");
		checkDurations(score, get_02a_Rests_Durations());
	}

	@Test @Override public void test_03a_Rhythm_Durations() {
		Score score = load("03a-Rhythm-Durations.xml");
		checkDurations(score, get_03a_Rhythm_Durations());
	}

	@Test @Override public void test_03b_Rhythm_Backup() {
		Score score = load("03b-Rhythm-Backup.xml");
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

	@Test @Override public void test_03c_Rhythm_DivisionChange() {
		Score score = load("03c-Rhythm-DivisionChange.xml");
		checkDurations(score, get_03c_Rhythm_DivisionChange());
	}
	
	private void checkDurations(Score score, Fraction[] expectedDurations) {
		Staff staff = score.getStaff(0);
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

	@Test @Override public void test_11a_TimeSignatures() {
		Score score = load("11a-TimeSignatures.xml");
		int iTime = 0;
		TimeType[] expectedTimes = get_11a_TimeSignatures();
		for (int iM = 0; iM < score.getMeasuresCount(); iM++) {
			//TODO: first time is wrong in MusicXML file - ignore
			if (iTime == 0) {
				iTime++;
				continue;
			}
			//check time
			assertEquals("element " + iTime, expectedTimes[iTime++], score.getColumnHeader(iM).getTime()
				.getType());
		}
		assertEquals("not all times found", expectedTimes.length, iTime);
	}

	@Test @Override public void test_11b_TimeSignatures_NoTime() {
		//successfull when it loads
		load("11b-TimeSignatures-NoTime.xml");
	}

	@Test @Override public void test_11h_TimeSignatures_SenzaMisura() {
		//successfull when it loads
		Score score = load("11h-TimeSignatures-SenzaMisura.xml");
		//time signature must be senza misura
		assertEquals(TimeType.timeSenzaMisura, score.getColumnHeader(0).getTime().getType());
		//measure must contain 3 notes and have a length of 3/8
		assertEquals(3, score.getVoice(mp0).getElements().size());
		assertEquals(fr(3, 8), score.getVoice(mp0).getFilledBeats());
	}
	
	@Test @Override public void test_12a_Clefs() {
		Score score = load("12a-Clefs.xml");
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
	
	@Test @Override public void test_12b_Clefs_NoKeyOrClef() {
		Score score = load("12b-Clefs-NoKeyOrClef.xml");
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

	@Test @Override public void test_13a_KeySignatures() {
		//TODO: Zong! supports only -7 to +7, starting in measure 9,
		//ending in measure 38
		Score score = load("13a-KeySignatures.xml");
		TraditionalKey[] expectedKeys = get_13a_KeySignatures();
		int iKey = 0;
		for (int i = 8; i <= 37; i++) {
			ColumnHeader column = score.getColumnHeader(i);
			TraditionalKey key = (TraditionalKey) column.getKeys().get(_0);
			assertEquals("measure " + i, expectedKeys[iKey].getFifths(), key.getFifths());
			assertEquals("measure " + i, expectedKeys[iKey].getMode(), key.getMode());
			iKey++;
		}
	}
	
	@Test @Override public void test_13b_KeySignatures_ChurchModes() {
		Score score = load("13b-KeySignatures-ChurchModes.xml");
		TraditionalKey[] expectedKeys = get_13b_KeySignatures_ChurchModes();
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

	private Score load(String filename) {
		try {
			String filepath = dir + filename;
			return new MusicXmlScoreFileInput().read(jsePlatformUtils().openFile(filepath), filepath);
		} catch (Exception ex) {
			Assert.fail("Could not load " + filename + ": " + ex.toString());
			return null;
		}
	}

}
