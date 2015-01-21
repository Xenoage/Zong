package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.WedgeStamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapLine;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for the {@link WedgeStamping} class.
 *
 * @author Andreas Wenger
 */
public class WedgeStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link WedgeStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		WedgeStamping wedge = (WedgeStamping) stamping;

		StaffStamping parentStaff = wedge.parentStaff;
		float scaling = args.scaling;

		//horizontal position
		float x1Mm = wedge.x1Mm + parentStaff.position.x;
		float x2Mm = wedge.x2Mm + parentStaff.position.x;

		//compute vertical distances at the start and end point
		float d1Mm = wedge.d1Is * parentStaff.is;
		float d2Mm = wedge.d2Is * parentStaff.is;

		//width and color of the line
		Color color = Color.black;
		float width = parentStaff.getLineWidth(); //like staff line
		float paintWidth;

		//compute the horizontal line and color
		float yMm;
		Color paintColor;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			BitmapStaff ss = parentStaff.screenInfo.getBitmapStaff(scaling);
			yMm = parentStaff.position.y + ss.getLPMm(wedge.lp);
			BitmapLine screenLine = parentStaff.screenInfo.getBitmapLine(scaling, width, color);
			paintColor = screenLine.color;
			paintWidth = screenLine.widthMm;
		}
		else {
			yMm = parentStaff.computeYMm(wedge.lp);
			paintColor = color;
			paintWidth = width;
		}

		//draw lines
		canvas.drawLine(new Point2f(x1Mm, yMm - d1Mm / 2), new Point2f(x2Mm, yMm - d2Mm / 2),
			paintColor, paintWidth);
		canvas.drawLine(new Point2f(x1Mm, yMm + d1Mm / 2), new Point2f(x2Mm, yMm + d2Mm / 2),
			paintColor, paintWidth);

	}

}
