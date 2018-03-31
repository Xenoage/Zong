package com.xenoage.utils.jse.io;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.io.FileFilter;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.index.FilesystemIndex;

/**
 * Some useful input/output methods for files on the filesystem
 * on a JSE based application.
 * 
 * The following directories are used:
 * <ul>
 *   <li>"System directory": The directory where the program is installed (program directory),
 *     also containing the provided data files</li>
 *   <li>"User directory": The user's custom data and settings directory (user directory)</li>
 *   <li>"Shared directory": Optional: a directory where to search for data, which is shared between
 *     different programs (shared directory)</li>
 * </ul>
 * 
 * Both relative and absolute paths are supported.
 * The following rules apply for relative paths:
 * 
 * When listing files, all directories are listed sequentially,
 * beginning with the user directory, then the system directory,
 * and the shared directory (if it exists).
 * 
 * When reading files, first the user's directory is read,
 * then the system directory and finally the shared directory.
 * 
 * When writing files, always the user's directory is written to.
 * 
 * This allows files to be overwritten by individual users, e.g.
 * to replace some provided files with own ones, without destroying
 * the original installation.
 *
 * @author Andreas Wenger
 */
public class FilesIO
	implements FilesystemInput {

	private File userDir;
	private File systemDir;
	private File sharedDir;

	
	/**
	 * Creates a {@link FilesIO}.
	 * When the directory "../shared" or "shared" exists, it is used as the shared directory.
	 * When the {@link FilesystemIndex} file exists in the classpath, bundled resources can be used.
	 * @param programName  Name of the program. An user directory for this program
	 *                     will be created if there is none. If null, no directory is
	 *                     created and the working directory is used as the user directory.
	 */
	public FilesIO(String programName) {
		sharedDir = new File("../shared");
		if (!sharedDir.exists())
			sharedDir = new File("shared");
		if (!sharedDir.exists())
			sharedDir = null;
		init(programName, null, sharedDir);
	}

	/**
	 * Creates a {@link FilesIO}.
	 * @param programName   Name of the program. An user directory for this program
	 *                      will be created if there is none. If null, no directory is
	 *                      created and the working directory is used as the user directory.
	 * @param systemDir     The custom system directory. If null, the working directory is used
	 * @param sharedDir     An additional directory with shared files. may be null.
	 */
	public FilesIO(String programName, File systemDir, File sharedDir) {
		init(programName, systemDir, sharedDir);
	}

	private void init(String programName, File systemDir, File sharedDir) {
		this.systemDir = systemDir != null ? systemDir : new File(System.getProperty("user.dir"));
		if (programName != null) {
			userDir = JseFileUtils.getUserAppDataDirectory(programName);
			if (!userDir.exists()) {
				userDir.mkdirs();
			}
		}
		else {
			userDir = systemDir;
		}
		this.sharedDir = sharedDir;
	}
	
	/**
	 * Creates a {@link FilesIO} for testing (e.g. unit tests).
	 * The program name is composed of "xenoage" and the name of the latest calling class
	 * ending with "Test" or "Try" (otherwise "unknown").
	 * When the directory "../shared" exists, it is used as the shared directory.
	 */
	public static FilesIO createTestIO() {
		File sharedDir = new File("../shared");
		if (!sharedDir.exists())
			sharedDir = null;
		//get first test class name
		String s = "unknown";
		int i = 2;
		try {
			while (s != null) {
				s = platformUtils().getCaller(i).getClassName();
				if (s.endsWith("Test") || s.endsWith("Tests") || s.endsWith("Try"))
					break;
				i++;
			}
		} catch (Exception ex) {
			s = "unknown";
		}
		return new FilesIO("xenoage/" + s, null, sharedDir);
	}
	
	@Override public void existsFileAsync(String filePath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsFile(filePath));
	}

	/**
	 * Like {@link #existsFileAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsFile(String filePath) {
		File absoluteFile = new File(filePath);
		if (absoluteFile.isAbsolute()) {
			//absolute path
			return absoluteFile.exists();
		}
		else {
			//relative path
			return new File(userDir, filePath).exists() || new File(systemDir, filePath).exists() ||
				(sharedDir != null ? new File(sharedDir, filePath).exists() : false);
		}
	}
	
	@Override public void existsDirectoryAsync(String dirPath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsDirectory(dirPath));
	}

	/**
	 * Like {@link #existsDirectoryAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsDirectory(String directory) {
		File absoluteDir = new File(directory);
		if (absoluteDir.isAbsolute()) {
			//absolute path
			return absoluteDir.exists() && absoluteDir.isDirectory();
		}
		else {
			//relative path
			File userFile = new File(userDir, directory);
			if (userFile.exists() && userFile.isDirectory())
				return true;
			File systemFile = new File(systemDir, directory);
			if (systemFile.exists() && systemFile.isDirectory())
				return true;
			if (sharedDir != null) {
				File sharedFile = new File(sharedDir, directory);
				if (sharedFile.exists() && sharedFile.isDirectory())
					return true;
			}
		}
		return false;
	}
	
	@Override public void listFilesAsync(String dirPath, AsyncResult<List<String>> fileNames) {
		fileNames.onSuccess(listFiles(dirPath));
	}

	/**
	 * Like {@link #listFilesAsync(String)}, but with direct return.
	 */
	public List<String> listFiles(String dirPath) {
		return listFiles(dirPath, null);
	}
	
	@Override public void listFilesAsync(String dirPath, FileFilter filter,
		AsyncResult<List<String>> fileNames) {
		fileNames.onSuccess(listFiles(dirPath, filter));
	}

	/**
	 * Like {@link #listFilesAsync(String, FileFilter, AsyncResult)}, but with direct return.
	 */
	public List<String> listFiles(String dirPath, FileFilter filter) {
		FilenameFilter jseFilenameFilter = JseFileUtils.getFilter(filter);
		File absoluteDir = new File(dirPath);
		if (absoluteDir.isAbsolute()) {
			//absolute path
			return listFiles(absoluteDir, jseFilenameFilter);
		}
		else {
			//relative path
			Set<String> ret = new HashSet<>();
			for (int iDir = 0; iDir < 3; iDir++) {
				File baseDir = null;
				switch (iDir) {
					case 0:
						baseDir = userDir;
						break;
					case 1:
						baseDir = systemDir;
						break;
					case 2:
						baseDir = sharedDir;
						break;
				}
				if (baseDir != null) {
					File dir = new File(baseDir, dirPath);
					if (dir.exists())
						ret.addAll(listFiles(dir, jseFilenameFilter));
				}
			}
			return alist(ret);
		}
	}
	
	private List<String> listFiles(File dir, @MaybeNull FilenameFilter filter) {
		String[] files = (filter != null ? dir.list(filter) : dir.list());
		if (files != null)
			return Arrays.asList(files);
		else
			return Collections.<String>emptyList();
	}
	
	@Override public void listDirectoriesAsync(String dirPath, AsyncResult<List<String>> dirNames) {
		dirNames.onSuccess(listDirectories(dirPath));
	}

	/**
	 * Like {@link #listDirectoriesAsync(String, AsyncResult)}, but with direct return.
	 */
	public List<String> listDirectories(String dirPath) {
		File absoluteDir = new File(dirPath);
		if (absoluteDir.isAbsolute()) {
			//absolute path
			return listDirectories(absoluteDir);
		}
		else {
			//relative path
			Set<String> ret = new HashSet<>();
			for (int iDir = 0; iDir < 3; iDir++) {
				File baseDir = null;
				switch (iDir) {
					case 0:
						baseDir = userDir;
						break;
					case 1:
						baseDir = systemDir;
						break;
					case 2:
						baseDir = sharedDir;
						break;
				}
				if (baseDir != null) {
					File dir = new File(baseDir, dirPath);
					if (dir.exists())
						ret.addAll(listDirectories(dir));
				}
			}
			return alist(ret);
		}
	}
	
	private List<String> listDirectories(File dir) {
		File[] dirs = dir.listFiles(JseFileUtils.getDirectoriesFilter());
		if (dirs != null) {
			List<String> ret = alist();
			for (File d : dirs) {
				ret.add(d.getName());
			}
			return ret;
		}
		else {
			return Collections.<String>emptyList();
		}
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
		File file = findFile(filePath);
		if (file == null)
			throw new FileNotFoundException(filePath);
		return new JseInputStream(new FileInputStream(file));
	}
	
	/**
	 * Gets the file at the given absolute or relative path.
	 * A relative path is searched in the user's application settings directory first,
	 * and if not found, in the directory where the program was installed, and then
	 * in the shared directory.
	 * If still not found, null is returned.
	 */
	public File findFile(String filePath)
		throws IOException {
		File file = new File(filePath);
		if (file.isAbsolute()) {
			//absolute path
			if (file.exists())
				return file;
			else
				return null;
		}
		else {
			//relative path
			file = new File(userDir, filePath);
			if (!file.exists())
				file = new File(systemDir, filePath);
			if (!file.exists() && sharedDir != null)
				file = new File(sharedDir, filePath);
			if (file.exists())
				return file;
			else
				return null;
		}
	}
	
	/**
	 * Gets the modification date of the given file, or null, if the date is unavailable.
	 */
	public Date getFileModificationDate(String filePath) {
		File file = new File(userDir, filePath);
		if (!file.exists()) {
			file = new File(systemDir, filePath);
		}
		if (!file.exists() && sharedDir != null) {
			file = new File(sharedDir, filePath);
		}
		if (file.exists()) {
			return new Date(file.lastModified());
		}
		else {
			return null;
		}
	}

	/**
	 * Gets the data file for writing at the given absolute or relative path.
	 * Relative files are always located in the user's application settings folder.
	 * If not existing, the parent directories are created.
	 */
	public File createFile(String filePath) {
		File file = new File(filePath);
		if (false == file.isAbsolute())
			file = new File(userDir, filePath);
		//create the parent directory on demand
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		//open output stream
		return file;
	}

	/**
	 * Removes the data file at the given absolute relative path.
	 * @param system  If true, not only the user's private data file is deleted,
	 *                but also the system data file.
	 *                Files in the shared folder are never deleted.
	 *                Used only for relative paths.
	 */
	public void deleteFile(String filePath, boolean system) {
		File file = new File(filePath);
		if (file.isAbsolute()) {
			//absolute path
			if (file.exists())
				file.delete();
		}
		else {
			//relative path
			file = new File(userDir, filePath);
			if (file.exists())
				file.delete();
			if (system) {
				file = new File(systemDir, filePath);
				if (file.exists())
					file.delete();
			}
		}
	}

	public File getUserDir() {
		return userDir;
	}
	
	public File getSystemDir() {
		return systemDir;
	}
	
	public File getSharedDir() {
		return sharedDir;
	}

}
