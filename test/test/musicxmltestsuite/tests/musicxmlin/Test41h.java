package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base41h;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.StavesList;


public class Test41h
	implements Base41h, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		StavesList stavesList = score.getStavesList();
		//we read only the correctly declared parts, and ignore the others
		assertEquals(1, stavesList.getParts().size());
		assertEquals("MusicXML Part", stavesList.getParts().get(0).getName());
	}
	
}
