package musicxmltestsuite.tests.musicxml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base03b;

import org.junit.Test;

import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent.MxlMusicDataContentType;
import com.xenoage.zong.musicxml.types.partwise.MxlMeasure;


public class Test03b
	implements Base03b, MusicXmlTest {
	
	@Test public void test() {
		//elements in this measure: attributes, note, note, backup, note, note
		MxlMeasure measure = getFirstMeasure();
		List<MxlMusicDataContent> content = measure.getMusicData().getContent();
		assertEquals(6, content.size());
		assertEquals(MxlMusicDataContentType.Attributes, content.get(0).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(1).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(2).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Backup, content.get(3).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(4).getMusicDataContentType());
		assertEquals(MxlMusicDataContentType.Note, content.get(5).getMusicDataContentType());
	}

}
