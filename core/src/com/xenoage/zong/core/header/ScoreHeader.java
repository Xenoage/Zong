package com.xenoage.zong.core.header;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.music.layout.SystemBreak;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.utils.exceptions.IllegalMPException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.setExtend;
import static com.xenoage.utils.kernel.Range.rangeReverse;


/**
 * This class contains general information
 * about the musical data in a score.
 * 
 * It contains a list of elements that are used in all staves.
 * Such elements are key signatures, time signatures and
 * tempo changes for example.
 * 
 * There is also layout information, like system and
 * page breaks and system and staff distances.
 *
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
@Data public final class ScoreHeader {

	/** The parent score. */
	@NonNull private final Score parent;
	/** List of system layouts. May contain null elements for standardly layouted systems. */
	@NonNull private List<SystemLayout> systemLayouts;
	/** List of system layouts. Must have the same size as the number of measures in the parent score. */
	@NonNull private List<ColumnHeader> columnHeaders;
	
	
	/**
	 * Creates an empty {@link ScoreHeader}.
	 */
	public static ScoreHeader scoreHeader(Score parent) {
		return new ScoreHeader(parent, new ArrayList<SystemLayout>(), new ArrayList<ColumnHeader>());
	}


	/**
	 * Gets layout information for the staff with the given index
	 * within the system with the given index, or null if undefined.
	 */
	@MaybeNull public StaffLayout getStaffLayout(int systemIndex, int staffIndex) {
		SystemLayout systemLayout = getSystemLayout(systemIndex);
		if (systemLayout != null) {
			return systemLayout.getStaffLayout(staffIndex);
		}
		return null;
	}


	/**
	 * Gets layout information for the system with the given index,
	 * or null if undefined.
	 */
	@MaybeNull public SystemLayout getSystemLayout(int systemIndex) {
		if (systemIndex >= 0 && systemIndex < systemLayouts.size()) {
			return systemLayouts.get(systemIndex);
		}
		return null;
	}


	/**
	 * Gets the system index of the measure with the given index.
	 * Only forced system breaks are considered.
	 */
	public int getSystemIndex(int measureIndex) {
		int ret = 0;
		for (int i = 0; i <= measureIndex; i++) {
			Break br = getColumnHeader(i).getMeasureBreak();
			if (br != null && br.getSystemBreak() == SystemBreak.NewSystem)
				ret++;
		}
		return ret;
	}


	/**
	 * Sets the {@link SystemLayout} with the given index.
	 */
	public void setSystemLayout(int systemIndex, SystemLayout systemLayout) {
		setExtend(systemLayouts, systemIndex, systemLayout, null);
	}


	/**
	 * Gets the {@link ColumnHeader} at the given index, or null if there is none.
	 */
	public ColumnHeader getColumnHeader(int measureIndex) {
		if (measureIndex >= columnHeaders.size()) {
			throw new IllegalMPException(MP.atMeasure(measureIndex));
		}
		return columnHeaders.get(measureIndex);
	}


	/**
	 * Sets the {@link ColumnHeader} for the measure column with the given index.
	 */
	public void setColumnHeader(ColumnHeader columnHeader, int measureIndex) {
		setExtend(columnHeaders, measureIndex, columnHeader, null);
	}


	/**
	 * Adds the given number of empty measure columns.
	 */
	public void addEmptyMeasures(int measuresCount) {
		for (int i = 0; i < measuresCount; i++) {
			columnHeaders.add(new ColumnHeader(parent, columnHeaders.size()));
		}
	}
	
	
	/**
	 * Gets the last {@link TimeSignature} that is defined at or before the measure
	 * with the given index. If there is none, {@link TimeSignature#implicitSenzaMisura} returned.
	 * @param measureIndex  the index of the measure. May also be 1 measure after the last measure.
	 */
	public TimeSignature getTimeAtOrBefore(int measureIndex) {
		if (measureIndex == columnHeaders.size())
			measureIndex--;
		//search for last time
		for (int iMeasure : rangeReverse(measureIndex, 0)) {
			ColumnHeader column = columnHeaders.get(iMeasure);
			if (column.getTime() != null)
				return column.getTime();
		}
		//no key found. return default time.
		return TimeSignature.implicitSenzaMisura;
	}


}
