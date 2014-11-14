package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.atVoice;
import static musicxmltestsuite.tests.utils.VoiceTest.assertEqualsVoice;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base46e;

import org.junit.Test;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.position.MP;


public class Test46e
	implements Base46e, MusicXmlInTest {
	
	private Score score = getScore();

	
	@Test public void test() {
		testMeasureFilledBeats();
		testVoiceElements();
	}

	private void testVoiceElements() {
		for (int iMeasure : range(2)) {
			Measure expectedMeasure = expectedStaff.getMeasure(iMeasure);
			Measure measure = score.getMeasure(atMeasure(0, iMeasure));
			int expectedVoicesCount = expectedMeasure.getVoices().size();
			assertEquals(expectedVoicesCount, measure.getVoices().size());
			for (int iVoice : range(expectedVoicesCount)) {
				MP mpVoice = atVoice(0, iMeasure, iVoice);
				Voice expectedVoice = expectedStaff.getVoice(mpVoice);
				Voice voice = score.getVoice(mpVoice);
				assertEqualsVoice(expectedVoice, voice, mpVoice);
			}
		}
	}
	
	private void testMeasureFilledBeats() {
		assertEquals(expectedMeasureFilledBeats.length, score.getMeasuresCount());
		for (int i : range(expectedMeasureFilledBeats)) {
			Fraction filledBeats = score.getMeasure(MP.atMeasure(0, i)).getFilledBeats();
			assertEquals("Measure " + i, expectedMeasureFilledBeats[i], filledBeats);
		}
	}
	
}
