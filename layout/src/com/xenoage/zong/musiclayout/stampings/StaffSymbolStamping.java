package com.xenoage.zong.musiclayout.stampings;

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
	public final Symbol symbol;

	/** The color of the stamping, or null for default color. */
	public final Color color;

	/** The position of the symbol. */
	public final SP position;

	/** The scaling; e.g. 1 means, that it fits perfect to the staff size. */
	public final float scaling;

	/** True for vertically mirroring the symbol, otherwise false. */
	public final boolean mirrorV;


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
		Rectangle2f bounds = symbol.getBoundingRect();
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
