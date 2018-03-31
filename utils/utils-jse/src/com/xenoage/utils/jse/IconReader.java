package com.xenoage.utils.jse;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.xenoage.utils.jse.io.DesktopIO;


/**
 * This class loads icons from a given directory.
 * 
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class IconReader {

	private String dir;


	/**
	 * Creates a new {@link IconReader}, that reads icons from the given directory.
	 * @param dir  the directory. It should end with "/".
	 */
	public IconReader(String dir) {
		this.dir = dir;
	}

	/**
	 * Returns the icon with the given ID as a {@link BufferedImage}.
	 * If it can not be read, null is returned and a warning is logged.
	 */
	public BufferedImage readImage(String iconID) {
		return readImageFromPath(dir + iconID);
	}

	/**
	 * Returns the icon with the given ID as a {@link ImageIcon}.
	 * If it can not be read, null is returned and a warning is logged.
	 */
	public Icon read(String iconID) {
		return readFromPath(dir + iconID);
	}
	
	/**
	 * Returns the icon at given path as a {@link BufferedImage}.
	 * If it can not be read, null is returned and a warning is logged.
	 */
	public static BufferedImage readImageFromPath(String path) {
		try {
			return ImageIO.read(io().openFile(path));
		} catch (IOException ex) {
			log(warning("Could not read icon", ex));
			return null;
		}
	}

	/**
	 * Returns the icon at the given path as a {@link ImageIcon}.
	 * If it can not be read, null is returned and a warning is logged.
	 */
	public static Icon readFromPath(String path) {
		Image img = readImageFromPath(path);
		if (img == null)
			return null;
		return new ImageIcon(img);
	}

}
