package com.xenoage.utils.io

import com.xenoage.utils.annotations.Untested

/**
 * Output stream into a string.
 */
@Untested
class StringOutputStream : OutputStream {

	private val data = StringBuilder()

	override fun write(text: String) {
		data.append(text)
	}

	override fun writeLine(line: String) {
		data.append(line)
		data.append('\n')
	}

	override fun write(b: Int) {
		val char = b.toChar()
		data.append(char)
	}

	override fun close() {
	}

	override fun toString() = data.toString()

}