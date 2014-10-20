package com.xenoage.zong.core.text;

import com.xenoage.utils.font.TextMetrics;

/**
 * Single-line part of a text. This can be a substring (using a font)
 * or a symbol (using a SVG path).
 *
 * @author Andreas Wenger
 */
public interface FormattedTextElement {

	/**
	 * Gets the number of charachters/symbols of this element.
	 */
	public int getLength();

	/**
	 * Gets the text of this element, or "" if inapplicable.
	 */
	public String getText();

	/**
	 * Gets the style of this element.
	 */
	public FormattedTextStyle getStyle();

	/**
	 * Gets the measurements of this element in mm.
	 */
	public TextMetrics getMetrics();

}
