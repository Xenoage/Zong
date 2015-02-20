package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.format.SP;

/**
 * Class for a leger line stamping.
 * 
 * Leger lines belong to a staff.
 * They have a horizontal position around which they are centered.
 * For simple chords, they are usually {@value #lengthNormalIs} spaces long,
 * and if the chord contains suspended notes, {@value #lengthSuspendedIs} is usual.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class LegerLineStamping
	extends Stamping {
	
	/** Length of a normal leger line. */ //TIDY: move in layout settings
	public static final float lengthNormalIs = 2 * 1.1f;
	/** Length of a leger line within a chord with suspended notes. */ //TIDY: move in layout settings
	public static final float lengthSuspendedIs = 3 * 1.1f;

	/** The position of this leger line, around which the line is horizontally centered. */
	public final SP sp;
	/** The width of this leger line in IS. */
	public final float widthIs;
	/** The parent staff. */
	public final StaffStamping parentStaff;
	

	@Override public StampingType getType() {
		return StampingType.LegerLineStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
