package com.xenoage.zong.musiclayout;

import com.xenoage.utils.math.geom.Point2f;


/**
 * This class contains a position within a score layout,
 * not in musical sense but in metric coordinates: frame index
 * and coordinates relative to the upper left corner in mm.
 * 
 * @author Andreas Wenger
 */
public final class ScoreLP
{
  
  public final int frameIndex;
  public final Point2f pMm;
  
  
  public ScoreLP(int frameIndex, Point2f pMm)
  {
    this.frameIndex = frameIndex;
    this.pMm = pMm;
  }
  

}
