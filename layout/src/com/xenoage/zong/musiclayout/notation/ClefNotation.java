package com.xenoage.zong.musiclayout.notation;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class contains layout information about a clef, like its width,
 * line position and its scaling.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ClefNotation
	implements Notation {

	/** The clef. */
	@Getter public final Clef element;
	/** The width of the notation. */
	public final ElementWidth width;
	/** The line position of the clef. */
	public final int linePosition;
	/** The scaling, which is needed e.g. for cue clefs. */
	public final float scaling;

	@Override public MP getMp() {
		return element.getMP();
	}

	@Override public ElementWidth getWidth() {
		return width;
	}

}
