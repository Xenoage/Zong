package com.xenoage.zong.renderer.awt.canvas;

import static com.xenoage.utils.jse.color.AwtColorUtils.toAwtColor;
import static com.xenoage.utils.kernel.Range.range;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.awt.image.AwtImageRenderer;
import com.xenoage.zong.renderer.awt.path.AwtPath;
import com.xenoage.zong.renderer.awt.text.TextLayoutTools;
import com.xenoage.zong.renderer.awt.text.TextLayouts;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.symbols.path.Path;

/**
 * AWT implementation of a {@link Canvas}
 *
 * @author Andreas Wenger
 */
public class AwtCanvas
	extends Canvas {

	/** The AWT graphics context. */
	private final Graphics2D g2d;
	
	//stack for stored transformation states
	private Stack<AffineTransform> transformStack = new Stack<AffineTransform>();


	/**
	 * Creates an {@link AwtCanvas} with the given size in mm for the given context,
	 * format, decoration mode and itegrity.
	 */
	public AwtCanvas(Graphics2D g2d, Size2f sizeMm, CanvasFormat format, CanvasDecoration decoration,
		CanvasIntegrity integrity) {
		super(sizeMm, format, decoration, integrity);
		this.g2d = g2d;
	}

	/**
	 * Gets the {@link Graphics2D} graphics context.
	 */
	@Override public Graphics2D getGraphicsContext() {
		return g2d;
	}

	/**
	 * Convenience method: Gets the {@link Graphics2D} graphics context from
	 * the given {@link Canvas}. If it is not a {@link AwtCanvas}, a {@link ClassCastException}
	 * is thrown.
	 */
	public static Graphics2D getGraphics2D(Canvas canvas) {
		return ((AwtCanvas) canvas).getGraphicsContext();
	}

	@Override public void drawText(FormattedText text, TextSelection selection, Point2f position,
		boolean yIsBaseline, float frameWidth) {
		
		if (decoration == CanvasDecoration.Interactive) {
			//interactive mode: show text selection
		}

		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayouts textLayouts = TextLayoutTools.create(text, frameWidth, yIsBaseline, frc);

		AffineTransform oldTransform = g2d.getTransform();
		g2d.translate(position.x, position.y);
		textLayouts.draw(g2d);
		g2d.setTransform(oldTransform);
	}

	@Override public void drawLine(Point2f p1, Point2f p2, Color color, float lineWidth) {
		g2d.setColor(toAwtColor(color));
		g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		g2d.draw(new Line2D.Float(p1.x, p1.y, p2.x, p2.y));
	}

	@Override public void drawStaff(Point2f pos, float length, int lines, Color color,
		float lineWidth, float interlineSpace) {
		g2d.setColor(toAwtColor(color));
		for (int i : range(lines))
			g2d.fill(new Rectangle2D.Float(pos.x, pos.y + i * interlineSpace - lineWidth / 2, length, lineWidth));
	}

	@Override public void drawSimplifiedStaff(Point2f pos, float length, float height, Color color) {
		g2d.setColor(toAwtColor(color));
		g2d.fill(new Rectangle2D.Float(pos.x, pos.y, length, height));
	}

	@Override public void fillPath(Path path, Color color) {
		g2d.setColor(toAwtColor(color));
		Shape shape = AwtPath.createShape(path);
		g2d.fill(shape);
	}

	@Override public void fillRect(Rectangle2f rect, Color color) {
		g2d.setColor(toAwtColor(color));
		g2d.fill(new Rectangle2D.Float(rect.position.x, rect.position.y, rect.size.width,
			rect.size.height));
	}
	
	@Override public void drawImage(Rectangle2f rect, String imagePath) {
		boolean force = integrity == CanvasIntegrity.Perfect;
		AwtImageRenderer.drawImage(imagePath, g2d, rect, force);
	}

	@Override public void transformSave() {
		transformStack.push(g2d.getTransform());
	}

	@Override public void transformRestore() {
		if (false == transformStack.isEmpty()) {
			g2d.setTransform(transformStack.pop());
		}
	}

	@Override public void transformTranslate(float x, float y) {
		g2d.translate(x, y);
	}

	@Override public void transformScale(float x, float y) {
		g2d.scale(x, y);
	}

	@Override public void transformRotate(float angle) {
		g2d.rotate(angle * Math.PI / 180f);
	}

}
