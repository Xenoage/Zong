package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.TupletStamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapLine;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;

/**
 * Renderer for the {@link TupletStamping} class.
 *
 * @author Andreas Wenger
 */
public class TupletStampingRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link TupletStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		TupletStamping tuplet = (TupletStamping) stamping;

		StaffStamping parentStaff = tuplet.parentStaff;
		float scaling = args.targetScaling;

		//horizontal position
		float x1Mm = tuplet.x1mm;
		float x2Mm = tuplet.x2mm;

		//height of hook is 1 IS
		float hookHeightPx = Units.mmToPx(parentStaff.is, scaling);

		//width and color of the line
		Color color = Color.black;
		float width = parentStaff.getLineWidth() * 1.5f; //a little bit thicker than staff line
		float paintWidth;

		//compute the horizontal line and color
		float y1Mm, y2Mm;
		Color paintColor;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			BitmapStaff ss = parentStaff.screenInfo.getBitmapStaff(scaling);
			y1Mm = parentStaff.position.y + ss.getLPMm(tuplet.y1lp);
			y2Mm = parentStaff.position.y + ss.getLPMm(tuplet.y2lp);
			BitmapLine screenLine = parentStaff.screenInfo.getBitmapLine(scaling, width, color);
			paintColor = screenLine.color;
			paintWidth = screenLine.widthMm;
		}
		else {
			y1Mm = parentStaff.computeYMm(tuplet.y1lp);
			y2Mm = parentStaff.computeYMm(tuplet.y2lp);
			paintColor = color;
			paintWidth = width;
		}

		//compute gap for text
		FormattedText text = tuplet.text;
		float gapMm = 0;
		float textMm = 0;
		if (text != null && text.getParagraphs().size() > 0) {
			textMm = text.getFirstParagraph().getMetrics().getWidth();
			gapMm = textMm * 2;
		}

		//draw line and hooks
		if (gapMm > 0) {
			//two lines, when there is text in between
			float xGapLMm = (x2Mm + x1Mm) / 2 - gapMm / 2;
			float xGapRMm = xGapLMm + gapMm;
			float gapVerticalMm = gapMm / (x2Mm - x1Mm) * (y2Mm - y1Mm);
			float yGapLMm = (y2Mm + y1Mm) / 2 - gapVerticalMm / 2;
			float yGapRMm = yGapLMm + gapVerticalMm;
			canvas.drawLine(new Point2f(x1Mm, y1Mm), new Point2f(xGapLMm, yGapLMm), paintColor,
				paintWidth);
			canvas.drawLine(new Point2f(xGapRMm, yGapRMm), new Point2f(x2Mm, y2Mm), paintColor,
				paintWidth);
		}
		else {
			//no gap
			canvas.drawLine(new Point2f(x1Mm, y1Mm), new Point2f(x2Mm, y2Mm), paintColor, paintWidth);
		}
		//hooks
		canvas.drawLine(new Point2f(x1Mm, y1Mm), new Point2f(x1Mm, y1Mm + hookHeightPx *
			(tuplet.y1lp < 0 ? -1 : 1)), paintColor, paintWidth);
		canvas.drawLine(new Point2f(x2Mm, y2Mm), new Point2f(x2Mm, y2Mm + hookHeightPx *
			(tuplet.y2lp < 0 ? -1 : 1)), paintColor, paintWidth);

		//draw text
		if (text != null && text.getParagraphs().size() > 0) {
			float textAscent = text.getFirstParagraph().getMetrics().getAscent();
			float textX = (x1Mm + x2Mm) / 2 - textMm / 2;
			float textY = (y1Mm + y2Mm) / 2 + textAscent / 2;
			canvas.drawText(tuplet.text, null, new Point2f(textX, textY), true, 0);
		}

	}

}
