package com.xenoage.zong.renderer.stamping;

import static com.xenoage.utils.color.Color.color;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapLine;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for a staff stamping.
 *
 * @author Andreas Wenger
 */
public class StaffStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link StaffStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		StaffStamping staff = (StaffStamping) stamping;

		float scaling = args.targetScaling;
		Point2f position = staff.positionMm;

		//TODO: custom line width
		float lineWidthMm = staff.getLineWidthMm();

		float length = staff.lengthMm;

		Color color = Color.black;

		if (canvas.getFormat() == CanvasFormat.Raster) {
			//render on screen
			BitmapLine screenLine = staff.getBitmapInfo().getBitmapLine(scaling, lineWidthMm, color);
			BitmapStaff ss = staff.getBitmapInfo().getBitmapStaff(scaling);
			position = new Point2f(position.x, position.y + ss.yOffsetMm);
			if (ss.isSimplifiedStaff) {
				//simplified staff (fill rectangle)
				color = screenLine.color;
				color = color(color.r, color.g, color.b, (int) (0.7f * color.a));

				//don't forget the line heights, they belong into the rectangle
				position = position.add(0, -1 * ss.lineHeightMm / 2);
				canvas.drawSimplifiedStaff(position, length, ss.heightMm + ss.lineHeightMm, color);
			}
			else {
				//normal staff (draw lines)
				canvas.drawStaff(position, length, staff.linesCount, screenLine.color, screenLine.widthMm,
					ss.interlineSpaceMm);
			}
		}
		else //if (canvas.getFormat() == CanvasFormat.Vector)
		{
			//render with high quality
			canvas.drawStaff(position, length, staff.linesCount, color, lineWidthMm, staff.is);
		}

	}

}
