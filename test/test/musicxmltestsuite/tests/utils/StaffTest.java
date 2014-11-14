package musicxmltestsuite.tests.utils;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Staff;


public class StaffTest {
	
	public static void testMeasureFilledBeats(Fraction[] expectedMeasureFilledBeats, Staff staff) {
		assertEquals(expectedMeasureFilledBeats.length, staff.getMeasures().size());
		for (int i : range(expectedMeasureFilledBeats)) {
			Fraction filledBeats = staff.getMeasure(i).getFilledBeats();
			assertEquals("Measure " + i, expectedMeasureFilledBeats[i], filledBeats);
		}
	}

}
