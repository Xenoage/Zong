package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.VoltaStamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapLine;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for the {@link VoltaStamping} class.
 *
 * @author Andreas Wenger
 */
public class VoltaStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link VoltaStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		VoltaStamping volta = (VoltaStamping) stamping;

		StaffStamping parentStaff = volta.parentStaff;
		float scaling = args.scaling;

		//horizontal position
		float x1 = volta.x1 + parentStaff.position.x;
		float x2 = volta.x2 + parentStaff.position.x;

		//compute hooks
		boolean hook = volta.leftHook || volta.rightHook;
		float hookHeight = 0;
		if (hook) {
			//height of hook is 2 interline spaces
			hookHeight = parentStaff.is * 2;
		}

		//width and color of the line
		Color color = Color.black;
		float width = parentStaff.getLineWidth() * 1.5f; //a little bit thicker than staff line
		float paintWidth;

		//compute the horizontal line and color
		float y;
		Color paintColor;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			BitmapStaff ss = parentStaff.screenInfo.getBitmapStaff(scaling);
			y = parentStaff.position.y + ss.getLPMm(volta.lp);
			BitmapLine screenLine = parentStaff.screenInfo.getBitmapLine(scaling, width, color);
			paintColor = screenLine.color;
			paintWidth = screenLine.widthMm;
		}
		else {
			y = parentStaff.computeYMm(volta.lp);
			paintColor = color;
			paintWidth = width;
		}

		//draw line and hooks
		canvas.drawLine(new Point2f(x1, y), new Point2f(x2, y), paintColor, paintWidth);
		if (volta.leftHook) {
			canvas.drawLine(new Point2f(x1, y), new Point2f(x1, y + hookHeight), paintColor, paintWidth);
		}
		if (volta.rightHook) {
			canvas.drawLine(new Point2f(x2, y), new Point2f(x2, y + hookHeight), paintColor, paintWidth);
		}

		//draw text
		FormattedText text = volta.text;
		if (text != null && text.getParagraphs().size() > 0) {
			float textAscent = text.getFirstParagraph().getMetrics().getAscent();
			float textX = x1 + parentStaff.is * 1;
			float textY = y + textAscent;
			canvas.drawText(volta.text, null, new Point2f(textX, textY), true, 0);
		}

	}

}
