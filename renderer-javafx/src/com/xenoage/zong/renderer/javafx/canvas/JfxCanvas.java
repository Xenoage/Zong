package com.xenoage.zong.renderer.javafx.canvas;

import static com.xenoage.utils.jse.javafx.color.JfxColorUtils.toJavaFXColor;
import static com.xenoage.utils.kernel.Range.range;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.javafx.path.JfxPath;
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
		
		if (decoration == CanvasDecoration.Interactive) {
			//interactive mode: show text selection
		}/* GOON

		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayouts textLayouts = TextLayoutTools.create(text, frameWidth, yIsBaseline, frc);

		AffineTransform oldTransform = g2d.getTransform();
		g2d.translate(position.x, position.y);
		textLayouts.draw(g2d);
		g2d.setTransform(oldTransform); */
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
