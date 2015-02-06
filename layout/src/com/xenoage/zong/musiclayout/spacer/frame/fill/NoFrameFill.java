package com.xenoage.zong.musiclayout.spacer.frame.fill;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;

/**
 * {@link FrameFiller}, which does nothing.
 * The systems stay on their positions.
 * 
 * @author Andreas Wenger
 */
public class NoFrameFill
	implements FrameFiller {

	public static final NoFrameFill noFrameFill = new NoFrameFill();

	
	@Override public void compute(FrameSpacing frame, Score score) {
	}

}
