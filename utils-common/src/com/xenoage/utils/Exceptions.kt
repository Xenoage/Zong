package com.xenoage.utils

/**
 * Throws an IllegalStateException with the given message.
 */
fun throwEx(message: String? = null): Nothing =
		throw IllegalStateException(message)