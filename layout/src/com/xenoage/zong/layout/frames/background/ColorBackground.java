package com.xenoage.zong.layout.frames.background;

import com.xenoage.utils.graphics.color.ColorInfo;


/**
 * A single color background for a frame.
 * 
 * @author Andreas Wenger
 */
public final class ColorBackground
  implements Background
{
  
  public final ColorInfo color;
  
  
  /**
   * Creates a background with the given color.
   */
  public ColorBackground(ColorInfo color)
  {
    this.color = color;
  }
  
  
  /**
   * Gets the type of this background.
   */
  @Override public BackgroundType getType()
  {
  	return BackgroundType.ColorBackground;
  }
  

}
