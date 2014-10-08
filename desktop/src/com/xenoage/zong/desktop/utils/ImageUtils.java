package com.xenoage.zong.desktop.utils;

import static com.xenoage.utils.jse.JsePlatformUtils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.image.Image;

import com.xenoage.utils.jse.io.DesktopIO;

/**
 * Useful methods to work with JavaFX images.
 * 
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class ImageUtils {
	
	/**
	 * Returns an {@link Image} with the image behind the
	 * given path, or null if it can't be loaded.
	 */
	public static Image imageOrNull(String filePath) {
		try {
			if (false == io().existsFile(filePath))
				return null;
			return new Image(io().openFile(filePath));
		} catch (IOException ex) {
			return null;
		}
	}
	
	/**
	 * Returns an {@link Image} with the image behind the
	 * given file, or null if it can't be loaded.
	 */
	public static Image imageOrNull(File file) {
		try {
			if (file == null)
				return null;
			return new Image(new FileInputStream(file));
		} catch (IOException ex) {
			return null;
		}
	}

}
