package com.xenoage.zong.renderer.background;

import java.awt.Paint;

import com.xenoage.utils.swing.color.AWTColorTools;
import com.xenoage.zong.layout.frames.background.Background;
import com.xenoage.zong.layout.frames.background.ColorBackground;


/**
 * AWT background renderer.
 * 
 * @author Andreas Wenger
 */
public class AWTBackgroundRenderer
{
	
	/**
   * Returns the Paint instance of the given background.
   */
  public static Paint getPaint(Background background)
  {
  	switch (background.getType())
  	{
  		case ColorBackground:
  			return getPaint((ColorBackground) background);
  	}
    return null;
  }
  
  
  /**
   * Returns the Paint instance of the given background.
   */
  public static Paint getPaint(ColorBackground background)
  {
    return AWTColorTools.createColor(background.color);
  }

}