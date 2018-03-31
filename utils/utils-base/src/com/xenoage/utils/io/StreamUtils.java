package com.xenoage.utils.io;

import java.io.IOException;

/**
 * Some useful methods for streams.
 * 
 * @author Andreas Wenger
 */
public class StreamUtils {
	
	/**
	 * Default implementation of {@link InputStream#read(byte[])}.
	 * Uses {@link #read(InputStream, byte[], int, int)} (see performance warning).
	 */
	public static int read(InputStream stream, byte... b)
		throws IOException {
		return stream.read(b, 0, b.length);
	}

	/**
	 * Default implementation of {@link InputStream#read(byte[], int, int)}.
	 * Reads byte after byte (uses {@link InputStream#read()}), which is very slow.
	 * Use a better implementation when possible.
	 */
	public static int read(InputStream stream, byte[] b, int off, int len)
		throws IOException {
		int i = 0;
		for (; i < len; i++) {
			int c = stream.read();
			if (c == -1) {
				break;
			}
			b[off + i] = (byte) c;
		}
		return i;
	}
	
	/**
	 * Default implementation of {@link OutputStream#write(byte[])}.
	 * Uses {@link #write(OutputStream, byte[], int, int)} (see performance warning).
	 */
	public static void write(OutputStream stream, byte... b)
		throws IOException {
    write(stream, b, 0, b.length);
	}
	
	/**
	 * Default implementation of {@link OutputStream#write(byte[], int, int)}.
	 * Writes byte after byte (uses {@link OutputStream#write(int)}), which is very slow.
	 * Use a better implementation when possible.
	 */
	public static void write(OutputStream stream, byte b[], int off, int len)
		throws IOException {
    for (int i = 0; i < len ; i++) {
    	stream.write(b[off + i]);
    }
	}

}
