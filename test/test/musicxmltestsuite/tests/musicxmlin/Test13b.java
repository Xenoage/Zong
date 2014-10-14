package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import musicxmltestsuite.tests.base.Base13b;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.position.MP;


public class Test13b
	implements Base13b, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		TraditionalKey[] expectedKeys = getExpectedKeys();
		MP mp = mp0;
		for (int iKey : range(expectedKeys)) {
			ColumnHeader column = score.getColumnHeader(mp.measure);
			TraditionalKey key = (TraditionalKey) column.getKeys().get(mp.beat);
			assertNotNull("mp " + mp, key);
			assertEquals("mp " + mp, expectedKeys[iKey].getFifths(), key.getFifths());
			assertEquals("mp " + mp, expectedKeys[iKey].getMode(), key.getMode());
			mp = mp.withBeat(mp.beat.add(fr(1, 4)));
			if (mp.beat.compareTo(_1) >= 0) {
				mp = mp.withMeasure(mp.measure + 1).withBeat(_0);
			}
		}
	}

}
