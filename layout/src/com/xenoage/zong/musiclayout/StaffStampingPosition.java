package com.xenoage.zong.musiclayout;

import com.xenoage.zong.musiclayout.stampings.StaffStamping;


/**
 * A {@link StaffStampingPosition} is a reference
 * to a StaffStamping and a x-coordinate
 * in mm, as well as the index of its score frame.
 * 
 * @author Andreas Wenger
 */
public final class StaffStampingPosition
{
  
  private final StaffStamping staff;
  private final int frameIndex;
  private final float positionX;
  
  
  public StaffStampingPosition(StaffStamping staff, int frameIndex, float positionX)
  {
    this.staff = staff;
    this.frameIndex = frameIndex;
    this.positionX = positionX;
  }


  public StaffStamping getStaff()
  {
    return staff;
  }
  
  
  public int getFrameIndex()
  {
    return frameIndex;
  }
  
  
  public float getPositionX()
  {
    return positionX;
  }
  

}
