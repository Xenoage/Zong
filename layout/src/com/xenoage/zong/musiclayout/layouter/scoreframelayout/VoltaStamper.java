package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.text.FormattedText.fText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.continued.ContinuedVolta;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.VoltaStamping;

/**
 * Creates the {@link VoltaStamping} of a {@link Volta}.
 * 
 * Since a volta can have line breaks inbetween, the open
 * voltas are stored in {@link ContinuedVolta}s.
 * 
 * @author Andreas Wenger
 */
public class VoltaStamper {
	
	public static final VoltaStamper voltaStamper = new VoltaStamper();
	
	
	/**
	 * Creates all volta stampings in the given system.
	 * All closed voltas are removed from the cache. The unclosed voltas (which have to
	 * be continued on the next system or frame) remain in the cache (or are added, if they are new).
	 */
	public List<VoltaStamping> stamp(int systemIndex, SystemSpacing system,
		ScoreHeader header, StaffStampings staffStampings, List<ContinuedVolta> openVoltasCache,
		FormattedTextStyle textStyle) {
		ArrayList<VoltaStamping> ret = alist();
		//find new voltas beginning in this system
		for (int iMeasure = 0; iMeasure < system.columns.size(); iMeasure++) {
			int scoreMeasure = system.getStartMeasureIndex() + iMeasure;
			ColumnHeader columnHeader = header.getColumnHeader(scoreMeasure);
			if (columnHeader.getVolta() != null) {
				openVoltasCache.add(new ContinuedVolta(columnHeader.getVolta(), scoreMeasure, 0)); //staff 0: TODO
			}
		}
		//draw voltas in the cache, and remove them if closed in this system
		int endMeasureIndex = system.getEndMeasureIndex();
		for (Iterator<ContinuedVolta> itV = openVoltasCache.iterator(); itV.hasNext();) {
			ContinuedVolta volta = itV.next();
			ret
				.add(voltaStamper.createVoltaStamping(volta.element,
					volta.startMeasureIndex, staffStampings.get(systemIndex, volta.getStaffIndex()),
					textStyle));
			if (volta.startMeasureIndex + volta.element.getLength() - 1 <= endMeasureIndex) {
				//volta is closed
				itV.remove();
			}
		}
		return ret;
	}
	

	/**
	 * Creates a {@link VoltaStamping} for the given volta, beginning at
	 * the given start measure index on the given staff. The end measure index
	 * is computed using the length of the volta element.
	 * 
	 * The start and end measure may be outside the staff. If the start measure is outside the
	 * staff, no left hook and no caption is drawn, since the volta is continued.
	 * If the end measure is outside the staff, no right hook is drawn, since the
	 * volta is continued in the next system.
	 */
	public VoltaStamping createVoltaStamping(Volta volta, int startMeasureIndex, StaffStamping staff,
		FormattedTextStyle textStyle) {
		boolean leftHook = true;
		boolean rightHook = volta.isRightHook();
		boolean caption = true;
		//clip start measure to staff
		Range systemMeasures = staff.system.getMeasureIndices();
		int start = startMeasureIndex;
		if (start < systemMeasures.getStart()) {
			start = systemMeasures.getStart();
			leftHook = false;
			caption = false;
		}
		//clip end measure to staff
		int end = startMeasureIndex + volta.getLength() - 1;
		if (end > systemMeasures.getStop()) {
			end = systemMeasures.getStop();
			rightHook = false;
		}
		//create stamping
		return createVoltaStamping(volta, start, end, staff, textStyle, caption, leftHook, rightHook);
	}

	/**
	 * Creates a {@link VoltaStamping} for the given volta spanning
	 * from the given start measure to the given end measure on the given staff
	 * (global measure indices). Left and right hooks and the caption are optional.
	 * 
	 * The measure indices must be within the staff, otherwise an exception may
	 * be thrown.
	 */
	private VoltaStamping createVoltaStamping(Volta volta, int startMeasureIndex,
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
