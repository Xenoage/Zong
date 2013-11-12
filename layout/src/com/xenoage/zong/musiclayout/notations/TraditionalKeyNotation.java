package com.xenoage.zong.musiclayout.notations;

import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;


/**
 * This class contains layout information
 * about a traditional key signature.
 *
 * @author Andreas Wenger
 */
public final class TraditionalKeyNotation
  implements Notation
{
  
  private final TraditionalKey element;
  private final ElementWidth width;
  private final int linePositionC4;
  private final int linePositionMin;
  
  
  /**
   * Creates a new TraditionalKeyElementLayout for the given key.
   */
  public TraditionalKeyNotation(TraditionalKey element, ElementWidth width,
    int linePositionC4, int linePositionMin)
  {
    this.element = element;
    this.width = width;
    this.linePositionC4 = linePositionC4;
    this.linePositionMin = linePositionMin;
  }
  
  
  @Override public ElementWidth getWidth()
  {
    return width;
  }
  
  
  /**
   * Gets the key.
   */
  @Override public TraditionalKey getMusicElement()
  {
    return element;
  }

  
  public int getLinePositionC4()
  {
    return linePositionC4;
  }

  
  public int getLinePositionMin()
  {
    return linePositionMin;
  }
  

}
