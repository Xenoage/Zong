package com.xenoage.zong.musiclayout.notations;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;


/**
 * Mock object for a {@link Notation}.
 * 
 * @author Andreas Wenger
 */
public class NotationMock
  implements Notation
{

  private MusicElement element;
  private ElementWidth width;
  
  
  /**
   * Creates a new {@link NotationMock} for the given
   * element with the given width.
   */
  public NotationMock(MusicElement element, ElementWidth width)
  {
    this.element = element;
    this.width = width;
  }
  
  
  @Override public ElementWidth getWidth()
  {
    return width;
  }
  
  
  @Override public MusicElement getMusicElement()
  {
    return element;
  }

  
}
