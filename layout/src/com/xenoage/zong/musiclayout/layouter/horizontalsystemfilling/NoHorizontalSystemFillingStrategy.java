package com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling;

import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * This horizontal system filling strategy
 * changes nothing. It simply reuses the
 * optimal layout for the systems that
 * was computed before.
 * 
 * @author Andreas Wenger
 */
public class NoHorizontalSystemFillingStrategy
	implements HorizontalSystemFillingStrategy {

	public static final NoHorizontalSystemFillingStrategy instance =
		new NoHorizontalSystemFillingStrategy();


	/**
	 * Nothing is changed. The given system arrangement is returned.
	 */
	@Override public SystemSpacing computeSystemArrangement(SystemSpacing systemArrangement,
		float usableWidth) {
		return systemArrangement;
	}

}
