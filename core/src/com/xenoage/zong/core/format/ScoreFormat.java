package com.xenoage.zong.core.format;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.getOrNull;
import static com.xenoage.utils.collections.CollectionUtils.setExtend;
import static com.xenoage.zong.core.format.Defaults.defaultFont;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.font.FontInfo;

/**
 * Default formats to be used in a score.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Data
public class ScoreFormat {

	/** The default space between two staff lines in mm ("Rastralgröße" in German). */
	private float interlineSpace;
	/** The default distance between the first line of the top system to the top page margin in mm. */
	private float topSystemDistance;
	/** The default layout information for systems. */
	@NonNull private SystemLayout systemLayout;
	/** Default staff layout information (may also be or contain null). */
	@MaybeNull private List<StaffLayout> staffLayouts;
	/** The default layout information for staves which have no own default layout. */
	@NonNull private StaffLayout staffLayoutOther;
	/** The default font used for lyrics. */
	@NonNull private FontInfo lyricFont;
	/** The style of measure numbering. */
	@NonNull private MeasureNumbering measureNumbering;

	/** Default score format. */
	public static final ScoreFormat defaultValue = new ScoreFormat(1.6f, 15,
		new SystemLayout(), null, StaffLayout.defaultValue, defaultFont, MeasureNumbering.System);


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
