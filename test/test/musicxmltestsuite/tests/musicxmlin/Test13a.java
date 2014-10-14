package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction._0;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base13a;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.key.TraditionalKey;

public class Test13a
	implements Base13a, MusicXmlInTest {

	//@ToDo("Zong! supports only -7 to +7, starting in measure 9, ending in measure 38")
	
	@Test public void test() {
		Score score = getScore();
		TraditionalKey[] expectedKeys = getExpectedKeys();
		int iKey = 0;
		for (int i = 8; i <= 37; i++) {
			ColumnHeader column = score.getColumnHeader(i);
			TraditionalKey key = (TraditionalKey) column.getKeys().get(_0);
			assertEquals("measure " + i, expectedKeys[iKey].getFifths(), key.getFifths());
			assertEquals("measure " + i, expectedKeys[iKey].getMode(), key.getMode());
			iKey++;
		}
	}

}
