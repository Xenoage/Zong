package com.xenoage.utils.math;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.math.geom.Size2i;

/**
 * Some functions to convert values into
 * different units.
 *
 * @author Andreas Wenger
 */
public class Units {

	public static final float mmToPx_1_1 = 1 * 72.0f / 25.4f * 1;
	public static final float pxToMm_1_1 = 1 / mmToPx_1_1;


	/**
	 * Converts the given mm into pixels,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static float mmToPx(float mm, float scaling) {
		return mm * 72.0f / 25.4f * scaling;
	}

	/**
	 * Converts the given mm into pixels,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static int mmToPxInt(float mm, float scaling) {
		return Math.round(mmToPx(mm, scaling));
	}

	/**
	 * Converts the given pixels into mm,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static float pxToMm(float px, float scaling) {
		return px / 720f * 254f / scaling;
	}

	/**
	 * Converts the given pixels into mm,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static int pxToMmInt(float px, float scaling) {
		return Math.round(pxToMm(px, scaling));
	}

	/**
	 * Converts the given mm into pixels,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static Point2f mmToPx(Point2f pMm, float scaling) {
		return new Point2f(mmToPx(pMm.x, scaling), mmToPx(pMm.y, scaling));
	}

	/**
	 * Converts the given mm into pixels,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static Point2i mmToPxInt(Point2f pMm, float scaling) {
		return new Point2i(Math.round(mmToPx(pMm.x, scaling)), Math.round(mmToPx(pMm.y, scaling)));
	}

	/**
	 * Converts the given pixels into mm,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static Point2f pxToMm(Point2f px, float scaling) {
		return new Point2f(pxToMm(px.x, scaling), pxToMm(px.y, scaling));
	}

	/**
	 * Converts the given mm into pixels,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static Size2f mmToPx(Size2f sizeMm, float scaling) {
		return new Size2f(mmToPx(sizeMm.width, scaling), mmToPx(sizeMm.height, scaling));
	}

	/**
	 * Converts the given mm into pixels,
	 * respecting the given scaling factorpxToMm
	 * relative to 72 dpi.
	 */
	public static Size2i mmToPxInt(Size2f sizeMm, float scaling) {
		return new Size2i(Math.round(mmToPx(sizeMm.width, scaling)), Math.round(mmToPx(sizeMm.height, scaling)));
	}

	/**
	 * Converts the given pixels into mm,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static Size2f pxToMm(Size2f sizePx, float scaling) {
		return new Size2f(pxToMm(sizePx.width, scaling), pxToMm(sizePx.height, scaling));
	}

	/**
	 * Converts the given pixels into mm,
	 * respecting the given scaling factor
	 * relative to 72 dpi.
	 */
	public static Size2f pxToMm(Size2i sizePx, float scaling) {
		return new Size2f(pxToMm(sizePx.width, scaling), pxToMm(sizePx.height, scaling));
	}

	private Units() {
	}

}
