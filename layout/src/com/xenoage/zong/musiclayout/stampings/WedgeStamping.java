package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;

/**
 * Class for a wedge (crescendo or decrescendo) stamping
 * belonging to a staff.
 * 
 * It has a vertical position (line position of the
 * center baseline of the wedge), a horizontal start
 * and end position in mm and the vertical distance at these
 * points in interline spaces.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public final class WedgeStamping
	extends Stamping {

	/** The line position of the (centered) baseline. */
	public final float lp;
	/** The horizontal start position in mm relative to the beginning of the staff. */
	public final float leftXMm;
	/** The horizontal end position in mm relative to the beginning of the staff. */
	public final float rightXMm;
	/** The vertical distance of the lines at the start position in IS. */
	public final float leftDistanceIs;
	/** The vertical distance of the lines at the end position in IS. */
	public final float rightDistanceIs;
	/** The parent staff. */
	public final StaffStamping parentStaff;
	

	@Override public StampingType getType() {
		return StampingType.WedgeStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
