package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.group.StavesRange;

/**
 * Wrong test description: First group goes from staff 1 to 4, and there is no square bracket.
 * 
 * @author Andreas Wenger
 */
public interface Base41f
	extends Base {

	@Override default String getFileName() {
		return "41f-StaffGroups-Overlapping.xml";
	}

	BracketGroup[] expectedBracketGroups = {
		new BracketGroup(new StavesRange(0, 3), BracketGroup.Style.Bracket),
		new BracketGroup(new StavesRange(2, 4), BracketGroup.Style.Bracket),
	};

	BarlineGroup[] expectedBarlineGroups = {
		//the two groups are merged to one group
		new BarlineGroup(new StavesRange(0, 4), BarlineGroup.Style.Common),
	};

}
