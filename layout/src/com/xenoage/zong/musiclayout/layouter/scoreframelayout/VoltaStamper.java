package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.text.FormattedText.fText;

import java.util.List;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.VoltaStamping;

/**
 * Creates the {@link VoltaStamping} of a {@link Volta}.
 * 
 * @author Andreas Wenger
 */
public class VoltaStamper {
	
	public static final VoltaStamper voltaStamper = new VoltaStamper();
	
	
	/**
	 * Creates all volta stampings in the given system.
	 * Note that the last volta may still be open and has to be stamped again
	 * in the next system, since system breaks within voltas are possible.
	 */
	public List<VoltaStamping> stamp(StaffStamping systemFirstStaff,
		ScoreHeader header, FormattedTextStyle textStyle) {
		List<VoltaStamping> ret = alist();
		for (int iMeasure : systemFirstStaff.system.getMeasureIndices()) {
			ColumnHeader columnHeader = header.getColumnHeader(iMeasure);
			if (columnHeader.getVolta() != null) {
				ret.add(stamp(columnHeader.getVolta(), systemFirstStaff, textStyle));
			}
		}
		return ret;
	}

	/**
	 * Creates a {@link VoltaStamping} for the given volta.
	 * 
	 * The start and end measure may be outside the staff. If the start measure is outside the
	 * staff, no left hook and no caption is drawn, since the volta is continued.
	 * If the end measure is outside the staff, no right hook is drawn, since the
	 * volta is continued in the next system.
	 */
	public VoltaStamping stamp(Volta volta, StaffStamping staff,
		FormattedTextStyle textStyle) {
		boolean leftHook = true;
		boolean rightHook = volta.isRightHook();
		boolean caption = true;
		//clip start measure to staff
		Range systemMeasures = staff.system.getMeasureIndices();
		int start = volta.getMP().measure;
		if (start < systemMeasures.getStart()) {
			start = systemMeasures.getStart();
			leftHook = false;
			caption = false;
		}
		//clip end measure to staff
		int end = start + volta.getLength() - 1;
		if (end > systemMeasures.getStop()) {
			end = systemMeasures.getStop();
			rightHook = false;
		}
		//create stamping
		return stamp(volta, start, end, staff, textStyle, caption, leftHook, rightHook);
	}

	/**
	 * Creates a {@link VoltaStamping} for the given volta spanning
	 * from the given start measure to the given end measure on the given staff
	 * (global measure indices). Left and right hooks and the caption are optional.
	 * 
	 * The measure indices must be within the staff, otherwise an exception may
	 * be thrown.
	 */
	private VoltaStamping stamp(Volta volta, int startMeasureIndex,
		int endMeasureIndex, StaffStamping staff, FormattedTextStyle textStyle, boolean drawCaption,
		boolean drawLeftHook, boolean drawRightHook) {
		//get start and end x coordinate of measure
		float x1 = staff.system.getMeasureStartMm(startMeasureIndex) + staff.is / 2;
		float x2 = staff.system.getMeasureEndMm(endMeasureIndex) - staff.is / 2;
		//line position of volta line: 5 IS over top line
		float lp = (staff.linesCount - 1 + 5) * 2;
		//caption
		FormattedText caption = null;
		if (drawCaption && volta.getCaption().length() > 0) {
			caption = fText(volta.getCaption(), textStyle, Alignment.Left);
		}
		//create stamping
		return new VoltaStamping(lp, x1, x2, caption, drawLeftHook, drawRightHook, staff);
	}

}
