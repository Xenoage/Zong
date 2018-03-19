package com.xenoage.zong.core.text


/**
 * Interface for text classes.
 *
 * A text has a length and a raw string representation.
 */
interface Text {

	/** The number of characters in this text. */
	val length: Int

	/** The text content of this text. */
	val rawText: String

}
