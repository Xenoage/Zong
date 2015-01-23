package com.xenoage.zong.renderer.javafx.canvas;

import static com.xenoage.utils.jse.javafx.color.JfxColorUtils.toJavaFXColor;
import static com.xenoage.utils.jse.javafx.font.JfxFontUtils.toJavaFXFont;
import static com.xenoage.utils.kernel.Range.range;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextElement;
import com.xenoage.zong.core.text.FormattedTextParagraph;
import com.xenoage.zong.core.text.FormattedTextString;
import com.xenoage.zong.core.text.FormattedTextSymbol;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.javafx.path.JfxPath;
import com.xenoage.zong.renderer.symbol.SymbolsRenderer;
import com.xenoage.zong.symbols.path.Path;

/**
 * JavaFX implementation of a {@link Canvas}
 *
 * @author Andreas Wenger
 */
public class JfxCanvas
	extends Canvas {

	private final GraphicsContext context;
	

	/**
	 * Creates an {@link JfxCanvas} with the given size in mm for the given context,
	 * format, decoration mode and itegrity.
	 */
	public JfxCanvas(GraphicsContext context, Size2f sizeMm, CanvasFormat format, CanvasDecoration decoration,
		CanvasIntegrity integrity) {
		super(sizeMm, format, decoration, integrity);
		this.context = context;
	}

	/**
	 * Gets the JavaFX graphics context.
	 */
	@Override public GraphicsContext getGraphicsContext() {
		return context;
	}

	/**
	 * Convenience method: Gets the JavaFX graphics context from
	 * the given {@link Canvas}. If it is not a {@link JfxCanvas}, a {@link ClassCastException}
	 * is thrown.
	 */
	public static GraphicsContext getGraphicsContext(Canvas canvas) {
		return ((JfxCanvas) canvas).getGraphicsContext();
	}

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
					//TODO formatting
					FormattedTextString t = (FormattedTextString) e;
					context.setFill(toJavaFXColor(t.getStyle().getColor()));
					Font font = toJavaFXFont(t.getStyle().getFont());
					context.setFont(font);
					context.save();
					context.scale(Units.pxToMm_1_1, Units.pxToMm_1_1);
					context.fillText(t.getText(), offsetX / Units.pxToMm_1_1, offsetY / Units.pxToMm_1_1);
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

			offsetY += (pMetrics.getDescent() + pMetrics.getLeading());
		}

		context.restore();
	}

	@Override public void drawLine(Point2f p1, Point2f p2, Color color, float lineWidth) {
		context.setStroke(toJavaFXColor(color));
		context.setLineWidth(lineWidth);
		context.setLineCap(StrokeLineCap.BUTT);
		context.strokeLine(p1.x, p1.y, p2.x, p2.y);
	}

	@Override public void drawStaff(Point2f pos, float length, int lines, Color color,
		float lineWidth, float interlineSpace) {
		context.setFill(toJavaFXColor(color));
		for (int i : range(lines))
			context.fillRect(pos.x, pos.y + i * interlineSpace - lineWidth / 2, length, lineWidth);
	}

	@Override public void drawSimplifiedStaff(Point2f pos, float length, float height, Color color) {
		context.setFill(toJavaFXColor(color));
		context.fillRect(pos.x, pos.y, length, height);
	}

	@Override public void fillPath(Path path, Color color) {
		context.setFill(toJavaFXColor(color));
		JfxPath.drawPath(path, context);
		context.fill();
	}

	@Override public void fillRect(Rectangle2f rect, Color color) {
		context.setFill(toJavaFXColor(color));
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
		context.rotate(angle * Math.PI / 180f);
	}

}
