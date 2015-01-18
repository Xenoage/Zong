package com.xenoage.zong.renderer.awt.frames;

import java.awt.Graphics2D;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.TextFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.awt.canvas.AwtCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.frames.FrameRenderer;

/**
 * AWT renderer for a {@link TextFrame}.
 * 
 * @author Andreas Wenger
 */
public class AwtTextFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		TextFrame textFrame = (TextFrame) frame;
		Graphics2D g2d = AwtCanvas.getGraphics2D(canvas);

		float w = frame.getSize().width;
		float h = frame.getSize().height;
		g2d.translate(-w / 2, -h / 2);

		canvas.drawText(textFrame.getTextWithLineBreaks(), null, new Point2f(0, 0), false, w);
	}

}
