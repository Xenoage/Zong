package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;

/**
 * Class for a cursor stamping that belongs to one staff.
 * This may represent an input cursor for example.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public final class StaffCursorStamping
	extends Stamping {

	/** The horizontal position of the cursor, relative to the left side of the staff. */
	public final float xMm;
	/** An additional offset for the cursor in interline spaces. */
	public final float offsetIs;
	/** The staff where the cursor is placed. */
	public final StaffStamping staff;


	@Override public StampingType getType() {
		return StampingType.StaffCursorStamping;
	}
	
	@Override public Level getLevel() {
		return Level.EmptySpace;
	}

}
