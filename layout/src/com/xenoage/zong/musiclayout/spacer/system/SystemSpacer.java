package com.xenoage.zong.musiclayout.spacer.system;

import static com.xenoage.utils.Optional.absent;
import static com.xenoage.utils.Optional.of;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.spacer.measure.ColumnSpacer.columnSpacer;

import java.util.List;

import com.xenoage.utils.Optional;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.layout.SystemBreak;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.notations.Notations;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;

/**
 * Arranges a list of measure columns into a {@link SystemSpacing}.
 * 
 * The systems will not be stretched to their full possible width.
 * This can be done in a later layouting step. However, a {@link LeadingSpacing}
 * is added at the beginning of the system.
 * 
 * This strategy also regards forced and prohibited system breaks
 * and custom system margins (left, right) and staff margins.
 * 
 * @author Andreas Wenger
 */
public class SystemSpacer {
	
	public static final SystemSpacer systemSpacer = new SystemSpacer();


	/**
	 * Arranges an optimum number of measures columns in a system, beginning at the given measure,
	 * if possible.
	 * @param context        the context of the layouter, with the {@link MP} set to the start measure
	 * @param usableSizeMm   the usable size within the score frame in mm
	 * @param offsetYMm      the vertical offset of the system in mm
	 * @param systemIndex    the global system index (over all frames)
	 * @param measureColumnSpacings  a list of all measure column spacings without leading spacings
	 * @param notations      the notations of the elements, needed when a column has to be recomputed
	 *                       because of a leading spacing
	 */
	public Optional<SystemSpacing> compute(Context context,
		Size2f usableSizeMm, float offsetYMm, int systemIndex, List<ColumnSpacing> measureColumnSpacings,
		Notations notations) {
		
		int startMeasure = context.mp.measure;
		
		//test if there is enough height for the system
		Score score = context.score;
		ScoreFormat scoreFormat = score.getFormat();
		ScoreHeader scoreHeader = score.getHeader();

		//compute staff heights
		float[] staffHeightsMm = new float[score.getStavesCount()];
		float totalStavesHeightsMm = 0;
		for (int iStaff : range(score.getStavesCount())) {
			Staff staff = score.getStaff(iStaff);
			float staffHeightMm = (staff.getLinesCount() - 1) * score.getInterlineSpace(iStaff);
			staffHeightsMm[iStaff] = staffHeightMm;
			totalStavesHeightsMm += staffHeightMm;
		}

		//compute staff distances
		float[] staffDistancesMm = new float[score.getStavesCount() - 1];
		float totalStavesDistancesMm = 0;
		for (int iStaff : range(1, staffHeightsMm.length - 1)) {
			StaffLayout staffLayout = scoreHeader.getStaffLayout(systemIndex, iStaff);
			float staffDistanceMm = 0;
			if (staffLayout != null) {
				//use custom staff distance
				staffDistanceMm = staffLayout.getDistance();
			}
			else {
				//use default staff distance
				staffDistanceMm = scoreFormat.getStaffLayoutNotNull(iStaff).getDistance();
			}
			staffDistancesMm[iStaff - 1] = staffDistanceMm;
			totalStavesDistancesMm += staffDistanceMm;
		}

		//enough space?
		if (offsetYMm + totalStavesHeightsMm + totalStavesDistancesMm > usableSizeMm.height) {
			//not enough space
			return absent();
		}

		//compute the usable width for the system
		float systemLeftMarginMm = getLeftMarginMm(systemIndex, scoreFormat, scoreHeader);
		float systemRightMarginMm = getRightMarginMm(systemIndex, scoreFormat, scoreHeader);
		float usableWidthMm = usableSizeMm.width - systemLeftMarginMm - systemRightMarginMm;

		//append system measure-by-measure, until there is no space any more
		//or until there are no measures left
		int measuresCount = score.getMeasuresCount();
		List<ColumnSpacing> system = alist();
		float usedWidthMm = 0;
		int iMeasure;
		while (startMeasure + system.size() < measuresCount) {
			iMeasure = startMeasure + system.size();

			//decide if to add a leading spacing to the current measure or not
			boolean firstMeasure = system.size() == 0;
			ColumnSpacing column;
			if (firstMeasure) {
				//first measure within this system: add leading elements (clef, time sig.)
				column = columnSpacer.compute(context, true /* leading! */, notations);
			}
			else {
				//otherwise: use the precomputed spacing (without leading spacing)
				column = measureColumnSpacings.get(iMeasure);
			}

			//try to add this measure to the current system. if there is no space left for
			//it, don't use it, and we are finished.
			if (false == canAppend(column, iMeasure, usableWidthMm, usedWidthMm, scoreHeader, firstMeasure)) {
				break;
			}
			else {
				usedWidthMm += column.getWidthMm();
				system.add(column);
			}
		}

		//we are finished
		if (system.size() == 0) {
			return absent(); //not enough space for the system on this area
		}
		else {
			SystemSpacing ret = new SystemSpacing(startMeasure, startMeasure + system.size() -
				1, system, systemLeftMarginMm, systemRightMarginMm, usedWidthMm, staffHeightsMm,
				staffDistancesMm, offsetYMm);
			return of(ret);
		}

	}

	/**
	 * Returns true, if the given measure can be appended to the currently computed system,
	 * otherwise false.
	 * It is not tested if there is enough vertical space, this must be done before.
	 */
	public boolean canAppend(ColumnSpacing column, int measureIndex, float usableWidthMm,
		float usedWidthMm, ScoreHeader scoreHeader, boolean isFirstMeasure) {

		//if a line break is forced, do it (but one measure is always allowed)
		Break br = scoreHeader.getColumnHeader(measureIndex).getMeasureBreak();
		if (br != null && br.getSystemBreak() == SystemBreak.NewSystem && false == isFirstMeasure)
			return false;

		//if a line break is prohibited, force the measure to be within this system
		if (br != null && br.getSystemBreak() == SystemBreak.NoNewSystem)
			return true;

		//enough horizontal space?
		float remainingWidthMm = usableWidthMm - usedWidthMm;
		if (remainingWidthMm < column.getWidthMm())
			return false;

		//ok, append the measure
		return true;
	}

	/**
	 * Returns the left margin of the given system (global index) in mm.
	 */
	private float getLeftMarginMm(int systemIndex, ScoreFormat scoreFormat, ScoreHeader scoreHeader) {
		SystemLayout systemLayout = scoreHeader.getSystemLayout(systemIndex);
		if (systemLayout != null) {
			//use custom system margin
			return systemLayout.getMarginLeft();
		}
		else {
			//use default system margin
			return scoreFormat.getSystemLayout().getMarginLeft();
		}
	}

	/**
	 * Returns the right margin of the given system (global index) in mm.
	 */
	private float getRightMarginMm(int systemIndex, ScoreFormat scoreFormat, ScoreHeader scoreHeader) {
		SystemLayout systemLayout = scoreHeader.getSystemLayout(systemIndex);
		if (systemLayout != null) {
			//use custom system margin
			return systemLayout.getMarginRight();
		}
		else {
			//use default system margin
			return scoreFormat.getSystemLayout().getMarginRight();
		}
	}

}
