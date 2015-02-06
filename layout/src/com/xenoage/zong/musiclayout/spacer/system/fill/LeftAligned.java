package com.xenoage.zong.musiclayout.spacer.system.fill;

import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * {@link SystemFiller} which does nothing.
 * Each system keeps its optimal width.
 * 
 * @author Andreas Wenger
 */
public class LeftAligned
	implements SystemFiller {

	public static final LeftAligned leftAligned = new LeftAligned();

	
	@Override public void compute(SystemSpacing system, float usableWidthMm) {
	}

}
