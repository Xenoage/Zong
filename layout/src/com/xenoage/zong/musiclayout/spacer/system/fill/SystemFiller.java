package com.xenoage.zong.musiclayout.spacer.system.fill;

import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * Modifies a {@link SystemSpacing}, applying horizontal distribution changes.
 * 
 * For example, an implementation could stretch the system so that
 * it uses it's whole usable width.
 * 
 * @author Andreas Wenger
 */
public interface SystemFiller {

	public void compute(SystemSpacing system, float usableWidthMm);

}
