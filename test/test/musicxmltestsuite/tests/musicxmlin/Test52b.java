package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base52b;

import org.junit.Test;

import com.xenoage.zong.core.Score;

public class Test52b
	implements Base52b, MusicXmlInTest {

	private Score score = getScore();


	@Test public void test() {
		for (int i : range(expectedBreaks))
			assertEquals("Measure " + i,
				expectedBreaks[i], score.getHeader().getColumnHeader(i).getMeasureBreak());
	}

}
