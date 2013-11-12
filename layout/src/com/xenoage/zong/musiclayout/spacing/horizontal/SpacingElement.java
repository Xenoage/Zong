package com.xenoage.zong.musiclayout.spacing.horizontal;

import static com.xenoage.utils.base.NullUtils.throwNullArg;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;


/**
 * A spacing element stores the beat and the position (offset)
 * of a given {@link MusicElement} in a layout.
 * 
 * All units are measured in interline spaces.
 *
 * @author Andreas Wenger
 */
public final class SpacingElement
{
  
	/** The corresponding music element, e.g. a chord. May be null, e.g. when this
	 * element denotes the end point of the measure. */
  @MaybeNull public final MusicElement element;
  /** The beat where this music element can be found in the measure */
  @NeverNull public final Fraction beat;
  /** The horizontal offset of the element in interline spaces */
  public final float offset;
  /** True, if this is a grace element (0 duration) */
  public final boolean grace;
  
  
  public SpacingElement(MusicElement element, Fraction beat, float offset, boolean grace)
  {
  	throwNullArg(beat, offset);
    this.element = element;
    this.beat = beat;
    this.offset = offset;
    this.grace = grace;
  }
  
  
  public SpacingElement(MusicElement element, Fraction beat, float offset)
  {
  	this(element, beat, offset, false);
  }
  
  
  public SpacingElement withOffset(float offset)
  {
    return new SpacingElement(element, beat, offset, grace);
  }
  
  
  @Override public String toString()
  {
  	return element + " at " + beat + ": " + offset;
  }
  
  
  /**
   * Two spacing elements are equal, if the have the same element, beat and offset and grace.
   */
  @Override public boolean equals(Object o)
  {
  	if (o instanceof SpacingElement)
  	{
  		SpacingElement se = (SpacingElement) o;
  		return this.element == se.element && this.beat.equals(se.beat) &&
  			Delta.equals(this.offset, se.offset) && this.grace == se.grace;
  	}
  	else
  	{
  		return super.equals(o);
  	}
  }

}
