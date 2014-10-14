package musicxmltestsuite.tests.musicxmlin;

import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base31c;

import org.junit.Test;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.position.MP;


public class Test31c
	implements Base31c, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		for (Tuple2<MP, Tempo> tempo : expectedTempos) {
			MP mp = tempo.get1();
			List<Tempo> temposAtBeat = score.getColumnHeader(mp.measure).getTempos().getAll(mp.beat);
			assertEquals(1, temposAtBeat.size());
			//TODO: add equals for tempo
			assertEquals(tempo.get2().getBaseBeat(), temposAtBeat.get(0).getBaseBeat());
			assertEquals(tempo.get2().getBeatsPerMinute(), temposAtBeat.get(0).getBeatsPerMinute());
		}
	}

}
