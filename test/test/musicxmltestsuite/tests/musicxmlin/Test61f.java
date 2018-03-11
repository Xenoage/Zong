package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static musicxmltestsuite.tests.utils.ChordTest.assertEqualsChordLyrics;
import musicxmltestsuite.tests.base.Base61f;
import musicxmltestsuite.tests.utils.ChordTest;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

public class Test61f
	implements Base61f, MusicXmlInTest {

	private Score score = getScore();


	@Test public void test() {
		for (int iMeasure : range(2))
		for (int iBeat : range(4)) {
			MP mp = MP.atBeat(0, iMeasure, 0, Companion.fr(iBeat, 4));
			Chord chord = ChordTest.getChordAt(score, mp);
			assertEqualsChordLyrics(expectedLyrics[iMeasure * 4 + iBeat], chord, mp);
		}
	}

}
