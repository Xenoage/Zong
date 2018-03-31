package com.xenoage.utils.jse.settings;

import java.io.File;

/**
 * Settings for the last file location.
 * 
 * @author Andreas Wenger
 */
public class FileSettings {
	
	/**
	 * Gets the the location that has been saved as the last directory
	 * within the {@link Settings}, if available, otherwise null.
	 */
	public static File getLastDir() {
		return getLastDir(new File("files"));
	}

	/**
	 * Gets the location that has been saved as the last directory
	 * within the {@link Settings}, if available.
	 * Otherwise, the given default directory is returned.
	 * If it does not exist, null is returned.
	 */
	public static File getLastDir(File defaultDir) {
		return getLastDir("lastdocumentdirectory", defaultDir);
	}
	
	/**
	 * Gets the location that has been saved under the given key within the {@link Settings},
	 * if available. Otherwise, the given default directory is returned.
	 * If it does not exist, null is returned.
	 */
	public static File getLastDir(String key, File defaultDir) {
		String lastDocumentDir = Settings.getInstance().getSetting(key, "paths");
		File file = lastDocumentDir != null ? new File(lastDocumentDir) : defaultDir;
		if (file.exists() && file.isFile())
			file = file.getParentFile();
		if (file.exists())
			return file;
		else
			return null;
	}
	
	/**
	 * Saves the directory of the given file as the last directory to the {@link Settings}.
	 */
	public static void rememberDir(File file) {
		rememberDir(file, "lastdocumentdirectory");
	}

	/**
	 * Saves the directory of the given file under the given key to the {@link Settings}.
	 */
	public static void rememberDir(File file, String key) {
		if (file.exists() && file.isFile())
			file = file.getParentFile();
		if (file.exists())
			Settings.getInstance().saveSetting(key, "paths", file.getAbsolutePath());
	}

}
