package com.xenoage.zong.renderer.frames;

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

	protected Map<FrameType, FrameRenderer> renderers;

	protected FramesRenderer() {
		renderers = getRenderers();
	}
	
	protected abstract Map<FrameType, FrameRenderer> getRenderers();

	@MaybeNull public FrameRenderer get(FrameType frameType) {
		return renderers.get(frameType);
	}

	/**
	 * Registers the given renderer for the given type of frame.
	 */
	public void registerRenderer(FrameType frameType, FrameRenderer renderer) {
		renderers.put(frameType, renderer);
	}

	/**
	 * Paints the given {@link Frame} on the
	 * given {@link Canvas} using the given {@link RendererArgs}.
	 */
	public void paintAny(Frame frame, Canvas canvas, RendererArgs args) {
		FrameRenderer renderer = get(frame.getType());
		if (renderer != null)
			renderer.paint(frame, canvas, args);
		/* handles TEMP 
		renderer = AWTFrameRenderer.getInstance().get(FrameType.FrameHandles);
		if (renderer != null)
			renderer.paint(frame, layout, (AWTCanvas) canvas, args); //*/
	}

}
