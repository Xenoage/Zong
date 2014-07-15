package com.xenoage.zong.desktop.gui.utils;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.File;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.swing.JFileChooser;

import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.jse.settings.Settings;

/**
 * Useful methods for working with a JavaFX {@link FileChooser}.
 * 
 * @author Andreas Wenger
 */
public class FileChooserUtils {
	
	/**
	 * Sets the initial directory, if it exists.
	 */
	public static void setInitialDir(FileChooser fileChooser, String dir) {
		if (dir == null)
			return;
		File file = new File(dir);
		if (file.exists()) {
			if (file.isFile())
				file = file.getParentFile();
			if (file != null)
				fileChooser.setInitialDirectory(file);
		}
	}

	/**
	 * Adds an extension filter for the given {@link FileFormat} and returns it.
	 */
	public static ExtensionFilter addFilter(FileChooser fileChooser, final FileFormat<?> fileFormat) {
		List<String> fcExtensions = alist();
		fcExtensions.add("*" + fileFormat.getDefaultExtension());
		for (String extension : fileFormat.getOtherExtensions())
			fcExtensions.add("*" + extension);
		ExtensionFilter filter = new ExtensionFilter(fileFormat.getFilterDescription(), fcExtensions);
		fileChooser.getExtensionFilters().add(filter);
		return filter;
	}

	/**
	 * Sets the given {@link FileChooser} to the location that has been saved
	 * as the last directory within the {@link Settings}, if available.
	 */
	public static void setDirFromSettings(FileChooser fileChooser) {
		setDirFromSettings(fileChooser, new File("files"));
	}

	/**
	 * Sets the given {@link FileChooser} to the location that has been saved
	 * as the last directory within the {@link Settings}, if available. Otherwise, the given default directory is set.
	 */
	public static void setDirFromSettings(FileChooser fileChooser, File defaultDir) {
		setDirFromSettings(fileChooser, "lastdocumentdirectory", defaultDir);
	}
	
	/**
	 * Sets the given {@link JFileChooser} to the location that has been saved under the given key
	 * within the {@link Settings}, if available. Otherwise, the given default directory is set.
	 */
	public static void setDirFromSettings(FileChooser fileChooser, String key, File defaultDir) {
		String lastDocumentDir = Settings.getInstance().getSetting(key, "paths");
		File file = lastDocumentDir != null ? new File(lastDocumentDir) : defaultDir;
		if (file.exists() && file.isFile())
			file = file.getParentFile();
		if (file.exists())
			fileChooser.setInitialDirectory(file);
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
