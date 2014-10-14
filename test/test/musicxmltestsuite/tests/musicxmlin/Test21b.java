package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base21b;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;


public class Test21b
	implements Base21b, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		MP mp = mp0;
		for (int i = 0; i < expectedChordsCount; i++) {
			Chord chord = (Chord) score.getVoice(mp).getElementAt(mp.beat);
			assertEquals(expectedChord.getNotes(), chord.getNotes());
			assertEquals(expectedChord.getDuration(), chord.getDuration());
			mp = mp.withBeat(mp.beat.add(fr(1, 4)));
			if (mp.beat.compareTo(_1) >= 0) {
				mp = mp.withMeasure(mp.measure + 1).withBeat(_0);
			}
		}
	}

}
