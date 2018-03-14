package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static musicxmltestsuite.tests.utils.ChordTest.getChordAt;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base43a;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.clef.ClefType;


public class Test43a
	implements Base43a, MusicXmlInTest {
	
	Score score = getScore();
	
	
	@Test public void test() {
		Score score = getScore();
		StavesList stavesList = score.getStavesList();
		assertEquals(1, stavesList.getParts().size());
		assertEquals(2, stavesList.getParts().get(0).getStavesCount());
		testStaff(0, ClefType.Companion.getClefTreble(), pi('F', 0, 4));
		testStaff(1, ClefType.Companion.getClefBass(), pi('B', 0, 2));
	}

	private void testStaff(int staff, ClefType clefType, Pitch pitch) {
		assertEquals(clefType, score.getMeasure(atMeasure(staff, 0)).getClefs().get(Companion.get_0()).getType());
		assertEquals(pitch, getChordAt(score, atBeat(staff, 0, 0, Companion.get_0())).getNotes().get(0).getPitch());
	}
	
}
