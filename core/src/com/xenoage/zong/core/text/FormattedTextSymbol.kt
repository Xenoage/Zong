package com.xenoage.zong.core.text

import com.xenoage.utils.Pt
import com.xenoage.utils.color.Color
import com.xenoage.utils.font.TextMetrics
import com.xenoage.utils.pxToMm_1_1
import com.xenoage.zong.symbols.Symbol

/**
 * Musical [Symbol] within a formatted text.
 */
class FormattedTextSymbol(
		val symbol: Symbol,
		/** The size of the symbol in pt (relative to the ascent
		 *  height defined in the symbol */
		val size: Pt,
		val color: Color
) : FormattedTextElement {

	override val style: FormattedTextStyle = FormattedTextStyle(color = color)
	val scaling: Float

	/** The horizontal offset of the symbol, which must be added so that
	 * the symbol is left-aligned).  */
	val offsetX: Float

	/** Measurements, with only ascent and width set. The other values are 0.  */
	override val metrics: TextMetrics

	init {
		//compute scaling
		this.scaling = computeScaling(symbol, size)
		//compute ascent and width
		val ascent = symbol.ascentHeight * this.scaling //TODO: right?
		val width = (symbol.rightBorder - symbol.leftBorder) * this.scaling
		this.metrics = TextMetrics(ascent, 0f, 0f, width)
		//horizontal offset: align symbol to the left side
		this.offsetX = -symbol.leftBorder * this.scaling
	}

	override val length: Int
		get() = 1

	/** Returns the Unicode Object Replacement character, `\ufffc`. */
	override val text: String
		get() = "\ufffc"

	override fun toString(): String =
			"[symbol ${symbol.id}]"

	companion object {

		/**
		 * Computes and returns the scaling factor that is needed to draw
		 * the given symbol fitting to the given font size.
		 */
		private fun computeScaling(symbol: Symbol, size: Pt): Float =
			//TODO: 0.65f is a constant that defines that the ascent has 65% of the complete hight
			size * pxToMm_1_1 / symbol.ascentHeight * 0.65f
	}

}
