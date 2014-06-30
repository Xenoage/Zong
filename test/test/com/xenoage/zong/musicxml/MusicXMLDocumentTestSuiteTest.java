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

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.types.MxlAttributes;
import com.xenoage.zong.musicxml.types.MxlDirection;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlKey;
import com.xenoage.zong.musicxml.types.MxlNormalTime;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.MxlWords;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent.MxlDirectionTypeContentType;
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
	extends MusicXMLTestSuite<MusicXMLDocument> {
	
	@Override public MusicXMLDocument load(String file) {
		try {
			return MusicXMLDocument.read(jsePlatformUtils().createXmlReader(
				jsePlatformUtils().openFile(dir + file)));
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + file + ": " + ex.toString());
			return null;
		}
	}

	 @Override public void test_01a_Pitches_Pitches(MusicXMLDocument doc, Pitch[] expectedPitches) {
		MxlPart part = doc.getScore().getParts().get(0);
		assertEquals(26, part.getMeasures().size());
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
		//TODO: the editiorial sharp (sharp in parenthesis) in the last measure
		//is not supported yet
	}

	@Override public void test_01b_Pitches_Intervals(MusicXMLDocument doc, Pitch[] expectedPitches) {
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

	@Override public void test_01c_Pitches_NoVoiceElement(MusicXMLDocument doc) {
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

	@Override public void test_02a_Rests_Durations(MusicXMLDocument doc, Fraction[] expectedDurations) {
		//multirests are not supported yet - TODO
		MxlPart part = doc.getScore().getParts().get(0);
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
	
	@Override public void test_03a_Rhythm_Durations(MusicXMLDocument doc, Fraction[] expectedDurations) {
		MxlPart part = doc.getScore().getParts().get(0);
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
	
	@Override public void test_03b_Rhythm_Backup(MusicXMLDocument doc) {
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
	
	@Override public void test_03c_Rhythm_DivisionChange(MusicXMLDocument doc, Fraction[] expectedDurations) {
		//successful when it loads
	}
	
	@Override public void test_11a_TimeSignatures(MusicXMLDocument doc, TimeType[] expectedTimes) {
		MxlPart part = doc.getScore().getParts().get(0);
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
	
	@Override public void test_11b_TimeSignatures_NoTime(MusicXMLDocument doc) {
		//successfull when it loads
	}
	
	@Override public void test_11h_TimeSignatures_SenzaMisura(MusicXMLDocument doc) {
		//successfull when it loads
	}
	
	@Override public void test_12a_Clefs(MusicXMLDocument doc) {
		//successfull when it loads - more is tested in musicxml-in
	}
	
	@Override public void test_12b_Clefs_NoKeyOrClef(MusicXMLDocument doc) {
		//successfull when it loads - more is tested in musicxml-in
	}
	
	@Override public void test_13a_KeySignatures(MusicXMLDocument doc, TraditionalKey[] expectedKeys) {
		//TODO: Zong! supports only -7 to +7, starting in measure 9,
		//ending in measure 38
		MxlPart part = doc.getScore().getParts().get(0);
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
	
	@Override public void test_13b_KeySignatures_ChurchModes(
		MusicXMLDocument doc, TraditionalKey[] expectedKeys) {
		MxlPart part = doc.getScore().getParts().get(0);
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
	
	@Override public void test_21a_Chord_Basic(MusicXMLDocument doc, Chord expectedChord) {
		//successfull when it loads - more is tested in musicxml-in
	}

	@Override public void test_21b_Chords_TwoNotes(MusicXMLDocument doc,
		int expectedChordsCount, Chord expectedChord) {
		//successfull when it loads - more is tested in musicxml-in
	}
	
	@Override public void test_21c_Chords_ThreeNotesDuration(MusicXMLDocument doc, Chord[] expectedChords) {
		//successfull when it loads - more is tested in musicxml-in
	}

	@Override public void test_21d_Chords_SchubertStabatMater(MusicXMLDocument doc,
		Chord[] expectedChords, List<Tuple2<MP, ? extends Direction>> expectedDirections) {
		//check only directions in this test	
		MxlPart part = doc.getScore().getParts().get(0);
		int iDirection = 0;
		for (int iMeasure = 0; iMeasure <= 1; iMeasure++) {
			MxlMeasure measure = part.getMeasures().get(iMeasure);
			for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
				if (data.getMusicDataContentType() == MxlMusicDataContentType.Direction) {
					//check type
					MxlDirection dir = (MxlDirection) data;
					MxlDirectionTypeContent content = dir.getDirectionTypes().get(0).getContent();
					if (iDirection == 0) {
						//Words "Largo"
						assertEquals(0, iMeasure);
						assertEquals(MxlDirectionTypeContentType.Words, content.getDirectionTypeContentType());
						assertEquals("Largo", ((MxlWords) content).getFormattedText().getValue());
					}
					else if (iDirection == 1) {
						//Dynamics "fp"
						assertEquals(0, iMeasure);
						assertEquals(MxlDirectionTypeContentType.Dynamics, content.getDirectionTypeContentType());
						assertEquals(DynamicsType.fp, ((MxlDynamics) content).getElement());
					}
					else if (iDirection == 2) {
						//Dynamics "p"
						assertEquals(1, iMeasure);
						assertEquals(MxlDirectionTypeContentType.Dynamics, content.getDirectionTypeContentType());
						assertEquals(DynamicsType.p, ((MxlDynamics) content).getElement());
					}
					iDirection++;
				}
			}
		}
		assertEquals("not all directions found", expectedDirections.size(), iDirection);
	}

	
	
}
