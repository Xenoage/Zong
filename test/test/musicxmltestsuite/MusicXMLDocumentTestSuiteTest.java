package musicxmltestsuite;

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
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musicxml.MusicXMLDocument;
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
 * This class tests the loading of MusicXML files into MusicXML data structures.
 * 
 * @author Andreas Wenger
 */
public class MusicXMLDocumentTestSuiteTest
	extends MusicXMLTestSuiteBase<MusicXMLDocument> {
	
	//the currently tested document
	private MusicXMLDocument doc;
	
	@Override public MusicXMLDocument loadData(String file) {
		try {
			return doc = MusicXMLDocument.read(jsePlatformUtils().createXmlReader(
				jsePlatformUtils().openFile(dir + file)));
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Could not load " + file + ": " + ex.toString());
			return null;
		}
	}

	@Override public void test_02a() {
		super.test_02a();
		Fraction[] expectedDurations = get_02a_Durations();
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
	
	@Override public void test_03a() {
		super.test_03a();
		Fraction[] expectedDurations = get_03a_Durations();
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
	
	@Override public void test_03b() {
		super.test_03b();
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
	
	@Override public void test_11a() {
		super.test_11a();
		TimeType[] expectedTimes = get_11a_Times();
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
	
	@Override public void test_13a() {
		super.test_13a();
		TraditionalKey[] expectedKeys = get_13a_Keys();
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
	
	@Override public void test_13b() {
		super.test_13b();
		TraditionalKey[] expectedKeys = get_13b_Keys();
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

	@Override public void test_21d() {
		super.test_21d();
		List<Tuple2<MP, ? extends Direction>> expectedDirections = get_21d_Directions();
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
