package com.xenoage.zong.renderer.canvas;


/**
 * Output format of rendering.
 * 
 * For bitmaps, special rendering algorithms may be needed,
 * e.g. to get beautiful staves without interpolation effects.
 * 
 * @author Andreas Wenger
 */
public enum CanvasFormat
{
	/** The output format is a vector format, like a PDF or when printed. */
	Vector,
	/** The output format is a bitmap format, e.g. rendering for screen or PNG file. */
	Bitmap;
}
