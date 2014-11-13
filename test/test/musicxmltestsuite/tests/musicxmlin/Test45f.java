package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsEndBarlines;
import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsStartBarlines;
import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsVoltas;
import musicxmltestsuite.tests.base.Base45f;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test45f
	implements Base45f, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		assertEqualsStartBarlines(expectedStartBarlines, score);
		assertEqualsEndBarlines(expectedEndBarlines, score);
		assertEqualsVoltas(expectedVoltas, score);
	}

	
	
}
