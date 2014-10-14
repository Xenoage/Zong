package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base11a;

import org.junit.Test;

import com.xenoage.zong.core.Score;

public class Test11a
	implements Base11a, MusicXmlInTest {

	@Test public void test() {
		Score score = getScore();
		int iTime = 0;
		for (int iM = 0; iM < score.getMeasuresCount(); iM++) {
			//TODO: first time is wrong in MusicXML file - ignore
			if (iTime == 0) {
				iTime++;
				continue;
			}
			//check time
			assertEquals("element " + iTime, expectedTimes[iTime++],
				score.getColumnHeader(iM).getTime().getType());
		}
		assertEquals("not all times found", expectedTimes.length, iTime);
	}

}
