package com.xenoage.zong.renderer.frames;

import java.util.HashMap;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.FrameType;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;


/**
 * This class manages the Android frame renderers.
 * 
 * @author Andreas Wenger
 */
public final class AndroidFrameRenderer
	implements FrameRenderer
{
	
	private HashMap<FrameType, AndroidFrameRendererBase> renderers;
	
	private static AndroidFrameRenderer instance;
	
	
	public static AndroidFrameRenderer getInstance()
	{
		if (instance == null)
			instance = new AndroidFrameRenderer();
		return instance;
	}
	
	
	private AndroidFrameRenderer()
	{
		renderers = new HashMap<FrameType, AndroidFrameRendererBase>();
		renderers.put(FrameType.GroupFrame, new AndroidGroupFrameRenderer());
		renderers.put(FrameType.ImageFrame, new AndroidImageFrameRenderer());
		renderers.put(FrameType.ScoreFrame, new AndroidScoreFrameRenderer());
		renderers.put(FrameType.TextFrame, new AndroidTextFrameRenderer());
	}
	
	
	@MaybeNull public AndroidFrameRendererBase get(FrameType frameType)
	{
		return renderers.get(frameType);
	}
	
	
	@Override public void paintAny(Frame frame, Layout layout, Canvas canvas, RendererArgs args)
	{
		AndroidFrameRendererBase renderer = get(frame.getType());
		if (renderer != null)
			renderer.paint(frame, layout, (AndroidCanvas) canvas, args);
	}
	

}
