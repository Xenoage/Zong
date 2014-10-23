package musicxmltestsuite.tests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;


/**
 * Test for slurs between indexed chords.
 * 
 * @author Andreas Wenger
 */
public class SlursTest {
	
	public static void assertSlurBetween(Chord[] chords, int chord1Index, int chord2Index,
		SlurType expectedSlurType) {
		Slur slur = getStartSlur(chords, chord1Index);
		assertEquals(expectedSlurType, slur.getType());
		List<SlurWaypoint> waypoints = slur.getWaypoints();
		assertEquals(2, waypoints.size());
		assertEquals(chords[chord1Index], waypoints.get(0).getChord());
		assertEquals(chords[chord2Index], waypoints.get(1).getChord());
	}
	
	private static Slur getStartSlur(Chord[] chords, int chordIndex) {
		for (Slur slur : chords[chordIndex].getSlurs()) {
			if (slur.getStart().getChord() == chords[chordIndex])
				return slur;
		}
		fail("No slur starts at chord " + chordIndex);
		return null;
	}

}
