package com.xenoage.zong.io.musicxml.in.readers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.musicxml.types.MxlStaffLayout;

/**
 * This class reads a staff-layout element into a
 * {@link StaffLayout} object.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class StaffLayoutReader {
	
	private final MxlStaffLayout mxlStaffLayout;
	private final float tenthsMm;
	
	/**
	 * Number of the staff.
	 * It refers to staff numbers within the part, from top to bottom in the system.
	 * Null means: for all staves (only if defined within the defaults element).
	 */
	@Getter private Integer number;
	private StaffLayout staffLayout;
	
	
	public StaffLayout read() {
		staffLayout = StaffLayout.defaultValue;

		//staff-distance
		Float mxlStaffDistance = mxlStaffLayout.getStaffDistance();
		if (mxlStaffDistance != null)
			staffLayout = new StaffLayout(tenthsMm * mxlStaffDistance);

		//number
		number = mxlStaffLayout.getNumber();
		
		return staffLayout;
	}

}
