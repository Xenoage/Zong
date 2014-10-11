package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base01b;

import org.junit.Test;

import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;


public class Test01b
	implements Base01b, MusicXmlInTest {
	
	@Test public void test() {
		Pitch[] expectedPitches = getExpectedPitches();
		int iPitch = 0;
		Staff staff = getFirstStaff();
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Measure measure = staff.getMeasures().get(iM);
			Voice voice = measure.getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				if (e instanceof Chord) {
					//check note and pitch
					Chord chord = (Chord) e;
					assertEquals(expectedPitches[iPitch++], chord.getNotes().get(0).getPitch());
				}
			}
		}
		//TODO - ignore this test, since MusicXML input file has a bug (only a single measure),
		//so currently only the first measure is tested
		//assertEquals("not all notes found", expectedPitches.length, iPitch);
	}

}
