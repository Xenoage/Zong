package com.xenoage.zong.core.format

/**
 * Default formats for a layout.
 */
data class LayoutFormat(
		val leftPageFormat: PageFormat,
		val rightPageFormat: PageFormat
) {

	/**
	 * Gets the format of the given page.
	 * @param pageIndex  the 0-based index of the page
	 */
	fun getPageFormat(pageIndex: Int) = if (pageIndex % 2 == 0) rightPageFormat else leftPageFormat

	companion object {
		/** Default layout format, consisting of the default [PageFormat] for both sides.  */
		val defaultValue = LayoutFormat(PageFormat.defaultValue, PageFormat.defaultValue)
	}


}
