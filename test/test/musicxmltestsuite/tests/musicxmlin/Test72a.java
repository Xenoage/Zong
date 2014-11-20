package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.ChordTest.getChordAt;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base72a;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

public class Test72a
	implements Base72a, MusicXmlInTest {

	private Score score = getScore();


	@Test public void testNotatedPitches() {
		for (int iStaff : range(3)) {
			for (int iMeasure : range(2)) {
				for (int iElement : range(4)) {
					MP mp = MP.atElement(iStaff, iMeasure, 0, iElement);
					Chord chord = getChordAt(score, mp);
					Pitch pitch = chord.getNotes().get(0).getPitch();
					assertEquals(""+mp, expectedNotatedPitches[iStaff][iMeasure * 4 + iElement], pitch);
				}
			}
		}
	}
	
	@Test public void testTransposes() {
		for (int iPart : range(3)) {
			Part part = score.getStavesList().getParts().get(iPart);
			PitchedInstrument instrument = (PitchedInstrument) part.getFirstInstrument();
			assertEquals("Part " + iPart, expectedTransposes[iPart], instrument.getTranspose());
		}
	}

}
