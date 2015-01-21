package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.SystemCursorStamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for a {@link SystemCursorStamping}.
 *
 * @author Andreas Wenger
 */
public class SystemCursorStampingRenderer
	extends StaffCursorStampingRenderer {

	/**
	 * Draws the given {@link SystemCursorStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		SystemCursorStamping cursor = (SystemCursorStamping) stamping;

		float viewScaling = args.targetScaling;
		StaffStamping topStaff = cursor.topStaff;
		StaffStamping bottomStaff = cursor.bottomStaff;

		float x, y1, y2;
		x = topStaff.position.x + cursor.x;

		if (canvas.getFormat() == CanvasFormat.Raster) {
			float staffY = topStaff.position.y;
			BitmapStaff ss = topStaff.screenInfo.getBitmapStaff(viewScaling);
			//top staff: top line
			y1 = staffY + ss.getLPMm(topStaff.linesCount * 2);
			//bottom staff: bottom line
			staffY = bottomStaff.position.y;
			ss = bottomStaff.screenInfo.getBitmapStaff(viewScaling);
			y2 = staffY + ss.getLPMm(-2);
		}
		else {
			y1 = topStaff.computeYMm(topStaff.linesCount * 2);
			y2 = bottomStaff.computeYMm(-2);
		}

		canvas.drawLine(new Point2f(x, y1), new Point2f(x, y2), new Color(50, 50, 230),
			getCursorWidth(viewScaling));

	}

}
