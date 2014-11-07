package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base45a;

import org.junit.Test;

import com.xenoage.zong.core.Score;


public class Test45a
	implements Base45a, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		for (int iMeasure : range(expectedEndBarlines.length))
			assertEquals(expectedEndBarlines[iMeasure], score.getHeader().getColumnHeader(iMeasure).getEndBarline());
	}
	
}
