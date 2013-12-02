package com.xenoage.zong.musiclayout.spacing.horizontal;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.xenoage.utils.annotations.Const;
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
@Const @Data @AllArgsConstructor public final class SpacingElement {

	/** The corresponding music element, e.g. a chord. May be null, e.g. when this
	 * element denotes the end point of the measure. */
	@MaybeNull public final MusicElement element;
	/** The beat where this music element can be found in the measure */
	@NonNull public final Fraction beat;
	/** The horizontal offset of the element in interline spaces */
	public final float offset;
	/** True, if this is a grace element (0 duration) */
	public final boolean grace;

	public SpacingElement(MusicElement element, Fraction beat, float offset) {
		this(element, beat, offset, false);
	}

	public SpacingElement withOffset(float offset) {
		return new SpacingElement(element, beat, offset, grace);
	}

}
