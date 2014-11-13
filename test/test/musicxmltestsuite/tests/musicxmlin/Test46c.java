package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.testClefs;
import musicxmltestsuite.tests.base.Base46c;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test46c
	implements Base46c, MusicXmlInTest {
	
	private Score score = getScore();

	
	@Test public void test() {
		testClefs(expectedClefs, score);
	}
	
}
