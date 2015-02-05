package com.xenoage.zong.musiclayout.spacing.horizontal;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;

/**
 * A spacing element stores the beat and the position (offset)
 * of a given {@link MusicElement} in a layout.
 * 
 * All units are measured in interline spaces.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class SpacingElement {

	/** The corresponding music element, e.g. a chord. May be null, e.g. when this
	 * element denotes the end point of the measure. */
	@MaybeNull public final MusicElement element;
	/** The beat where this music element can be found in the measure */
	@NonNull public final Fraction beat;
	/** True, if this is a grace element (0 duration) */
	public final boolean grace;
	/** The horizontal offset of the element in interline spaces */
	public float offsetIs;
	

	public SpacingElement(MusicElement element, Fraction beat, float offset) {
		this(element, beat, false, offset);
	}


	@Deprecated
	public SpacingElement withOffset(float offset) {
		return new SpacingElement(element, beat, grace, offset);
	}

}
