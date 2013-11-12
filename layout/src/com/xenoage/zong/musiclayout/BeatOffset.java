package com.xenoage.zong.musiclayout;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;


/**
 * Offset of a beat in mm.
 * 
 * For instance, an offset of 3 and a beat of 2/4 means,
 * that the chord/rest on beat 2/4 begins
 * 3 mm after the barline of the measure.
 * 
 * The offset is in mm and not in interline spaces, so that
 * it can be used for a whole measure column without respect
 * to the sizes of its staves.
 * 
 * This class is comparable to other instances of this
 * class by comparing the beat.
 *
 * @author Andreas Wenger
 */
public final class BeatOffset
{
 
  private final Fraction beat;
  private final float offsetMm;
  
  
  /**
   * Creates a new {@link BeatOffset} for the given beat with
   * the given offset in mm.
   */
  public BeatOffset(Fraction beat, float offsetMm)
  {
    this.beat = beat;
    this.offsetMm = offsetMm;
  }
  
  
  /**
   * Gets the beat.
   */
  public Fraction getBeat()
  {
  	return beat;
  }
  
  
  /**
   * Gets the offset in mm.
   */
  public float getOffsetMm()
  {
  	return offsetMm;
  }
  
  
  /**
   * Returns a copy of this {@link SpacingElement}, but using the
   * given the offset in mm.
   */
  public BeatOffset withOffsetMm(float offsetMm)
  {
  	return new BeatOffset(beat, offsetMm);
  }
  
  
  /**
   * Shifts the offset by the given value.
   */
  public BeatOffset shiftOffsetMm(float deltaMm)
  {
  	return new BeatOffset(beat, offsetMm + deltaMm);
  }
  
  
  /**
   * Returns true, if the given object is a {@link BeatOffset}
   * that is numerically equal to this one, otherwise false.
   */
  @Override public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    else if (o instanceof BeatOffset)
    {
    	BeatOffset bo = (BeatOffset) o;
      return (this.beat.equals(bo.beat) && this.offsetMm == bo.offsetMm);
    }
    else
    {
      return false;
    }
  }
  
  
  @Override public String toString()
  {
  	return beat.toString() + " at " + offsetMm + " mm";
  }
  
  
}
