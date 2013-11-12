package com.xenoage.zong.musiclayout.layouter;

import com.xenoage.zong.core.Score;


/**
 * This class describes, what portions of a score
 * have to be computed and where to place the
 * layout.
 * 
 * @author Andreas Wenger
 * @deprecated UNUSED TODO
 */
@Deprecated public class LayoutRange
{
	
	/* private Score score;
	
	private Integer startMeasureIndex;
	private Integer endMeasureIndex;
	
	private Integer startScoreFrameIndex;
	private Integer endScoreFrameIndex; */
	
	
	/**
	 * Creates a new {@link LayoutRange}.
	 * @param score                 the score to layout
	 * @param startMeasureIndex     measure where to begin the layout (or null for measure 0)
	 * @param endMeasureIndex       measure where to end the layout (or null for till the end,
	 *                                
	 * @param startScoreFrameIndex  first score frame that will be used
	 * @param endScoreFrameIndex    last score frame that will be used
	 */
	public LayoutRange(Score score, Integer startMeasureIndex, Integer endMeasureIndex,
		Integer startScoreFrameIndex, Integer endScoreFrameIndex)
	{
		
	}
	

}
