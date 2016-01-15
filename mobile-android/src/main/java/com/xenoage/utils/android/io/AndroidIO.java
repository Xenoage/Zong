package com.xenoage.utils.android.io;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.res.Resources;

import com.xenoage.utils.android.AndroidPlatformUtils;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.io.FileFilter;
import com.xenoage.utils.io.FilesystemInput;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.jse.io.JseFile;
import com.xenoage.utils.jse.io.JseInputStream;

/**
 * Some useful input/output methods for an Android application.
 * 
 * Currently only reading the assets of the app is supported.
 * 
 * Use {@link AndroidPlatformUtils#io()} to get an instance of this class.
 *
 * @author Andreas Wenger
 */
public class AndroidIO
	implements FilesystemInput {

	private Resources res;
	
	/**
	 * Initializes the {@link AndroidIO} with the given {@link Resources}.
	 */
	public AndroidIO(Resources res) {
		this.res = res;
	}

	@Override public void existsFileAsync(String filePath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsFile(filePath));
	}

	/**
	 * Like {@link #existsFileAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsFile(String filepath) {
		try {
			java.io.InputStream is = res.getAssets().open(filepath);
			is.close();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	@Override public void existsDirectoryAsync(String dirPath, AsyncResult<Boolean> exists) {
		exists.onSuccess(existsDirectory(dirPath));
	}

	/**
	 * Like {@link #existsDirectoryAsync(String, AsyncResult)}, but with direct return.
	 */
	public boolean existsDirectory(String directory) {
		try {
			res.getAssets().list(directory);
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	@Override public void listFilesAsync(String dirPath, AsyncResult<List<String>> fileNames) {
		try {
			fileNames.onSuccess(listFiles(dirPath));
		} catch (IOException ex) {
			fileNames.onFailure(ex);
		}
	}

	/**
	 * Like {@link #listFilesAsync(String, AsyncResult)}, but with direct return.
	 */
	public List<String> listFiles(String directory)
			throws IOException {
		return listFiles(directory, null);
	}

	public List<String> listFiles(String directory, FileFilter filter)
			throws IOException {
		Set<String> ret = new HashSet<String>();
		String[] files = res.getAssets().list(directory);
		for (String file : files) {
			if (filter == null || filter.accept(new JseFile(new File(directory, file))))
				ret.add(file);
		}
		return alist(ret);
	}

	@Override public void listFilesAsync(String dirPath, FileFilter filter,
										 AsyncResult<List<String>> fileNames) {
		try {
			fileNames.onSuccess(listFiles(dirPath, filter));
		} catch (IOException ex) {
			fileNames.onFailure(ex);
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
	public InputStream openFile(String filePath)
		throws IOException {
		return new JseInputStream(res.getAssets().open(filePath));
	}

	/**
	 * Not supported on Android.
	 */
	public void listDirectoriesAsync(String dirPath, AsyncResult<List<String>> dirNames) {
		dirNames.onFailure(new IOException(
				new UnsupportedOperationException("listDirectories unsupported on Android")));
	}
}
