package com.xenoage.zong.musiclayout.stampings;


/**
 * Class for a cursor stamping
 * that belongs to one staff.
 *
 * @author Andreas Wenger
 */
public final class StaffCursorStamping
  extends Stamping
{

	/** The horizontal position of the cursor, relative to the left side of the staff. */
  public final float xMm;
  
  /** An additional offset for the cursor in interline spaces. */
  public final float offsetIs;
  
  
  public StaffCursorStamping(StaffStamping parentStaff, float xMm, float offsetIs)
  {
    super(parentStaff, Level.EmptySpace, null, null);
    this.xMm = xMm;
    this.offsetIs = offsetIs;
  }
  
  
  /**
   * Gets the type of this stamping.
   */
  @Override public StampingType getType()
  {
  	return StampingType.StaffCursorStamping;
  }
  
}
