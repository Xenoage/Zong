package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import musicxmltestsuite.tests.base.Base01c;

import org.junit.Test;

import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;


public class Test01c
	implements Base01c, MusicXmlInTest {
	
	@Test public void test() {
		Voice voice = getScore().getVoice(mp0);
		for (VoiceElement e : voice.getElements()) {
			if (e instanceof Chord) {
				Chord chord = (Chord) e;
				//check pitch
				assertEquals(Companion.pi('G', 0, 4), chord.getNotes().get(0).getPitch());
				//check lyric
				assertEquals(1, chord.getLyrics().size());
				assertEquals("A", chord.getLyrics().get(0).getText().toString());
				return;
			}
		}
		fail("note not found");
	}

}
