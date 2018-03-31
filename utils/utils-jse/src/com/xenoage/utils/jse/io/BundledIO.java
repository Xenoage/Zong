package com.xenoage.utils.jse.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.io.FileFilter;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.index.FilesystemIndex;
import com.xenoage.utils.io.index.FilesystemIndexReader;
import com.xenoage.utils.jse.xml.JseXmlReader;

/**
 * Some useful input/output methods for JAR bundled resource files
 * for a JSE based application.
 * 
 * Since the contents of the JAR files can not be listed out of the box,
 * a {@link FilesystemIndex} file must be available in the classpath.
 * If not existing, files from the JAR archives can not be listed or read.
 *
 * @author Andreas Wenger
 */
public class BundledIO
	implements FilesystemInput {

	private FilesystemIndex index = null;

	
	/**
	 * Returns a {@link BundledIO} instance for the current classpath,
	 * or null if no index file exists. Throws an exception if an index file
	 * exists, but can not be read.
	 */
	public static BundledIO createIfIndexAvailable() {
		FilesystemIndex index = readIndex();
		if (index != null)
			return new BundledIO(index);
		else
			return null;
	}
	
	public BundledIO(FilesystemIndex index) {
		this.index = index;
	}
	
	private static FilesystemIndex readIndex() {
		try {
			JseInputStream indexStream = openFileUnchecked(FilesystemIndex.indexFile);
			return FilesystemIndexReader.read(new JseXmlReader(indexStream));
		}
		catch (FileNotFoundException ex) {
			//normal. index just does not exist.
		}
		catch (IOException ex) {
			//logging or error handling is not initialized at this point
			//so print to console
			ex.printStackTrace(); 
		}
		return null;
	}
	
	@Override public void existsFileAsync(String filePath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsFile(filePath));
	}

	/**
	 * Like {@link #existsFileAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsFile(String filePath) {
		return index.existsFile(filePath);
	}
	
	@Override public void existsDirectoryAsync(String dirPath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsDirectory(dirPath));
	}

	/**
	 * Like {@link #existsDirectoryAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsDirectory(String dirPath) {
		return index.existsDirectory(dirPath);
	}
	
	@Override public void listFilesAsync(String dirPath, AsyncResult<List<String>> fileNames) {
		fileNames.onSuccess(listFiles(dirPath));
	}

	/**
	 * Like {@link #listFilesAsync(String, AsyncResult)}, but with direct return.
	 */
	public List<String> listFiles(String dirPath) {
		return FileUtils.getNames(index.listFiles(dirPath));
	}
	
	@Override public void listFilesAsync(String dirPath, FileFilter filter,
		AsyncResult<List<String>> fileNames) {
		fileNames.onSuccess(listFiles(dirPath, filter));
	}

	/**
	 * Like {@link #listFilesAsync(String, FileFilter, AsyncResult)}, but with direct return.
	 */
	public List<String> listFiles(String dirPath, FileFilter filter) {
		return FileUtils.getNames(index.listFiles(dirPath, filter));
	}
	
	@Override public void listDirectoriesAsync(String dirPath, AsyncResult<List<String>> dirNames) {
		dirNames.onSuccess(listDirectories(dirPath));
	}

	/**
	 * Like {@link #listDirectoriesAsync(String, AsyncResult)}, but with direct return.
	 */
	public List<String> listDirectories(String dirPath) {
		return FileUtils.getNames(index.listDirectories(dirPath));
	}

	@Override public void openFileAsync(String filePath, AsyncResult<InputStream> inputStream) {
		try {
			inputStream.onSuccess(openFile(filePath));
		} catch (IOException ex) {
			inputStream.onFailure(ex);
		}
	}
	
	/**
	 * Like {@link #openFileAsync(String)}, but with direct return.
	 * Returns an input stream for the given bundled resource file, but only if that
	 * file is listed in the {@link FilesystemIndex}. Otherwise, or if the file
	 * can not be found, a {@link FileNotFoundException} is thrown.
	 */
	public JseInputStream openFile(String filePath)
		throws IOException {
		if (false == index.existsFile(filePath))
			throw new FileNotFoundException(filePath);
		return openFileUnchecked(filePath);
	}
	
	/**
	 * Returns an input stream for the given bundled resource file
	 * without checking if it exists in the index.
	 * If the file can not be found, null is returned.
	 */
	private static JseInputStream openFileUnchecked(String filePath)
		throws IOException {
		java.io.InputStream stream = BundledIO.class.getClassLoader().getResourceAsStream(filePath);
		if (stream != null)
			return new JseInputStream(stream);
		else
			throw new FileNotFoundException(filePath);
	}

}
