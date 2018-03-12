package com.xenoage.zong.core.format

import com.xenoage.utils.math.Size2f

/**
 * Class for a page format, containing
 * width and height in mm and margins.
 */
class PageFormat(
		val size: Size2f,
		val margins: PageMargins
) {

	/**
	 * The usable width of the page. This is the
	 * horizontal size minus the left and right margin.
	 */
	val useableWidth: Float
		get() = size.width - margins.left - margins.right


	/**
	 * The usable height of the page. This is the
	 * vertical size minus the top and bottom margin.
	 */
	val useableHeight: Float
		get() = size.height - margins.top - margins.bottom

	companion object {
		/** Default page format, DIN A4, default [PageMargins].  */
		val defaultValue = PageFormat(Size2f(210f, 297f), PageMargins.defaultValue)
	}

}
