package com.xenoage.utils.font

/**
 * This interfaces provides methods to measure strings
 * with a given [FontInfo].
 */
interface TextMeasurer {

	/** Measures the given text, using the given [FontInfo]. */
	fun measure(font: FontInfo, text: String): TextMetrics

}
