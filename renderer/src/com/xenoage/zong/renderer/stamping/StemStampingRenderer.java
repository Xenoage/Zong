package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapLine;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for a stem stamping.
 * 
 * A stem has a notehead position and an end position
 * and is slightly thinner than a staff line.
 *
 * @author Andreas Wenger
 */
public class StemStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link StemStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		StemStamping stem = (StemStamping) stamping;

		float scaling = args.targetScaling;
		StaffStamping parentStaff = stem.parentStaff;
		float positionX = stem.xMm;
		float noteheadLinePosition = stem.noteheadLp;
		float endLinePosition = stem.endLp;

		float lineWidthMm = parentStaff.getLineWidth(); //TODO: stem is thinner
		Point2f p1Mm = new Point2f(positionX - lineWidthMm / 2, parentStaff.position.y);
		Point2f p2Mm = new Point2f(positionX + lineWidthMm / 2, p1Mm.y);
		Color color = Color.black;

		//shorten stem a little bit at the notehead - TODO: looks good. is code ok?
		if (endLinePosition > noteheadLinePosition)
			noteheadLinePosition += 0.2f;
		else
			noteheadLinePosition -= 0.2f;

		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen or print
			BitmapLine screenLine = new BitmapLine(lineWidthMm, color, scaling);
			BitmapStaff screenStaff = parentStaff.screenInfo.getBitmapStaff(scaling);
			p1Mm = new Point2f(p1Mm.x, p1Mm.y + screenStaff.getLPMm(noteheadLinePosition));
			p2Mm = new Point2f(p2Mm.x, p2Mm.y + screenStaff.getLPMm(endLinePosition));
			float width = screenLine.widthMm; //ensure same width for each stem in this staff
			canvas.fillRect(new Rectangle2f(p1Mm.x, p1Mm.y, width, p2Mm.y - p1Mm.y),
			/* TEST Color.green */screenLine.color);
		}
		else if (canvas.getFormat() == CanvasFormat.Vector) {
			//render with high quality
			p1Mm = new Point2f(p1Mm.x, parentStaff.computeYMm(noteheadLinePosition));
			p2Mm = new Point2f(p2Mm.x, parentStaff.computeYMm(endLinePosition));
			canvas.fillRect(new Rectangle2f(p1Mm.x, p1Mm.y, p2Mm.x - p1Mm.x, p2Mm.y - p1Mm.y), color);
		}

	}

}
