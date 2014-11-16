package musicxmltestsuite.tests.musicxml;

import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base51b;

import org.junit.Test;

import com.xenoage.zong.musicxml.types.MxlScorePart;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;


public class Test51b
	implements Base51b, MusicXmlTest {
	
	@Test public void test() {
		MxlScorePartwise score = getScore();
		assertEquals(expectedMovementTitle, score.getScoreHeader().getMovementTitle());
		assertEquals(expectedComposer, score.getScoreHeader().getIdentification().getCreators().get(0).getValue());
		MxlScorePart part = (MxlScorePart) score.getScoreHeader().getPartList().getContent().get(0);
		assertEquals(expectedPartName, part.getPartName());
	}

}
