package com.xenoage.utils.jse.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class provides a method to download a file.
 * 
 * @author Andreas Wenger
 */
public class DownloadTools {

	/**
	 * Downloads the file behind the given path.
	 * This is done in the current thread, so the method blocks.
	 * If an error occurs, an {@link IOException} is thrown.
	 */
	public static void downloadFile(String source, File target)
		throws IOException {
		URL url = new URL(source);
		URLConnection urlc = url.openConnection();
		InputStream raw = urlc.getInputStream();
		int contentLength = urlc.getContentLength();
		if (contentLength == -1) {
			throw new IOException("Unknown file length (content-length header field)");
		}
		InputStream in = new BufferedInputStream(raw);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1)
				break;
			offset += bytesRead;
		}
		in.close();
		if (offset != contentLength) {
			throw new IOException("Only read " + offset + " bytes, but expected " + contentLength +
				" bytes");
		}
		FileOutputStream out = new FileOutputStream(target);
		out.write(data);
		out.flush();
		out.close();
	}

}
