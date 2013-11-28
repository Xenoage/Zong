package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Articulation;

/**
 * The alignment of a single articulation assigned
 * to a chord: its vertical position, its
 * horizontal offset and its type.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ArticulationAlignment {

	/** The vertical position of the articulation as a line position. */
	public final float yLP;
	/** The horizontal offset of the articulation in interline spaces. */
	public final float xOffsetIS;
	/** The type of the articulation (staccato, tenuto, ...). */
	public final Articulation type;

}
