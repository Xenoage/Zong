package com.xenoage.zong.symbols;

import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Symbols which consist of a complex path.
 * 
 * The type of the shape is dependent on the graphics library.
 * Currently, AWT apps use the <code>java.awt.geom.GeneralPath</code> class, while
 * Android apps use the <code>android.graphics.Path</code> class.
 *
 * @author Andreas Wenger
 */
public final class PathSymbol
	extends Symbol {

	/** The geometric path describing this symbol. */
	public final Object path;


	public PathSymbol(String id, Object path, Rectangle2f boundingRect, Float baselineOffset,
		Float ascentHeight, Float leftBorder, Float rightBorder) {
		super(id, boundingRect, baselineOffset, ascentHeight, leftBorder, rightBorder);
		this.path = path;
	}

	/**
	 * Gets the type of this symbol.
	 */
	@Override public SymbolType getType() {
		return SymbolType.PathSymbol;
	}

	/**
	 * Gets the shape of this symbol, whose type is dependent on the graphics library.
	 */
	@Override public Object getShape() {
		return path;
	}

}
