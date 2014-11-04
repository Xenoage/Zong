package musicxmltestsuite.tests.base;

import com.xenoage.zong.core.music.group.BarlineGroup;
import com.xenoage.zong.core.music.group.BracketGroup;
import com.xenoage.zong.core.music.group.StavesRange;

/**
 * This piece has more part elements than the part-list section gives.
 * One can either convert all the parts present, but not listed in the part-list,
 * or simply not import / ignore them. 
 * 
 * @author Andreas Wenger
 */
public interface Base41h
	extends Base {

	@Override default String getFileName() {
		return "41h-TooManyParts.xml";
	}

}
