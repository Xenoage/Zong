package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.MusicXMLTestSuite;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;

/**
 * Test the {@link MusicXmlScoreFileInput} class for the documents in the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLScoreFileInputTestSuiteTest {

	/**
	 * All pitches from G to c”” in ascending steps; First without accidentals,
	 * then with a sharp and then with a flat accidental.
	 * Double alterations and cautionary accidentals are tested at the end. 
	 */
	@Test public void test_01a_Pitches_Pitches() {
		Score score = load("01a-Pitches-Pitches.xml");
		Staff staff = score.getStaff(0);
		assertEquals(26, staff.getMeasures().size());
		int iPitch = 0;
		Pitch[] expectedPitches = MusicXMLTestSuite.get_01a_Pitches_Pitches();
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
	
	/**
	 * All pitch intervals in ascending jump size. 
	 */
	@Test public void test_01b_Pitches_Intervals() {
		//the MusicXML file contains only a single measure (possibly an error in the test suite)
		Score score = load("01b-Pitches-Intervals.xml");
		Pitch[] expectedPitches = MusicXMLTestSuite.get_01b_Pitches_Intervals();
		Voice voice = score.getVoice(mp0);
		int iPitch = 0;
		for (VoiceElement e : voice.getElements()) {
			if (e instanceof Chord) {
				//check note and pitch
				Chord chord = (Chord) e;
				assertEquals(expectedPitches[iPitch++], chord.getNotes().get(0).getPitch());
			}
		}
		assertEquals("not all notes found", expectedPitches.length, iPitch);
	}
	
	/**
	 * The {@literal <voice>} element of notes is optional in MusicXML (although Dolet always writes it out).
	 * Here, there is one note with lyrics, but without a voice assigned.
	 * It should still be correctly converted.  
	 */
	@Test public void tes_01c_Pitches_NoVoiceElement() {
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

	private Score load(String filename) {
		try {
			String filepath = MusicXMLTestSuite.dir + filename;
			return new MusicXmlScoreFileInput().read(jsePlatformUtils().openFile(filepath), filepath);
		} catch (Exception ex) {
			Assert.fail("Could not load " + filename + ": " + ex.toString());
			return null;
		}
	}

}
