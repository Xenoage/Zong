package com.xenoage.zong.renderer.frames;

import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.TextFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Renderer for a {@link TextFrame}.
 * 
 * @author Andreas Wenger
 */
public class TextFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		TextFrame textFrame = (TextFrame) frame;
		Rectangle2f rect = getLocalRect(textFrame);
		canvas.drawText(textFrame.getTextWithLineBreaks(), null, rect.position, false, rect.width());
	}

}
