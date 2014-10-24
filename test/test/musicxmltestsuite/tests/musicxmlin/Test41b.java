package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;

import java.util.List;

import musicxmltestsuite.tests.base.Base41b;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.StavesList;


public class Test41b
	implements Base41b, MusicXmlInTest {
	
	@Test public void test() {
		Score score = getScore();
		StavesList stavesList = score.getStavesList();
		List<Part> parts = stavesList.getParts();
		assertEquals(expectedNames.length, parts.size());
		for (int i : range(parts)) {
			assertEquals(expectedNames[i], parts.get(i).getName());
		}
	}
	
}
