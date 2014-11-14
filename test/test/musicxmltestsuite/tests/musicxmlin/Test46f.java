package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.StaffTest.testMeasureFilledBeats;
import musicxmltestsuite.tests.base.Base46f;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test46f
	implements Base46f, MusicXmlInTest {
	
	private Score score = getScore();

	
	@Test public void test() {
		testMeasureFilledBeats(expectedMeasureFilledBeats, score.getStaff(0));
	}
	
}
