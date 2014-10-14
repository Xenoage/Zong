package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.Utils.checkGraceChords;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base24f;

import org.junit.Test;

import com.xenoage.zong.core.music.slur.Slur;


public class Test24f
	implements Base24f, MusicXmlInTest {

	@Test public void test() {
		checkGraceChords(getFirstStaff(), expectedChords);
		//slur between chord 1 and 2
		Slur slur = expectedChords[1].getSlurs().get(0);
		assertEquals(expectedChords[1], slur.getStart().getChord());
		assertEquals(expectedChords[2], slur.getStop().getChord());
	}

}
