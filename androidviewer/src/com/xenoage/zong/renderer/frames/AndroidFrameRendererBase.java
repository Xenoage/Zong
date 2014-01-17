package com.xenoage.zong.renderer.frames;

import android.graphics.Canvas;

import com.xenoage.utils.graphics.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;


/**
 * Abstract base class for frame renderers working with Android.
 * 
 * @author Andreas Wenger
 */
public abstract class AndroidFrameRendererBase
{
	
	/**
	 * Paints the given {@link Frame} of the given {@link Layout} on the
	 * given {@link AndroidCanvas} using the given {@link RendererArgs}.
	 */
  public void paint(Frame frame, Layout layout, AndroidCanvas canvas, RendererArgs args)
  {
  	Canvas c = canvas.getGraphicsContext();
  	
    //backup old transformation
    int oldTransform = c.save();
    
    //apply scaling
    float scaling = Units.mmToPx(args.scaling, 1);
    c.scale(scaling, scaling);
    //apply translation: absolute position in layout space
    Point2f pos = frame.getAbsolutePosition(layout);
    c.translate(pos.x, pos.y);
    //apply rotation
    c.rotate(-frame.getAbsoluteRotation(layout));
    
    //paint the frame
    paintTransformed(frame, layout, canvas, args);
    
    //restore old transformation
    c.restoreToCount(oldTransform);
  }
  
  
  /**
	 * Paints the given {@link Frame} of the given {@link Layout} on the
	 * given {@link AndroidCanvas} using the given {@link RendererArgs}.
   * This method is called by the {@link #paint(Frame, Layout, AndroidCanvas, RendererArgs)}
   * method, the transformations are alread done. When painting, 1 unit corresponds
   * to 1 mm, and the center point is in the middle of the frame.
   */
  protected abstract void paintTransformed(Frame frame, Layout layout,
  	AndroidCanvas canvas, RendererArgs args);

}
