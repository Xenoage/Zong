package com.xenoage.zong.renderer.awt.frames;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.Map;

import com.xenoage.zong.layout.frames.FrameType;
import com.xenoage.zong.renderer.frames.FrameRenderer;
import com.xenoage.zong.renderer.frames.FramesRenderer;

/**
 * AWT implementation of a {@link FramesRenderer}.
 * 
 * @author Andreas Wenger
 */
public class AwtFramesRenderer
	extends FramesRenderer {
	
	public static AwtFramesRenderer instance = null;
	
	public static AwtFramesRenderer awtFramesRenderer() {
		if (instance == null)
			instance = new AwtFramesRenderer();
		return instance;
	}

	@Override protected Map<FrameType, FrameRenderer> getRenderers() {
		Map<FrameType, FrameRenderer> renderers = map();
		renderers.put(FrameType.GroupFrame, new AwtGroupFrameRenderer());
		renderers.put(FrameType.ImageFrame, new AwtImageFrameRenderer());
		renderers.put(FrameType.ScoreFrame, new AwtScoreFrameRenderer());
		renderers.put(FrameType.TextFrame, new AwtTextFrameRenderer());
		return renderers;
	}

}
