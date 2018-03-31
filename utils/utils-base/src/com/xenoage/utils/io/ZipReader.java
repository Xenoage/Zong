package com.xenoage.utils.io;

import java.util.List;

/**
 * System independent reader interface for ZIP content.
 * 
 * @author Andreas Wenger
 */
public interface ZipReader {

	/**
	 * Gets a list of the files in the ZIP file.
	 * Directories are not listed.
	 */
	public List<String> getFiles();
	
	/**
	 * Gets an {@link InputStream} for the file at the given path.
	 */
	public InputStream openFile(String file)
		throws FileNotFoundException;
	
	/**
	 * Closes the ZIP document.
	 */
	public void close();
	
}
