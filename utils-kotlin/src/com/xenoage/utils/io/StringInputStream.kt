package com.xenoage.utils.io

import com.xenoage.utils.annotations.Untested

/**
 * Input stream from a given string.
 */
@Untested
class StringInputStream(
		val data: String
) : InputStream {

	var pos = 0

	override fun read(): Int {
		if (pos >= data.length)
			return -1
		val ret = data[pos]
		pos++
		return ret.toInt()
	}

	override fun readLine(): String? {
		if (pos >= data.length)
			return null
		//find next \n, \r or end of data
		var lineStartPos = pos
		while (pos < data.length && data[pos] != '\n' && data[pos] != '\r')
			pos++
		if (pos == data.length) //end of file reached
			return data.substring(lineStartPos)
		else if (data[pos] == '\n') { // \n found
			pos++
			return data.substring(lineStartPos, pos - 1)
		}
		else if (data[pos] == '\r' && pos + 1 < data.length && data[pos + 1] == '\n') { //special case "\r\n"
			pos += 2
			return data.substring(lineStartPos, pos - 2)
		}
		else { // \r found
			pos++
			return data.substring(lineStartPos, pos - 1)
		}
	}

	/* TODO: needed?
	override fun read(b: ByteArray): Int =
			read(b, 0, 1024)

	override fun read(b: ByteArray, off: Int, len: Int): Int {
		var i = 0
		while (i < len && pos < b.size) {
			b[off + i] = data[pos].toByte()
			pos++
			i++
		}
		return i
	}

	*/

	override fun close() {
		pos = 0
	}

}
