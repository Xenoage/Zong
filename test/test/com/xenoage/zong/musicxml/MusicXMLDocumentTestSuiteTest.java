package com.xenoage.zong.musicxml;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlKey;
import com.xenoage.zong.musicxml.types.MxlNormalTime;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent.MxlFullNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.enums.MxlTimeSymbol;
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
public class MusicXMLDocumentTestSuiteTest
	extends MusicXMLTestSuite {

	@Test @Override public void test_01a_Pitches_Pitches() {
		MusicXMLDocument doc = load("01a-Pitches-Pitches.xml");
		MxlPart part = doc.getScore().getParts().get(0);
		assertEquals(26, part.getMeasures().size());
		int iPitch = 0;
		Pitch[] expectedPitches = get_01a_Pitches_Pitches();
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

	@Test @Override public void test_01b_Pitches_Intervals() {
		MusicXMLDocument doc = load("01b-Pitches-Intervals.xml");
		Pitch[] expectedPitches = get_01b_Pitches_Intervals();
		MxlPart part = doc.getScore().getParts().get(0);
		int iPitch = 0;
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
	}

	@Test @Override public void test_01c_Pitches_NoVoiceElement() {
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
				assertEquals("A", ((MxlSyllabicText) note.getLyrics().get(0).getContent()).getText()
					.getValue());
				return;
			}
		}
		fail("note not found");
	}

	@Test @Override public void test_02a_Rests_Durations() {
		//multirests are not supported yet - TODO
		MusicXMLDocument doc = load("02a-Rests-Durations.xml");
		MxlPart part = doc.getScore().getParts().get(0);
		Fraction[] expectedDurations = get_02a_Rests_Durations();
		int iDuration = 0;
		int divisions = 64; //from MusicXML file
		for (int iM = 0; iM < part.getMeasures().size(); iM++) {
			MxlMeasure measure = part.getMeasures().get(iM);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Note) {
					//check type and duration
					MxlNormalNote note = (MxlNormalNote) ((MxlNote) data).getContent();
					assertEquals(MxlFullNoteContentType.Rest, note.getFullNote().getContent()
						.getFullNoteContentType());
					assertEquals("rest " + iDuration, expectedDurations[iDuration++],
						fr(note.getDuration(), divisions * 4));
				}
			}
		}
		assertEquals("not all rests found", expectedDurations.length, iDuration);
	}
	
	@Test @Override public void test_03a_Rhythm_Durations() {
		MusicXMLDocument doc = load("03a-Rhythm-Durations.xml");
		MxlPart part = doc.getScore().getParts().get(0);
		Fraction[] expectedDurations = get_03a_Rhythm_Durations();
		int iDuration = 0;
		int divisions = 64; //from MusicXML file
		for (int iM = 0; iM < part.getMeasures().size(); iM++) {
			MxlMeasure measure = part.getMeasures().get(iM);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Note) {
					//check type and duration
					MxlNormalNote note = (MxlNormalNote) ((MxlNote) data).getContent();
					assertEquals(MxlFullNoteContentType.Pitch, note.getFullNote().getContent()
						.getFullNoteContentType());
					assertEquals("note " + iDuration, expectedDurations[iDuration++],
						fr(note.getDuration(), divisions * 4));
				}
			}
		}
		assertEquals("not all notes found", expectedDurations.length, iDuration);
	}
	
	@Test @Override public void test_03b_Rhythm_Backup() {
		MusicXMLDocument doc = load("03b-Rhythm-Backup.xml");
		//elements in this measure: attributes, note, note, backup, note, note
		MxlMeasure measure = doc.getScore().getParts().get(0).getMeasures().get(0);
		List<MxlMusicDataContent> content = measure.getMusicData().getContent();
		assertEquals(6, content.size());
		assertEquals(MxlMusicDataContentType.Attributes, content.get(0).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(1).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(2).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Backup, content.get(3).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(4).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(5).getMusicDataContentType());
	}
	
	@Override public void test_03c_Rhythm_DivisionChange() {
		//test not useful in this project
	}
	
	@Test @Override public void test_11a_TimeSignatures() {
		MusicXMLDocument doc = load("11a-TimeSignatures.xml");
		MxlPart part = doc.getScore().getParts().get(0);
		TimeType[] expectedTimes = get_11a_TimeSignatures();
		int iTime = 0;
		for (int i = 0; i < part.getMeasures().size(); i++) {
			MxlMeasure measure = part.getMeasures().get(i);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					//check type
					MxlAttributes attr = (MxlAttributes) data;
					MxlNormalTime mxlTime = (MxlNormalTime) attr.getTime().getContent();
					TimeType expectedTime = expectedTimes[iTime++];
					assertEquals("time " + iTime, expectedTime.getNumerator(), mxlTime.getBeats());
					assertEquals("time " + iTime, expectedTime.getDenominator(), mxlTime.getBeatType());
					if (i == 0)
						assertEquals("time " + iTime, MxlTimeSymbol.Common, attr.getTime().getSymbol()); //TODO: bug in MusicXML file, should be "Cut"
					else if (i == 1)
						assertEquals("time " + iTime, MxlTimeSymbol.Common, attr.getTime().getSymbol());
					else
						assertNull("time " + iTime, attr.getTime().getSymbol()); //= Normal
					break; //no more time signature in this measure
				}
			}
		}
		assertEquals("not all times found", expectedTimes.length, iTime);
	}
	
	@Test @Override public void test_11b_TimeSignatures_NoTime() {
		//successfull when it loads
		load("11b-TimeSignatures-NoTime.xml");
	}
	
	@Test @Override public void test_11h_TimeSignatures_SenzaMisura() {
		//successfull when it loads
		load("11h-TimeSignatures-SenzaMisura.xml");
	}
	
	@Test @Override public void test_12a_Clefs() {
		//successfull when it loads - more is tested in musicxml-in
		load("12a-Clefs.xml");
	}
	
	@Test @Override public void test_12b_Clefs_NoKeyOrClef() {
		//successfull when it loads - more is tested in musicxml-in
		load("12b-Clefs-NoKeyOrClef.xml");
	}
	
	@Test @Override public void test_13a_KeySignatures() {
		//TODO: Zong! supports only -7 to +7, starting in measure 9,
		//ending in measure 38
		MusicXMLDocument doc = load("13a-KeySignatures.xml");
		MxlPart part = doc.getScore().getParts().get(0);
		TraditionalKey[] expectedKeys = get_13a_KeySignatures();
		int iKey = 0;
		for (int i = 8; i <= 37; i++) {
			MxlMeasure measure = part.getMeasures().get(i);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					//check type
					MxlAttributes attr = (MxlAttributes) data;
					MxlKey key = attr.getKey();
					assertEquals(expectedKeys[iKey].getFifths(), key.getFifths());
					assertEquals(expectedKeys[iKey].getMode(), getEnumValue(""+key.getMode(), Mode.values()));
					iKey++;
				}
			}
		}
	}
	
	@Test @Override public void test_13b_KeySignatures_ChurchModes() {
		MusicXMLDocument doc = load("13b-KeySignatures-ChurchModes.xml");
		MxlPart part = doc.getScore().getParts().get(0);
		TraditionalKey[] expectedKeys = get_13b_KeySignatures_ChurchModes();
		int iKey = 0;
		for (int i = 0; i <= 2; i++) {
			MxlMeasure measure = part.getMeasures().get(i);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Attributes) {
					//check type
					MxlAttributes attr = (MxlAttributes) data;
					MxlKey key = attr.getKey();
					assertEquals(expectedKeys[iKey].getFifths(), key.getFifths());
					assertEquals(expectedKeys[iKey].getMode(), getEnumValue(""+key.getMode(), Mode.values()));
					iKey++;
					if (iKey >= expectedKeys.length)
						break;
				}
			}
		}
		assertEquals("not all keys found", expectedKeys.length, iKey);
	}
	
	@Override public void test_21a_Chord_Basic() {
		//successfull when it loads - more is tested in musicxml-in
		load(file_21a_Chord_Basic());
	}

	private MusicXMLDocument load(String filename) {
		try {
			return MusicXMLDocument.read(jsePlatformUtils().createXmlReader(
				jsePlatformUtils().openFile(dir + filename)));
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + filename + ": " + ex.toString());
			return null;
		}
	}

}
