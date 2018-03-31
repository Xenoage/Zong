package com.xenoage.utils.jse.javafx;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.File;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import com.xenoage.utils.document.io.FileFormat;

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

}
