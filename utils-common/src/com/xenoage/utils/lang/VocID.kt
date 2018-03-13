package com.xenoage.utils.lang

import kotlin.coroutines.experimental.EmptyCoroutineContext.get

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

/**
 * Vocabulary ID as a string.
 */
class VocByString(
		override val id: String
) : VocID {

	override val defaultValue: String
		get() = id
}

fun voc(id: String) = VocByString(id)