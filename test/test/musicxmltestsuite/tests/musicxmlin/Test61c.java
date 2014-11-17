package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.ChordTest.assertEqualsChordLyrics;
import musicxmltestsuite.tests.base.Base61c;
import musicxmltestsuite.tests.utils.ChordTest;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

public class Test61c
	implements Base61c, MusicXmlInTest {

	private Score score = getScore();


	@Test public void test() {
		for (int iStaff : range(2)) {
			for (int iElement : range(4)) {
				MP mp = MP.atElement(iStaff, 0, 0, iElement);
				Chord chord = ChordTest.getChordAt(score, mp);
				assertEqualsChordLyrics(expectedLyrics[iStaff][iElement], chord, mp);
			}
		}
	}

}
