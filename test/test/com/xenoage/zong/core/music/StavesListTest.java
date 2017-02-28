package com.xenoage.zong.core.music;

import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.StavesRange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.*;

/**
 * Tests for {@link StavesList}.
 * 
 * @author Andreas Wenger
 */
public class StavesListTest {
	
	@Test public void addBarlineGroupTest() {
		StavesList stavesList = create5Staves();
		//add barline groups from staves 0 to 2 and 1 to 3. should merge to 0 to 3
		stavesList.addBarlineGroup(new StavesRange(0, 2), BarlineGroup.Style.Common);
		stavesList.addBarlineGroup(new StavesRange(1, 3), BarlineGroup.Style.Common);
		assertEquals(1, stavesList.getBarlineGroups().size());
		assertEquals(new BarlineGroup(new StavesRange(0, 3), BarlineGroup.Style.Common),
			stavesList.getBarlineGroups().get(0));
	}
	
	private StavesList create5Staves() {
		StavesList stavesList = new StavesList();
		List<Part> parts = new ArrayList<>();
		List<Staff> staves = new ArrayList<>();
		for (int i : range(5)) {
			parts.add(new Part("Part " + i, "", 1, null));
			staves.add(Staff.staffMinimal());
		}
		stavesList.setParts(parts);
		stavesList.setStaves(staves);
		return stavesList;
	}

}
