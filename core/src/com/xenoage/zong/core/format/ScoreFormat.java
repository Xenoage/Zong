package com.xenoage.zong.core.format;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.zong.core.format.Defaults.defaultFont;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Wither;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.font.FontInfo;

/**
 * Default formats to be used in a score.
 *
 * @author Andreas Wenger
 */
@Const @Data @Wither public final class ScoreFormat {

	/** The default space between two staff lines in mm ("Rastralgröße" in German). */
	private final float interlineSpace;
	/** The default distance between the first line of the top system to the top page margin in mm. */
	private final float topSystemDistance;
	/** The default layout information for systems. */
	@NonNull private final SystemLayout systemLayout;
	/** Default staff layout information (may also be or contain null). */
	@MaybeNull public final IList<StaffLayout> staffLayouts;
	/** The default layout information for staves which have no own default layout. */
	@NonNull public final StaffLayout staffLayoutOther;
	/** The default font used for lyrics. */
	@NonNull public final FontInfo lyricFont;
	/** The style of measure numbering. */
	@NonNull public final MeasureNumbering measureNumbering;

	/** Default score format. */
	public static final ScoreFormat defaultValue = new ScoreFormat(1.6f, 15,
		SystemLayout.defaultValue, null, StaffLayout.defaultValue, defaultFont, MeasureNumbering.System);


	/**
	 * Gets the default layout information for the given staff, or null if undefined.
	 */
	public StaffLayout getStaffLayout(int staff) {
		if (staffLayouts != null && staff < staffLayouts.size()) {
			return staffLayouts.get(staff);
		}
		else {
			return null;
		}
	}

	/**
	 * Gets the default layout information for the given staff, or
	 * the default information for all other staves if unknown.
	 * Thus, null is never returned.
	 */
	public StaffLayout getStaffLayoutNotNull(int staff) {
		return notNull(getStaffLayout(staff), staffLayoutOther);
	}

}
