package com.xenoage.zong.renderer.stamping;

import static com.xenoage.utils.math.geom.Point2f.p;

import com.xenoage.utils.color.Color;
import com.xenoage.zong.musiclayout.stampings.LegerLineStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapLine;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for a leger line stamping.
 * 
 * Leger lines belong to a staff. They have
 * a horizontal position around which they
 * are centered. They are 2 spaces long.
 *
 * @author Andreas Wenger
 */
public class LegerLineStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link LegerLineStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		LegerLineStamping legerLine = (LegerLineStamping) stamping;

		StaffStamping parentStaff = legerLine.parentStaff;
		float linePosition = legerLine.lp;
		float x = legerLine.x;

		float scaling = args.targetScaling;
		int width = (int) (1.6f * parentStaff.is);
		float p1xMm = x - width / 2;
		float p2xMm = p1xMm + width;
		float lineWidthMm = parentStaff.getLineWidth();
		Color color = Color.black;

		float yMm = 0;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen or print
			BitmapLine screenLine = new BitmapLine(lineWidthMm, color, scaling);
			BitmapStaff screenStaff = parentStaff.screenInfo.getBitmapStaff(scaling);
			yMm = parentStaff.position.y + screenStaff.getLPMm(linePosition);
			lineWidthMm = screenLine.widthMm;
		}
		else if (canvas.getFormat() == CanvasFormat.Vector) {
			//render with high quality
			yMm = parentStaff.computeYMm(linePosition);
		}
		canvas.drawLine(p(p1xMm, yMm), p(p2xMm, yMm), color, lineWidthMm);
	}

}
