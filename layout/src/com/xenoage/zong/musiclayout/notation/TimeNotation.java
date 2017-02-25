package com.xenoage.zong.musiclayout.notation;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class contains layout information
 * about a time signature.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class TimeNotation
	implements Notation {

	/** The time signature element. */
	@Getter public final TimeSignature element;
	/** The front gap, element width and rear gap. */
	@Getter public final ElementWidth width;
	/** The horizontal offset of the numerator in interline spaces. */
	public final float numeratorOffset;
	/** The horizontal offset of the denominator in interline spaces. */
	public final float denominatorOffset;
	/** The gap between the digits in interline spaces. */
	public final float digitGap;

	@Override public MP getMp() {
		return element.getMP();
	}

}
