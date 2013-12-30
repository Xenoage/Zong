package com.xenoage.zong.renderer.symbols;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.symbols.PathSymbol;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.WarningSymbol;

/**
 * Interface for {@link Symbol} renderers.
 * 
 * @author Andreas Wenger
 */
public abstract class SymbolsRenderer {

	/**
	 * Draws the given {@link Symbol} on the given canvas
	 * with the given color at the given position and the given scaling.
	 */
	public void draw(Symbol symbol, Canvas canvas, Color color, Point2f position, Point2f scaling) {
		switch (symbol.getType()) {
			case PathSymbol:
				draw((PathSymbol) symbol, canvas, color, position, scaling);
				break;
			case WarningSymbol:
				draw((WarningSymbol) symbol, canvas, color, position, scaling);
				break;
		}
	}

	/**
	 * Draws the given {@link PathSymbol} on the given canvas
	 * with the given color at the given position and the given scaling.
	 */
	public abstract void draw(PathSymbol symbol, Canvas canvas, Color color, Point2f position,
		Point2f scaling);

	/**
	 * Draws the given {@link WarningSymbol} on the given canvas
	 * with the given color at the given position and the given scaling.
	 */
	public abstract void draw(WarningSymbol symbol, Canvas canvas, Color color, Point2f position,
		Point2f scaling);

}
