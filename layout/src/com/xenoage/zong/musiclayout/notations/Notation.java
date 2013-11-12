package com.xenoage.zong.musiclayout.notations;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;


/**
 * This is the interface for {@link MusicElement} layout
 * information classes.
 * 
 * It is called notation, because it contains the
 * positioned representation of the by then
 * ambiguous raw musical data.
 * 
 * It contains the width of an element
 * and additional layout information.
 * 
 * For example a class ChordNotation
 * can be written which contains the
 * alignment of its notes and accidentals.
 *
 * @author Andreas Wenger
 */
public interface Notation
{
 
  public MusicElement getMusicElement();
  
  
  public ElementWidth getWidth();

}
