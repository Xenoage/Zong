package musicxmltestsuite.tests.musicxml;

import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base02a;
import musicxmltestsuite.tests.utils.ToDo;

import org.junit.Test;

import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent.MxlFullNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


public class Test02a
	implements Base02a, MusicXmlTest {
	
	@ToDo("multirests are not supported yet")
	
	@Test public void test() {
		MxlPart part = getFirstPart();
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
						Companion.fr(note.getDuration(), divisions * 4));
				}
			}
		}
		assertEquals("not all rests found", expectedDurations.length, iDuration);
	}

}
