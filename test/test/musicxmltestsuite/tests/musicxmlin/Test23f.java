package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.Utils.checkDurations;
import musicxmltestsuite.tests.base.Base23f;

import org.junit.Test;

import com.xenoage.zong.core.Score;

public class Test23f
	implements Base23f, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		checkDurations(score.getStaff(0), expectedDurations[0]);
		checkDurations(score.getStaff(1), expectedDurations[1]);
	}

}
