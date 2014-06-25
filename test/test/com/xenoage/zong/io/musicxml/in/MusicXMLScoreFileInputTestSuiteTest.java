package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.MusicXMLTestSuite;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent.MxlFullNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
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
		//start in measure 3 (multirests are not supported yet - TODO)
		Score score = load("02a-Rests-Durations.xml");
		Staff staff = score.getStaff(0);
		Fraction[] expectedDurations = get_02a_Rests_Durations();
		int iDuration = 0;
		for (int iM = 2; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Rest) {
					Rest rest = (Rest) e;
					//check duration
					assertEquals("rest " + iDuration, expectedDurations[iDuration++], rest.getDuration());
				}
			}
		}
		assertEquals("not all rests found", expectedDurations.length, iDuration);
	}
	
	@Test @Override public void test_03a_Rhythm_Durations() {
		Score score = load("03a-Rhythm-Durations.xml");
		Staff staff = score.getStaff(0);
		Fraction[] expectedDurations = get_03a_Rhythm_Durations();
		int iDuration = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Chord) {
					Chord rest = (Chord) e;
					//check duration
					assertEquals("note " + iDuration, expectedDurations[iDuration++], rest.getDuration());
				}
			}
		}
		assertEquals("not all notes found", expectedDurations.length, iDuration);
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
