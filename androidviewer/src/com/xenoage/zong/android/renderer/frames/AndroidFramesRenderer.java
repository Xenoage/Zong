package com.xenoage.zong.android.renderer.frames;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.Map;

import com.xenoage.zong.layout.frames.FrameType;
import com.xenoage.zong.renderer.frames.FrameRenderer;
import com.xenoage.zong.renderer.frames.FramesRenderer;

/**
 * This class manages the Android frame renderers.
 * 
 * @author Andreas Wenger
 */
public final class AndroidFramesRenderer
	extends FramesRenderer {

	public static AndroidFramesRenderer androidFramesRenderer = new AndroidFramesRenderer();


	@Override protected Map<FrameType, FrameRenderer> getRenderers() {
		Map<FrameType, FrameRenderer> renderers = map();
		renderers.put(FrameType.GroupFrame, new AndroidGroupFrameRenderer());
		renderers.put(FrameType.ImageFrame, new AndroidImageFrameRenderer());
		renderers.put(FrameType.ScoreFrame, new AndroidScoreFrameRenderer());
		renderers.put(FrameType.TextFrame, new AndroidTextFrameRenderer());
		return renderers;
	}

}
