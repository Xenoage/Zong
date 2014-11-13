package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import musicxmltestsuite.tests.base.Base46d;

import org.junit.Test;

import com.xenoage.zong.core.music.Staff;


public class Test46d
	implements Base46d, MusicXmlInTest {
	
	@Test public void test() {
		Staff staff = getFirstStaff();
		assertEquals(expectedMeasureBeats.length, staff.getMeasures().size());
		for (int i : range(expectedMeasureBeats))
			assertEquals(expectedMeasureBeats[i], staff.getMeasure(i).getFilledBeats());
	}
	
}
