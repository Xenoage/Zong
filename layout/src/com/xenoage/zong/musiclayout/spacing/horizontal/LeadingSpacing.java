package com.xenoage.zong.musiclayout.spacing.horizontal;

import com.xenoage.utils.pdlib.Vector;



/**
 * This class contains the leading spacing
 * of a measure of a single staff.
 * 
 * This spacing contains for example elements
 * like initial clefs and key signatures.
 *
 * @author Andreas Wenger
 */
public final class LeadingSpacing
{
  
  private final Vector<SpacingElement> leadingSpacing;
  private final float width;
  
  
  /**
   * Creates a leading spacing for an empty measure.
   */
  public LeadingSpacing(Vector<SpacingElement> leadingSpacing, float width)
  {
    this.leadingSpacing = leadingSpacing;
    this.width = width;
  }
  
  
  /**
   * Gets the spacing elements of this measure leading.
   */
  public Vector<SpacingElement> getSpacingElements()
  {
    return leadingSpacing;
  }
  
  
  /**
   * Gets the width of this leading spacing
   * in interline spaces.
   */
  public float getWidth()
  {
    return width;
  }
  

}
