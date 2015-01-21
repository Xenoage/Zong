package com.xenoage.zong.renderer.awt.image;

import java.awt.image.BufferedImage;

import com.xenoage.zong.layout.frames.ImageFrame;

/**
 * A cache for images, used for example within {@link ImageFrame}s.
 * It is needed for better performance, since it would be too slow
 * to load each image each time before it is rendered.
 *
 * @author Andreas Wenger
 */
public interface ImagePool {

	/**
	 * Gets the image at the given path, or null.
	 */
	public BufferedImage getImage(String path);

}
