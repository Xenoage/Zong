package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import musicxmltestsuite.tests.base.Base03b;

import org.junit.Test;

import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.rest.Rest;


public class Test03b
	implements Base03b, MusicXmlInTest {
	
	@Test public void test() {
		Measure measure = getScore().getMeasure(mp0);
		//two voices
		assertEquals(2, measure.getVoices().size());
		//check first voice
		Voice voice = measure.getVoice(0);
		assertEquals(2, voice.getElements().size());
		assertEquals(Companion.pi(0, 0, 4), ((Chord) voice.getElement(0)).getNotes().get(0).getPitch());
		assertEquals(Companion.fr(1, 4), voice.getElement(0).getDuration());
		assertEquals(Companion.pi(0, 0, 4), ((Chord) voice.getElement(1)).getNotes().get(0).getPitch());
		assertEquals(Companion.fr(1, 4), voice.getElement(1).getDuration());
		//check second voice
		//in Zong!, there is no "empty" space in voices. Instead, an invisible rest is used
		voice = measure.getVoice(1);
		assertTrue(voice.getElement(0) instanceof Rest);
		assertTrue(((Rest) voice.getElement(0)).isHidden());
		assertEquals(Companion.fr(1, 4), voice.getElement(0).getDuration());
		assertEquals(Companion.pi(5, 0, 3), ((Chord) voice.getElement(1)).getNotes().get(0).getPitch());
		assertEquals(Companion.fr(1, 4), voice.getElement(1).getDuration());
		assertEquals(Companion.pi(5, 0, 3), ((Chord) voice.getElement(2)).getNotes().get(0).getPitch());
		assertEquals(Companion.fr(1, 4), voice.getElement(2).getDuration());
	}

}
