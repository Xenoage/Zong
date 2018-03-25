package com.xenoage.utils.font

import com.xenoage.utils.Mm

/**
 * Metrics of text measured with a [TextMeasurer].
 */
class TextMetrics(

		/** The ascent of this text in mm. This is the height of the text above the baseline. */
		val ascent: Mm,

		/** The descent of this font in mm. This is the height of the paragraph below the baseline. */
		val descent: Mm,

		/**
		 * The leading of this font in mm.
		 * This is the height between the bottommost point of this text
		 * to the topmost point of the following line of text
		 */
		val leading: Mm,

		/** The width of this text in mm. */
		val width: Mm
)
