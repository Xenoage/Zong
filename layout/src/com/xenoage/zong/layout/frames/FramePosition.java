package com.xenoage.zong.layout.frames;

import com.xenoage.utils.math.geom.Point2f;


/**
 * A frame position is a reference to a frame
 * and a position in mm in frame space.
 * 
 * @author Andreas Wenger
 */
public class FramePosition
{
  
  public final Frame frame;
  public final Point2f position;
  
  
  public FramePosition(Frame frame, Point2f position)
  {
    this.frame = frame;
    this.position = position;
  }
  

  public Frame getFrame()
  {
    return frame;
  }
  
  
  public Point2f getPosition()
  {
    return position;
  }
  

}
