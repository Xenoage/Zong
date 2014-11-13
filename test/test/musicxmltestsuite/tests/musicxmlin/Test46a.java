package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsEndBarlines;
import musicxmltestsuite.tests.base.Base46a;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test46a
	implements Base46a, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		assertEqualsEndBarlines(expectedEndBarlines, score);
	}	
	
}
