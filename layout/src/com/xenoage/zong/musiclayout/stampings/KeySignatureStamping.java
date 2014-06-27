package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.zong.core.music.key.TraditionalKey.getLinePosition;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Class for a key signature stamping.
 * It consists of a number of flats and/or sharps.
 * 
 * At the moment this stamping can only be created for a {@link TraditionalKey}.
 *
 * @author Andreas Wenger
 */
@Const public final class KeySignatureStamping
	extends Stamping {

	/** The horizontal position in mm. */
	public final float positionX;

	/** The key signature. */
	public final TraditionalKey traditionalKey;

	/** The line position of a C4. */
	public final int linePositionC4;

	/** The minimal line position for a sharp/flat. */
	public final int linePositionMin;

	/** General layout preferences, containing the widths for sharps and flats */
	public final LayoutSettings layoutSettings;


	public KeySignatureStamping(TraditionalKey traditionalKey, int linePositionC4,
		int linePositionMin, float positionX, StaffStamping parentStaff, SymbolPool symbolPool,
		LayoutSettings layoutSettings) {
		super(parentStaff, Level.Music, null, createBoundingShape(traditionalKey, parentStaff,
			linePositionC4, linePositionMin, positionX, symbolPool, layoutSettings));
		this.traditionalKey = traditionalKey;
		this.linePositionC4 = linePositionC4;
		this.linePositionMin = linePositionMin;
		this.positionX = positionX;
		this.layoutSettings = layoutSettings;
	}

	private static Shape createBoundingShape(TraditionalKey traditionalKey,
		StaffStamping parentStaff, int linePositionC4, int linePositionMin, float positionX,
		SymbolPool symbolPool, LayoutSettings layoutSettings) {
		int fifth = traditionalKey.getFifths();
		if (fifth == 0)
			return null;
		boolean useSharps = (fifth > 0);
		float distance = (useSharps ? layoutSettings.spacings.widthSharp
			: layoutSettings.spacings.widthFlat);
		Symbol symbol = symbolPool.getSymbol(useSharps ? CommonSymbol.AccidentalSharp
			: CommonSymbol.AccidentalFlat);
		//create bounding shape
		Rectangle2f shape = null;
		fifth = Math.abs(fifth);
		float interlineSpace = parentStaff.is;
		for (int i = 0; i < fifth; i++) {
			int linePosition = getLinePosition(i, useSharps, linePositionC4, linePositionMin);
			Rectangle2f bounds = symbol.getBoundingRect();
			bounds = bounds.scale(interlineSpace);
			bounds = bounds.move(positionX + i * distance * interlineSpace,
				parentStaff.computeYMm(linePosition));
			if (shape == null)
				shape = bounds;
			else
				shape = shape.extend(bounds);
		}
		return shape;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.KeySignatureStamping;
	}

}
