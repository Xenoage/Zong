package com.xenoage.utils.font

/**
 * Enumeration of different font styles like bold or italic.
 * For performance reasons, the style is stored in an Int value.
 */
data class FontStyle(private val style: Int) {

	/**
	 * Sets the given flag, like Bold, to true or the given value.
	 */
	fun with(flag: Int, value: Boolean = true): FontStyle {
		var style = this.style
		if (value)
			style = style or flag
		else
			style = style and flag.inv()
		return FontStyle(style)
	}

	/**
	 * Returns true, when the given flag is set.
	 */
	fun isSet(flag: Int): Boolean {
		return style and flag > 0
	}

	override fun toString(): String {
		return (if (isSet(Bold)) "B" else "b") + (if (isSet(Italic)) "I" else "i") +
				(if (isSet(Underline)) "U" else "u") + if (isSet(Strikethrough)) "S" else "s"
	}

	companion object {

		val Bold = 1 shl 0
		val Italic = 1 shl 1
		val Underline = 1 shl 2
		val Strikethrough = 1 shl 3

		val normal = FontStyle(0)


		fun fontStyle(vararg flags: Int): FontStyle {
			var style = 0
			for (flag in flags)
				style = style or flag
			return if (style == 0)
				normal
			else
				FontStyle(style)
		}
	}

}
