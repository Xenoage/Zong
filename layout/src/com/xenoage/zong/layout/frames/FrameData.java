package com.xenoage.zong.layout.frames;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.frames.background.Background;


/**
 * This class encapsulates basic data about frames of each type.
 * 
 * @author Andreas Wenger
 */
public final class FrameData
{
	
	/** Center point of the frame in mm, relative to its parent. */
  public final Point2f position; //
  
  /** Size of the frame in mm. */
  public final Size2f size;
  
  /** Ccw. rotation of the frame in degrees, relative to its parent. */
  public final float rotation;
  
  /** Background of the frame, or null. */
  @MaybeNull public final Background background;
  
  
  public FrameData(Point2f position, Size2f size)
  {
    this.position = position;
    this.size = size;
    this.rotation = 0;
    this.background = null;
  }
  
  
  public FrameData(Point2f position, Size2f size, float rotation, Background background)
  {
    this.position = position;
    this.size = size;
    this.rotation = rotation;
    this.background = background;
  }
  
  
  /**
   * Changes the position of this frame.
   */
  public FrameData withPosition(Point2f position)
  {
  	return new FrameData(position, size, rotation, background);
  }
  
  
  /**
   * Changes the size of this frame.
   */
  public FrameData withSize(Size2f size)
  {
  	return new FrameData(position, size, rotation, background);
  }
  
  
  /**
   * Changes the relative rotation of this frame.
   */
  public FrameData withRotation(float rotation)
  {
  	return new FrameData(position, size, rotation, background);
  }
  
  
  /**
   * Changes the background of this frame.
   */
  public FrameData withBackground(Background background)
  {
  	return new FrameData(position, size, rotation, background);
  }

}
