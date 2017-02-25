package com.xenoage.zong.musiclayout.spacer.beam.stem;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.StemDirection;
import lombok.Value;

/**
 * Position of a beamed stem: The horizontal offset in mm, the stem direction,
 * the stem-side note LP and the preferred length in IS.
 *
 * @author Andreas Wenger
 */
@Const @Value
public class BeamedStem {

	public float xIs;
	public StemDirection dir;
	/** LP of the outermost stem-side note. */
	public int noteLp;
	public float lengthIs;

	/**
	 * Gets the LP where the stem ends.
	 */
	public float getEndLp() {
		return noteLp + dir.getSign() * lengthIs * 2;
	}

}
