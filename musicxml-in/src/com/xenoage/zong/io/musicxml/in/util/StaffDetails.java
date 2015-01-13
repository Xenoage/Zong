package com.xenoage.zong.io.musicxml.in.util;

import com.xenoage.zong.io.musicxml.in.readers.Context;

/**
 * More information on a staff.
 * 
 * @author Andreas Wenger
 */
public class StaffDetails {
	public float tenthsMm;
	public int linesCount;
	
	public static StaffDetails fromContext(Context context, int staffIndexInPart) {
		StaffDetails ret = new StaffDetails();
		ret.tenthsMm = context.getTenthMm();
		ret.linesCount = context.getStaffLinesCount(staffIndexInPart);
		return ret;
	}
}
