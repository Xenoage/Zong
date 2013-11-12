package com.xenoage.zong.musiclayout.notations;

import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;


/**
 * This class contains layout information
 * about a clef, like its width, line positio and
 * its scaling.
 *
 * @author Andreas Wenger
 */
public final class ClefNotation
  implements Notation
{
  
  private final Clef element;
  private final ElementWidth width;
  private final int linePosition;
  private final float scaling;
  
  
  /**
   * Creates a new ClefElementLayout for the given
   * chord with the given width, line position and scaling.
   */
  public ClefNotation(Clef element, ElementWidth width,
    int linePosition, float scaling)
  {
    this.element = element;
    this.width = width;
    this.linePosition = linePosition;
    this.scaling = scaling;
  }
  
  
  @Override public ElementWidth getWidth()
  {
    return width;
  }
  
  
  /**
   * Gets the line position of the clef.
   */
  public int getLinePosition()
  {
    return linePosition;
  }
  
  
  /**
   * Gets the clef.
   */
  @Override public Clef getMusicElement()
  {
    return element;
  }
  
  
  /**
   * Gets the scaling, which is needed e.g. for cue clefs.
   */
  public float getScaling()
  {
    return scaling;
  }
  

}
