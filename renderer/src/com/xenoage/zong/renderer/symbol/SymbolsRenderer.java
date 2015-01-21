package com.xenoage.zong.renderer.symbol;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.WarningSymbol;


/**
 * Renderer for all kinds of {@link Symbol}s.
 * 
 * @author Andreas Wenger
 */
public class SymbolsRenderer {

	/**
	 * Draws the given {@link Symbol} on the given canvas
	 * with the given color at the given position and the given scaling.
	 */
	public static void draw(Symbol symbol, Canvas canvas, Color color, Point2f position, Point2f scaling) {
		switch (symbol.getType()) {
			case PathSymbol:
				draw((PathSymbol) symbol, canvas, color, position, scaling);
				break;
			case WarningSymbol:
				draw((WarningSymbol) symbol, canvas, color, position, scaling);
				break;
		}
	}

	public static void draw(PathSymbol symbol, Canvas canvas, Color color, Point2f position, Point2f scaling) {
		canvas.transformSave();
		canvas.transformTranslate(position.x, position.y);
		canvas.transformScale(scaling.x, scaling.y);
		canvas.fillPath(symbol.path, color);
		canvas.transformRestore();
	}

	public static void draw(WarningSymbol symbol, Canvas canvas, Color color,
		Point2f position, Point2f scaling) {
		//the warning symbol is not visible on the rendering of the result
		if (canvas.getDecoration() == CanvasDecoration.Interactive)
			return;
		//TODO: paint warning symbol
		//Graphics2D g2d = AWTCanvas.getGraphics2D(canvas);
		//...
	}

}
