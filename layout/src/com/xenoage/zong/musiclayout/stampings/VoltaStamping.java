package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.text.FormattedText;

/**
 * Class for a volta stamping belonging to a staff.
 * 
 * It has a vertical position (line position of the
 * horizontal volta line), and optionally a left downward
 * hook and a right downward hook. Optionally, there may
 * be a {@link FormattedText} left-aligned under the
 * volta line.
 * 
 * Many values are optional, since voltas can contain
 * line breaks, and e.g. a left hook and text is only used
 * in the first system, but not in the following ones.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public final class VoltaStamping
	extends Stamping {

	/** The line position of the horizontal line. */
	public final float lp;
	/** The horizontal start position in mm relative to the beginning of the staff. */
	public final float leftXMm;
	/** The horizontal end position in mm relative to the beginning of the staff. */
	public final float rightXMm;
	/** The caption of the volta (or null). */
	@MaybeNull public final FormattedText text;
	/** True, if there is a left downward hook, else false. */
	public final boolean leftHook;
	/** True, if there is a right downward hook, else false. */
	public final boolean rightHook;
	/** The parent staff. */
	public final StaffStamping parentStaff;
	

	@Override public StampingType getType() {
		return StampingType.VoltaStamping;
	}

	@Override public Level getLevel() {
		return Level.Music;
	}
	
}
