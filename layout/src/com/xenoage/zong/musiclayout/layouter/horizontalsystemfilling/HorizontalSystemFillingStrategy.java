package com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling;

import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;

/**
 * A horizontal system filling strategy
 * creates a new {@link SystemArrangement}
 * from the given one, applying
 * horizontal distribution changes.
 * 
 * For example, an implementation could stretch
 * the system so that it uses it's whole
 * usable width.
 * 
 * @author Andreas Wenger
 */
public interface HorizontalSystemFillingStrategy
	extends ScoreLayouterStrategy {

	public SystemArrangement computeSystemArrangement(SystemArrangement systemArrangement,
		float usableWidth);

}
