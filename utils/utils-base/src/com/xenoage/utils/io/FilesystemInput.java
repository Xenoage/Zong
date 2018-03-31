package com.xenoage.utils.io;

import java.util.List;

import com.xenoage.utils.async.AsyncResult;

/**
 * Interface for reading from filesystems.
 * 
 * For example, there may be implementations for the Java SE
 * filesystem, Android or GWT for example.
 * 
 * All implementations are asynchronous. This is the only way which
 * is supported on all platforms (e.g. GWT/JavaScript in the browser
 * supports no blocking IO). Specific platform implementations
 * may also provide blocking variants of the methods.
 * 
 * Some technologies like HTTP might not support listing directories
 * out of the box. In this case, implementing classes must include
 * a workaround, e.g. using a file list script on the server or a
 * static file including all available paths.
 * 
 * @author Andreas Wenger
 */
public interface FilesystemInput {
	
	/**
	 * Yields true, when the given file exists, otherwise false.
	 */
	public void existsFileAsync(String filePath, AsyncResult<Boolean> exists);

	/**
	 * Yields true, when the given directory exists, otherwise false.
	 */
	public void existsDirectoryAsync(String dirPath, AsyncResult<Boolean> exists);

	/**
	 * Finds and yields the files in the given directory.
	 * If nothing is found, an empty list is yielded.
	 */
	public void listFilesAsync(String dirPath, AsyncResult<List<String>> fileNames);
	
	/**
	 * Finds and yields the files in the given directory
	 * matching the given file filter.
	 * If nothing is found, an empty list is yielded.
	 */
	public void listFilesAsync(String dirPath, FileFilter filter, AsyncResult<List<String>> fileNames);

	/**
	 * Finds and yields the directories in the given directory.
	 * If nothing is found, an empty list is yielded.
	 */
	public void listDirectoriesAsync(String dirPath, AsyncResult<List<String>> dirNames);
	
	/**
	 * Yields an input stream for the file at the given path.
	 */
	public void openFileAsync(String filePath, AsyncResult<InputStream> inputStream);

}
