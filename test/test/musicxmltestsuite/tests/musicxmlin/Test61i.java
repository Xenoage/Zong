package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.tests.utils.ChordTest.assertEqualsChordLyrics;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base61i;
import musicxmltestsuite.tests.utils.ChordTest;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

public class Test61i
	implements Base61i, MusicXmlInTest {

	private Score score = getScore();


	@Test public void test() {
		MP mp = MP.atElement(0, 0, 0, 0);
		Chord chord = ChordTest.getChordAt(score, mp);
		assertEquals(expectedChordNotesCount[0], chord.getNotes().size());
		assertEqualsChordLyrics(expectedLyrics[0], chord, mp);
	}

}
