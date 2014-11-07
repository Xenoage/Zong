package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.testClefs;
import static musicxmltestsuite.tests.utils.ScoreTest.testDirections;
import static musicxmltestsuite.tests.utils.ScoreTest.testKeys;
import musicxmltestsuite.tests.base.Base43e;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test43e
	implements Base43e, MusicXmlInTest {
	
	private Score score = getScore();

	
	@Test public void test() {
		testClefs(expectedClefs, score);
		testDirections(expectedDirections, score);
		testKeys(expectedKeys, score);
	}
	
}
