package com.xenoage.utils.io

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests for [StringInputStream].
 */
class StringInputStreamTest {

	@Test fun readTest() {
		val data = "12345"
		val stream = StringInputStream(data)
		for (i in 1..5)
			assertEquals("$i"[0].toInt(), stream.read())
		assertEquals(-1, stream.read())
	}

	@Test fun readLineTest() {
		val data = "line 1\n" +
				"line 2\r" +
				"line 3\r\n" +
				"line 4"
		val stream = StringInputStream(data)
		for (i in 1..4)
			assertEquals("line $i", stream.readLine())
		assertNull(stream.readLine())
	}

	@Test fun readLineTestWithNewLineAtEnd() {
		val data = "string with line break at end\n"
		val stream = StringInputStream(data)
		assertEquals(data.trim(), stream.readLine())
		assertNull(stream.readLine())
	}


}