package com.xenoage.zong.renderer.frames;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xenoage.utils.io.IO;
import com.xenoage.zong.desktop.renderer.canvas.AWTCanvas;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ImageFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.pool.ImagePool;


/**
 * AWT renderer for an image frame.
 * 
 * If the image is not available in the {@link ImagePool} of the
 * {@link RendererArgs}, it is either replaced by a placeholder
 * or it is loaded, renderered and immediately forgotten again.
 * This depends on the {@link CanvasIntegrity} setting of the
 * {@link Canvas}. If it is {@link CanvasIntegrity#Perfect},
 * the image is always painted.
 * 
 * @author Andreas Wenger
 */
public class AWTImageFrameRenderer
  extends AWTFrameRendererBase
{
  
  
  /**
   * {@inheritDoc}
   */
  @Override protected void paintTransformed(Frame frame, Layout layout,
    AWTCanvas canvas, RendererArgs args)
  {
    Graphics2D g2d = canvas.getGraphicsContext();
    
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
  				image = ImageIO.read(IO.openInputStream(imagePath));
  			} catch (IOException ex) {
  			}
      }
    }
    
    float w = frame.data.size.width;
		float h = frame.data.size.height;
		
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
