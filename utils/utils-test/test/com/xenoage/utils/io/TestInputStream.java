package com.xenoage.utils.io;

import java.io.IOException;

/**
 * Stream which returns the numbers from 0 to (length - 1) % 256.
 * 
 * @author Andreas Wenger
 */
public class TestInputStream
	implements InputStream {
	
	private int length;
	private int pos = 0;
	
	public TestInputStream(int length) {
		this.length = length;
	}

	@Override public int read()
		throws IOException {
		if (pos >= length)
			return -1;
		int ret = pos % 256;
		pos++;
		return ret;
	}
	
	@Override public int read(byte... b)
		throws IOException {
		return StreamUtils.read(this, b);
	}

	@Override public int read(byte[] b, int off, int len)
		throws IOException {
		return StreamUtils.read(this, b, off, len);
	}

	@Override public void close() {
	}

}
