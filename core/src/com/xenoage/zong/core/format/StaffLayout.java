package com.xenoage.zong.core.format;

import lombok.Data;

import com.xenoage.utils.annotations.Const;


/**
 * Layout information for a staff.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class StaffLayout
{
  
  /** The distance between the bottom line of the previous staff to the top line
   * of this staff in mm. This value has no meaning for the first staff of a system. */
  private final float distance;
  
  /** Default top distance: 10 mm. */
  public static final StaffLayout defaultValue = new StaffLayout(10);

  
}