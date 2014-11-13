package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.testMiddleBarlines;
import musicxmltestsuite.tests.base.Base46b;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test46b
	implements Base46b, MusicXmlInTest {
	
	private Score score = getScore();

	
	@Test public void test() {
		testMiddleBarlines(expectedMiddleBarlines, score);
	}
	
}
