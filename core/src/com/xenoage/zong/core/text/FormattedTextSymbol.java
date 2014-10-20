package com.xenoage.zong.core.text;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.TextMetrics;
import com.xenoage.utils.math.Units;
import com.xenoage.zong.symbols.Symbol;

/**
 * Musical {@link Symbol} within a formatted text.
 *
 * @author Andreas Wenger
 */
@Const @Data public class FormattedTextSymbol
	implements FormattedTextElement {

	private final Symbol symbol;
	private final FormattedTextStyle style;
	private final float scaling;
	/** The horizontal offset of the symbol, which must be added so that
	 * the symbol is left-aligned). */
	private final float offsetX;

	/** Measurements, with only ascent and width set. The other values are 0. */
	private transient final TextMetrics metrics;


	/**
	 * Creates a new {@link FormattedTextSymbol}.
	 * @param symbol   the symbol    
	 * @param sizePt   the size of the symbol in pt (relative to the ascent
	 *                 height defined in the symbol)
	 * @param color    the color of the symbol
	 */
	public FormattedTextSymbol(Symbol symbol, float sizePt, Color color) {
		this.symbol = symbol;
		this.style = new FormattedTextStyle(sizePt, color);
		//compute scaling
		this.scaling = computeScaling(symbol, sizePt);
		//compute ascent and width
		float ascent = symbol.ascentHeight * this.scaling; //TODO: right?
		float width = (symbol.getRightBorder() - symbol.getLeftBorder()) * this.scaling;
		this.metrics = new TextMetrics(ascent, 0, 0, width);
		//horizontal offset: align symbol to the left side
		this.offsetX = -symbol.getLeftBorder() * this.scaling;
	}

	@Override public int getLength() {
		return 1;
	}

	/**
	 * Returns the Unicode Object Replacement character, <code>\ufffc</code>.
	 */
	@Override public String getText() {
		return "\ufffc"; //unicode object replacement character
	}

	/**
	 * Computes and returns the scaling factor that is needed to draw
	 * the given symbol fitting to the given font size.
	 */
	public static float computeScaling(Symbol symbol, float sizePt) {
		//TODO: 0.65f is a constant that defines that the ascent has 65% of the complete hight
		return sizePt * Units.pxToMm_1_1 / symbol.ascentHeight * 0.65f;
	}

	@Override public String toString() {
		return "[symbol " + symbol.id + "]";
	}

}
