package com.xenoage.zong.core.text

import com.xenoage.utils.Cache
import com.xenoage.utils.color.Color
import com.xenoage.utils.font.FontInfo
import com.xenoage.utils.font.FontInfo.Companion.defaultFontInfo
import com.xenoage.zong.core.format.Defaults

/**
 * Style of a [FormattedTextString].
 */
data class FormattedTextStyle private constructor(
		val fontInfo: FontInfo,
		val color: Color,
		val superscript: Superscript
) {

	companion object {

		val defaultColor = Color.black
		val defaultSuperscript = Superscript.Normal
		val defaultFormattedTextStyle = FormattedTextStyle(defaultFontInfo, defaultColor, defaultSuperscript)

		var cache = Cache<FormattedTextStyle, FormattedTextStyle>(100)

		operator fun invoke(fontInfo: FontInfo = Defaults.defaultFont, color: Color = defaultColor,
			superscript: Superscript = defaultSuperscript): FormattedTextStyle {
			val style = FormattedTextStyle(fontInfo, color, superscript) //can be garbage collected if already in cache
			return cache[style, { style }]
		}

	}

}
