package com.xenoage.zong.renderer.frames;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.background.AndroidBackgroundRenderer;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;


/**
 * Android renderer for a group frame.
 * 
 * @author Andreas Wenger
 */
public class AndroidGroupFrameRenderer
  extends AndroidFrameRendererBase
{
  
  
	/**
	 * {@inheritDoc}
	 */
	@Override public void paint(Frame frame, Layout layout, AndroidCanvas canvas, RendererArgs args)
	{
		super.paint(frame, layout, canvas, args);
		//paint child frames
		GroupFrame groupFrame = (GroupFrame) frame;
		for (Frame child : groupFrame.children) {
			AndroidFrameRenderer.getInstance().paintAny(child, layout, canvas, args);
		}
	}
  
  
  /**
   * {@inheritDoc}
   */
  @Override protected void paintTransformed(Frame frame, Layout layout,
  	AndroidCanvas canvas, RendererArgs args)
  {
    Canvas c = canvas.getGraphicsContext();
    //if there is a background, draw it
    if (frame.data.background != null)
    {
      Paint background = AndroidBackgroundRenderer.getPaint(frame.data.background);
      float x = frame.data.size.width;
      float y = frame.data.size.height;
      c.drawRect(new RectF(-x/2, -y/2, x/2, y/2), background);
    }
  }


}
