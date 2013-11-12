package com.xenoage.zong.layout;

import static com.xenoage.utils.pdlib.PVector.pvec;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.FramePosition;


/**
 * One page within a page layout.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public final class Page
{
  
	/** The format of the page. */
	public final PageFormat format;
  
  /** The list of frames. */
  public final PVector<Frame> frames;
  
  
  public Page(PageFormat format)
  {
    this.format = format;
    this.frames = pvec();
  }
  
  
  private Page(PageFormat format, PVector<Frame> frames)
  {
    this.format = format;
    this.frames = frames;
  }
  
  
  /**
   * Adds the given frame.
   */
  public Page plusFrame(Frame frame)
  {
    return new Page(format, frames.plus(frame));
  }
  
  
  /**
   * Replaces the given frame, or deletes it if the given
   * new one is null.
   */
  public Page replaceFrame(Frame oldFrame, Frame newFrame)
  {
  	return new Page(format, frames.replaceOrMinus(oldFrame, newFrame));
  }
  
  
  /**
   * Changes the format of this page.
   */
  public Page withFormat(PageFormat format)
  {
  	return new Page(format, frames);
  }
  
  
  /**
   * Transforms the given coordinates in page space to
   * a frame position.
   * If there is no frame, null is returned.
   */
  public FramePosition computeFramePosition(Point2f p, Layout layout)
  {
    //since frames are painted in forward direction,
    //the last one is the highest one. so we have to
    //check for clicks in reverse order.
    for (int i = frames.size() - 1; i >= 0; i--)
    {
      FramePosition fp = frames.get(i).computeFramePosition(p, layout);
      if (fp != null)
      {
        return fp;
      }
    }
    return null;
  }
  
  
}
