package com.xenoage.zong.musiclayout.stampings;


/**
 * Class for a cursor stamping that belongs to a consecutive
 * range of staves (often the whole system).
 *
 * @author Andreas Wenger
 */
public final class SystemCursorStamping
  extends Stamping
{

	/** The top staff stamping, where the cursor begins. */
	public final StaffStamping topStaff;
	
	/** The bottom staff stamping, where the cursor ends. */
	public final StaffStamping bottomStaff;
	
	/** The horizontal position of the cursor, relative to the left side of the top staff. */
	public final float x;
  
  
  public SystemCursorStamping(StaffStamping topStaff, StaffStamping bottomStaff, float x)
  {
    super(topStaff, Level.EmptySpace, null, null);
    this.topStaff = topStaff;
    this.bottomStaff = bottomStaff;
    this.x = x;
  }
  
  
  /**
   * Gets the type of this stamping.
   */
  @Override public StampingType getType()
  {
  	return StampingType.SystemCursorStamping;
  }
  
}
