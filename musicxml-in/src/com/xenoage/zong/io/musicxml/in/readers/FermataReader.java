package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlFermata;
import com.xenoage.zong.musicxml.types.enums.MxlUprightInverted;

/**
 * Reads a {@link Fermata} from a {@link MxlFermata}.
 * 
 * @author Andreas Wenger
 */
public class FermataReader {

	public static Fermata read(MxlFermata mxlFermata, StaffDetails staffDetails) {
		//determine position
		Positioning positioning = new PositioningReader(staffDetails).readFromAny(mxlFermata);
		if (positioning == null) {
			if (mxlFermata.getType() == MxlUprightInverted.Upright)
				positioning = Placement.Above;
			else if (mxlFermata.getType() == MxlUprightInverted.Inverted)
				positioning = Placement.Below;
		}
		Fermata fermata = new Fermata();
		fermata.setPositioning(positioning);
		return fermata;
	}
	
}
