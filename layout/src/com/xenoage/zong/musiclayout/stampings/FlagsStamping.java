package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Class for a flags stamping.
 * 
 * It consists of one ore more flags needed for unbeamed
 * eighth, 16th, 32th notes and so on.
 *
 * @author Andreas Wenger
 */
@Const public class FlagsStamping
	extends Stamping {

	/** The direction of a flag. This is usually the opposite stem direction. */
	public enum FlagsDirection {
		Up,
		Down;
	}

	/** The direction of this flag. This is usually the opposite stem direction. */
	public final FlagsDirection flagsDirection;
	/** The number of flags. */
	public final int flagsCount;
	/** The scaling of the flags. This is 1 for full chords and < 1 for grace/cue chords */
	public final float scaling;
	/** The position of the flag. The vertical coordinate is the LP where the flag starts.
	 * This should always be the end position of the stem.
	 */
	public final SP position;


	public FlagsStamping(FlagsDirection flagsDirection, int flagsCount, float scaling,
		StaffStamping parentStaff, Chord chord, SP position, SymbolPool symbolPool) {
		super(parentStaff, Level.Music, chord, createBoundingShape(flagsDirection, flagsCount, scaling,
			parentStaff, position, symbolPool));
		this.flagsDirection = flagsDirection;
		this.flagsCount = flagsCount;
		this.scaling = scaling;
		this.position = position;
	}

	private static Shape createBoundingShape(FlagsDirection flagsDirection, int flagsCount,
		float scaling, StaffStamping parentStaff, SP position, SymbolPool symbolPool) {
		Symbol symbol = symbolPool.getSymbol(CommonSymbol.NoteFlag);
		float flagsDistance = getFlagsDistance(flagsDirection, scaling);
		float interlineSpace = parentStaff.is;
		Rectangle2f flagsBounds = null;
		for (int i = 0; i < flagsCount; i++) {
			Rectangle2f bounds = symbol.getBoundingRect().scale(scaling * interlineSpace);
			if (flagsDirection == FlagsDirection.Up) {
				bounds = bounds.move(0, -bounds.size.height);
			}
			bounds = bounds.move(0, -i * flagsDistance * interlineSpace);
			if (flagsBounds == null)
				flagsBounds = bounds;
			else
				flagsBounds = flagsBounds.extend(bounds);
		}
		flagsBounds.move(position.xMm, parentStaff.computeYMm(position.lp));
		return flagsBounds;
	}

	/**
	 * Gets the distance between the flags in interline spaces.
	 */
	public static float getFlagsDistance(FlagsDirection flagsDirection, float scaling) {
		return (flagsDirection == FlagsDirection.Down ? -1 : 1) * scaling;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.FlagsStamping;
	}

}
