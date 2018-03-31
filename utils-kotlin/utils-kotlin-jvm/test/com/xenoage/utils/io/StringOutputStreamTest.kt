package com.xenoage.utils.io

import org.junit.Test
import kotlin.test.assertEquals

/**
 * Tests for [StringOutputStream].
 */
class StringOutputStreamTest {

	@Test fun test() {
		val chunks = listOf("123", "abc\n", "X", "456", "Y")
		val stream = StringOutputStream()
		for (chunk in chunks) {
			if (chunk.endsWith("\n"))
				stream.writeLine(chunk.trim())
			else if (chunk.length == 0)
				stream.write(chunk[0].toInt())
			else
				stream.write(chunk)
		}
		assertEquals(chunks.joinToString(""), stream.toString())
	}

}