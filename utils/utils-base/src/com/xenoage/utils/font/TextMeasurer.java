package com.xenoage.utils.font;

/**
 * This interfaces provides methods to measure Strings.
 * 
 * @author Andreas Wenger
 */
public interface TextMeasurer {

	/**
	 * Measures the given text, using the given {@link FontInfo}.
	 */
	public TextMetrics measure(FontInfo font, String text);

}
