package com.xenoage.zong.musiclayout.spacer.beam.stem;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.SLP;
import lombok.Value;

/**
 * Position of a beamed stem: The horizontal offset in mm, the stem direction,
 * the stem-side note {@link SLP} and the end {@link SLP}.
 *
 * The staff indices are needed, since a stem may begin and end on different
 * staves (e.g. for cross-staff beams).
 *
 * @author Andreas Wenger
 */
@Const @Value
public class BeamedStem {

	public float xIs;
	public StemDirection dir;
	/** SLP of the outermost stem-side note. */
	public SLP noteSlp;
	/** SLP of the end of the stem. */
	public SLP endSlp;

}
