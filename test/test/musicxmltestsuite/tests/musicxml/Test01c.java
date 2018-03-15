package musicxmltestsuite.tests.musicxml;

import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import musicxmltestsuite.tests.base.Base01c;

import org.junit.Test;

import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;


public class Test01c
	implements Base01c, MusicXmlTest {
	
	@Test public void test() {
		MxlMeasure measure = getFirstMeasure();
		for (MxlMusicDataContent data : measure.getMusicData().getContent()) {
			if (data.getMusicDataContentType() == MxlMusicDataContentType.Note) {
				//check pitch
				MxlNote note = (MxlNote) data;
				MxlFullNote fullNote = note.getContent().getFullNote();
				MxlPitch pitch = (MxlPitch) (fullNote.getContent());
				assertEquals(Companion.pi('G', 0, 4), pitch.getPitch());
				//check lyric
				assertEquals(1, note.getLyrics().size());
				assertEquals("A", ((MxlSyllabicText) note.getLyrics().get(0).getContent()).getText()
					.getValue());
				return;
			}
		}
		fail("note not found");
	}

}
