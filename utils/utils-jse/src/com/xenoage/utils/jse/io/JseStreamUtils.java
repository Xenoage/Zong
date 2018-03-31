package com.xenoage.utils.jse.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Useful methods to work with streams.
 * 
 * @author Andreas Wenger
 */
public class JseStreamUtils {

	/**
	 * Mark an {@link BufferedInputStream}),
	 * with enough memory so that the mark never gets invalid if less
	 * than 32k are read.
	 */
	public static void markInputStream(BufferedInputStream is) {
		is.mark(8192 * 4); //8192 is the default buffer size in BufferedInputStream
	}

	/**
	 * Reads the given input stream into a byte array.
	 */
	public static byte[] readToByteArray(InputStream in)
		throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4096);
		byte[] bytes = new byte[4096];
		int readBytes;
		while ((readBytes = in.read(bytes)) > 0)
			outputStream.write(bytes, 0, readBytes);
		return outputStream.toByteArray();
	}
	
	/**
	 * Reads the given input stream into a string and returns it.
	 * If the file does not exist or there is another I/O error,
	 * null is returned.
	 */
	public static String readToString(InputStream in) {
		try {
			StringBuilder fileData = new StringBuilder(1024);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
			}
			reader.close();
			return fileData.toString();
		} catch (IOException ex) {
			return null;
		}
	}

}
