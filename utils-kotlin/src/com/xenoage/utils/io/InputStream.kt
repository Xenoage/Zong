package com.xenoage.utils.io

/**
 * System independent interface for input streams.
 *
 * All methods may throw an [IoException].
 */
interface InputStream {

	/**
	 * Reads a line of text. A line is considered to be terminated by any one of a
	 * line feed ('\n'), a carriage return ('\r'), or a carriage return followed
	 * immediately by a linefeed. If the end of the stream has been reached, null is returned.
	 * Use this method only for text files.
	 */
	fun readLine(): String?

	/**
	 * Reads the next byte of data from the input stream.
	 * The returned value is in the range 0 to 255, or -1 if the end of the stream
	 * has been reached.
	 */
	fun read(): Int

	/* TODO: needed?

	/**
	 * Reads some number of bytes from the input stream and stores them into
	 * the buffer array [b]. The number of bytes actually read is returned.
	 */
	fun read(b: ByteArray): Int

	/**
	 * Reads up to [len] bytes of data from the input stream into
	 * the buffer array [b]. An attempt is made to read as many as
	 * [len] bytes, but a smaller number may be read.
	 * The number of bytes actually read is returned.
	 * The first byte read is stored into the element at index [off].
	 */
	fun read(b: ByteArray, off: Int, len: Int): Int

	*/

	/**
	 * Closes this input stream and releases any system resources associated
	 * with the stream.
	 */
	fun close()

}
