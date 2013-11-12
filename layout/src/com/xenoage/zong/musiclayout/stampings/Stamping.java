package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;


/**
 * Class for an stamping. Stampings can
 * be visible objects like notes, clefs, texts, but
 * also invisible objects like empty rooms between
 * staves and so on are possible.
 * 
 * Stamps were used in the early days of music notation to
 * paint the symbols. This class is called stamping, because it
 * is the result of placing a stamp, that means, in most cases,
 * a given symbol at a given position. 
 * 
 * Stampings can be painted of course. Each stamping
 * can delegate the painting to another class, e.g.
 * special renderers for this stamping.
 *
 * @author Andreas Wenger
 */
public abstract class Stamping
{
  

	public enum Level
	{
		/** empty space */
		EmptySpace,
		/** staff */
		Staff,
		/** notes, barlines, ... */
		Music,
		/** text, dynamic symbols, ... */
		Text;
	}
	public final Level level;
  
  /**
   * The parent staff stamping of this staff or null.
   * This is important for the renderer, when it needs some
   * information from the parent staff of this element.
   * But the stamping may also belong to more than only this staff.
   */
  public final StaffStamping parentStaff;
  
  /**
   * The musical element for which this stamping was created,
   * or null, if not availabe (e.g. for staves)
   * this may be another element than expected, e.g. an accidental layout
   * element may refer to a chord musical element.
   */
  public final MusicElement musicElement;

  /**
   * Bounding geometry.
   */
  public final Shape boundingShape;
  

  public Stamping(StaffStamping parentStaff, Level level, MusicElement musicElement,
  	Shape boundingShape)
  {
    this.level = level;
    this.parentStaff = parentStaff;
    this.musicElement = musicElement;
    this.boundingShape = boundingShape;
  }
  
  
  /**
   * Gets the type of this stamping.
   */
  public abstract StampingType getType();
  
  
}
