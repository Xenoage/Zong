package musicxmltestsuite.tests.musicxmlin;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import musicxmltestsuite.tests.base.Base41c;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;


public class Test41c
	implements Base41c, MusicXmlInTest {
	
	private StavesList stavesList;
	
	@Before public void before() {
		Score score = getScore();
		stavesList = score.getStavesList();
	}
	
	@Test public void testPartNames() {
		List<Part> parts = stavesList.getParts();
		assertEquals(expectedPartNames.length, parts.size());
		for (int i : range(parts)) {
			assertEquals(expectedPartNames[i], parts.get(i).getName());
		}
	}
	
	@Test public void testBracketGroups() {
		List<BracketGroup> groups = stavesList.getBracketGroups();
		for (BracketGroup expectedGroup : expectedBracketGroups)
			assertContains(groups, expectedGroup);
		assertEquals(expectedBracketGroups.length, groups.size());
	}
	
	@Test public void testBarlineGroups() {
		List<BarlineGroup> groups = stavesList.getBarlineGroups();
		for (BarlineGroup expectedGroup : expectedBarlineGroups)
			assertContains(groups, expectedGroup);
		assertEquals(expectedBarlineGroups.length, groups.size());
	}
	
	private <T> void assertContains(List<T> list, T expectedElement) {
		if (false == list.contains(expectedElement))
			fail("Expected group not found: " + expectedElement);
	}
	
}
