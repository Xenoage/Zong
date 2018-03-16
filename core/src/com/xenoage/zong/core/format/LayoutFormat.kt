package com.xenoage.zong.core.format

/**
 * Default formats for a layout.
 */
data class LayoutFormat(
		val leftPageFormat: PageFormat,
		val rightPageFormat: PageFormat
) {

	/**
	 * Gets the format of the page with the given [pageIndex].
	 */
	fun getPageFormat(pageIndex: Int) = if (pageIndex % 2 == 0) rightPageFormat else leftPageFormat

	companion object {
		/** Default layout format, consisting of the default [PageFormat] for both sides.  */
		val defaultValue = LayoutFormat(PageFormat.defaultValue, PageFormat.defaultValue)
	}


}
