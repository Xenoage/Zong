package com.xenoage.utils.io

/**
 * System independent interface for files.
 * For example, there may be implementations for the JVM, Android or JavaScript.
 *
 * All methods may throw an [IoException].
 */
interface OutputStream {

	/**
	 * Writes a line of text, terminated by '\n'.
	 * Use this method only for text files.
	 */
	fun writeLine(line: String)

	/**
	 * Writes the specified byte to this output stream. Only the eight low-order
	 * bits of the given value are used.
	 */
	fun write(b: Int)

	/* TODO: needed ?
	/**
	 * Writes the given bytes to this output stream.
	 */
	fun write(b: ByteArray)

	/**
	 * Writes [len] bytes from the given byte array starting at
	 * offset [off] to this output stream.
	 */
	fun write(b: ByteArray, off: Int, len: Int)

	*/

	/**
	 * Closes this input stream and releases any system resources associated
	 * with the stream.
	 */
	fun close()

}
