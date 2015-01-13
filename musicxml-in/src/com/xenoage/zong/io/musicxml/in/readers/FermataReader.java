package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPositioning;

import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.io.musicxml.in.util.StaffDetails;
import com.xenoage.zong.musicxml.types.MxlFermata;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.enums.MxlUprightInverted;

/**
 * Reads a {@link Fermata} from a {@link MxlFermata}.
 * 
 * @author Andreas Wenger
 */
public class FermataReader {

	public static Fermata read(MxlFermata mxlFermata, StaffDetails staffDetails) {
		//determine position
		MxlPrintStyle printStyle = mxlFermata.getPrintStyle();
		MxlPosition position = (printStyle != null ? printStyle.getPosition() : null);
		Positioning positioning = readPositioning(position, staffDetails);
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
