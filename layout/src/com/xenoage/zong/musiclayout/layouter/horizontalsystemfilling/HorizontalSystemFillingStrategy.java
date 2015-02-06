package com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling;

import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.spacing.system.SystemSpacing;

/**
 * A horizontal system filling strategy
 * creates a new {@link SystemSpacing}
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

	public SystemSpacing computeSystemArrangement(SystemSpacing systemArrangement,
		float usableWidth);

}
