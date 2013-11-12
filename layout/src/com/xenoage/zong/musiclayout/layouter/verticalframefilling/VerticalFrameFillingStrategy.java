package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.FrameArrangement;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;


/**
 * A vertical frame filling strategy
 * creates a new {@link FrameArrangement}
 * from the given one so that the vertical distribution
 * of its systems changes.
 * 
 * For example, an implementation could increase
 * the distance of the systems to that the
 * vertical space is completely used.
 * 
 * @author Andreas Wenger
 */
public interface VerticalFrameFillingStrategy
	extends ScoreLayouterStrategy
{
	
	public abstract FrameArrangement computeFrameArrangement(FrameArrangement frameArr, Score score);

}
