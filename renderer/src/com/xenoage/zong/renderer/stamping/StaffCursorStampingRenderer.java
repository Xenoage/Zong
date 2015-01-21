package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.stampings.StaffCursorStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for a cursor stamping
 * that belongs to one staff.
 *
 * @author Andreas Wenger
 */
public class StaffCursorStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link StaffCursorStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		StaffCursorStamping cursor = (StaffCursorStamping) stamping;

		float viewScaling = args.targetScaling;
		StaffStamping parentStaff = cursor.parentStaff;

		float x, y1, y2;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			float staffY = parentStaff.position.y;
			BitmapStaff ss = parentStaff.screenInfo.getBitmapStaff(viewScaling);
			//top: one interline space above staff
			y1 = staffY + ss.getLPMm(parentStaff.linesCount * 2);
			//bottom: one interline space under staff
			y2 = staffY + ss.getLPMm(-2);
		}
		else {
			y1 = parentStaff.computeYMm(parentStaff.linesCount * 2);
			y2 = parentStaff.computeYMm(-2);
		}
		x = parentStaff.position.x + cursor.xMm + cursor.offsetIs * parentStaff.is;

		canvas.drawLine(new Point2f(x, y1), new Point2f(x, y2), new Color(30, 200, 30),
			getCursorWidth(viewScaling));
	}

	public float getCursorWidth(float viewScaling) {
		//cursor width: at least 2 px
		return Units.pxToMm(Math.max(2, Units.mmToPx(0.5f, viewScaling)), viewScaling);
	}

}
