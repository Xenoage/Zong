package musicxmltestsuite.tests.musicxml;

import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.ToDo;
import musicxmltestsuite.tests.base.Base03a;

import org.junit.Test;

import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent.MxlFullNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


public class Test03a
	implements Base03a, MusicXmlTest {
	
	@ToDo("multiple-rest not yet supported")
	
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
					assertEquals(MxlFullNoteContentType.Pitch, note.getFullNote().getContent()
						.getFullNoteContentType());
					assertEquals("note " + iDuration, expectedDurations[iDuration++],
						fr(note.getDuration(), divisions * 4));
				}
			}
		}
		assertEquals("not all notes found", expectedDurations.length, iDuration);
	}

}
