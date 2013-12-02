package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;

/**
 * This class contains layout information
 * about a time signature.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class TimeNotation
	implements Notation {

	/** The time signature element. */
	public final Time element;
	/** The front gap, element width and rear gap. */
	public final ElementWidth width;
	/** The horizontal offset of the numerator in interline spaces. */
	public final float numeratorOffset;
	/** The horizontal offset of the denominator in interline spaces. */
	public final float denominatorOffset;
	/** The gap between the digits in interline spaces. */
	public final float digitGap;


	@Override public ElementWidth getWidth() {
		return width;
	}

	@Override public Time getMusicElement() {
		return element;
	}

}
