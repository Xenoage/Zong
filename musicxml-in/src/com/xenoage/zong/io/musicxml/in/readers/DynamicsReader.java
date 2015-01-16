package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlDynamics;

/**
 * Reads {@link Dynamics} from {@link MxlDynamics}.
 * 
 * @author Andreas Wenger
 */
public class DynamicsReader {

	public static Dynamics read(MxlDynamics mxlDynamics, StaffDetails staffDetails) {
		DynamicsType type = mxlDynamics.getElement();
		Positioning positioning = new PositioningReader(staffDetails).readFromAny(mxlDynamics);
		Dynamics dynamics = new Dynamics(type);
		dynamics.setPositioning(positioning);
		return dynamics;
	}
	
}
