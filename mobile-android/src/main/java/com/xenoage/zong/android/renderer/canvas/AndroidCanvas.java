package com.xenoage.zong.android.renderer.canvas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.xenoage.utils.android.Conversion;
import com.xenoage.utils.android.color.AndroidColorUtils;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.android.renderer.path.AndroidPath;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextSymbol;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.slur.SimpleSlurShape;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.path.Path;

import static com.xenoage.zong.android.renderer.symbols.AndroidSymbolsRenderer.androidSymbolsRenderer;

/**
 * This class contains methods for painting
 * on an Android device.
 *
 * @author Andreas Wenger
 */
public class AndroidCanvas
	extends com.xenoage.zong.renderer.canvas.Canvas {

	/** The Android graphics context. */
	private Canvas canvas;


	/**
	 * Creates an {@link AndroidCanvas} with the given size in mm for the given context,
	 * format, decoration mode and integrity.
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
	 * the given {@link com.xenoage.zong.renderer.canvas.Canvas}.
	 * If it is not a {@link AndroidCanvas}, a {@link ClassCastException} is thrown.
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
					androidSymbolsRenderer.draw((PathSymbol) fts.getSymbol(), canvas, Color.black,
							new Point2f(offsetX + fts.getOffsetX(), offsetY + fts.getSymbol().baselineOffset * scaling),
							new Point2f(scaling, scaling));
				}
				offsetX += e.getMetrics().getWidth();
			}

			offsetY += (pMetrics.getDescent() + pMetrics.getLeading());
		}

		canvas.restoreToCount(oldTransform);
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

	@Override public void fillPath(Path path, Color color) {
		Paint paint = AndroidColorUtils.createPaintFill(color);
		android.graphics.Path p = AndroidPath.createPath(path);
		canvas.drawPath(p, paint);
	}

	@Override public void fillRect(Rectangle2f rect, Color color) {
		Paint paint = AndroidColorUtils.createPaintFill(color);
		RectF r = Conversion.rectF(rect);
		canvas.drawRect(r, paint);
	}

	@Override public void drawImage(Rectangle2f rect, String imagePath) {
		//TODO: not supported yet
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
