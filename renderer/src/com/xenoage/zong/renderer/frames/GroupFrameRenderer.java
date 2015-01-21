package com.xenoage.zong.renderer.frames;

import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Renderer for a {@link GroupFrame}.
 * 
 * @author Andreas Wenger
 */
public class GroupFrameRenderer
	extends FrameRenderer {

	@Override public void paint(Frame frame, Canvas canvas, RendererArgs args) {
		super.paint(frame, canvas, args);
		//paint child frames
		GroupFrame groupFrame = (GroupFrame) frame;
		for (Frame child : groupFrame.children) {
			FramesRenderer.paintAny(child, canvas, args);
		}
	}

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
	}

}
