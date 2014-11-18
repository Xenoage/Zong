package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.ChordTest.assertEqualsChordLyrics;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base61e;
import musicxmltestsuite.tests.utils.ChordTest;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

public class Test61e
	implements Base61e, MusicXmlInTest {

	private Score score = getScore();


	@Test public void test() {
		for (int iElement : range(4)) {
			MP mp = MP.atElement(0, 0, 0, iElement);
			Chord chord = ChordTest.getChordAt(score, mp);
			assertEquals(expectedChordNotesCount[iElement], chord.getNotes().size());
			assertEqualsChordLyrics(expectedLyrics[iElement], chord, mp);
		}
	}

}
