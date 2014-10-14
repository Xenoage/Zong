package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base21a;

import org.junit.Test;

import com.xenoage.zong.core.music.chord.Chord;


public class Test21a
	implements Base21a, MusicXmlInTest {
	
	@Test public void test() {
		Chord chord = (Chord) getFirstVoice().getElement(0);
		assertEquals(2, chord.getNotes().size());
		assertEquals(expectedChord.getNotes(), chord.getNotes());
		assertEquals(expectedChord.getDuration(), chord.getDuration());
	}

}
