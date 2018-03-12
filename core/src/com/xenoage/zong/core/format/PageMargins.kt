package com.xenoage.zong.core.format

/**
 * Class for page margins in mm:
 * left, right, top and bottom.
 */
class PageMargins(
		val left: Float,
		val right: Float,
		val top: Float,
		val bottom: Float
) {

	companion object {
		/** Default page margins of 20 mm.  */
		val defaultValue = PageMargins(20f, 20f, 20f, 20f)
	}

}
