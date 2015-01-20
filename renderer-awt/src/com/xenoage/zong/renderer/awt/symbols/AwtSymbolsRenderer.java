package com.xenoage.zong.renderer.awt.symbols;

import static com.xenoage.utils.jse.color.AwtColorUtils.toAwtColor;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.renderer.awt.canvas.AwtCanvas;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.symbols.SymbolsRenderer;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.WarningSymbol;

/**
 * AWT implementation of a {@link SymbolsRenderer}.
 * 
 * @author Andreas Wenger
 */
public class AwtSymbolsRenderer
	extends SymbolsRenderer {

	public static final AwtSymbolsRenderer instance = new AwtSymbolsRenderer();

	private AwtSymbolsCache cache = new AwtSymbolsCache();
	

	@Override public void draw(PathSymbol symbol, Canvas canvas, Color color, Point2f position,
		Point2f scaling) {
		Graphics2D g2d = AwtCanvas.getGraphics2D(canvas);
		AffineTransform g2dTransform = g2d.getTransform();
		g2d.translate(position.x, position.y);
		g2d.scale(scaling.x, scaling.y);
		
		g2d.setColor(toAwtColor(color));
		Shape shape = cache.getShape(symbol.getPath());
		g2d.fill(shape);

		/* TEST
		g2d.setColor(Color.green);
		g2d.setStroke(new BasicStroke(0.1f));
		g2d.draw(new Line2D.Float(0, -1, 0, +1)); //*/

		g2d.setTransform(g2dTransform);
	}

	@Override public void draw(WarningSymbol symbol, Canvas canvas, Color color,
		Point2f position, Point2f scaling) {
		//the warning symbol is not visible on the rendering of the result
		if (canvas.getDecoration() == CanvasDecoration.Interactive)
			return;
		//TODO: paint warning symbol
		//Graphics2D g2d = AWTCanvas.getGraphics2D(canvas);
		//...
	}

}
