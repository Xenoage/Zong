package com.xenoage.utils.jse.files;

import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.io.JseStreamUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

/**
 * Manages a list of the recently opened files.
 * It is loaded from and saved to a text file called
 * "data/recentfiles". Only existing files
 * are listed, other ones are automatically removed.
 * 
 * Listeners can be registered that are notified when the list changes.
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class RecentFiles {

	static final String filePath = "data/recentfiles";
	static final int maxEntries = 5;

	private static List<RecentFilesListener> listeners = alist();


	/**
	 * Gets the list of recent files. If an error occurs, an empty
	 * list is returned (no error reporting is done).
	 */
	public static ArrayList<File> getRecentFiles() {
		ArrayList<File> ret = new ArrayList<>(maxEntries);
		if (io().existsFile(filePath)) {
			try {
				String list = JseStreamUtils.readToString(io().openFile(filePath));
				if (list != null) {
					String[] files = list.split("\n");
					for (int i = 0; i < maxEntries && i < files.length; i++) {
						File entryFile = new File(files[i]);
						if (entryFile.exists())
							ret.add(entryFile);
					}
				}
			} catch (IOException ex) {
				log(warning(ex));
			}
		}
		return ret;
	}

	/**
	 * Adds a new file to the beginning of the list.
	 * It is automatically stored on disk.
	 * Errors are not reported.
	 */
	public static void addRecentFile(File file) {
		if (!file.exists())
			return;
		file = file.getAbsoluteFile();
		//get list and add given file
		ArrayList<File> files = getRecentFiles();
		files.remove(file);
		files.add(0, file);
		//trim list
		while (files.size() > maxEntries)
			files.remove(files.size() - 1);
		//save list
		try {
			Writer writer = new FileWriter(io().createFile(filePath));
			for (File f : files) {
				writer.append(f.getAbsolutePath() + "\n");
			}
			writer.close();
		} catch (IOException ex) {
		}
		//notify listeners
		for (RecentFilesListener listener : listeners) {
			listener.recentFilesChanged();
		}
	}

	/**
	 * Adds the given {@link RecentFilesListener}.
	 */
	public static void addListener(RecentFilesListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes all {@link RecentFilesListener}s.
	 */
	static void removeAllListeners() {
		listeners.clear();
	}

}
