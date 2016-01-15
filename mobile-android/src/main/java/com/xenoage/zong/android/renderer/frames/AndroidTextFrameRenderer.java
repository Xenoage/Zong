package com.xenoage.zong.android.renderer.frames;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.android.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.TextFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.frames.FrameRenderer;

/**
 * Android renderer for a text frame.
 * 
 * @author Andreas Wenger
 */
public class AndroidTextFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		android.graphics.Canvas c = AndroidCanvas.getCanvas(canvas);
		TextFrame textFrame = (TextFrame) frame;

		float w = frame.getSize().width;
		float h = frame.getSize().height;
		c.translate(-w / 2, -h / 2);

		canvas.drawText(textFrame.getTextWithLineBreaks(), null, new Point2f(0, 0), false, w);

	}

}
