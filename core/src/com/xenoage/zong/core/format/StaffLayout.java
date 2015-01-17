package com.xenoage.zong.core.format;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Layout information for a staff.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Data
public class StaffLayout {
	
  /** Default top distance: 10 mm. */
  public static final StaffLayout defaultValue = new StaffLayout(10);
  
  /** The distance between the bottom line of the previous staff to the top line
   * of this staff in mm. This value has no meaning for the first staff of a system. */
  private final float distance;

  
}