package com.xenoage.zong.musiclayout.stampings.bitmap;

import com.xenoage.utils.color.Color;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * This class contains some information that is
 * useful to draw a staff on a bitmap, like on the
 * screen or in a bitmap file.
 * 
 * It could be recomputed in each rendering
 * step, but caching this information saves
 * some time.
 * 
 * @author Andreas Wenger
 */
public final class StaffStampingBitmapInfo {

	private final StaffStamping parentStaff;

	//cache
	private BitmapStaff bitmapStaff = null;
	private float lastBitmapStaffScaling;
	private BitmapLine bitmapLine = null;
	private float lastBitmapLineScaling;


	public StaffStampingBitmapInfo(StaffStamping parentStaff) {
		this.parentStaff = parentStaff;
	}

	/**
	 * Gets the {@link BitmapLine} instance for nice rendering for the given scaling.
	 * @param scaling  the current scaling factor. e.g. 1 means 72 dpi, 2 means 144 dpi.
	 */
	public BitmapLine getBitmapLine(float scaling, float widthMm, Color color) {
		if (bitmapLine == null || scaling != lastBitmapLineScaling) {
			bitmapLine = new BitmapLine(widthMm, color, scaling);
			lastBitmapLineScaling = scaling;
		}
		return bitmapLine;
	}

	/**
	 * For screen display: Gets the {@link BitmapStaff} instance
	 * for nice rendering for the given scaling.
	 */
	public BitmapStaff getBitmapStaff(float scaling) {
		if (bitmapStaff == null || scaling != lastBitmapStaffScaling) {
			bitmapStaff = new BitmapStaff(parentStaff.linesCount, parentStaff.is,
				parentStaff.getLineWidth() / parentStaff.is, scaling);
			lastBitmapStaffScaling = scaling;
		}
		return bitmapStaff;
	}

}
