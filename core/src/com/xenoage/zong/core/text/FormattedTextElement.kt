package com.xenoage.zong.core.text

import com.xenoage.utils.font.TextMetrics

/**
 * Single-line part of a text. This can be a substring (using a font)
 * or a symbol (using a SVG path).
 */
interface FormattedTextElement {

	/** The number of charachters/symbols of this element. */
	val length: Int

	/** The raw text of this element, or "" if inapplicable. */
	val text: String

	/** The style of this element. */
	val style: FormattedTextStyle

	/** The measurements of this element. */
	val metrics: TextMetrics

}
