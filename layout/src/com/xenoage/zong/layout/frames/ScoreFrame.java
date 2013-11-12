package com.xenoage.zong.layout.frames;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.HorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling.StretchHorizontalSystemFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.NoVerticalFrameFillingStrategy;
import com.xenoage.zong.musiclayout.layouter.verticalframefilling.VerticalFrameFillingStrategy;


/**
 * A score frame is a frame that contains
 * a musical score.
 * 
 * A score frame can be linked to another score frame, where the score goes on,
 * if it does not have enough space in this one. But these links are saved in the
 * {@link Layout} class.
 * 
 * To get the {@link Score} and the {@link ScoreLayout} belonging to a score frame,
 * ask the parent {@link Layout}.
 * 
 * @author Andreas Wenger
 */
public class ScoreFrame
  extends Frame
{
  
  //alignment of the systems
	public final HorizontalSystemFillingStrategy hFill;
	public final VerticalFrameFillingStrategy vFill;
  
  //default values
  public static final HorizontalSystemFillingStrategy defaultHFill =
  	StretchHorizontalSystemFillingStrategy.getInstance();
  public static final VerticalFrameFillingStrategy defaultVFill =
  	NoVerticalFrameFillingStrategy.getInstance();
  
  
  
  /**
   * Creates a new score frame without any content. If must be linked
   * within a {@link ScoreFrameChainOLD} to be usable.
   */
  public ScoreFrame(FrameData data)
  {
    this(data, defaultHFill, defaultVFill);
  }
  
  
  private ScoreFrame(FrameData data,
  	HorizontalSystemFillingStrategy hFill, VerticalFrameFillingStrategy vFill)
  {
  	super(data);
  	this.hFill = hFill;
  	this.vFill = vFill;
  }
  
  
  /**
   * Converts the given position from frame space into
   * score layout space.
   * Both spaces use mm units, the difference is the origin:
   * While frames have their origin in the center, the
   * origin of a score layout is in the upper left corner.
   */
  public Point2f computeScoreLayoutPosition(Point2f framePosition)
  {
    Point2f ret = framePosition;
    ret = ret.add(data.size.width / 2, data.size.height / 2);
    return ret;
  }


	/**
	 * Sets the horizontal system filling strategy for this score frame.
	 */
	public ScoreFrame withHFill(
		HorizontalSystemFillingStrategy horizontalFillingStrategy)
	{
		return new ScoreFrame(data, horizontalFillingStrategy, vFill);
	}


	/**
	 * Sets the vertical frame filling strategy for this score frame.
	 */
	public ScoreFrame withVFill(VerticalFrameFillingStrategy verticalFillingStrategy)
	{
		return new ScoreFrame(data, hFill, verticalFillingStrategy);
	}
	
	
	/**
   * Gets the type of this frame.
   */
  @Override public FrameType getType()
  {
  	return FrameType.ScoreFrame;
  }
	
	
	/**
   * Changes the basic data of this frame.
   */
  @Override public ScoreFrame withData(FrameData data)
  {
  	return new ScoreFrame(data, hFill, vFill);
  }
	

}
