package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static musicxmltestsuite.tests.utils.ChordTest.getChordAt;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base72c;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;

public class Test72c
	implements Base72c, MusicXmlInTest {

	private Score score = getScore();


	@Test public void testNotatedPitches() {
		for (int iMeasure : range(expectedTransposes.length)) {
			MP mp = MP.atElement(0, iMeasure, 0, 0);
			Chord chord = getChordAt(score, mp);
			Pitch pitch = chord.getNotes().get(0).getPitch();
			assertEquals(""+mp, expectedNotatedPitch, pitch);
		}
	}
	
	@Test public void testTransposes() {
		//start instrument
		Part part = score.getStavesList().getParts().get(0);
		PitchedInstrument instrument = (PitchedInstrument) part.getFirstInstrument();
		assertEquals(expectedTransposes[0], instrument.getTranspose());
		//instrument change in measure 1
		InstrumentChange instrChange = score.getMeasure(MP.atMeasure(0, 1)).getInstrumentChanges().get(_0);
		instrument = (PitchedInstrument) instrChange.getInstrument();
		assertEquals(expectedTransposes[1], instrument.getTranspose());
	}

}
