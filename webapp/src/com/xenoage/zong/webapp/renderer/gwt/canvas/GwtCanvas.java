package com.xenoage.zong.webapp.renderer.gwt.canvas;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.gwt.color.GwtColorUtils;
import com.xenoage.utils.gwt.font.GwtFontUtils;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.text.*;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.symbol.SymbolsRenderer;
import com.xenoage.zong.symbols.path.Path;
import com.xenoage.zong.webapp.renderer.gwt.path.GwtPath;

/**
 * This class contains methods for painting
 * on a HTML5 canvas using GWT.
 *
 * @author Andreas Wenger
 */
public class GwtCanvas
	extends com.xenoage.zong.renderer.canvas.Canvas {

	//the HTML5 graphics context
	private Context2d context;


	/**
	 * Creates an {@link GwtCanvas} with the given size in mm for the given context,
	 * format, decoration mode and itegrity.
	 */
	public GwtCanvas(Context2d context, CanvasFormat format,
		CanvasDecoration decoration, CanvasIntegrity integrity) {
		super(new Size2f(10, 10), format, decoration, integrity);
		this.context = context;
	}

	/**
	 * Gets the HTML5 canvas.
	 */
	@Override public Context2d getGraphicsContext() {
		return context;
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
					FormattedTextString t = (FormattedTextString) e;
					context.setFillStyle(GwtColorUtils.createColor(t.getStyle().getColor()));
					context.save();
					context.scale(Units.pxToMm_1_1, Units.pxToMm_1_1);
					context.setFont(GwtFontUtils.getCssFont(t.getStyle().getFont()));
					context.fillText(t.getText(), offsetX / Units.pxToMm_1_1, offsetY / Units.pxToMm_1_1);
					//Debug: Paint dot at text offset
					//context.fillRect(offsetX / Units.pxToMm_1_1, offsetY / Units.pxToMm_1_1, 2, 2);
					context.restore();
				}
				else {
					//symbol
					FormattedTextSymbol fts = (FormattedTextSymbol) e;
					float scaling = fts.getScaling();
					SymbolsRenderer.draw(fts.getSymbol(), this, Color.black, new Point2f(
						offsetX + fts.getOffsetX(), offsetY + fts.getSymbol().baselineOffset * scaling),
						new Point2f(scaling, scaling));
				}
				offsetX += e.getMetrics().getWidth();
			}

			//next line
			offsetY += p.getMetrics().getAscent() +
				p.getMetrics().getDescent() + p.getMetrics().getLeading();
		}

		context.restore();
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
	
	@Override public void fillPath(Path path, Color color) {
		context.setFillStyle(GwtColorUtils.createColor(color));
		GwtPath.drawPath(path, context);
		context.fill();
	}

	@Override public void fillRect(Rectangle2f rect, Color color) {
		context.setFillStyle(GwtColorUtils.createColor(color));
		context.fillRect(rect.position.x, rect.position.y, rect.size.width, rect.size.height);
	}
	
	@Override public void drawImage(Rectangle2f rect, String imagePath) {
		//TODO
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
