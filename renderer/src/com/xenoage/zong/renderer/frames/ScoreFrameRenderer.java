package com.xenoage.zong.renderer.frames;

import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.stamping.StampingRenderer;

/**
 * Renderer for a {@link ScoreFrame}.
 * 
 * @author Andreas Wenger
 */
public class ScoreFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas, RendererArgs args) {
		Rectangle2f rect = getLocalRect(frame);
		
		//draw musical elements
		ScoreFrame scoreFrame = (ScoreFrame) frame;
		ScoreFrameLayout scoreLayout = scoreFrame.getScoreFrameLayout();
		if (scoreLayout != null) {
			
			//the coordinates of the layout elements are relative to the upper left
			//corner, so we have to translate them
			canvas.transformSave();
			canvas.transformTranslate(rect.x1(), rect.y1());

			//get musical stampings, and in interactive mode, also
			//stampings like for playback and selection
			Iterable<Stamping> stampings = (canvas.getDecoration() == CanvasDecoration.Interactive ? scoreLayout
				.getAllStampings() : scoreLayout.getMusicalStampings());
			//render them
			for (Stamping s : stampings) {
				StampingRenderer.drawAny(s, canvas, args);
			}

			//restore old transformation
			canvas.transformRestore();
		}

	}

}
