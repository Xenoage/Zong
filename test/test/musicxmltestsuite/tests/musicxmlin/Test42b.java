package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atVoice;
import static musicxmltestsuite.tests.utils.VoiceTest.assertEqualsVoice;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import musicxmltestsuite.tests.base.Base42b;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.position.MP;


public class Test42b
	implements Base42b, MusicXmlInTest {
	
	private Score score = getScore();

	
	@Test public void test() {
		testVoiceElements();
		testClefs();
	}

	private void testVoiceElements() {
		for (int iStaff : range(2)) {
			for (int iMeasure : range(2)) {
				MP mpVoice = atVoice(iStaff, iMeasure, 0);
				Voice expectedVoice = expectedStaves[iStaff].getVoice(mpVoice);
				Voice voice = score.getVoice(mpVoice);
				assertEqualsVoice(expectedVoice, voice, mpVoice);
			}
		}
	}
	
	private void testClefs() {
		for (Tuple2<MP, ClefType> expectedClef : expectedClefs) {
			MP mp = expectedClef.get1();
			Measure measure = score.getMeasure(mp);
			Clef clef = measure.getClefs().get(mp.beat);
			assertNotNull(""+mp, clef);
			assertEquals(""+mp, expectedClef.get2(), clef.getType());
		}
	}
	
}
