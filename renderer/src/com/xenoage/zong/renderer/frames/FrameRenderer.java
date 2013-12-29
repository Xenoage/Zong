package com.xenoage.zong.renderer.frames;

import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;


/**
 * Classes implementing this interface allow to draw all
 * type of frames.
 * 
 * @author Andreas Wenger
 */
public interface FrameRenderer
{
	
	/**
	 * Paints the given {@link Frame} of the given {@link Layout} on the
	 * given {@link Canvas} using the given {@link RendererArgs}.
	 */
	public void paintAny(Frame frame, Layout layout, Canvas canvas, RendererArgs args);

}
