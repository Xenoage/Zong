package com.xenoage.zong.musiclayout.layouter.verticalframefilling;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.FrameArrangement;


/**
 * This vertical frame filling strategy changes nothing.
 * It simply returns the given frame arrangement.
 * 
 * @author Andreas Wenger
 */
public class NoVerticalFrameFillingStrategy
  implements VerticalFrameFillingStrategy
{
	
	private static NoVerticalFrameFillingStrategy instance = null;
	
	
	public static NoVerticalFrameFillingStrategy getInstance()
	{
		if (instance == null)
			instance = new NoVerticalFrameFillingStrategy();
		return instance;
	}
	
	
	@Override public FrameArrangement computeFrameArrangement(
		FrameArrangement frameArr, Score score)
	{
		return frameArr;
	}
  

}
