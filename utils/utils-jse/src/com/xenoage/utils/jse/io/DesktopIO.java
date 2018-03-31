package com.xenoage.utils.jse.io;

import static com.xenoage.utils.collections.CollectionUtils.mergeNoDuplicates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.io.FileFilter;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.jse.JsePlatformUtils;

/**
 * Some useful input/output methods for a JSE based desktop
 * or WebStart application.
 * 
 * This class supports both files on the normal filesystem (using
 * {@link FilesIO}) and files bundled in JARs (using {@link BundledIO}).
 * 
 * The filesystem has a higher priority when reading files, i.e.
 * a file in a JAR is only opened if it can not be found on the
 * normal filesystem.
 * 
 * Creating and deleting files can only be done on the normal filesystem.
 * 
 * When files and directories are listed, both the contents of the
 * filesystem and the JARs are used.
 * 
 * Use {@link JsePlatformUtils#io()} to get an instance of this class.
 *
 * @author Andreas Wenger
 */
public class DesktopIO
	implements FilesystemInput {

	private FilesIO filesIO;
	private BundledIO bundledIO;

	
	public DesktopIO(String programName) {
		this(new FilesIO(programName), BundledIO.createIfIndexAvailable());
	}
	
	/**
	 * Creates a {@link DesktopIO} for testing (e.g. unit tests).
	 * See {@link FilesIO#createTestIO()}. Bundled files are also supported.
	 */
	public static DesktopIO createTestIO() {
		return new DesktopIO(FilesIO.createTestIO(), BundledIO.createIfIndexAvailable());
	}
	
	private DesktopIO(FilesIO filesIO, BundledIO bundledIO) {
		this.filesIO = filesIO;
		this.bundledIO = bundledIO;
	}
	
	@Override public void existsFileAsync(String filePath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsFile(filePath));
	}

	/**
	 * Like {@link #existsFileAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsFile(String filePath) {
		return filesIO.existsFile(filePath) ||
			(bundledIO != null && bundledIO.existsFile(filePath));
	}
	
	@Override public void existsDirectoryAsync(String dirPath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsDirectory(dirPath));
	}

	/**
	 * Like {@link #existsDirectoryAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsDirectory(String directory) {
		return filesIO.existsDirectory(directory) ||
			(bundledIO != null && bundledIO.existsDirectory(directory));
	}
	
	@Override public void listFilesAsync(String dirPath, AsyncResult<List<String>> fileNames) {
		fileNames.onSuccess(listFiles(dirPath));
	}

	/**
	 * Like {@link #listFilesAsync(String, AsyncResult)}, but with direct return.
	 */
	public List<String> listFiles(String dirPath) {
		List<String> fileNames = filesIO.listFiles(dirPath);
		if (bundledIO == null)
			return fileNames;
		else
			return mergeNoDuplicates(bundledIO.listFiles(dirPath), fileNames);
	}
	
	@Override public void listFilesAsync(String dirPath, FileFilter filter,
		AsyncResult<List<String>> fileNames) {
		fileNames.onSuccess(listFiles(dirPath, filter));
	}

	/**
	 * Like {@link #listFilesAsync(String, FileFilter, AsyncResult)}, but with direct return.
	 */
	public List<String> listFiles(String dirPath, FileFilter filter) {
		List<String> fileNames = filesIO.listFiles(dirPath, filter);
		if (bundledIO == null)
			return fileNames;
		else
			return mergeNoDuplicates(bundledIO.listFiles(dirPath, filter), fileNames);
	}
	
	@Override public void listDirectoriesAsync(String dirPath,
		AsyncResult<List<String>> dirNames) {
		dirNames.onSuccess(listDirectories(dirPath));
	}

	/**
	 * Like {@link #listDirectoriesAsync(String, AsyncResult)}, but with direct return.
	 */
	public List<String> listDirectories(String dirPath) {
		List<String> dirNames = filesIO.listDirectories(dirPath);
		if (bundledIO == null)
			return dirNames;
		else
			return mergeNoDuplicates(bundledIO.listDirectories(dirPath), dirNames);
	}

	@Override public void openFileAsync(String filePath, AsyncResult<InputStream> inputStream) {
		try {
			inputStream.onSuccess(openFile(filePath));
		} catch (IOException ex) {
			inputStream.onFailure(ex);
		}
	}
	
	/**
	 * Like {@link #openFileAsync(String, AsyncResult)}, but with direct return.
	 */
	public JseInputStream openFile(String filePath)
		throws IOException {
		if (filesIO.existsFile(filePath))
			return filesIO.openFile(filePath);
		else if (bundledIO != null && bundledIO.existsFile(filePath))
			return bundledIO.openFile(filePath);
		else
			throw new FileNotFoundException(filePath);
	}
	
	/**
	 * See {@link FilesIO#findFile(String)}.
	 * Finds only normal files, not bundled files.
	 */
	public File findNormalFile(String filePath)
		throws IOException {
		return filesIO.findFile(filePath);
	}
	
	/**
	 * Gets the modification date of the given file, or null, if the date is unavailable.
	 * For bundled files, the date is not available.
	 */
	public Date getFileModificationDate(String filePath) {
		return filesIO.getFileModificationDate(filePath);
	}
	
	/**
	 * See {@link FilesIO#createFile(String)}.
	 */
	public File createFile(String filePath) {
		return filesIO.createFile(filePath);
	}
	
	/**
	 * See {@link FilesIO#deleteFile(String, boolean)}.
	 */
	public void deleteFile(String filePath, boolean system) {
		filesIO.deleteFile(filePath, system);
	}
	
	/**
	 * See {@link FilesIO#getUserDir()}.
	 */
	public File getUserDir() {
		return filesIO.getUserDir();
	}
	
	/**
	 * See {@link FilesIO#getSystemDir()}.
	 */
	public File getSystemDir() {
		return filesIO.getSystemDir();
	}
	
	/**
	 * See {@link FilesIO#getSharedDir()}.
	 */
	public File getSharedDir() {
		return filesIO.getSharedDir();
	}

}
