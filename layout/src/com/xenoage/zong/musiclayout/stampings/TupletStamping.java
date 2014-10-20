package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.text.FormattedText;

/**
 * Class for a tuplet stamping.
 * 
 * A tuplet stamping consists of a number painted above or below
 * the chords that form the tuplet, and optionally a bracket.
 *
 * @author Andreas Wenger
 */
@Const public final class TupletStamping
	extends Stamping {

	/** The horizontal start position in mm. */
	public final float x1mm;

	/** The horizontal end position in mm. */
	public final float x2mm;

	/** The vertical start position as a LP. */
	public final float y1lp;

	/** The vertical end position as a LP. */
	public final float y2lp;

	/** True, if a bracket should be drawn, otherwise false. */
	public final boolean bracket;

	/** The text in the middle of the tuplet bracket, or null. */
	public final FormattedText text;


	public TupletStamping(float x1, float x2, float lp1, float lp2, boolean bracket,
		FormattedText text, StaffStamping parentStaff) {
		super(parentStaff, Level.Music, null, null);
		this.x1mm = x1;
		this.x2mm = x2;
		this.y1lp = lp1;
		this.y2lp = lp2;
		this.bracket = bracket;
		this.text = text;
	}

	@Override public StampingType getType() {
		return StampingType.TupletStamping;
	}

}
