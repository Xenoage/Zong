package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atVoice;
import static musicxmltestsuite.tests.utils.SlursTest.assertNoSlurAt;
import static musicxmltestsuite.tests.utils.SlursTest.assertSlurBetween;
import musicxmltestsuite.tests.base.Base33i;
import musicxmltestsuite.tests.utils.SlursTest;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.SlurType;


public class Test33i
	implements Base33i, MusicXmlInTest {
	
	/*
	The interpretation is up to the software, when <tied type="stop"/> is missing.
	See the statement of Michael Good in the MusicXML forum, topic "Not ended tie":
	http://forums.makemusic.com/viewtopic.php?f=12&t=1476&p=3076&hilit=tied#p3073
	
	In Zong!, we rely on complete MusicXML data. Not ended ties are simply ignored,
	and the start beat of the stop note must be equal to the end beat of the start note.
	In this test, the only correct tie is between the last two measures.
	*/
	
	private Chord[] chords = new Chord[5];
	
	@Before public void before() {
		Score score = getScore();
		for (int iMeasure : range(5)) {
			chords[iMeasure] = (Chord) score.getVoice(atVoice(0, iMeasure, 0)).getElement(0);
		}
	}
	
	@Test public void test() {
		for (int iMeasure : range(3))
			assertNoSlurAt(chords, iMeasure);
		assertSlurBetween(chords, 3, 4, SlurType.Tie);
	}
	
}
