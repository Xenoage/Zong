package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlDynamics;

/**
 * Reads {@link Dynamic} from {@link MxlDynamics}.
 * 
 * @author Andreas Wenger
 */
public class DynamicsReader {

	public static Dynamic read(MxlDynamics mxlDynamics, StaffDetails staffDetails) {
		DynamicValue type = mxlDynamics.getElement();
		Positioning positioning = new PositioningReader(staffDetails).readFromAny(mxlDynamics);
		Dynamic dynamics = new Dynamic(type);
		dynamics.setPositioning(positioning);
		return dynamics;
	}
	
}
