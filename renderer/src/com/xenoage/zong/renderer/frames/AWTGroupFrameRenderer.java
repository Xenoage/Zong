package com.xenoage.zong.renderer.frames;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.background.AWTBackgroundRenderer;
import com.xenoage.zong.renderer.canvas.AWTCanvas;


/**
 * AWT renderer for a group frame.
 * 
 * @author Andreas Wenger
 */
public class AWTGroupFrameRenderer
  extends AWTFrameRendererBase
{
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public void paint(Frame frame, Layout layout, AWTCanvas canvas, RendererArgs args)
	{
		super.paint(frame, layout, canvas, args);
		//paint child frames
		GroupFrame groupFrame = (GroupFrame) frame;
		for (Frame child : groupFrame.children) {
			AWTFrameRenderer.getInstance().paintAny(child, layout, canvas, args);
		}
	}
  
  
  /**
   * {@inheritDoc}
   */
  @Override protected void paintTransformed(Frame frame, Layout layout,
    AWTCanvas canvas, RendererArgs args)
  {
    Graphics2D g2d = AWTCanvas.getGraphics2D(canvas);
    //if there is a background, draw it
    if (frame.data.background != null)
    {
      Paint background = AWTBackgroundRenderer.getPaint(frame.data.background);
      g2d.setPaint(background);
      float x = frame.data.size.width;
      float y = frame.data.size.height;
      g2d.fill(new Rectangle2D.Float(-x/2, -y/2, x, y));
    }
  }


}
