package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.zong.core.text.FormattedText.fText;

import java.util.List;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.stampings.FrameTextStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * Creates the {@link FrameTextStamping} for a part name
 * at the left side of a part.
 * 
 * @author Andreas Wenger
 */
public class PartNameStamper {
	
	public static final PartNameStamper partNameStamper = new PartNameStamper();
	
	public enum Style {
		Full,
		Abbreviated;
	}
	
	
	@MaybeNull public FrameTextStamping stamp(Part part, int firstStaffIndex,
		List<StaffStamping> systemStaves, Style style) {
		StaffStamping firstStaff = systemStaves.get(firstStaffIndex);
		StaffStamping lastStaff = systemStaves.get(firstStaffIndex + part.getStavesCount() - 1);
		String name = (style == Style.Full ? part.getName() : part.getAbbreviation());
		if (name == null || name.length() == 0)
			return null;
		//in the middle of the staves
		float top = firstStaff.positionMm.y;
		float bottom = lastStaff.positionMm.y + (lastStaff.linesCount - 1) * lastStaff.is;
		FormattedText text = fText(name, new FormattedTextStyle(firstStaff.is * 2.5f * 2.67f), //TODO
			Alignment.Right);
		float middle = (top + bottom) / 2 + text.getFirstParagraph().getMetrics().getAscent() / 3; //TODO correction of baseline. /3 looks good.
		return new FrameTextStamping(text, new Point2f(firstStaff.positionMm.x -
			firstStaff.is * 2.5f, middle), null); //TODO
	}

}
