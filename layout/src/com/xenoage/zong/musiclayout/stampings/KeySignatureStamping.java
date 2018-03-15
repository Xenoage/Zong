package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import static com.xenoage.zong.core.music.key.TraditionalKey.getLinePosition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Class for a key signature stamping.
 * It consists of a number of flats and/or sharps.
 * 
 * At the moment this stamping can only be created for a {@link TraditionalKey}.
 *
 * @author Andreas Wenger
 */
@Const @RequiredArgsConstructor @Getter
public final class KeySignatureStamping
	extends Stamping {

	/** The key signature. */
	public final TraditionalKeyNotation key;
	/** The horizontal position in mm. */
	public final float xMm;
	/** The parent staff. */
	public final StaffStamping parentStaff;
	/** The accidental symbol. */
	public final Symbol symbol;
	/** The distance of the accidentals in mm. */
	public final float distanceMm;
	
	@Optimized(Performance)
	private Rectangle2f cachedBoundingShape = null;


	@Override public Shape getBoundingShape() {
		if (cachedBoundingShape == null)
			return cachedBoundingShape;
		//compute bounding shape
		int fifth = key.element.getFifths();
		if (fifth == 0)
			return null;
		boolean useSharps = (fifth > 0);
		//create bounding shape
		fifth = Math.abs(fifth);
		float interlineSpace = parentStaff.is;
		for (int i = 0; i < fifth; i++) {
			int linePosition = Companion.getLinePosition(i, useSharps, key.c4Lp, key.minLp);
			Rectangle2f bounds = symbol.getBoundingRect();
			bounds = bounds.scale(interlineSpace);
			bounds = bounds.move(xMm + i * distanceMm * interlineSpace,
				parentStaff.computeYMm(linePosition));
			if (cachedBoundingShape == null)
				cachedBoundingShape = bounds;
			else
				cachedBoundingShape = cachedBoundingShape.extend(bounds);
		}
		return cachedBoundingShape;
	}

	@Override public StampingType getType() {
		return StampingType.KeySignatureStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
