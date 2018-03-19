package com.xenoage.utils.color

import com.xenoage.utils.Cache
import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason.MemorySaving
import com.xenoage.utils.math.clamp

/**
 * A platform independent way to store a color.
 * All values are between 0 and 255.
 */
data class Color private constructor(
		val r: Int, val g: Int, val b: Int, val a: Int
) {

	val rgb: Int
		get() = rgba(r, g, b, a)

	/**
	 * Adds the given value to the red, green and blue color. The values
	 * are clamped to the range between 0 and 255. The given summand may also be negative
	 * to remove light.
	 */
	fun addWhite(summand: Int): Color {
		return Color((r + summand).clamp(0, 255), (g + summand).clamp(0, 255),
				(b + summand).clamp(0, 255), a)
	}

	companion object {

		val black = Color(0, 0, 0, 255)
		val blue = Color(0, 0, 255, 255)
		val gray = Color(128, 128, 128, 255)
		val green = Color(0, 255, 0, 255)
		val lightGray = Color(192, 192, 192, 255)
		val red = Color(255, 0, 0, 255)
		val white = Color(255, 255, 255, 255)
		val yellow = Color(255, 255, 0, 255)

		private val cache = Cache<Int, Color>(100)

		private fun rgba(r: Int, g: Int, b: Int, a: Int): Int =
				(a and 0xFF shl 24) or
				(r and 0xFF shl 16) or
				(g and 0xFF shl 8) or
				(b and 0xFF)

		/** Creates a new color or returns a shared instance. */
		@Optimized(MemorySaving)
		operator fun invoke(r: Int, g: Int, b: Int, a: Int = 255): Color =
				cache[rgba(r, g, b, a), { Color(r, g, b, a) }]
	}

}
