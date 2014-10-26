package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.group.StavesRange;

/**
 * Two properly nested part groups: One group (with a square bracket) goes from
 * staff 2 to 4) and another group (with a curly bracket) goes from staff 3 to 4. 
 * 
 * @author Andreas Wenger
 */
public interface Base41d
	extends Base {

	@Override default String getFileName() {
		return "41d-StaffGroups-Nested.xml";
	}


	BracketGroup[] expectedBracketGroups = {
		new BracketGroup(new StavesRange(1, 3), BracketGroup.Style.Line),
		new BracketGroup(new StavesRange(2, 3), BracketGroup.Style.Bracket),
	};

	BarlineGroup[] expectedBarlineGroups = {
		new BarlineGroup(new StavesRange(1, 3), BarlineGroup.Style.Common),
		//second barline group is contained in the first one
	};
	
}
