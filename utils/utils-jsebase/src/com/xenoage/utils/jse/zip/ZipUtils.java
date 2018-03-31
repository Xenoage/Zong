package com.xenoage.utils.jse.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Useful methods for working with ZIP data and files.
 * 
 * @author Andreas Wenger
 */
public class ZipUtils {

	/**
	 * Extracts all files within the given ZIP stream
	 * into the given directory.
	 */
	public static void extractAll(InputStream zipStream, File destDir)
		throws IOException {
		byte[] buf = new byte[4096];
		ZipInputStream zipinputstream = new ZipInputStream(zipStream);
		ZipEntry zipentry;

		//extract all files
		while ((zipentry = zipinputstream.getNextEntry()) != null) {
			String entryName = zipentry.getName();
			File newFile = new File(destDir, entryName);
			if (zipentry.isDirectory()) {
				newFile.mkdirs();
			}
			else {
				FileOutputStream fileoutputstream;
				fileoutputstream = new FileOutputStream(newFile);
				int n;
				while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
					fileoutputstream.write(buf, 0, n);
				fileoutputstream.close();
				zipinputstream.closeEntry();
			}
		}

		zipinputstream.close();
	}

	/**
	 * Zips the contents of the given directory and saves it in the given ZIP file.
	 * Subdirectories are ignored (no recursion).
	 */
	public static void compressAll(File dir, File file)
		throws IOException {
		if (!dir.isDirectory())
			throw new IllegalArgumentException("Given file is no directory");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
		out.setLevel(0);
		String[] entries = dir.list();
		byte[] buffer = new byte[4096];
		int bytesRead;
		for (String e : entries) {
			File f = new File(dir, e);
			if (f.isDirectory())
				continue;
			FileInputStream in = new FileInputStream(f);
			ZipEntry entry = new ZipEntry(f.getName());
			out.putNextEntry(entry);
			while ((bytesRead = in.read(buffer)) != -1)
				out.write(buffer, 0, bytesRead);
			in.close();
		}
		out.close();
	}

}
