package com.xenoage.zong.android.renderer.canvas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.xenoage.utils.android.color.AndroidColorUtils;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.android.renderer.slur.AndroidSlurRenderer;
import com.xenoage.zong.android.renderer.symbols.AndroidSymbolsRenderer;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.slur.SimpleSlurShape;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.text.FormattedText;
import com.xenoage.zong.text.FormattedTextElement;
import com.xenoage.zong.text.FormattedTextParagraph;
import com.xenoage.zong.text.FormattedTextString;
import com.xenoage.zong.text.FormattedTextSymbol;

/**
 * This class contains methods for painting
 * on an Android device.
 *
 * @author Andreas Wenger
 */
public class AndroidCanvas
	extends com.xenoage.zong.renderer.canvas.Canvas {

	//the Android graphics context
	private Canvas canvas;


	/**
	 * Creates an {@link AndroidCanvas} with the given size in mm for the given context,
	 * format, decoration mode and itegrity.
	 */
	public AndroidCanvas(Canvas canvas, Size2f sizeMm, CanvasFormat format,
		CanvasDecoration decoration, CanvasIntegrity integrity) {
		super(sizeMm, format, decoration, integrity);
		this.canvas = canvas;
	}

	/**
	 * Gets the Android graphics context.
	 */
	@Override public Canvas getGraphicsContext() {
		return canvas;
	}

	/**
	 * Convenience method: Gets the {@link Canvas} graphics context from
	 * the given {@link com.xenoage.zong.renderer.canvas.Canvas}. If it is not a {@link Canvas},
	 * a {@link ClassCastException} is thrown.
	 */
	public static Canvas getCanvas(com.xenoage.zong.renderer.canvas.Canvas canvas) {
		return ((AndroidCanvas) canvas).getGraphicsContext();
	}

	/**
	 * {@inheritDoc}
	 * The text selection is ignored.
	 */
	@Override public void drawText(FormattedText text, TextSelection selection, Point2f position,
		boolean yIsBaseline, float frameWidth) {
		int oldTransform = canvas.save();
		canvas.translate(position.x, position.y);

		//print the text frame paragraph for paragraph
		float offsetX = 0;
		float offsetY = 0;

		for (FormattedTextParagraph p : text.getParagraphs()) {
			TextMetrics pMetrics = p.getMetrics();
			if (!yIsBaseline)
				offsetY += pMetrics.getAscent();

			//adjustment
			if (p.getAlignment() == Alignment.Center)
				offsetX = (frameWidth - pMetrics.getWidth()) / 2;
			else if (p.getAlignment() == Alignment.Right)
				offsetX = frameWidth - pMetrics.getWidth();
			else
				offsetX = 0;

			//draw elements
			for (FormattedTextElement e : p.getElements()) {
				if (e instanceof FormattedTextString) {
					//TODO - formatting
					FormattedTextString t = (FormattedTextString) e;
					Paint paint = new Paint(AndroidColorUtils.black);
					paint.setTypeface(Typeface.SERIF);
					paint.setTextSize(Units.pxToMm(t.getStyle().getFont().getSize(), 1));
					paint.setAntiAlias(true);
					canvas.drawText(t.getText(), offsetX, offsetY, paint);
				}
				else {
					//symbol
					FormattedTextSymbol fts = (FormattedTextSymbol) e;
					float scaling = fts.getScaling();
					AndroidSymbolsRenderer.instance.draw(fts.getSymbol(), this, Color.black, new Point2f(
						offsetX + fts.getOffsetX(), offsetY + fts.getSymbol().baselineOffset * scaling),
						new Point2f(scaling, scaling));
				}
				offsetX += e.getMetrics().getWidth();
			}

			offsetY += (pMetrics.getDescent() + pMetrics.getLeading());
		}

		canvas.restoreToCount(oldTransform);
	}

	@Override public void drawSymbol(Symbol symbol, Color color, Point2f position, Point2f scaling) {
		AndroidSymbolsRenderer.instance.draw(symbol, this, color, position, scaling);
	}

	@Override public void drawLine(Point2f p1, Point2f p2, Color color, float lineWidth) {
		Paint paint = new Paint();
		paint.setColor(AndroidColorUtils.createColor(color));
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(lineWidth);
		paint.setStrokeCap(Cap.BUTT);
		paint.setStrokeJoin(Join.BEVEL);
		canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
	}

	@Override public void drawStaff(Point2f pos, float length, int lines, Color color,
		float lineWidth, float interlineSpace) {
		Paint paint = AndroidColorUtils.createPaintFill(color);
		for (int i = 0; i < lines; i++) {
			float x = pos.x;
			float y = pos.y + i * interlineSpace - lineWidth / 2;
			canvas.drawRect(new RectF(x, y, x + length, y + lineWidth), paint);
		}
	}

	@Override public void drawSimplifiedStaff(Point2f pos, float length, float height, Color color) {
		Paint paint = AndroidColorUtils.createPaintFill(color);
		canvas.drawRect(new RectF(pos.x, pos.y, pos.x + length, pos.y + height), paint);
	}

	public void fillEllipse(Point2f pCenter, float width, float height, Color color) {
		Paint paint = AndroidColorUtils.createPaintFill(color);
		canvas.drawOval(new RectF(pCenter.x - width / 2, pCenter.y - height / 2, pCenter.x + width / 2,
			pCenter.y + height / 2), paint);
	}

	@Override public void drawBeam(Point2f[] points, Color color, float interlineSpace) {
		RectF beamSymbol = new RectF(-1f, -0.25f, 1f, 0.25f);

		Paint paint = AndroidColorUtils.createPaintFill(color);

		int oldTransform = canvas.save();

		float imageWidth = points[2].x - points[0].x;
		float imageHeight = points[3].y - points[0].y;
		float beamGrowthHeight = points[2].y - points[0].y;

		canvas.translate(points[0].x + imageWidth / 2, points[0].y + imageHeight / 2);
		canvas.skew(0, beamGrowthHeight / imageWidth);
		canvas.scale(imageWidth / beamSymbol.width(), (points[1].y - points[0].y) / beamSymbol.height());
		canvas.drawRect(beamSymbol, paint);

		canvas.restoreToCount(oldTransform);
	}

	@Override public void drawCurvedLine(Point2f p1, Point2f p2, Point2f c1, Point2f c2,
		float interlineSpace, Color color) {
		Paint paint = AndroidColorUtils.createPaintFill(color);
		SimpleSlurShape slurShape = new SimpleSlurShape(p1, p2, c1, c2, interlineSpace);
		canvas.drawPath(AndroidSlurRenderer.getPath(slurShape), paint);
	}

	@Override public void fillRect(Rectangle2f rect, Color color) {
		Paint paint = AndroidColorUtils.createPaintFill(color);
		canvas.drawRect(new RectF(rect.x1(), rect.y1(), rect.x2(), rect.y2()), paint);
	}

	@Override public void transformSave() {
		canvas.save();
	}

	@Override public void transformRestore() {
		canvas.restore();
	}

	@Override public void transformTranslate(float x, float y) {
		canvas.translate(x, y);
	}

	@Override public void transformScale(float x, float y) {
		canvas.scale(x, y);
	}

	@Override public void transformRotate(float angle) {
		canvas.rotate(angle);
	}

}
