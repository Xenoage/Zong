package com.xenoage.utils.jse;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.jse.settings.Settings;

/**
 * Useful methods for working with a {@link JFileChooser}.
 * 
 * @author Andreas Wenger
 */
public class JFileChooserUtil {

	/**
	 * Removes all file filters.
	 */
	public static void clearFileFilters(JFileChooser fileChooser) {
		while (fileChooser.getChoosableFileFilters().length > 0)
			fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
	}

	/**
	 * Creates a Swing {@link FileFilter} for the given {@link FileFormat}.
	 */
	public static FileFilter createFileFilter(final FileFormat<?> fileFormat) {
		FileFilter ret = new FileFilter() {

			@Override public String getDescription() {
				return fileFormat.getFilterDescription();
			}

			@Override public boolean accept(File f) {
				return isAccepted(fileFormat, f);
			}
		};
		return ret;
	}
	
	/**
	 * Returns true, if a file filter dialog would accept the given file.
	 * By default, directories and files ending with the default extension
	 * (case insensitive) are accepted.
	 */
	private static boolean isAccepted(FileFormat<?> format, File file) {
		String name = file.getName().toLowerCase();
		return file.isDirectory() || name.endsWith(format.getDefaultExtension());
	}

	/**
	 * Sets the given {@link JFileChooser} to the location that has been saved
	 * as the last directory within the {@link Settings}, if available.
	 */
	public static void setDirFromSettings(JFileChooser fileChooser) {
		setDirFromSettings(fileChooser, new File("files"));
	}

	/**
	 * Sets the given {@link JFileChooser} to the location that has been saved
	 * as the last directory within the {@link Settings}, if available. Otherwise, the given default directory is set.
	 */
	public static void setDirFromSettings(JFileChooser fileChooser, File defaultDir) {
		setDirFromSettings(fileChooser, "lastdocumentdirectory", defaultDir);
	}

	/**
	 * Saves the directory of the given {@link JFileChooser}
	 * as the last directory to the {@link Settings}.
	 */
	public static void rememberDir(JFileChooser fileChooser) {
		rememberDir(fileChooser, "lastdocumentdirectory");
	}
	
	/**
	 * Sets the given {@link JFileChooser} to the location that has been saved under the given key
	 * within the {@link Settings}, if available. Otherwise, the given default directory is set.
	 */
	public static void setDirFromSettings(JFileChooser fileChooser, String key, File defaultDir) {
		String lastDocumentDir = Settings.getInstance().getSetting(key, "paths");
		if (lastDocumentDir != null) {
			fileChooser.setCurrentDirectory(new File(lastDocumentDir));
		}
		else {
			fileChooser.setCurrentDirectory(defaultDir);
		}
	}

	/**
	 * Saves the directory of the given {@link JFileChooser}
	 * under the given key to the {@link Settings}.
	 */
	public static void rememberDir(JFileChooser fileChooser, String key) {
		Settings.getInstance().saveSetting(key, "paths",
			fileChooser.getCurrentDirectory().toString());
	}

}
