package com.xenoage.utils.font;

/**
 * Metrics of text measured with a {@link TextMeasurer}.
 * 
 * @author Andreas Wenger
 */
public class TextMetrics {
	
	private float ascent, descent, leading, width;
	

	public TextMetrics(float ascent, float descent, float leading, float width) {
		this.ascent = ascent;
		this.descent = descent;
		this.leading = leading;
		this.width = width;
	}

	/**
	 * Gets the ascent of this text in mm.
	 * This is the height of the text above the baseline.
	 */
	public float getAscent() {
		return ascent;
	}

	/**
	 * Gets the descent of this font in mm.
	 * This is the height of the paragraph below the baseline.
	 */
	public float getDescent() {
		return descent;
	}

	/**
	 * Gets the leading of this font in mm.
	 * This is the height between the bottommost point of this text
	 * to the topmost point of the following line of text
	 */
	public float getLeading() {
		return leading;
	}

	/**
	 * Gets the width of this text in mm.
	 */
	public float getWidth()  {
		return width;
	}

}
