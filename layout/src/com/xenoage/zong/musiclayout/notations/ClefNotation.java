package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;

/**
 * This class contains layout information about a clef, like its width,
 * line position and its scaling.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ClefNotation
	implements Notation {

	/** The clef. */
	public final Clef element;
	/** The width of the notation. */
	public final ElementWidth width;
	/** The line position of the clef. */
	public final int linePosition;
	/** The scaling, which is needed e.g. for cue clefs. */
	public final float scaling;


	@Override public ElementWidth getWidth() {
		return width;
	}

	@Override public Clef getMusicElement() {
		return element;
	}

}
