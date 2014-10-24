package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.atVoice;
import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base41a;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.chord.Chord;


public class Test41a
	implements Base41a, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		StavesList stavesList = score.getStavesList();
		List<Part> parts = stavesList.getParts();
		assertEquals(expectedNames.length, parts.size());
		for (int i : range(parts)) {
			assertEquals(expectedNames[i], parts.get(i).getName());
			int firstStaffIndex = stavesList.getPartStaffIndices(parts.get(i)).getStart();
			assertEquals(expectedPitches[i], getFirstPitch(score, firstStaffIndex));
		}
	}
	
	private Pitch getFirstPitch(Score score, int staff) {
		return ((Chord) score.getVoice(atVoice(staff, 0, 0)).getElement(0)).getNotes().get(0).getPitch();
	}
	
}
