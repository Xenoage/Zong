package com.xenoage.zong.renderer.awt.canvas;

import static com.xenoage.utils.jse.color.AwtColorUtils.toAwtColor;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.io.selection.text.TextSelection;
import com.xenoage.zong.renderer.awt.slur.AwtSlurRenderer;
import com.xenoage.zong.renderer.awt.symbols.AwtSymbolsRenderer;
import com.xenoage.zong.renderer.awt.text.TextLayoutTools;
import com.xenoage.zong.renderer.awt.text.TextLayouts;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;
import com.xenoage.zong.renderer.slur.SimpleSlurShape;
import com.xenoage.zong.symbols.Symbol;

/**
 * AWT implementation of a {@link Canvas}
 *
 * @author Andreas Wenger
 */
public class AwtCanvas
	extends Canvas {

	//the AWT graphics context
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

	@Override public void drawSymbol(Symbol symbol, Color color, Point2f position, Point2f scaling) {
		AwtSymbolsRenderer.instance.draw(symbol, this, color, position, scaling);
	}

	@Override public void drawLine(Point2f p1, Point2f p2, Color color, float lineWidth) {
		g2d.setColor(toAwtColor(color));
		g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		g2d.draw(new Line2D.Float(p1.x, p1.y, p2.x, p2.y));
	}

	@Override public void drawStaff(Point2f pos, float length, int lines, Color color,
		float lineWidth, float interlineSpace) {
		g2d.setColor(toAwtColor(color));
		for (int i = 0; i < lines; i++) {
			g2d.fill(new Rectangle2D.Float(pos.x, pos.y + i * interlineSpace - lineWidth / 2, length,
				lineWidth));
		}
	}

	@Override public void drawSimplifiedStaff(Point2f pos, float length, float height, Color color) {
		g2d.setColor(toAwtColor(color));
		g2d.fill(new Rectangle2D.Float(pos.x, pos.y, length, height));
	}

	public void fillEllipse(Point2f pCenter, float width, float height, Color color) {
		g2d.setColor(toAwtColor(color));
		//use float coordinates to allow Java2D to optimize quality
		g2d.fill(new Ellipse2D.Float(pCenter.x - width / 2, pCenter.y - height / 2, width, height));
	}

	@Override public void drawBeam(Point2f[] points, Color color, float interlineSpace) {
		Rectangle2D beamSymbol = new Rectangle2D.Float(-1f, -0.25f, 2f, 0.5f);

		g2d.setColor(toAwtColor(color));

		AffineTransform g2dTransform = g2d.getTransform();

		float imageWidth = points[2].x - points[0].x;
		float imageHeight = points[3].y - points[0].y;
		float beamGrowthHeight = points[2].y - points[0].y;

		g2d.translate(points[0].x + imageWidth / 2, points[0].y + imageHeight / 2);
		g2d.shear(0, beamGrowthHeight / imageWidth);
		g2d.scale(imageWidth / beamSymbol.getWidth(),
			(points[1].y - points[0].y) / beamSymbol.getHeight());
		//g2d.fillRect(-5000000, -50000, 10000000, 100000);
		g2d.fill(beamSymbol);

		g2d.setTransform(g2dTransform);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void drawCurvedLine(Point2f p1, Point2f p2, Point2f c1, Point2f c2,
		float interlineSpace, Color color) {
		g2d.setColor(toAwtColor(color));
		SimpleSlurShape slurShape = new SimpleSlurShape(p1, p2, c1, c2, interlineSpace);
		g2d.fill(AwtSlurRenderer.getShape(slurShape));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void fillRect(Rectangle2f rect, Color color) {
		g2d.setColor(toAwtColor(color));
		g2d.fill(new Rectangle2D.Float(rect.position.x, rect.position.y, rect.size.width,
			rect.size.height));
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
