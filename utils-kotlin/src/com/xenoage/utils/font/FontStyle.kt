package com.xenoage.utils.font

import com.xenoage.utils.Cache
import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason
import com.xenoage.utils.annotations.Reason.MemorySaving

/**
 * Enumeration of different font styles like bold or italic.
 */
data class FontStyle private constructor(
		val bold: Boolean,
		val italic: Boolean,
		val underline: Boolean,
		val strikethrough: Boolean
) {

	override fun toString(): String {
		return (if (bold) "B" else "b") + (if (italic) "I" else "i") +
				(if (underline) "U" else "u") + if (strikethrough) "S" else "s"
	}

	companion object {

		val normal = FontStyle(false, false, false, false)

		var cache = Cache<Int, FontStyle>(16) //16 possibilities

		@Optimized(MemorySaving)
		operator fun invoke(bold: Boolean = false, italic: Boolean = false,
		                    underline: Boolean = false, strikethrough: Boolean = false): FontStyle {
			val id = if (bold) 8 else 0 + if (italic) 4 else 0 + if (underline) 2 else 0 + if (strikethrough) 1 else 0
			return cache[id, { FontStyle(bold, italic, underline, strikethrough) }]
		}

	}

}
