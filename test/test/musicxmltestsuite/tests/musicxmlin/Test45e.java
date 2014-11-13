package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsEndBarlines;
import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsStartBarlines;
import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsVoltas;
import musicxmltestsuite.tests.base.Base45e;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test45e
	implements Base45e, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		assertEqualsStartBarlines(expectedStartBarlines, score);
		assertEqualsEndBarlines(expectedEndBarlines, score);
		assertEqualsVoltas(expectedVoltas, score);
	}
	
}
