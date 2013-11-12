package com.xenoage.zong.musiclayout.notations;

import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;


/**
 * This class contains layout information
 * about a rest.
 *
 * @author Andreas Wenger
 */
public final class RestNotation
  implements Notation
{
  
  private final Rest element;
  private final ElementWidth width;
  
  
  /**
   * Creates a new RestElementLayout for the given
   * chord with the given width.
   */
  public RestNotation(Rest element, ElementWidth width)
  {
    this.element = element;
    this.width = width;
  }
  
  
  @Override public ElementWidth getWidth()
  {
    return width;
  }
  
  
  /**
   * Gets the rest.
   */
  @Override public Rest getMusicElement()
  {
    return element;
  }
  

}
