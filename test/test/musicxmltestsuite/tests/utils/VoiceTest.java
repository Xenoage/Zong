package musicxmltestsuite.tests.utils;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.ChordTest.assertEqualsChord;
import static org.junit.Assert.assertEquals;

import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;


public class VoiceTest {

	/**
	 * Tests the equality of the given voices, but only the durations of the rests
	 * and the durations and notes of the chords.
	 */
	public static void assertEqualsVoice(Voice expectedVoice, Voice voice, MP voiceMP) {
		assertEquals(expectedVoice.getElements().size(), voice.getElements().size());
		for (int i : range(expectedVoice.getElements())) {
			VoiceElement expElement = expectedVoice.getElement(i);
			VoiceElement element = voice.getElement(i);
			assertEquals(expElement.getClass(), element.getClass());
			assertEquals(expElement.getDuration(), element.getDuration());
			if (expElement instanceof Chord)
				assertEqualsChord((Chord) expElement, (Chord) element, voiceMP.withElement(i));
		}
	}
	
}
