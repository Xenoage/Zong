package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Class for a flags stamping.
 * 
 * It consists of one ore more flags needed for unbeamed
 * eighth, 16th, 32th notes and so on.
 *
 * @author Andreas Wenger
 */
@Const @RequiredArgsConstructor @Getter
public class FlagsStamping
	extends Stamping {

	/** The direction of a flag. This is usually the opposite stem direction. */
	public enum FlagsDirection {
		Up,
		Down;
	}

	/** The parent chord. */
	public final ChordNotation chord;
	/** The parent staff. */
	public final StaffStamping parentStaff;
	/** The direction of this flag. This is usually the opposite stem direction. */
	public final FlagsDirection flagsDirection;
	/** The number of flags. */
	public final int flagsCount;
	/** The flag symbol. */
	public final Symbol symbol;
	/** The scaling of the flags. This is 1 for full chords and < 1 for grace/cue chords */
	public final float scaling;
	/** The position of the flag. The vertical coordinate is the LP where the flag starts.
	 * This should always be the end position of the stem. */
	public final SP position;
	
	@Optimized(Performance)
	private Rectangle2f cachedBoundingShape = null;


	@Override public Shape getBoundingShape() {
		if (cachedBoundingShape != null)
			return cachedBoundingShape;
		//compute bounding shape
		float flagsDistance = getFlagsDistance(flagsDirection, scaling);
		float interlineSpace = parentStaff.is;
		for (int i = 0; i < flagsCount; i++) {
			Rectangle2f bounds = symbol.getBoundingRect().scale(scaling * interlineSpace);
			if (flagsDirection == FlagsDirection.Up) {
				bounds = bounds.move(0, -bounds.size.height);
			}
			bounds = bounds.move(0, -i * flagsDistance * interlineSpace);
			if (cachedBoundingShape == null)
				cachedBoundingShape = bounds;
			else
				cachedBoundingShape = cachedBoundingShape.extend(bounds);
		}
		cachedBoundingShape.move(position.xMm, parentStaff.computeYMm(position.lp));
		return cachedBoundingShape;
	}

	/**
	 * Gets the distance between the flags in interline spaces.
	 */
	public static float getFlagsDistance(FlagsDirection flagsDirection, float scaling) {
		return (flagsDirection == FlagsDirection.Down ? -1 : 1) * scaling;
	}

	@Override public StampingType getType() {
		return StampingType.FlagsStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
