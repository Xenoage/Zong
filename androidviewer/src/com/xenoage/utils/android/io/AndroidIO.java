package com.xenoage.utils.android.io;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.res.Resources;

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
 * @author Andreas Wenger
 */
public class AndroidIO
	implements FilesystemInput {

	private static AndroidIO instance = null;
	
	private Resources res;
	
	/**
	 * Initializes the {@link AndroidIO} with the given {@link Resources}.
	 * This methods has to be called before this class can be used.
	 */
	public static void init(Resources res) {
		instance = new AndroidIO();
		instance.res = res;
	}

	/**
	 * Gets the only instance of the {@link AndroidIO} class.
	 * The <code>init</code> method has to be called before, otherwise null is returned.
	 */
	public static AndroidIO androidIO() {
		return instance;
	}

	@Override public boolean existsFile(String filepath) {
		try {
			java.io.InputStream is = res.getAssets().open(filepath);
			is.close();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	@Override public boolean existsDirectory(String directory) {
		try {
			res.getAssets().list(directory);
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
	
	@Override public InputStream openFile(String filePath)
		throws IOException {
		return new JseInputStream(res.getAssets().open(filePath));
	}

	@Override public List<String> listFiles(String directory)
		throws IOException {
		return listFiles(directory, null);
	}

	@Override public List<String> listFiles(String directory, FileFilter filter)
		throws IOException {
		Set<String> ret = new HashSet<String>();
		String[] files = res.getAssets().list(directory);
		for (String file : files) {
			if (filter == null || filter.accept(new JseFile(new File(directory, file))))
				ret.add(file);
		}
		return alist(ret);
	}

	/**
	 * Not supported on Android.
	 */
	@Override public List<String> listDirectories(String directory)
		throws IOException {
		throw new IOException(new UnsupportedOperationException("listDirectories unsupported on Android"));
	}

}
