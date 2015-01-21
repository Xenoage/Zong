package com.xenoage.zong.renderer.frames;

import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.Map;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.FrameType;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Renderer for all types of {@link Frame}s.
 * 
 * Platform-specific implementations are provided in subclasses.
 * 
 * @author Andreas Wenger
 */
public abstract class FramesRenderer {

	private static Map<FrameType, FrameRenderer> renderers = getRenderers();
	
	private static Map<FrameType, FrameRenderer> getRenderers() {
		Map<FrameType, FrameRenderer> renderers = map();
		renderers.put(FrameType.GroupFrame, new GroupFrameRenderer());
		renderers.put(FrameType.ImageFrame, new ImageFrameRenderer());
		renderers.put(FrameType.ScoreFrame, new ScoreFrameRenderer());
		renderers.put(FrameType.TextFrame, new TextFrameRenderer());
		return renderers;
	}
	

	@MaybeNull public static FrameRenderer get(FrameType frameType) {
		return renderers.get(frameType);
	}

	/**
	 * Registers the given renderer for the given type of frame.
	 */
	public static void registerRenderer(FrameType frameType, FrameRenderer renderer) {
		renderers.put(frameType, renderer);
	}

	/**
	 * Paints the given {@link Frame} on the
	 * given {@link Canvas} using the given {@link RendererArgs}.
	 */
	public static void paintAny(Frame frame, Canvas canvas, RendererArgs args) {
		FrameRenderer renderer = get(frame.getType());
		if (renderer != null)
			renderer.paint(frame, canvas, args);
	}

}
