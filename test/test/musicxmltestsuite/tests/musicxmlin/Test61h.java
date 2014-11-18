package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.ChordTest.assertEqualsChordLyrics;
import musicxmltestsuite.tests.base.Base61h;
import musicxmltestsuite.tests.utils.ChordTest;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

public class Test61h
	implements Base61h, MusicXmlInTest {

	private Score score = getScore();


	@Test public void test() {
		for (int iMeasure : range(3))
		for (int iElement : range(7)) { //8th element is a rest
			MP mp = MP.atElement(0, iMeasure, 0, iElement);
			Chord chord = ChordTest.getChordAt(score, mp);
			assertEqualsChordLyrics(expectedLyrics[iMeasure * 8 + iElement], chord, mp);
		}
	}

}
