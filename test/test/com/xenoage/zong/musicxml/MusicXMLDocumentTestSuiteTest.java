package com.xenoage.zong.musicxml;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
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
					assertEquals(expectedPitches[iPitch++], pitch.getPitch());
				}
			}
		}
		//TODO: the editiorial sharp (sharp in parenthesis) in the last measure
		//is not supported yet
	}

	private MusicXMLDocument load(String filename) {
		try {
			return MusicXMLDocument.read(jsePlatformUtils().createXmlReader(
				jsePlatformUtils().openFile(MusicXMLTestSuite.dir + filename)));
		} catch (Exception ex) {
			Assert.fail("Could not load " + filename + ": " + ex.toString());
			return null;
		}
	}

}
