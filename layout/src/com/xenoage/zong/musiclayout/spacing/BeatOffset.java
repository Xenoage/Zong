package com.xenoage.zong.musiclayout.spacing;

import lombok.Data;
import lombok.experimental.Wither;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;

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
@Const @Data @Wither
public class BeatOffset {

	/** The beat. */
	public final Fraction beat;
	/** The offset in mm. */
	public final float offsetMm;

}
