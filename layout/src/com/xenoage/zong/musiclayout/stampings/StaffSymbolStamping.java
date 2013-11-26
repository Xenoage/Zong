package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.symbols.Symbol;

/**
 * Class for a symbol stamping that belongs to a staff.
 * 
 * This stamping can be used for all musical symbols.
 * To attach individual context menus (e.g. noteheads need
 * have different methods than clefs), extend this class
 * and overwrite the createHandler() method.
 *
 * @author Andreas Wenger
 */
@Const public abstract class StaffSymbolStamping
	extends Stamping {

	/** The musical symbol. */
	@Getter protected final Symbol symbol;

	/** The color of the stamping, or null for default color. */
	@Getter protected final Color color;

	/** The position of the symbol. */
	@Getter protected final SP position;

	/** The scaling; e.g. 1 means, that it fits perfect to the staff size. */
	@Getter protected final float scaling;

	/** True for vertically mirroring the symbol, otherwise false. */
	@Getter protected final boolean mirrorV;


	public StaffSymbolStamping(StaffStamping parentStaff, MusicElement musicElement, Symbol symbol,
		Color color, SP position, float scaling, boolean mirrorV) {
		super(parentStaff, Level.Music, musicElement, createBoundingShape(symbol, scaling, parentStaff,
			position));
		this.symbol = symbol;
		this.color = color;
		this.position = position;
		this.scaling = scaling;
		this.mirrorV = mirrorV;
	}

	/**
	 * Creates the bounding geometry.
	 */
	private static Shape createBoundingShape(Symbol symbol, float scaling, StaffStamping parentStaff,
		SP position) {
		Rectangle2f bounds = symbol.boundingRect;
		bounds = bounds.scale(scaling * parentStaff.is);
		bounds = bounds.move(position.xMm, parentStaff.computeYMm(position.lp));
		return bounds;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.StaffSymbolStamping;
	}

}
