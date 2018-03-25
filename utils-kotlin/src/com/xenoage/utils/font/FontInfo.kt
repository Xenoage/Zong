package com.xenoage.utils.font

import com.xenoage.utils.Cache
import com.xenoage.utils.Pt

/**
 * Cross-platform information about a font.
 */
data class FontInfo(
		val size: Pt,
		val style: FontStyle,
		val families: List<String>
) {

	companion object {

		val defaultFamilies = listOf("Times New Roman", "Linux Libertine", "Times")
		val defaultSize: Pt = 12f
		val defaultFontStyle = FontStyle.normal
		val defaultFontInfo = FontInfo(defaultFamilies, defaultSize, defaultFontStyle)

		val cache = Cache<FontInfo, FontInfo>(100)

		operator fun invoke(families: List<String> = defaultFamilies,
		                    size: Pt = defaultSize, style: FontStyle = defaultFontStyle): FontInfo {
			val fontInfo = FontInfo(size, style, families) //can be garbage collected if already in cache
			return cache[fontInfo, { fontInfo }]
		}

	}

}
