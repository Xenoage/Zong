package com.xenoage.zong.desktop.renderer.frames;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.xenoage.zong.desktop.renderer.background.AwtBackgroundRenderer;
import com.xenoage.zong.desktop.renderer.canvas.AwtCanvas;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.frames.FrameRenderer;
import com.xenoage.zong.renderer.stampings.StampingRenderer;

/**
 * AWT renderer for a {@link ScoreFrame}.
 * 
 * @author Andreas Wenger
 */
public class AwtScoreFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		Graphics2D g2d = AwtCanvas.getGraphics2D(canvas);
		float w = frame.getSize().width;
		float h = frame.getSize().height;

		//if there is a background, draw it
		if (frame.getBackground() != null) {
			Paint background = AwtBackgroundRenderer.getPaint(frame.getBackground());
			g2d.setPaint(background);
			g2d.fill(new Rectangle2D.Float(-w / 2, -h / 2, w, h));
		}

		//draw musical elements
		ScoreFrame scoreFrame = (ScoreFrame) frame;
		ScoreFrameLayout scoreLayout = scoreFrame.getScoreFrameLayout();
		if (scoreLayout != null) {
			//the coordinates of the layout elements are relative to the upper left
			//corner, so we have to translate them
			AffineTransform oldTransform = g2d.getTransform();
			g2d.translate(-w / 2, -h / 2);

			//get musical stampings, and in interactive mode, also
			//stampings like for playback and selection
			Iterable<Stamping> stampings = (canvas.getDecoration() == CanvasDecoration.Interactive ? scoreLayout
				.getAllStampings() : scoreLayout.getMusicalStampings());
			//render them
			for (Stamping s : stampings) {
				StampingRenderer.drawAny(s, canvas, args);
			}

			//restore old transformation
			g2d.setTransform(oldTransform);
		}

	}

}
