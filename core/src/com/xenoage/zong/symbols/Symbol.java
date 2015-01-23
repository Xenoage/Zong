package com.xenoage.zong.symbols;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Base class for all musical symbol images,
 * like noteheads, clefs or ornaments.
 *
 * @author Andreas Wenger
 */
public abstract class Symbol {

	public static float DEFAULT_BASELINE = 0;
	public static float DEFAULT_ASCENT = 2f;

	/** The ID of the symbol */
	@Getter public final String id;
	/** The minimal bounding rectangle of this symbol */
	@Getter public final Rectangle2f boundingRect;
	/** The vertical offset of the baseline (e.g. needed for dynamics letters) */
	@Getter public final float baselineOffset;
	/** The height of the ascent (e.g. needed for dynamics letters) */
	@Getter public final float ascentHeight;

	private final Float leftBorder;
	private final Float rightBorder;


	Symbol(String id, Rectangle2f boundingRect, @MaybeNull Float baselineOffset,
		@MaybeNull Float ascentHeight, @MaybeNull Float leftBorder, @MaybeNull Float rightBorder) {
		this.id = id;
		this.boundingRect = boundingRect;
		this.baselineOffset = notNull(baselineOffset, DEFAULT_BASELINE);
		this.ascentHeight = notNull(ascentHeight, DEFAULT_ASCENT);
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
	}

	/**
	 * Gets the left border of the symbol. Used for symbols that are often used
	 * within texts (like forte or piano). If undefined, the left border of
	 * the bounding rect is returned.
	 */
	public float getLeftBorder() {
		if (leftBorder != null)
			return leftBorder;
		else
			return boundingRect.position.x;
	}

	/**
	 * Gets the right border of the symbol. Used for symbols that are often used
	 * within texts (like forte or piano). If undefined, the right border of
	 * the bounding rect is returned.
	 */
	public float getRightBorder() {
		if (rightBorder != null)
			return rightBorder;
		else
			return boundingRect.position.x + boundingRect.size.width;
	}

	/**
	 * Gets the type of this symbol.
	 */
	public abstract SymbolType getType();

}
