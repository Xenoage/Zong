package com.xenoage.zong.musiclayout;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;

/**
 * Offset of a beat in mm.
 * 
 * For instance, an offset of 3 and a beat of 2/4 means,
 * that the chord/rest on beat 2/4 begins
 * 3 mm after the barline of the measure.
 * 
 * The offset is in mm and not in interline spaces, so that
 * it can be used for a whole measure column without respect
 * to the sizes of its staves.
 *
 * @author Andreas Wenger
 */
@Const @Data public final class BeatOffset {

	/** The beat. */
	private final Fraction beat;
	/** The offset in mm. */
	private final float offsetMm;


	/**
	 * Returns a copy of this {@link SpacingElement}, but using the
	 * given the offset in mm.
	 */
	public BeatOffset withOffsetMm(float offsetMm) {
		return new BeatOffset(beat, offsetMm);
	}

	/**
	 * Shifts the offset by the given value.
	 */
	public BeatOffset shiftOffsetMm(float deltaMm) {
		return new BeatOffset(beat, offsetMm + deltaMm);
	}

}
