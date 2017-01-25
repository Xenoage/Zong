package musicxmltestsuite.tests.musicxmlin;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.rest.Rest;
import musicxmltestsuite.tests.base.Base21f;
import org.junit.Test;

import java.util.List;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.mp0;
import static org.junit.Assert.*;

public class Test21f
	implements Base21f, MusicXmlInTest {

	@Test public void test() {
		Score score = getScore();
		//1/4 with 3 notes at beat 0
		List<VoiceElement> e = score.getVoice(mp0).getElements();
		Chord chord = (Chord) e.get(0);
		assertEquals(3, chord.getNotes().size());
		assertEquals(fr(1, 4), chord.getDuration());
		//followed by 2 rests, 1/4 and 2/4
		assertEquals(fr(1, 4), ((Rest) e.get(1)).getDuration());
		assertEquals(fr(2, 4), ((Rest) e.get(2)).getDuration());
		//segno at beat 1/4 in column (moved to the end of the measure, since we accept no mid-measure segnos)
		Direction segno = (Segno) score.getColumnHeader(0).getNavigationOrigin();
		assertNotNull(segno);
		//dynamics p at beat 1/4 in measure
		Dynamic dynamics = (Dynamic) score.getMeasure(mp0).getDirections().get(fr(1, 4));
		assertNotNull(dynamics);
		assertEquals(DynamicValue.p, dynamics.getValue());
	}

}
