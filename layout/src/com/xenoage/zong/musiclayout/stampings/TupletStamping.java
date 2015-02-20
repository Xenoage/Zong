package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.text.FormattedText;

/**
 * Class for a tuplet stamping.
 * 
 * A tuplet stamping consists of a number painted above or below
 * the chords that form the tuplet, and optionally a bracket.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public final class TupletStamping
	extends Stamping {

	/** The left start position. */
	public final SP startSP;
	/** The right end position. */
	public final SP endSP;
	/** True, if a bracket should be drawn, otherwise false. */
	public final boolean bracket;
	/** The text in the middle of the tuplet bracket, or null. */
	@MaybeNull public final FormattedText text;
	/** The parent staff. */
	public final StaffStamping parentStaff;
	

	@Override public StampingType getType() {
		return StampingType.TupletStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
