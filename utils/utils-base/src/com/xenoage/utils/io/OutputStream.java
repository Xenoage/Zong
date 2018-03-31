package com.xenoage.utils.io;

import java.io.IOException;

/**
 * System independent interface for files.
 * 
 * For example, there may be implementations for Java SE, Android or GWT.
 * 
 * @author Andreas Wenger
 */
public interface OutputStream extends AutoCloseable {

	/**
	 * Writes the specified byte to this output stream. Only the eight low-order
	 * bits of the given value are used.
	 */
	public void write(int b) throws IOException;

	/**
	 * Writes the given bytes to this output stream.
	 */
	public void write(byte... b) throws IOException;

	/**
	 * Writes <code>len</code> bytes from the given byte array starting at
	 * offset <code>off</code> to this output stream.
	 */
	public void write(byte b[], int off, int len) throws IOException;

	/**
	 * Closes this input stream and releases any system resources associated
	 * with the stream.
	 */
	@Override public void close();

}
