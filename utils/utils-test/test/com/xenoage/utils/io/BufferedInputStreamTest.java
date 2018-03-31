package com.xenoage.utils.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * Tests for {@link BufferedInputStream}.
 * 
 * @author Andreas Wenger
 */
public class BufferedInputStreamTest {

	private BufferedInputStream getTestStream(int length) {
		return new BufferedInputStream(new TestInputStream(length));
	}
	
	/**
	 * Tests the stream without marking.
	 */
	@Test public void testWithoutMarking()
		throws IOException {
		int length = 5000;
		BufferedInputStream stream = getTestStream(length);
		for (int i = 0; i < length; i++)
			assertEquals(i % 256, stream.read());
		assertEquals(-1, stream.read());
	}
	
	/**
	 * Tests the stream with marking after 3 bytes, read 3 more bytes and reset.
	 */
	@Test public void testSmallData()
		throws IOException {
		int length = 10;
		BufferedInputStream stream = getTestStream(length);
		for (int i = 0; i < 3; i++)
			assertEquals(i % 256, stream.read());
		stream.mark();
		for (int i = 3; i < 6; i++)
			assertEquals(i % 256, stream.read());
		stream.reset();
		for (int i = 3; i < length; i++)
			assertEquals(i % 256, stream.read());
		assertEquals(-1, stream.read());
	}
	
	/**
	 * Tests the stream with marking after 2000 bytes, read 2000 more bytes and reset.
	 */
	@Test public void testMoreData()
		throws IOException {
		int length = 5000;
		BufferedInputStream stream = getTestStream(length);
		for (int i = 0; i < 2000; i++)
			assertEquals(i % 256, stream.read());
		stream.mark();
		for (int i = 2000; i < 4000; i++)
			assertEquals(i % 256, stream.read());
		stream.reset();
		for (int i = 2000; i < length; i++)
			assertEquals("at " + i, i % 256, stream.read());
		assertEquals(-1, stream.read());
	}
	
	/**
	 * Tests the stream with a random combination of mark, unmark and reset.
	 */
	@Test public void testRandom()
		throws IOException {
		int length = 100000;
		BufferedInputStream stream = getTestStream(length);
		int markedAt = -1;
		for (int nextExpected = 0; nextExpected < length; nextExpected++) {
			if (Math.random() < 0.01) {
				//mark
				stream.mark();
				markedAt = nextExpected;
			}
			if (markedAt > -1 && Math.random() < 0.005) {
				//reset
				stream.reset();
				nextExpected = markedAt;
			}
			if (Math.random() < 0.001) {
				//unmark
				stream.unmark();
				markedAt = -1;
			}
			assertEquals(nextExpected % 256, stream.read());
		}
		assertEquals(-1, stream.read());
	}

}
