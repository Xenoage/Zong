package com.xenoage.utils.gwt.io;

/**
 * Simple input stream for reading a byte array.
 * 
 * Based on the <code>ByteArrayInputStream</code> in Java SE 7,
 * but without support for mark/reset.
 * 
 * @author Andreas Wenger
 */
public class ByteArrayInputStream {

	private byte buf[];
	private int pos;
	private int count;

	public ByteArrayInputStream(byte... buf) {
		this.buf = buf;
		this.pos = 0;
		this.count = buf.length;
	}

	public int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	public int read(byte b[], int off, int len) {
		if (b == null) {
			throw new NullPointerException();
		}
		else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}

		if (pos >= count) {
			return -1;
		}

		int avail = count - pos;
		if (len > avail) {
			len = avail;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

}
