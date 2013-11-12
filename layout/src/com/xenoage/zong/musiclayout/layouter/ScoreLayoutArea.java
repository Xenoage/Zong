package com.xenoage.zong.musiclayout.layouter;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.HorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.VerticalFrameFillingStrategy;


/**
 * An area in which a score should be layouted.
 * 
 * @author Andreas Wenger
 */
public class ScoreLayoutArea
{
	
	public final Size2f size;
	public final HorizontalSystemFillingStrategy hFill;
	public final VerticalFrameFillingStrategy vFill;
	
	
	private ScoreLayoutArea(Size2f size,
		HorizontalSystemFillingStrategy hFill, VerticalFrameFillingStrategy vFill)
	{
		this.size = size;
		this.hFill = hFill;
		this.vFill = vFill;
	}
	
	
	public static ScoreLayoutArea area(Size2f size,
		HorizontalSystemFillingStrategy hFill, VerticalFrameFillingStrategy vFill)
	{
		return new ScoreLayoutArea(size, hFill, vFill);
	}
	
	
	public static ScoreLayoutArea area(Size2f size)
	{
		return new ScoreLayoutArea(size, ScoreFrame.defaultHFill, ScoreFrame.defaultVFill);
	}
	
}
