package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.zong.core.position.MP.atVoice;
import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base33b;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;


public class Test33b
	implements Base33b, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		Chord chord1 = (Chord) score.getVoice(atVoice(0, 0, 0)).getElement(0);
		Chord chord2 = (Chord) score.getVoice(atVoice(0, 1, 0)).getElement(0);
		assertEquals(1, chord1.getSlurs().size());
		Slur tie = chord1.getSlurs().get(0);
		assertEquals(SlurType.Tie, tie.getType());
		List<SlurWaypoint> waypoints = tie.getWaypoints();
		assertEquals(2, waypoints.size());
		assertEquals(chord1, waypoints.get(0).getChord());
		assertEquals(chord2, waypoints.get(1).getChord());
	}
	
}
