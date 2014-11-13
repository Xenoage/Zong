package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsEndBarlines;
import static musicxmltestsuite.tests.utils.ScoreTest.assertEqualsVoltas;
import musicxmltestsuite.tests.base.Base45b;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test45b
	implements Base45b, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		assertEqualsEndBarlines(expectedEndBarlines, score);
		assertEqualsVoltas(expectedVoltas, score);
	}
	
}
