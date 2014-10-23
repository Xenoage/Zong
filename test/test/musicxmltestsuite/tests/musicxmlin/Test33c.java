package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atVoice;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base33c;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;


public class Test33c
	implements Base33c, MusicXmlInTest {
	
	private Chord[] chords = new Chord[8];
	
	@Before public void before() {
		Score score = getScore();
		for (int iMeasure : range(2)) {
			for (int iChord : range(4)) {
				chords[iMeasure * 4 + iChord] = (Chord) score.getVoice(atVoice(0, iMeasure, 0)).getElement(iChord);
			}
		}
	}
	
	@Test public void test() {
		assertSlurBetween(0, 1);
		assertSlurBetween(1, 2);
		assertSlurBetween(2, 3);
		assertSlurBetween(4, 7);
		assertSlurBetween(5, 6);
	}
	
	private void assertSlurBetween(int chord1Index, int chord2Index) {
		Slur slur = getStartSlur(chord1Index);
		assertEquals(SlurType.Slur, slur.getType());
		List<SlurWaypoint> waypoints = slur.getWaypoints();
		assertEquals(2, waypoints.size());
		assertEquals(chords[chord1Index], waypoints.get(0).getChord());
		assertEquals(chords[chord2Index], waypoints.get(1).getChord());
	}
	
	private Slur getStartSlur(int chordIndex) {
		for (Slur slur : chords[chordIndex].getSlurs()) {
			if (slur.getStart().getChord() == chords[chordIndex])
				return slur;
		}
		fail("No slur starts at chord " + chordIndex);
		return null;
	}
	
}
