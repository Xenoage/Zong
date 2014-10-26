package musicxmltestsuite.tests.utils;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;


/**
 * Tests on {@link StavesList} data.
 * 
 * @author Andreas Wenger
 */
public class StavesListTest {
	
	public static void checkPartNames(StavesList stavesList, String[] expectedPartNames) {
		List<Part> parts = stavesList.getParts();
		assertEquals(expectedPartNames.length, parts.size());
		for (int i : range(parts)) {
			assertEquals(expectedPartNames[i], parts.get(i).getName());
		}
	}
	
	public static void checkBracketGroups(StavesList stavesList, BracketGroup[] expectedBracketGroups) {
		List<BracketGroup> groups = stavesList.getBracketGroups();
		for (BracketGroup expectedGroup : expectedBracketGroups)
			assertContains(groups, expectedGroup);
		assertEquals(expectedBracketGroups.length, groups.size());
	}
	
	public static void checkBarlineGroups(StavesList stavesList, BarlineGroup[] expectedBarlineGroups) {
		List<BarlineGroup> groups = stavesList.getBarlineGroups();
		for (BarlineGroup expectedGroup : expectedBarlineGroups)
			assertContains(groups, expectedGroup);
		assertEquals(expectedBarlineGroups.length, groups.size());
	}
	
	private static <T> void assertContains(List<T> list, T expectedElement) {
		if (false == list.contains(expectedElement))
			fail("Expected group not found: " + expectedElement);
	}

}
