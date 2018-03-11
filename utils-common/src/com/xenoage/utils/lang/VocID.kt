package com.xenoage.utils.lang

/**
 * This interface must be implemented by all enums that contain
 * vocabulary IDs.
 *
 * There must be a property [defaultValue] which
 * contains the text in the default language of the application
 * (used for the default language and for fallback)
 * and a property [id] that returns the ID as a String needed to index
 * the vocabulary in the vocabulary files (like `Error_UnknownValue`).
 */
interface VocID {

	/** The ID of the vocabulary as a String. */
	val id: String

	/** The text in the default language. */
	val defaultValue: String

}
