package com.xenoage.zong.musiclayout.spacer.system.fill;

import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * Changes the length of all staves of
 * the given system to their maximum width.
 * 
 * @author Andreas Wenger
 */
public class EmptyStaves
	implements SystemFiller {

	public static final EmptyStaves emptyStaves = new EmptyStaves();


	@Override public void compute(SystemSpacing system, float usableWidthMm) {
		system.widthMm = usableWidthMm;
	}

}
