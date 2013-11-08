package com.xenoage.zong.core.music.chord;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;


/**
 * Class for a stem, that is belongs to a chord.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class Stem {

	/** The direction of the stem, or null for default. */
	@MaybeNull private final StemDirection direction;

	/** The length of the stem, measured from the outermost chord not at the stem side
	 * to the end of the stem, in interline spaces, or null for default. */
	@MaybeNull private final Float length;

}
