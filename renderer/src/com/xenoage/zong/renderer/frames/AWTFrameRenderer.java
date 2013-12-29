package com.xenoage.zong.renderer.frames;

import java.util.HashMap;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.FrameType;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.AWTCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;


/**
 * This class manages the AWT frame renderers.
 * 
 * @author Andreas Wenger
 */
public final class AWTFrameRenderer
	implements FrameRenderer
{
	
	private HashMap<FrameType, AWTFrameRendererBase> renderers;
	
	private static AWTFrameRenderer instance;
	
	
	public static AWTFrameRenderer getInstance()
	{
		if (instance == null)
			instance = new AWTFrameRenderer();
		return instance;
	}
	
	
	private AWTFrameRenderer()
	{
		renderers = new HashMap<FrameType, AWTFrameRendererBase>();
		renderers.put(FrameType.GroupFrame, new AWTGroupFrameRenderer());
		renderers.put(FrameType.ImageFrame, new AWTImageFrameRenderer());
		renderers.put(FrameType.ScoreFrame, new AWTScoreFrameRenderer());
		renderers.put(FrameType.TextFrame, new AWTTextFrameRenderer());
	}
	
	
	@MaybeNull public AWTFrameRendererBase get(FrameType frameType)
	{
		return renderers.get(frameType);
	}
	
	
	/**
	 * Registers the given renderer for the given type of frame.
	 */
	public void registerRenderer(FrameType frameType, AWTFrameRendererBase renderer)
	{
		renderers.put(frameType, renderer);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public void paintAny(Frame frame, Layout layout, Canvas canvas, RendererArgs args)
	{
		AWTFrameRendererBase renderer = AWTFrameRenderer.getInstance().get(frame.getType());
		if (renderer != null)
			renderer.paint(frame, layout, (AWTCanvas) canvas, args);
		/* handles TEMP 
		renderer = AWTFrameRenderer.getInstance().get(FrameType.FrameHandles);
		if (renderer != null)
			renderer.paint(frame, layout, (AWTCanvas) canvas, args); //*/
	}
	

}
