package com.xenoage.zong.layout;

import com.xenoage.utils.math.geom.Point2f;


/**
 * Layout position.
 * 
 * This class contains a position within a layout:
 * a page index and coordinates relative to the
 * upper left corner in mm.
 * 
 * @author Andreas Wenger
 */
public final class LP
{
  
	public final Layout layout;
  public final int pageIndex;
  public final Point2f position;
  
  
  public static LP lp(Layout layout, int pageIndex, Point2f position)
  {
    return new LP(layout, pageIndex, position);
  }
  
  
  private LP(Layout layout, int pageIndex, Point2f position)
  {
  	this.layout = layout;
    this.pageIndex = pageIndex;
    this.position = position;
  }
  

}
