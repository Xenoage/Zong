package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.collections.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import musicxmltestsuite.tests.base.Base32a;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.position.MP;


public class Test32a
	implements Base32a, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		for (Tuple2<MP, ?> item : expectedAnnotations) {
			MP mp = item.get1();
			List<?> annotations = null;
			Chord chord = (Chord) score.getVoice(mp).getElementAt(mp.beat);
			assertNotNull(""+mp, chord);
			//in this test, the chords contain either directions or annotations
			if (false == chord.getDirections().isEmpty())
				annotations = chord.getDirections();
			else
				annotations = chord.getAnnotations();
			//check correct classes
			if (item.get2() instanceof Direction) {
				//single direction at this beat expected
				assertEquals(""+mp, 1, annotations.size());
				assertEquals(""+mp, item.get2().getClass(), annotations.get(0).getClass());
			}
			else if (item.get2() instanceof List<?>) {
				//list of annotations
				//we ignore the order here (MusicXML does not contain a order)
				assertEquals(""+mp, set((List<?>) item.get2()), set(annotations));
			}
			else {
				//single annotation
				assertEquals(""+mp, 1, annotations.size());
				assertEquals(""+mp, item.get2(), annotations.get(0));
			}
		}
	}

}
