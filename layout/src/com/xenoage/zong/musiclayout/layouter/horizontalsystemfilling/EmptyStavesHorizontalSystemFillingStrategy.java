package com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling;

import com.xenoage.zong.musiclayout.spacing.system.SystemSpacing;

/**
 * This horizontal system filling strategy
 * changes the length of all staves of
 * the given system to their maximum width.
 * 
 * @author Andreas Wenger
 */
public class EmptyStavesHorizontalSystemFillingStrategy
	implements HorizontalSystemFillingStrategy {

	public static final EmptyStavesHorizontalSystemFillingStrategy instance =
		new EmptyStavesHorizontalSystemFillingStrategy();


	/**
	 * Changes the length of all staves of the given system to their maximum width.
	 */
	@Override public SystemSpacing computeSystemArrangement(SystemSpacing systemArrangement,
		float usableWidth) {
		systemArrangement.width = usableWidth;
		return systemArrangement;
	}

}
