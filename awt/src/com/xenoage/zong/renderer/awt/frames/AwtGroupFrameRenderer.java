package com.xenoage.zong.renderer.awt.frames;

import static com.xenoage.zong.renderer.awt.frames.AwtFramesRenderer.awtFramesRenderer;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.awt.background.AwtBackgroundRenderer;
import com.xenoage.zong.renderer.awt.canvas.AwtCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.frames.FrameRenderer;

/**
 * AWT renderer for a {@link GroupFrame}.
 * 
 * @author Andreas Wenger
 */
public class AwtGroupFrameRenderer
	extends FrameRenderer {

	@Override public void paint(Frame frame, Canvas canvas, RendererArgs args) {
		super.paint(frame, canvas, args);
		//paint child frames
		GroupFrame groupFrame = (GroupFrame) frame;
		for (Frame child : groupFrame.children) {
			awtFramesRenderer().paintAny(child, canvas, args);
		}
	}

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		Graphics2D g2d = AwtCanvas.getGraphics2D(canvas);
		//if there is a background, draw it
		if (frame.getBackground() != null) {
			Paint background = AwtBackgroundRenderer.getPaint(frame.getBackground());
			g2d.setPaint(background);
			float x = frame.getSize().width;
			float y = frame.getSize().height;
			g2d.fill(new Rectangle2D.Float(-x / 2, -y / 2, x, y));
		}
	}

}
