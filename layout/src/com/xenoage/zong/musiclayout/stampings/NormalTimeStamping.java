package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.zong.core.music.time.NormalTime;


/**
 * Class for a normal time signature stamping.
 * It consists of a fraction, like "4/4" or "7/16".
 *
 * @author Andreas Wenger
 */
public final class NormalTimeStamping
  extends Stamping
{
 
	/** The horizontal position in mm. */
  public final float positionX;
  
  /** The normal time signature. */
  public final NormalTime normalTime;
  
  /** The horizontal offset of the numerator in interline spaces. */
  public final float numeratorOffset;
  
  /** The horizontal offset of the denominator in interline spaces. */
  public final float denominatorOffset;
  
  /** The gap between the digits in interline spaces. */
  public final float digitGap;
  
  
  public NormalTimeStamping(NormalTime normalTime,
    float positionX, StaffStamping parentStaff,
    float numeratorOffset, float denominatorOffset, float digitGap)
  {
    super(parentStaff, Level.Music, null, null);
    this.normalTime = normalTime;
    this.positionX = positionX;
    this.numeratorOffset = numeratorOffset;
    this.denominatorOffset = denominatorOffset;
    this.digitGap = digitGap;
  }
  
  
  /**
   * Gets the type of this stamping.
   */
  @Override public StampingType getType()
  {
  	return StampingType.NormalTimeStamping;
  }

}
