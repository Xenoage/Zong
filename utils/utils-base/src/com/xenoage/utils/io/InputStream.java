package com.xenoage.utils.io;

import java.io.IOException;

/**
 * System independent interface for input streams.
 * 
 * For example, there may be implementations for Java SE, Android or GWT.
 * 
 * @author Andreas Wenger
 */
public interface InputStream {
	
	/**
	 * Reads the next byte of data from the input stream.
	 * The returned value is in the range 0 to 255, or -1 if the end of the stream
	 * has been reached.
	 */
	public int read()
		throws IOException;

  /**
   * Reads some number of bytes from the input stream and stores them into
   * the buffer array <code>b</code>. The number of bytes actually read is
   * returned as an integer.
   */
  public int read(byte... b)
  	throws IOException;
  
  /**
   * Reads up to <code>len</code> bytes of data from the input stream into
   * an array of bytes.  An attempt is made to read as many as
   * <code>len</code> bytes, but a smaller number may be read.
   * The number of bytes actually read is returned as an integer.
   * The first byte read is stored into element <code>b[off]</code>.
   */
  public int read(byte b[], int off, int len)
  	throws IOException;

	/**
	 * Closes this input stream and releases any system resources associated
	 * with the stream.
	 */
	public void close();

}
