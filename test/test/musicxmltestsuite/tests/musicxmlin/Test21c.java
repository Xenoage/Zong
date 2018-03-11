package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base21c;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;


public class Test21c
	implements Base21c, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		MP mp = mp0;
		for (int i = 0; i < expectedChords.length; i++) {
			Chord chord = (Chord) score.getVoice(mp).getElementAt(mp.beat);
			assertEquals("chord " + i, expectedChords[i].getNotes(), chord.getNotes());
			assertEquals("chord " + i, expectedChords[i].getDuration(), chord.getDuration());
			mp = mp.withBeat(mp.beat.add(expectedChords[i].getDuration()));
			if (mp.beat.compareTo(Companion.get_1()) >= 0) {
				mp = mp.withMeasure(mp.measure + 1).withBeat(Companion.get_0());
			}
		}
	}

}
