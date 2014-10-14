package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base21e;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.position.MP;

public class Test21e
	implements Base21e, MusicXmlInTest {

	@Test public void test() {
		Score score = getScore();
		//4/4 time in first measure (implicit)
		assertEquals(fr(4, 4), score.getColumnHeader(0).getTime().getType().getMeasureBeats());
		//first measure has only a 1/4 chord and a total length of 1/4
		assertEquals(1, score.getVoice(mp0).getElements().size());
		assertEquals(fr(1, 4), score.getVoice(mp0).getElement(0).getDuration());
		assertEquals(fr(1, 4), score.getMeasure(mp0).getFilledBeats());
		//second measure has two 1/4 chords and total length of 2/4
		MP m2 = mp0.withMeasure(1);
		assertEquals(2, score.getVoice(m2).getElements().size());
		assertEquals(fr(1, 4), score.getVoice(m2).getElement(0).getDuration());
		assertEquals(fr(1, 4), score.getVoice(m2).getElement(1).getDuration());
		assertEquals(fr(2, 4), score.getMeasure(m2).getFilledBeats());
	}

}
