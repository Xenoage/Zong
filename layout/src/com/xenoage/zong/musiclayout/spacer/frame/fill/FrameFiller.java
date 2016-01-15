package com.xenoage.zong.musiclayout.spacer.frame.fill;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;

/**
 * Modifies a {@link FrameSpacing}, applying vertical distribution changes.
 * 
 * For example, an implementation could increase the distance of the systems
 * so that the vertical space is completely used.
 * 
 * @author Andreas Wenger
 */
public interface FrameFiller {

	public void compute(FrameSpacing frame, Score score);

}
