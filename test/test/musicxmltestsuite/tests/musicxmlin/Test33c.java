package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atVoice;
import static musicxmltestsuite.tests.utils.SlursTest.assertSlurBetween;
import musicxmltestsuite.tests.base.Base33c;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.SlurType;


public class Test33c
	implements Base33c, MusicXmlInTest {
	
	private Chord[] chords = new Chord[8];
	
	@Before public void before() {
		Score score = getScore();
		for (int iMeasure : range(2)) {
			for (int iChord : range(4)) {
				chords[iMeasure * 4 + iChord] = (Chord) score.getVoice(atVoice(0, iMeasure, 0)).getElement(iChord);
			}
		}
	}
	
	@Test public void test() {
		assertSlurBetween(chords, 0, 1, SlurType.Slur);
		assertSlurBetween(chords, 1, 2, SlurType.Slur);
		assertSlurBetween(chords, 2, 3, SlurType.Slur);
		assertSlurBetween(chords, 4, 7, SlurType.Slur);
		assertSlurBetween(chords, 5, 6, SlurType.Slur);
	}
	
}
