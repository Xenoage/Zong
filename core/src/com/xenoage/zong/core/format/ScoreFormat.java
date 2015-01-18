package com.xenoage.zong.core.format;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.getOrNull;
import static com.xenoage.utils.collections.CollectionUtils.setExtend;
import static com.xenoage.zong.core.format.Defaults.defaultFont;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.font.FontInfo;

/**
 * Default formats to be used in a score.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor @Data
public class ScoreFormat {
	
	public static final float defaultInterlineSpace = 1.6f;
	public static final float defaultTopSystemDistance = 15f;

	/** The default space between two staff lines in mm ("Rastralgröße" in German). */
	private float interlineSpace = defaultInterlineSpace;
	/** The default distance between the first line of the top system to the top page margin in mm. */
	private float topSystemDistance = defaultTopSystemDistance;
	/** The default layout information for systems. */
	@NonNull private SystemLayout systemLayout = new SystemLayout();
	/** Default staff layout information (may also be or contain null). */
	@MaybeNull private List<StaffLayout> staffLayouts = null;
	/** The default layout information for staves which have no own default layout. */
	@NonNull private StaffLayout staffLayoutOther = StaffLayout.defaultValue;
	/** The default font used for lyrics. */
	@NonNull private FontInfo lyricFont = defaultFont;
	/** The style of measure numbering. */
	@NonNull private MeasureNumbering measureNumbering = MeasureNumbering.System;


	/**
	 * Gets the default layout information for the given staff, or null if undefined.
	 */
	@MaybeNull public StaffLayout getStaffLayout(int staff) {
		return getOrNull(staffLayouts, staff);
	}

	/**
	 * Gets the default layout information for the given staff, or
	 * the default information for all other staves if unknown.
	 * Thus, null is never returned.
	 */
	@NonNull public StaffLayout getStaffLayoutNotNull(int staff) {
		return notNull(getStaffLayout(staff), staffLayoutOther);
	}
	
	/**
	 * Sets the {@link StaffLayout} of the staff with the given index.
	 */
	public void setStaffLayout(int staff, StaffLayout staffLayout) {
		staffLayouts = setExtend(staffLayouts, staff, staffLayout, null);
	}

}
