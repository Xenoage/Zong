package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.group.BracketGroup;

/**
 * Class for a bracket stamping.
 * 
 * This element groups one or more adjacent staves with a brace or
 * a square bracket at the very beginning of the system.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public class BracketStamping
	extends Stamping {

	/** The first staff stamping this bracket embraces. */
	public final StaffStamping firstStaff;
	/** The last staff stamping this bracket embraces. */
	public final StaffStamping lastStaff;
	/** The horizontal position of the bracket in mm. */
	public final float xMm;
	/** The style of the bracket. */
	public final BracketGroup.Style groupStyle;


	@Override public StampingType getType() {
		return StampingType.BracketStamping;
	}

	@Override public Level getLevel() {
		return Level.Music;
	}
	
}
