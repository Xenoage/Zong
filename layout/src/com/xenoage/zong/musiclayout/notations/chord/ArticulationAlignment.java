package com.xenoage.zong.musiclayout.notations.chord;

import com.xenoage.zong.core.music.chord.Articulation;


/**
 * The alignment of a single articulation assigned
 * to a chord: its vertical position, its
 * horizontal offset and its type.
 *
 * @author Andreas Wenger
 */
public final class ArticulationAlignment
{
  
  private final float yLP;
  private final float xOffsetIS;
  private final Articulation.Type type;
  
  
  /**
   * Creates a new {@link ArticulationAlignment}.
   * @param yLP        vertical position as a line position
   * @param xOffsetIS  horizontal offset in interline spaces
   * @param type       staccato, tenuto, ...
   */
  public ArticulationAlignment(float yLP, float xOffsetIS, Articulation.Type type)
  {
    this.yLP = yLP;
    this.xOffsetIS = xOffsetIS;
    this.type = type;
  }
  
  
  /**
   * Gets the vertical position of the articulation as a line position.
   */
  public float getYLP()
  {
    return yLP;
  }
  
  
  /**
   * Gets the horizontal offset of the articulation in interline spaces.
   */
  public float getXOffsetIS()
  {
    return xOffsetIS;
  }
  
  
  /**
   * Gets the type of the articulation.
   */
  public Articulation.Type getType()
  {
    return type;
  }
  

}
