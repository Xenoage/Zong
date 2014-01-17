package com.xenoage.zong.renderer.frames;

import android.graphics.Canvas;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.TextFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;


/**
 * Android renderer for a text frame.
 * 
 * Notice that this renderer is not optimized
 * for performance in any way. It should
 * only be used for printing.
 * 
 * @author Andreas Wenger
 */
public class AndroidTextFrameRenderer
  extends AndroidFrameRendererBase
{
  
  
	/**
	 * {@inheritDoc}
	 */
	@Override protected void paintTransformed(Frame frame, Layout layout, AndroidCanvas canvas,
		RendererArgs args)
	{
		Canvas c = canvas.getGraphicsContext();
  	TextFrame textFrame = (TextFrame) frame;
  	
  	float w = frame.data.size.width;
  	float h = frame.data.size.height;
  	c.translate(-w/2, -h/2);
  	
  	canvas.drawText(
  		textFrame.getTextWithLineBreaks(), null, new Point2f(0, 0), false, w);
  	
  }

}
