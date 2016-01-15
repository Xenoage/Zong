package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.symbols.Symbol;

/**
 * Class for a symbol stamping that belongs to a staff.
 * 
 * This stamping can be used for all musical symbols.
 * To attach individual content (e.g. noteheads need
 * have different methods than clefs), extend this class.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public class StaffSymbolStamping
	extends Stamping {

	/** The stamped element. */
	public final Notation element;
	/** The parent staff. */
	public final StaffStamping parentStaff;
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
	

	@Override public Rectangle2f getBoundingShape() {
		Rectangle2f bounds = symbol.getBoundingRect();
		bounds = bounds.scale(scaling * parentStaff.is);
		bounds = bounds.move(position.xMm, parentStaff.computeYMm(position.lp));
		return bounds;
	}

	@Override public StampingType getType() {
		return StampingType.StaffSymbolStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
