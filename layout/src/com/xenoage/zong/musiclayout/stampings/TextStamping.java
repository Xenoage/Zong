package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.text.FormattedText;


/**
 * Base class for all text stampings.
 *
 * @author Andreas Wenger
 */
public abstract class TextStamping
	extends Stamping
{

	/** The formatted text. */
  public final FormattedText text;
  
  
  public TextStamping(@NeverNull FormattedText text, @MaybeNull StaffStamping parentStaff,
  	@MaybeNull MusicElement musicElement, @MaybeNull Shape boundingShape)
  {
    super(parentStaff, Level.Text, musicElement, boundingShape);
    this.text = text;
  }
  
  
  /**
   * Gets the type of this stamping.
   */
  @Override public StampingType getType()
  {
  	return StampingType.TextStamping;
  }
  
  
  /**
   * Gets the position within the frame in mm.
   */
  public abstract Point2f getPositionInFrame();
  
  
}
