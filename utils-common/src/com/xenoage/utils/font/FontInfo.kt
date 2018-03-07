package com.xenoage.utils.font

import com.xenoage.utils.SizePt

/**
 * Information about a font.
 *
 * This class allows to save multiple font names/families and allows
 * the attributes to be unset. It is independent of a specific API like
 * AWT or Android, and can thus be used on each device.
 */
data class FontInfo(var familiesOrNull: List<String>?,
                    val sizeOrNull: SizePt?,
                    val styleOrNull: FontStyle?) {

	init {
		//instead of empty font list, store null
		if (familiesOrNull?.size == 0)
			familiesOrNull = null
	}

	/**
	 * The list of families, or the default families if unset.
	 * The first entry is the preferred font, the alternative fonts can be found at the following entries.
	 */
	val families: List<String>
		get() = familiesOrNull ?: defaultFamilies

	/**
	 * The size of the font in pt, or the default size if unset.
	 */
	val size: SizePt
		get() = sizeOrNull ?: defaultSize

	/**
	 * The style of the font, or the default style if unset.
	 */
	val style: FontStyle
		get() = styleOrNull ?: defaultFontStyle

	companion object {
		val defaultFamilies = listOf("Times New Roman", "Linux Libertine", "Times")
		val defaultSize: SizePt = 12f
		val defaultFontStyle = FontStyle.normal
		val defaultValue = FontInfo(defaultFamilies, defaultSize, defaultFontStyle)
	}

}
