package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atVoice;
import static musicxmltestsuite.tests.utils.SlursTest.assertSlurBetween;
import static musicxmltestsuite.tests.utils.VoiceTest.assertEqualsVoice;

import java.util.List;

import musicxmltestsuite.tests.base.Base42a;

import org.junit.Test;

import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
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
				assertEqualsVoice(expectedVoice, voice, mpVoice);
			}
		}
		//test slurs
		assertSlurBetween(getVoiceChords(staff.getVoice(atVoice(0, 1, 0))), 1, 2, SlurType.Slur);
		assertSlurBetween(getVoiceChords(staff.getVoice(atVoice(0, 1, 1))), 1, 2, SlurType.Slur);
	}
	
	private Chord[] getVoiceChords(Voice voice) {
		List<Chord> chords = alist();
		for (VoiceElement e : voice.getElements())
			if (e instanceof Chord)
				chords.add((Chord) e);
		return chords.toArray(new Chord[chords.size()]);
	}
	
}
