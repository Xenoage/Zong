package com.xenoage.zong.renderer.frames;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.xenoage.utils.graphics.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.desktop.renderer.canvas.AWTCanvas;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.RendererArgs;


/**
 * Abstract base class for frame renderers based on AWT.
 * 
 * @author Andreas Wenger
 */
public abstract class AWTFrameRendererBase
{
	
	/**
	 * Paints the given {@link Frame} of the given {@link Layout} on the
	 * given {@link AWTCanvas} using the given {@link RendererArgs}.
	 */
  public void paint(Frame frame, Layout layout, AWTCanvas canvas, RendererArgs args)
  {
    Graphics2D g2d = AWTCanvas.getGraphics2D(canvas);
    //backup old transformation
    AffineTransform oldTransform = g2d.getTransform();
    //apply translation: origin offset in screen space
    g2d.translate(args.offsetPx.x, args.offsetPx.y);
    //apply scaling
    double scaling = Units.mmToPx(args.scaling, 1);
    g2d.scale(scaling, scaling);
    //apply translation: absolute frame position in layout space
    Point2f pos = frame.getAbsolutePosition(layout);
    g2d.translate(pos.x, pos.y);
    //apply rotation
    g2d.rotate(-frame.getAbsoluteRotation(layout) * Math.PI / 180f);
    
    //DEMO
    /*
    g2d.setColor(java.awt.Color.green);
    g2d.setStroke(new java.awt.BasicStroke(1));
    float w = frame.getSize().width;
    float h = frame.getSize().height;
    g2d.draw(new java.awt.geom.Rectangle2D.Float(-w/2, -h/2, w, h));
    //*/
    
    //paint the frame
    paintTransformed(frame, layout, canvas, args);
    
    //restore old transformation
    g2d.setTransform(oldTransform);
  }
  
  
  /**
	 * Paints the given {@link Frame} of the given {@link Layout} on the
	 * given {@link AWTCanvas} using the given {@link RendererArgs}.
   * This method is called by the {@link #paint(Frame, Layout, AWTCanvas, RendererArgs)}
   * method, the transformations are alread done. When painting, 1 unit corresponds
   * to 1 mm, and the center point is in the middle of the frame.
   */
  protected abstract void paintTransformed(Frame frame, Layout layout,
  	AWTCanvas canvas, RendererArgs args);
  

}
