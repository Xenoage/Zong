package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.TextStamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Renderer for a {@link TextStamping}.
 *
 * @author Andreas Wenger
 */
public class TextStampingRenderer
	extends StampingRenderer {

	public static final boolean yIsBaseline = true;
	public static final float frameWidth = 0;


	/**
	 * Draws the given {@link TextStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		TextStamping s = (TextStamping) stamping;
		drawWith(s.getText(), null, s.getPositionInFrame(), canvas);
	}

	/**
	 * Paints the given formatted text using the given information and rendering parameters.
	 */
	public static void drawWith(FormattedText text, TextSelection selection, Point2f position,
		Canvas canvas) {
		canvas.drawText(text, selection, position, yIsBaseline, frameWidth);
	}

}
