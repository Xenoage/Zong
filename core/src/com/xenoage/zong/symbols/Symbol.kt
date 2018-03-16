package com.xenoage.zong.symbols

import com.xenoage.utils.math.Rectangle2f

/**
 * Base class for all musical symbol images,
 * like noteheads, clefs or ornaments.
 */
abstract class Symbol(
		/** The ID of the symbol. */
		val id: String,
		/** The type of this symbol. */
		val type: Type,
		/** The minimal bounding rectangle of this symbol. */
		val boundingRect: Rectangle2f,
		/** The vertical offset of the baseline (e.g. needed for dynamics letters). */
		val baselineOffset: Float = defaultBaseline,
		/** The height of the ascent (e.g. needed for dynamics letters)  */
		val ascentHeight: Float = defaultAccent
) {

	var leftBorderOrNull: Float? = null
	var rightBorderOrNull: Float? = null

	/**
	 * Gets the left border of the symbol. Used for symbols that are often used
	 * within texts (like forte or piano). If undefined, the left border of
	 * the bounding rect is returned.
	 */
	val leftBorder: Float =
			leftBorderOrNull ?: boundingRect.position.x

	/**
	 * Gets the right border of the symbol. Used for symbols that are often used
	 * within texts (like forte or piano). If undefined, the right border of
	 * the bounding rect is returned.
	 */
	val rightBorder: Float =
			rightBorderOrNull ?: boundingRect.position.x + boundingRect.size.width

	/**
	 * List of types of symbols.
	 *
	 * Needed as a fast workaround for the missing
	 * multiple dispatch feature in Java.
	 */
	enum class Type {
		/** A symbol, where the geometrical path is known. */
		PathSymbol,
		/** The warning symbol used for showing problems or missing symbols. */
		WarningSymbol
	}

	companion object {
		val defaultBaseline = 0f
		val defaultAccent = 2f
	}

}