package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;
import musicxmltestsuite.ToDo;
import musicxmltestsuite.tests.base.Base01a;

import org.junit.Test;

import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;


public class Test01a
	implements Base01a, MusicXmlInTest {

	@ToDo("the editiorial sharp (sharp in parenthesis) in the last measure is not supported yet")
	
	@Test public void test() {
		Pitch[] expectedPitches = getExpectedPitches();
		Staff staff = getFirstStaff();
		assertEquals(26, staff.getMeasures().size());
		int iPitch = 0;
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
		assertEquals("not all notes found", expectedPitches.length, iPitch); 
	}

}
