package musicxmltestsuite.tests.layout;

import musicxmltestsuite.tests.base.Base01a;
import musicxmltestsuite.tests.utils.ToDo;

import org.junit.Test;

import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;


public class Test01a
	implements Base01a, LayoutTest {

	@ToDo("the editiorial sharp (sharp in parenthesis) in the last measure is not supported yet")
	
	@Test public void test() {
		int[] expectedLPs = getExpectedLPs();
		Staff staff = getFirstStaff();
		ScoreFrameLayout scoreFrameLayout = getScoreFrameLayout();
		int chordIndex = 0;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Measure measure = staff.getMeasures().get(iM);
			Voice voice = measure.getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Chord) {
					//check LP
					//TODO
				}
			}
		}
	}

}
