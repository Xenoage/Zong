package com.xenoage.zong.webapp.renderer.canvas;

import static com.xenoage.utils.math.Units.pxToMm;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.gwt.color.GwtColorUtils;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.text.FormattedText;
import com.xenoage.zong.text.FormattedTextElement;
import com.xenoage.zong.text.FormattedTextParagraph;
import com.xenoage.zong.text.FormattedTextString;
import com.xenoage.zong.text.FormattedTextSymbol;

/**
 * This class contains methods for painting
 * on a HTML5 canvas using GWT.
 *
 * @author Andreas Wenger
 */
public class GwtCanvas
	extends com.xenoage.zong.renderer.canvas.Canvas {

	//the HTML5 canvas and graphics context
	private Canvas canvas;
	private Context2d context;


	/**
	 * Creates an {@link GwtCanvas} with the given size in mm for the given context,
	 * format, decoration mode and itegrity.
	 */
	public GwtCanvas(Canvas canvas, CanvasFormat format,
		CanvasDecoration decoration, CanvasIntegrity integrity) {
		super(new Size2f(10, 10), format, decoration, integrity);
		this.canvas = canvas;
		this.context = canvas.getContext2d();
	}

	/**
	 * Gets the HTML5 canvas.
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
		return ((GwtCanvas) canvas).getGraphicsContext();
	}

	/**
	 * {@inheritDoc}
	 * The text selection is ignored.
	 */
	@Override public void drawText(FormattedText text, TextSelection selection, Point2f position,
		boolean yIsBaseline, float frameWidth) {
		context.save();
		context.translate(position.x, position.y);

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
					/* GOON Paint paint = new Paint(AndroidColorUtils.black);
					paint.setTypeface(Typeface.SERIF);
					paint.setTextSize(Units.pxToMm(t.getStyle().getFont().getSize(), 1));
					paint.setAntiAlias(true);
					canvas.drawText(t.getText(), offsetX, offsetY, paint); */
					context.fillText(t.getText(), offsetX, offsetY);
				}
				else {
					//symbol
					FormattedTextSymbol fts = (FormattedTextSymbol) e;
					/* GOON float scaling = fts.getScaling();
					AndroidSymbolsRenderer.instance.draw(fts.getSymbol(), this, Color.black, new Point2f(
						offsetX + fts.getOffsetX(), offsetY + fts.getSymbol().baselineOffset * scaling),
						new Point2f(scaling, scaling)); */
				}
				offsetX += e.getMetrics().getWidth();
			}

			offsetY += (pMetrics.getDescent() + pMetrics.getLeading());
		}

		context.restore();
	}

	@Override public void drawSymbol(Symbol symbol, Color color, Point2f position, Point2f scaling) {
		/* GOON AndroidSymbolsRenderer.instance.draw(symbol, this, color, position, scaling); */
	}

	@Override public void drawLine(Point2f p1, Point2f p2, Color color, float lineWidth) {
		//set style
		context.setStrokeStyle(GwtColorUtils.createColor(color));
		context.setLineWidth(lineWidth);
		context.setLineCap(LineCap.BUTT);
		context.setLineJoin(LineJoin.BEVEL);
		//draw line
		context.beginPath();
		context.moveTo(p1.x, p1.y);
		context.lineTo(p2.x, p2.y);
		context.closePath();
		context.stroke();
	}

	@Override public void drawStaff(Point2f pos, float length, int lines, Color color,
		float lineWidth, float interlineSpace) {
		context.setFillStyle(GwtColorUtils.createColor(color));
		for (int i = 0; i < lines; i++) {
			float x = pos.x;
			float y = pos.y + i * interlineSpace - lineWidth / 2;
			context.fillRect(x, y, length, lineWidth);
		}
	}

	@Override public void drawSimplifiedStaff(Point2f pos, float length, float height, Color color) {
		context.setFillStyle(GwtColorUtils.createColor(color));
		context.fillRect(pos.x, pos.y, length, height);
	}

	public void fillEllipse(Point2f pCenter, float width, float height, Color color) {
		//GOON
	}

	@Override public void drawBeam(Point2f[] points, Color color, float interlineSpace) {
		/* GOON RectF beamSymbol = new RectF(-1f, -0.25f, 1f, 0.25f);

		Paint paint = AndroidColorUtils.createPaintFill(color);

		int oldTransform = canvas.save();

		float imageWidth = points[2].x - points[0].x;
		float imageHeight = points[3].y - points[0].y;
		float beamGrowthHeight = points[2].y - points[0].y;

		canvas.translate(points[0].x + imageWidth / 2, points[0].y + imageHeight / 2);
		canvas.skew(0, beamGrowthHeight / imageWidth);
		canvas.scale(imageWidth / beamSymbol.width(), (points[1].y - points[0].y) / beamSymbol.height());
		canvas.drawRect(beamSymbol, paint);

		canvas.restoreToCount(oldTransform); */
	}

	@Override public void drawCurvedLine(Point2f p1, Point2f p2, Point2f c1, Point2f c2,
		float interlineSpace, Color color) {
		/* Paint paint = AndroidColorUtils.createPaintFill(color);
		SimpleSlurShape slurShape = new SimpleSlurShape(p1, p2, c1, c2, interlineSpace);
		canvas.drawPath(AndroidSlurRenderer.getPath(slurShape), paint); */ //GOON
	}

	@Override public void fillRect(Rectangle2f rect, Color color) {
		context.setFillStyle(GwtColorUtils.createColor(color));
		context.fillRect(rect.position.x, rect.position.y, rect.size.width, rect.size.height);
	}

	@Override public void transformSave() {
		context.save();
	}

	@Override public void transformRestore() {
		context.restore();
	}

	@Override public void transformTranslate(float x, float y) {
		context.translate(x, y);
	}

	@Override public void transformScale(float x, float y) {
		context.scale(x, y);
	}

	@Override public void transformRotate(float angle) {
		context.rotate(angle);
	}

}
