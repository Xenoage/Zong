package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.SlursTest.assertSlurBetween;
import musicxmltestsuite.tests.base.Base33g;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.SlurType;


public class Test33g
	implements Base33g, MusicXmlInTest {
	
	private Chord[] chords = new Chord[4];
	
	@Before public void before() {
		Voice voice = getFirstVoice();
		for (int iChord : range(4)) {
			chords[iChord] = (Chord) voice.getElement(iChord);
		}
	}
	
	@Test public void test() {
		assertSlurBetween(chords, 0, 2, SlurType.Slur);
		assertSlurBetween(chords, 2, 3, SlurType.Slur);
	}
	
}
