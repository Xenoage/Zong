package musicxmltestsuite.tests.musicxmlin;

import static musicxmltestsuite.Utils.checkGraceChords;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import musicxmltestsuite.tests.base.Base24a;

import org.junit.Test;

import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;

public class Test24a
	implements Base24a, MusicXmlInTest {
	
	@Test public void testGraceChords() {
		Staff staff = getFirstStaff();
		checkGraceChords(staff, expectedChords);
	}
	
	@Test public void testBeams() {
		Staff staff = getFirstStaff();
		int iChord = 0;
		Beam currentBeam = null;
		for (int iM = 0; iM < staff.getMeasures().size(); iM++) {
			Voice voice = staff.getMeasure(iM).getVoice(0);
			for (VoiceElement e : voice.getElements()) {
				Chord expectedChord = expectedChords[iChord];
				//beams between chord 2 and 3 and between 11 and 12
				if (iChord == 2 || iChord == 11) {
					assertNotNull("chord " + iChord + " unbeamed", expectedChord.getBeam());
					assertEquals("chord " + iChord, WaypointPosition.Start,
						expectedChord.getBeam().getWaypointPosition(expectedChord));
					currentBeam = expectedChord.getBeam();
				}
				else if (iChord == 3 || iChord == 12) {
					assertNotNull("chord " + iChord + " unbeamed", expectedChord.getBeam());
					assertEquals("wrong beam", currentBeam, expectedChord.getBeam());
					assertEquals("chord " + iChord, WaypointPosition.Stop,
						expectedChord.getBeam().getWaypointPosition(expectedChord));
					currentBeam = null;
				}
				else {
					assertNull("chord " + iChord + " beamed", expectedChord.getBeam());
				}
				iChord++;
			}
		}
		assertEquals("not all chords found", expectedChords.length, iChord);
	}

}
