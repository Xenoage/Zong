package com.xenoage.zong.desktop.utils;

import static com.xenoage.utils.jse.JsePlatformUtils.io;

import java.io.File;
import java.io.IOException;

import javafx.scene.image.Image;

import com.xenoage.utils.jse.io.JseIO;

/**
 * Useful methods to work with JavaFX images.
 * 
 * This class uses the {@link JseIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class ImageUtils {
	
	/**
	 * Returns an {@link Image} with the image behind the
	 * given path, or null if it can't be loaded.
	 */
	public static Image imageOrNull(String filepath) {
		try {
			return imageOrNull(io().findFile(filepath));
		} catch (IOException e) {
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
			return new Image(io().openFile(file));
		} catch (IOException e) {
			return null;
		}
	}

}
