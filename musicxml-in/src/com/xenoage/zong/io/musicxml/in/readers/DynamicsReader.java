package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPositioning;

import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;

/**
 * Reads {@link Dynamics} from {@link MxlDynamics}.
 * 
 * @author Andreas Wenger
 */
public class DynamicsReader {

	public static Dynamics read(MxlDynamics mxlDynamics, StaffDetails staffDetails) {
		DynamicsType type = mxlDynamics.getElement();
		MxlPrintStyle printStyle = mxlDynamics.getPrintStyle();
		MxlPosition position = (printStyle != null ? printStyle.getPosition() : null);
		Positioning positioning = readPositioning(position, staffDetails, mxlDynamics.getPlacement());
		Dynamics dynamics = new Dynamics(type);
		dynamics.setPositioning(positioning);
		return dynamics;
	}
	
}
