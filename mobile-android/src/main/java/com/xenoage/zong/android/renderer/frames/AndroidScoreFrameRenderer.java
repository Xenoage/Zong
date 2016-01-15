package com.xenoage.zong.android.renderer.frames;

import android.graphics.Paint;
import android.graphics.RectF;

import com.xenoage.zong.android.renderer.background.AndroidBackgroundRenderer;
import com.xenoage.zong.android.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.frames.FrameRenderer;
import com.xenoage.zong.renderer.stampings.StampingRenderer;

/**
 * Android renderer for a score frame.
 * 
 * @author Andreas Wenger
 */
public class AndroidScoreFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas,	RendererArgs args) {
		android.graphics.Canvas c = AndroidCanvas.getCanvas(canvas);

		float w = frame.getSize().width;
		float h = frame.getSize().height;

		//if there is a background, draw it
		if (frame.getBackground() != null) {
			Paint background = AndroidBackgroundRenderer.getPaint(frame.getBackground());
			c.drawRect(new RectF(-w / 2, -h / 2, w / 2, h / 2), background);
		}

		//draw musical elements
		ScoreFrame scoreFrame = (ScoreFrame) frame;
		ScoreFrameLayout scoreLayout = scoreFrame.getScoreFrameLayout();

		//the coordinates of the layout elements are relative to the upper left
		//corner, so we have to translate them
		int oldTransform = c.save();
		c.translate(-w / 2, -h / 2);

		for (Stamping s : scoreLayout.getMusicalStampings()) {
			StampingRenderer.drawAny(s, canvas, args);
		}

		c.restoreToCount(oldTransform);
	}

}
