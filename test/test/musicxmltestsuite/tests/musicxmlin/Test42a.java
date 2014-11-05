package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atVoice;
import static musicxmltestsuite.tests.utils.SlursTest.assertSlurBetween;
import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base42a;

import org.junit.Test;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.position.MP;


public class Test42a
	implements Base42a, MusicXmlInTest {
	
	@Test public void test() {
		Staff staff = getFirstStaff();
		//test chords
		for (int iMeasure : range(2)) {
			for (int iVoice : range(2)) {
				MP mpVoice = atVoice(0, iMeasure, iVoice);
				Voice expectedVoice = expectedStaff.getVoice(mpVoice);
				Voice voice = staff.getVoice(mpVoice);
				assertEqualsVoice(expectedVoice, voice);
			}
		}
		//test slurs
		assertSlurBetween(getVoiceChords(staff.getVoice(atVoice(0, 1, 0))), 1, 2, SlurType.Slur);
		assertSlurBetween(getVoiceChords(staff.getVoice(atVoice(0, 1, 1))), 1, 2, SlurType.Slur);
	}
	
	private void assertEqualsVoice(Voice expectedVoice, Voice voice) {
		assertEquals(expectedVoice.getElements().size(), voice.getElements().size());
		for (int i : range(expectedVoice.getElements())) {
			VoiceElement expElement = expectedVoice.getElement(i);
			VoiceElement element = voice.getElement(i);
			assertEquals(expElement.getClass(), element.getClass());
			assertEquals(expElement.getDuration(), element.getDuration());
			if (expElement instanceof Chord)
				assertEqualsChord((Chord) expElement, (Chord) element);
		}
	}
	
	private void assertEqualsChord(Chord expectedChord, Chord chord) {
		assertEquals(1, chord.getNotes().size());
		assertEquals(expectedChord.getNotes().get(0).getPitch(), chord.getNotes().get(0).getPitch());
		assertEquals(1, chord.getLyrics().size());
		assertEqualsLyric(expectedChord.getLyrics().get(0), chord.getLyrics().get(0));
	}
	
	private void assertEqualsLyric(Lyric expectedLyric, Lyric lyric) {
		assertEquals(expectedLyric.getSyllableType(), lyric.getSyllableType());
		assertEquals(expectedLyric.getText().toString(), lyric.getText().toString());
		assertEquals(expectedLyric.getVerse(), lyric.getVerse());
	}
	
	private Chord[] getVoiceChords(Voice voice) {
		List<Chord> chords = alist();
		for (VoiceElement e : voice.getElements())
			if (e instanceof Chord)
				chords.add((Chord) e);
		return chords.toArray(new Chord[chords.size()]);
	}
	
}
