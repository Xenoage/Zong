package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import musicxmltestsuite.tests.base.Base12b;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.Interval;

public class Test12b
	implements Base12b, MusicXmlInTest {

	@Test public void test() {
		Score score = getScore();
		//musical context must be 4/4, C clef and no accidentals
		MusicContext context = score.getMusicContext(mp0, Interval.At, Interval.At);
		assertEquals(Companion.fr(4, 4), score.getMeasureBeats(0));
		assertEquals(ClefType.Companion.getClefTreble(), context.getClef());
		for (int i = 0; i < 7; i++)
			assertEquals(0, context.getKey().getAlterations()[i]);
		//there should be a C clef in the first measure
		assertEquals(ClefType.Companion.getClefTreble(), score.getMeasure(mp0).getClefs().get(Companion.get_0()).getType());
		//there should be a time signature and key signature in the measure column
		ColumnHeader header = score.getHeader().getColumnHeader(0);
		assertEquals(TimeType.Companion.getTime_4_4(), header.getTime().getType());
		assertNotNull(header.getKeys().get(Companion.get_0()));
	}

}
