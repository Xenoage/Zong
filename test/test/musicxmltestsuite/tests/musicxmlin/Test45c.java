package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsEndBarlines;
import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsStartBarlines;
import musicxmltestsuite.tests.base.Base45c;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test45c
	implements Base45c, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		assertEqualsStartBarlines(expectedStartBarlines, score);
		assertEqualsEndBarlines(expectedEndBarlines, score);
	}

	
	
}
