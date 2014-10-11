package musicxmltestsuite.tests.musicxml;

import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base01a;

import org.junit.Test;

import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;


public class Test01a
	implements Base01a, MusicXmlTest {

	@Test public void test() {
		Pitch[] expectedPitches = getExpectedPitches();
		MxlPart part = getFirstPart();
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

}
