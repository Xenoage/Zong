package com.xenoage.zong.core.text

//GOON
import com.xenoage.utils.font.TextMetrics

/**
 * Single-line part of a text. This can be a substring (using a font)
 * or a symbol (using a SVG path).
 *
 * @author Andreas Wenger
 */
interface FormattedTextElement {

	/**
	 * Gets the number of charachters/symbols of this element.
	 */
	val length: Int

	/**
	 * Gets the text of this element, or "" if inapplicable.
	 */
	val text: String

	/**
	 * Gets the style of this element.
	 */
	val style: FormattedTextStyle

	/**
	 * Gets the measurements of this element in mm.
	 */
	val metrics: TextMetrics

}
