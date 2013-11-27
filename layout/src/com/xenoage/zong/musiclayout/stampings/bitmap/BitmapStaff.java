package com.xenoage.zong.musiclayout.stampings.bitmap;

import static com.xenoage.utils.math.Units.mmToPx;
import static com.xenoage.utils.math.Units.pxToMm;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Units;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * This class helps drawing nice staves on a bitmap,
 * like the screen or a bitmap file.
 * 
 * Because the bitmap works with integer
 * coordinates, a staff could look very ugly,
 * when it uses for example the interline spaces
 * 2-1-2-1-2. Just view a score in Adobe Reader 7
 * for example and the need of a better display
 * is obvious.
 * 
 * This class computes the best possible
 * display of staves on a bitmap: It ensures
 * that each interline space has the same size.
 * Unfortunately this also means that the displayed
 * size is not really the actual one, but an
 * approximation. But it's really much better
 * than just rounding to integer interline spaces.
 * 
 * When the staff is too small, a rectangle should be drawn
 * (called a "simplified staff").
 * In this case the interline space if a float value
 * and the height of the staff is just rounded
 * to the next integer value.
 *
 * @author Andreas Wenger
 */
@Const public class BitmapStaff {

	/** The width of a px in mm */
	public final float pxMm;

	/** True, if a simplified staff (just a filled rectangle) should be drawn */
	public final boolean isSimplifiedStaff;

	/** The adjusted height of the staff in mm, that fits best to the screen.
	 * The half top line and half bottom line are, as always in a "staff height", not included! */
	public final float heightMm;

	/** The height of a line in mm. This value may be less than 1. If you need the displayed height
	 * of the line, use the {@link BitmapLine} class. */
	public final float lineHeightMm;

	/** The height scaling factor of the staff, that fits best to the screen */
	public final float heightScaling;

	/** The adjusted interline space of the staff in mm, that fits best to the screen */
	public final float interlineSpaceMm;

	/** The additional vertical offset of the staff in mm, that fits best to the screen. */
	public final float yOffsetMm;

	/** The vertical position in mm, relative to the normal y-position
	 * of the {@link StaffStamping}, of line position 0 (bottom line).
	 * This is the y-offset plus the adjusted staff height and is just stored
	 * for performance reasons. */
	public final float lp0Mm;


	/**
	 * Creates a {@link BitmapStaff} with the given number of lines,
	 * interline space in mm, line width (in interline spaces) and the given scaling.
	 * @param scaling  the current scaling factor. e.g. 1 means 72 dpi, 2 means 144 dpi.
	 */
	public BitmapStaff(int lines, float realInterlineSpaceMm, float lineWidthIS, float scaling) {
		this.pxMm = pxToMm(1, scaling);
		float realHeightMm = realInterlineSpaceMm * (lines - 1);
		float interlineSpacePxFloat = mmToPx(realInterlineSpaceMm, scaling);
		//height of a line (may be smaller than 1)
		this.lineHeightMm = pxToMm(lineWidthIS * interlineSpacePxFloat, scaling);
		//simplified staff?
		this.isSimplifiedStaff = (interlineSpacePxFloat < 2);
		if (this.isSimplifiedStaff) {
			//no place for lines, the staff is displayed too small
			//just round the height to the next integer value
			//explanation: Lines are only useful, if there is (at least) 1-pixel-line, then
			//a 1-pixel space, and so on.
			//if the normalHeightPxFloat (because of the
			//above explanation) is smaller than this value, there can be no
			//useful display with lines, but a rectangle should be drawn.

			//use real interline space
			this.interlineSpaceMm = realInterlineSpaceMm;
			this.heightMm = this.interlineSpaceMm * (lines - 1);
			//no vertical offset is needed
			this.yOffsetMm = 0;
		}
		else {
			//ensure equal interline spaces
			int interlineSpacePx = Math.round(interlineSpacePxFloat);
			this.interlineSpaceMm = pxToMm(interlineSpacePx, scaling);
			this.heightMm = this.interlineSpaceMm * (lines - 1);
			//compute vertical offset. if for example the staff is 0.4 mm "too high",
			//move it 0.2 mm up. Thanks for the idea to Andreas Schmid.
			float yOffsetPx = Math.round(Units.mmToPx((realHeightMm - this.heightMm) / 2, scaling));
			this.yOffsetMm = Units.pxToMm(yOffsetPx, scaling);
		}
		//height scaling factor
		this.heightScaling = heightMm / realHeightMm;
		//cache
		this.lp0Mm = yOffsetMm + heightMm;
	}

	/**
	 * Gets the vertical position in mm, relative to the normal y-position
	 * of the {@link StaffStamping}, of the given line position.
	 */
	public float getLPMm(float lp) {
		return lp0Mm - lp / 2 * interlineSpaceMm;
	}

}
