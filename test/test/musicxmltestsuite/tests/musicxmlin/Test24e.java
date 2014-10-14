package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.mp0;
import static musicxmltestsuite.Utils.checkGraceChords;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base24e;

import org.junit.Test;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.position.MP;


public class Test24e
	implements Base24e, MusicXmlInTest {

	@Test public void test() {
		Score score = getScore();
		for (int i : range(2))
			checkGraceChords(score.getStaff(i), expectedStavesChords[i], true);
		//check start beats
		Fraction[][] startBeats = expectedStavesBeats;
		assertEquals(startBeats[0][0], MP.getMP(score.getVoice(mp0).getElement(0)).getBeat());
		assertEquals(startBeats[0][1], MP.getMP(score.getVoice(mp0).getElement(1)).getBeat());
		assertEquals(fr(2, 4), score.getVoice(mp0.withStaff(1)).getElement(0).getDuration()); //half rest
		assertEquals(startBeats[1][0], MP.getMP(score.getVoice(mp0.withStaff(1)).getElement(1)).getBeat());
		assertEquals(startBeats[1][1], MP.getMP(score.getVoice(mp0.withStaff(1)).getElement(2)).getBeat());
	}

}
