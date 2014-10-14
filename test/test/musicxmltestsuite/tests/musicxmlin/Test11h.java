package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base11h;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.time.TimeType;

public class Test11h
	implements Base11h, MusicXmlInTest {

	@Test public void test() {
		Score score = getScore();
		//time signature must be senza misura
		assertEquals(TimeType.timeSenzaMisura, score.getColumnHeader(0).getTime().getType());
		//measure must contain 3 notes and have a length of 3/8
		assertEquals(3, score.getVoice(mp0).getElements().size());
		assertEquals(fr(3, 8), score.getVoice(mp0).getFilledBeats());
	}

}
