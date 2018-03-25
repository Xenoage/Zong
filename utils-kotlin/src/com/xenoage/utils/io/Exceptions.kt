package com.xenoage.utils.io

/**
 * Signals a failed I/O operation.
 */
class IoException(message: String?, cause: Throwable?) : Exception(message, cause)


/**
 * This exception is thrown, when some data has a wrong format.
 *
 * It can be used for example within a file reader that expected another format,
 * or when the content of the file is incorrect.
 */
class InvalidFormatException(message: String?, cause: Throwable?) : Exception(message, cause)