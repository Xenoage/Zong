package com.xenoage.utils.io;

import java.io.IOException;

/**
 * {@link BufferedInputStream} similar to the BufferedInputStream from Java SE,
 * but simpler (e.g. not tuned for performance, no buffer limit, no concurrency support)
 * and platform independent. 
 *
 * @author Andreas Wenger
 */
public class BufferedInputStream
	implements InputStream {

	private static int defaultBufferSize = 1024;

	private InputStream stream;

	//true, when mark was set
	private boolean marked = false;
	//buffered bytes
	private byte buffer[];
	//number of buffered bytes
	private int bufferSize = 0;
	//position within the buffer
	private int bufferPos = 0;
	
	
	public BufferedInputStream(InputStream stream) {
		this.stream = stream;
		this.buffer = new byte[defaultBufferSize];
	}

	@Override public int read()
		throws IOException {
		//if there is remaining data in the buffer, use it
		if (bufferPos < bufferSize) {
			return buffer[bufferPos++] & 0xFF;
		}
		//read from stream
		int data = stream.read();
		if (data == -1)
			return -1;
		//remember data, if marked
		if (marked) {
			if (bufferSize == buffer.length) {
				//buffer is too small. extend it.
				byte[] newBuffer = new byte[buffer.length + defaultBufferSize];
				System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
				buffer = newBuffer;
			}
			//save data in the buffer
			buffer[bufferSize] = (byte) data;
			bufferPos++;
			bufferSize++;
		}
		return data;
	}
	
	@Override public int read(byte... b)
		throws IOException {
		return StreamUtils.read(this, b);
	}

	@Override public int read(byte[] b, int off, int len)
		throws IOException {
		return StreamUtils.read(this, b, off, len);
	}
	
	/**
	 * Sets the position to where a call of {@link #reset()} will reset the cursor.
	 */
	public void mark() {
		this.marked = true;
		//delete data before new position
		bufferSize = bufferSize - bufferPos;
		System.arraycopy(buffer, bufferPos, buffer, 0, bufferSize);
		bufferPos = 0;
	}
	
	/**
	 * Deletes the marked position (if any) and goes on from the current position.
	 * No more data is cached until {@link #mark()} is called again.
	 */
	public void unmark() {
		this.marked = false;
		//delete data before current position
		bufferSize = bufferSize - bufferPos;
		System.arraycopy(buffer, bufferPos, buffer, 0, bufferSize);
		bufferPos = 0;
	}
	
	/**
	 * Resets the cursor to the last marked position (if any).
	 */
	public void reset() {
		//move position back to 0
		bufferPos = 0;
	}

	@Override public void close() {
		//clear buffer immediately
		buffer = new byte[0];
		bufferSize = bufferPos = 0;
	}

	/**
	 * Gets the underlying stream.
	 * Call this method only if you know what you are doing.
	 */
	public InputStream getInternalStream() {
		return stream;
	}

}
