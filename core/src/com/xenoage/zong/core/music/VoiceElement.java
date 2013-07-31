package com.xenoage.zong.core.music;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.position.MPElement;


/**
 * Interface for all classes that are child of a voice
 * and have a duration.
 * 
 * These are chords and rests.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public interface VoiceElement
	extends MPElement
{
	
	/**
   * Gets the duration of this element.
   */
  public Fraction getDuration();
  
  
  /**
   * Sets the duration of this element.
   */
  public void setDuration(Fraction duration);
  
  
  /**
	 * Gets the parent voice, or null.
	 */
	@Override public Voice getParent();
	
	
	/**
	 * Sets the parent voice, or null.
	 */
	public void setParent(Voice parent);
	
}
