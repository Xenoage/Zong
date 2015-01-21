package com.xenoage.zong.renderer.awt.image;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static java.awt.Color.lightGray;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Draws an image using AWT.
 * 
 * A global {@link ImagePool} is used for caching images. 
 * If the image is not available there, it is either replaced by a placeholder
 * or it is loaded, rendered and immediately forgotten again.
 * 
 * @author Andreas Wenger
 */
public class AwtImageRenderer {
	
	private static ImagePool imagePool = null; //TODO
	
	/**
	 * Draws an image.
	 * @param imagePath  the filepath of the image, or null for drawing a placeholder
	 * @param g2d        the AWT graphics context
	 * @param rect       the destination rectangle
	 * @param force      if true, the image is drawn (if existing), even when it can
	 *                   not be cached and the performance may be low
	 */
	public static void drawImage(String imagePath, Graphics2D g2d, Rectangle2f rect, boolean force) {
		BufferedImage image = null;
		if (imagePath != null) {
			//try to load from image pool
			if (imagePool != null)
				image = imagePool.getImage(imagePath);
			//otherwise, try to load image temporarily
			if (image == null && force) {
				try {
					image = ImageIO.read(io().openFile(imagePath));
				} catch (IOException ex) {
				}
			}
		}
		//if image could be retrieved, paint it
		if (image != null) {
			//create transformation, that maps the image
			//to the frame borders
			AffineTransform transform = new AffineTransform();
			transform.translate(rect.x1(), rect.x2());
			transform.scale(rect.width() / image.getWidth(), rect.height() / image.getHeight());
			//draw image with this transform
			g2d.drawImage(image, transform, null);
		}
		//otherwise paint a placeholder
		else {
			//fill frame with light gray
			Rectangle2D.Float r = new Rectangle2D.Float(rect.x1(), rect.y1(), rect.x2(), rect.y2());
			g2d.setColor(lightGray);
			g2d.fill(r);
			//border in gray
			g2d.setColor(java.awt.Color.gray);
			g2d.setStroke(new BasicStroke(0.5f)); //0.5 mm line
			g2d.draw(r);
			//diagonals cross in gray
			g2d.draw(new Line2D.Float(rect.x1(), rect.y1(), rect.x2(), rect.y2()));
			g2d.draw(new Line2D.Float(rect.x1(), rect.y2(), rect.x2(), rect.y1()));
		}
	}

}
