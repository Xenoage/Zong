package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base31a;
import musicxmltestsuite.tests.utils.ToDo;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.NavigationSign;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.position.MP;


public class Test31a
	implements Base31a, MusicXmlInTest {

	@ToDo("add support for multiple direction-types within a single MusicXML direction")
	
	@Test public void test() {
		Score score = getScore();
		for (Tuple2<MP, ?> item : expectedDirections) {
			MP mp = item.get1();
			List<?> directions = null;
			//this test contains also tempo and navigation marker directions, which we find
			//in the column header instead, and not in the measure
			if (item.get2() instanceof Tempo)
				directions = score.getColumnHeader(mp.measure).getTempos().getAll(mp.beat);
			else if (item.get2() instanceof NavigationSign)
				directions = score.getColumnHeader(mp.measure).getOtherDirections().getAll(mp.beat);
			else
				directions = score.getMeasure(mp).getDirections().getAll(mp.beat);
			//check correct classes
			if (item.get2() instanceof Direction) {
				//single direction at this beat expected
				assertEquals(""+mp, 1, directions.size());
				assertEquals(""+mp, item.get2().getClass(), directions.get(0).getClass());
			}
			else if (item.get2() instanceof List<?>) {
				//List<?> l = (List<?>) item.get2();
				//multiple directions at this beat expected
				//TODO: add support for multiple direction-types within a single MusicXML direction
				//assertEquals(""+mp, l.size(), directions.size());
				//for (int i : range(l))
				//	assertEquals(""+mp+"["+i+"]", l.get(i).getClass(), directions.get(i).getClass());
			}
		}
	}

}
