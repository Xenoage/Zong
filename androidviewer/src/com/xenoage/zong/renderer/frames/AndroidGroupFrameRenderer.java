package com.xenoage.zong.renderer.frames;

import static com.xenoage.zong.renderer.frames.AndroidFramesRenderer.androidFramesRenderer;
import android.graphics.Paint;
import android.graphics.RectF;

import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.background.AndroidBackgroundRenderer;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Android renderer for a group frame.
 * 
 * @author Andreas Wenger
 */
public class AndroidGroupFrameRenderer
	extends FrameRenderer {
	
	@Override public void paint(Frame frame, Canvas canvas, RendererArgs args) {
		super.paint(frame, canvas, args);
		//paint child frames
		GroupFrame groupFrame = (GroupFrame) frame;
		for (Frame child : groupFrame.children) {
			androidFramesRenderer.paintAny(child, canvas, args);
		}
	}

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		android.graphics.Canvas c = AndroidCanvas.getCanvas(canvas);
		//if there is a background, draw it
		if (frame.getBackground() != null) {
			Paint background = AndroidBackgroundRenderer.getPaint(frame.getBackground());
			float x = frame.getSize().width;
			float y = frame.getSize().height;
			c.drawRect(new RectF(-x / 2, -y / 2, x / 2, y / 2), background);
		}
	}

}
