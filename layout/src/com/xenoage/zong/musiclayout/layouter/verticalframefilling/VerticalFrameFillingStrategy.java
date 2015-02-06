package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;

/**
 * A vertical frame filling strategy creates a new {@link FrameSpacing}
 * from the given one so that the vertical distribution of its systems changes.
 * 
 * For example, an implementation could increase the distance of the systems
 * so that the vertical space is completely used.
 * 
 * @author Andreas Wenger
 */
public interface VerticalFrameFillingStrategy
	extends ScoreLayouterStrategy {

	public FrameSpacing computeFrameArrangement(FrameSpacing frameArr, Score score);

}
