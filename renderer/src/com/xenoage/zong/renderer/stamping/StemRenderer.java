package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapLine;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for a {@link StemStamping}.
 *
 * @author Andreas Wenger
 */
public class StemRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link StemStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		StemStamping stem = (StemStamping) stamping;

		float scaling = args.targetScaling;

		float lineWidthMm = stem.noteStaff.getLineWidthMm(); //TODO: stem is thinner
		Point2f p1Mm = new Point2f(stem.xMm - lineWidthMm / 2, stem.noteStaff.positionMm.y);
		Point2f p2Mm = new Point2f(stem.xMm + lineWidthMm / 2, stem.endStaff.positionMm.y);
		Color color = Color.black;

		//shorten stem a little bit at the notehead - TODO: looks good. is code ok?
		float noteLp = stem.noteLp + 0.2f * (stem.endLp > stem.noteLp ? 1 : -1);

		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen or print
			BitmapLine screenLine = new BitmapLine(lineWidthMm, color, scaling);
			BitmapStaff noteScreenStaff = stem.noteStaff.getBitmapInfo().getBitmapStaff(scaling);
			BitmapStaff endScreenStaff = stem.endStaff.getBitmapInfo().getBitmapStaff(scaling);
			p1Mm = new Point2f(p1Mm.x, p1Mm.y + noteScreenStaff.getYMm(noteLp));
			p2Mm = new Point2f(p2Mm.x, p2Mm.y + endScreenStaff.getYMm(stem.endLp));
			float width = screenLine.widthMm; //ensure same width for each stem in this staff
			canvas.fillRect(new Rectangle2f(p1Mm.x, p1Mm.y, width, p2Mm.y - p1Mm.y), screenLine.color);
		}
		else if (canvas.getFormat() == CanvasFormat.Vector) {
			//render with high quality
			p1Mm = new Point2f(p1Mm.x, stem.noteStaff.computeYMm(noteLp));
			p2Mm = new Point2f(p2Mm.x, stem.endStaff.computeYMm(stem.endLp));
			canvas.fillRect(new Rectangle2f(p1Mm.x, p1Mm.y, p2Mm.x - p1Mm.x, p2Mm.y - p1Mm.y), color);
		}

	}

}
