package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.annotations.Demo;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.TestStamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Renderer for a test stamping,
 * used for testing purposes.
 * 
 * Draws the border of a rectangle with
 * a given color.
 *
 * @author Andreas Wenger
 */
@Demo
public class TestStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link TestStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		TestStamping s = (TestStamping) stamping;
		drawWith(s.position, s.size, s.color, canvas, args);
	}

	public static void drawWith(Point2f position, Size2f size, Color color, Canvas canvas,
		RendererArgs args) {
		Point2f pNW = position;
		Point2f pSE = new Point2f(position.x + size.width, position.y + size.height);
		Point2f pNE = new Point2f(pSE.x, pNW.y);
		Point2f pSW = new Point2f(pNW.x, pSE.y);

		canvas.drawLine(pNW, pNE, color, 1);
		canvas.drawLine(pNE, pSE, color, 1);
		canvas.drawLine(pSE, pSW, color, 1);
		canvas.drawLine(pSW, pNW, color, 1);
	}

}
