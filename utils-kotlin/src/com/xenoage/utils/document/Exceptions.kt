package com.xenoage.utils.document


/**
 * This exception is thrown when any operation was cancelled.
 */
class CancelledException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

/**
 * This exception is thrown when a property of an object should be changed, but when
 * this property is already set or when the new and old value are the same.
 */
class PropertyAlreadySetException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

/**
 * This exception is thrown when any operation was cancelled because it is useless.
 * For example, setting a green area to green or adding 0 to a number may be useless operations.
 */
class UselessException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)