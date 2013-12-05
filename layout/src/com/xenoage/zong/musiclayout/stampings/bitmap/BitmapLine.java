package com.xenoage.zong.musiclayout.stampings.bitmap;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.math.Units;

/**
 * This class helps drawing nice lines on a bitmap,
 * like the screen or in a bitmap file.
 * 
 * Because a bitmap works with integer coordinates, no lines
 * thinner than 1 px can be drawn.
 * 
 * This class computes the best possible display of lines:
 * It ensures that each line is at least 1 px wide,
 * but is drawn with some transparency to fake the "thinner"-effect.
 *
 * @author Andreas Wenger
 */
@Const public final class BitmapLine {

	/** The width in mm, that fits best to the bitmap */
	public final float widthMm;
	/** The color, that fits best to the bitmap */
	public final Color color;


	/**
	 * Creates a {@link BitmapLine} with the given width in mm and
	 * the given color at the given scaling factor.
	 * @param scaling  the current scaling factor. e.g. 1 means 72 dpi, 2 means 144 dpi.
	 */
	public BitmapLine(float widthMm, Color color, float scaling) {
		//width
		float widthPxFloat = Units.mmToPx(widthMm, scaling);
		float widthPx = MathUtils.clampMin(Math.round(widthPxFloat), 1);
		this.widthMm = Units.pxToMm(widthPx, scaling);
		//color
		if (widthPxFloat < 1)
			this.color = new Color(color.r, color.g, color.b, (int) (color.a * widthPxFloat));
		else
			this.color = color;
	}

}
