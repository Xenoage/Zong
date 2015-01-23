package com.xenoage.zong.renderer.awt.symbol;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GraphicAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.renderer.awt.path.AwtPath;
import com.xenoage.zong.symbols.PathSymbol;

/**
 * This class can draw {@link PathSymbol}s in a {@link TextLayout}.
 * 
 * @author Andreas Wenger
 */
public final class PathSymbolGraphicAttribute
	extends GraphicAttribute {

	private final PathSymbol symbol;
	private final float scaling;


	/**
	 * Creates a new {@link PathSymbolGraphicAttribute} from the given symbol
	 * using the given scaling.
	 * @throws IllegalArgumentException if the symbol does not provide a {@link Shape}
	 */
	public PathSymbolGraphicAttribute(PathSymbol symbol, float scaling) {
		super(GraphicAttribute.ROMAN_BASELINE);
		this.symbol = symbol;
		this.scaling = scaling * Units.mmToPx_1_1;
	}

	/**
	 * Returns the distance from the origin to the top bounds of the symbol.
	 */
	@Override public float getAscent() {
		return symbol.ascentHeight * scaling;
	}

	/**
	 * Returns the distance from the origin to the bottom bounds of the symbol.
	 * By convention, this is 0, even if something is painted below the baseline.
	 */
	@Override public float getDescent() {
		return 0;
	}

	/**
	 * Returns the distance from the origin to the right side of the bounds of the symbol.
	 */
	@Override public float getAdvance() {
		return (-1 * symbol.getLeftBorder() + symbol.getRightBorder()) * scaling;
	}

	@Override public void draw(Graphics2D g, float x, float y) {
		AffineTransform oldTransform = g.getTransform();
		g.translate(x, y);
		g.scale(scaling, scaling);
		g.translate(-1 * symbol.getLeftBorder(), symbol.baselineOffset);
		g.fill(AwtPath.createShape(symbol.getPath()));
		g.setTransform(oldTransform);
	}

	@Override public Rectangle2D getBounds() {
		Rectangle2f f = symbol.boundingRect;
		f.scale(scaling);
		return new Rectangle2D.Float(f.x1(), f.y1(), f.width(), f.height());
	}

	@Override public Shape getOutline(AffineTransform tx) {
		Shape s = AffineTransform.getScaleInstance(scaling, scaling).createTransformedShape(
			AwtPath.createShape(symbol.getPath()));
		return tx == null ? s : tx.createTransformedShape(s);
	}

}
