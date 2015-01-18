package com.xenoage.zong.renderer.awt.frames;

import static com.xenoage.utils.jse.JsePlatformUtils.io;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ImageFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.awt.canvas.AwtCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.frames.FrameRenderer;
import com.xenoage.zong.renderer.pool.ImagePool;

/**
 * AWT renderer for an {@link ImageFrame}.
 * 
 * If the image is not available in the {@link ImagePool} of the
 * {@link RendererArgs}, it is either replaced by a placeholder
 * or it is loaded, rendered and immediately forgotten again.
 * This depends on the {@link CanvasIntegrity} setting of the
 * {@link Canvas}. If it is {@link CanvasIntegrity#Perfect},
 * the image is guaranteed to be painted.
 * 
 * @author Andreas Wenger
 */
public class AwtImageFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas,	RendererArgs args) {
		Graphics2D g2d = AwtCanvas.getGraphics2D(canvas);
		
		//retrieve the image
		ImageFrame imageFrame = (ImageFrame) frame;
		String imagePath = imageFrame.getImagePath();
		BufferedImage image = null;
		if (imagePath != null) {
			//try to load from image pool
			if (args.imagePool != null)
				image = (BufferedImage) args.imagePool.getImageFrameImage(imagePath);
			//otherwise, try to load image temporarily
			if (image == null && canvas.getIntegrity() == CanvasIntegrity.Perfect) {
				try {
					image = ImageIO.read(io().openFile(imagePath));
				} catch (IOException ex) {
				}
			}
		}

		float w = frame.getSize().width;
		float h = frame.getSize().height;

		//if image could be retrieved, paint it
		if (image != null) {
			//create transformation, that maps the image
			//to the frame borders
			AffineTransform transform = new AffineTransform();

			transform.translate(-w / 2, -h / 2);
			transform.scale(w / image.getWidth(), h / image.getHeight());
			//draw image with this transform
			g2d.drawImage(image, transform, null);
		}
		//otherwise paint a placeholder
		else {
			//fill frame with light gray
			g2d.setColor(Color.lightGray);
			g2d.fill(new Rectangle2D.Float(-w / 2, -h / 2, w, h));
			//border in gray
			g2d.setColor(Color.lightGray);
			g2d.setStroke(new BasicStroke(0.5f)); //0.5 mm line
			g2d.draw(new Rectangle2D.Float(-w / 2, -h / 2, w, h));
			//diagonals cross in gray
			g2d.draw(new Line2D.Float(-w / 2, -h / 2, w / 2, h / 2));
			g2d.draw(new Line2D.Float(w / 2, -h / 2, -w / 2, h / 2));
		}
	}

}
