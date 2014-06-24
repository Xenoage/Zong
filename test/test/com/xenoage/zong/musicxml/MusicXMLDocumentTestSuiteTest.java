package com.xenoage.zong.musicxml;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;

/**
 * Test the {@link MusicXMLDocument} class for the documents in the
 * <a href="http://lilypond.org/doc/v2.12/input/regression/musicxml/collated-files">
 * Unofficial MusicXML test suite</a>, published under the GPL license.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLDocumentTestSuiteTest {

	/**
	 * All pitches from G to c”” in ascending steps; First without accidentals,
	 * then with a sharp and then with a flat accidental.
	 * Double alterations and cautionary accidentals are tested at the end. 
	 */
	@Test public void test_01a_Pitches_Pitches() {
		MusicXMLDocument doc = load("01a-Pitches-Pitches.xml");
		MxlPart part = doc.getScore().getParts().get(0);
		assertEquals(26, part.getMeasures().size());
		int iPitch = 0;
		Pitch[] expectedPitches = MusicXMLTestSuite.get_01a_Pitches_Pitches();
		for (int iM = 0; iM < part.getMeasures().size(); iM++) {
			MxlMeasure measure = part.getMeasures().get(iM);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Note) {
					//check note and pitch
					MxlFullNote note = ((MxlNote) data).getContent().getFullNote();
					MxlPitch pitch = (MxlPitch) (note.getContent());
					assertEquals("note " + iPitch, expectedPitches[iPitch++], pitch.getPitch());
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
		MusicXMLDocument doc = load("01b-Pitches-Intervals.xml");
		Pitch[] expectedPitches = MusicXMLTestSuite.get_01b_Pitches_Intervals();
		MxlMeasure measure = doc.getScore().getParts().get(0).getMeasures().get(0);
		int iPitch = 0;
		for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
			if (data.getMusicDataContentType() == MxlMusicDataContentType.Note) {
				//check note and pitch
				MxlFullNote note = ((MxlNote) data).getContent().getFullNote();
				MxlPitch pitch = (MxlPitch) (note.getContent());
				assertEquals("note " + iPitch, expectedPitches[iPitch++], pitch.getPitch());
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
		MusicXMLDocument doc = load("01c-Pitches-NoVoiceElement.xml");
		MxlMeasure measure = doc.getScore().getParts().get(0).getMeasures().get(0);
		for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
			if (data.getMusicDataContentType() == MxlMusicDataContentType.Note) {
				//check pitch
				MxlNote note = (MxlNote) data;
				MxlFullNote fullNote = note.getContent().getFullNote();
				MxlPitch pitch = (MxlPitch) (fullNote.getContent());
				assertEquals(pi('G', 0, 4), pitch.getPitch());
				//check lyric
				assertEquals(1, note.getLyrics().size());
				assertEquals("A", ((MxlSyllabicText) note.getLyrics().get(0).getContent()).getText().getValue());
				return;
			}
		}
		fail("note not found");
	}

	private MusicXMLDocument load(String filename) {
		try {
			return MusicXMLDocument.read(jsePlatformUtils().createXmlReader(
				jsePlatformUtils().openFile(MusicXMLTestSuite.dir + filename)));
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + filename + ": " + ex.toString());
			return null;
		}
	}

}
