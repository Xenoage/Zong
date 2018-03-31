package com.xenoage.utils.jse;

import static com.xenoage.utils.jse.JsePlatformUtils.io;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.xenoage.utils.jse.io.DesktopIO;

/**
 * Useful methods to work with images.
 * 
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class ImageUtils {

	/**
	 * Returns an {@link ImageIcon} with the image behind the
	 * given path, or null if it can't be loaded.
	 */
	public static ImageIcon iconOrNull(String filepath) {
		Image img = imageOrNull(filepath);
		if (img != null)
			return new ImageIcon();
		else
			return null;
	}

	/**
	 * Returns an {@link Image} with the image behind the
	 * given path, or null if it can't be loaded.
	 */
	public static Image imageOrNull(String filepath) {
		try {
			return ImageIO.read(io().openFile(filepath));
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * Creates a new system-compatible image with the given size, which is much faster when drawn
	 * than an image with a custom format.
	 */
	public static BufferedImage createCompatibleImage(int width, int height, boolean withAlpha) {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage newImage = gc.createCompatibleImage(width, height,
			(withAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE));
		return newImage;
	}

	/**
	 * Converts the given image to a system-compatible image, which is much faster when drawn.
	 */
	public static BufferedImage convertToCompatibleImage(BufferedImage image) {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage newImage = gc.createCompatibleImage(image.getWidth(), image.getHeight(),
			Transparency.TRANSLUCENT);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

}
