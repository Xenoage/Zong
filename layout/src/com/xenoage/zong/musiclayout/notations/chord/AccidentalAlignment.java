package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Accidental;

/**
 * The alignment of a single accidental within
 * a chord: its vertical position, its
 * horizontal offset and its type.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter public final class AccidentalAlignment {

	/** The vertical position of the accidental in half-space steps,
	 * beginning at the bottom line. Some examples:
	 * <ul>
	 *   <li>0: note is on the bottom line</li>
	 *   <li>-2: note is on the first lower leger line</li>
	 *   <li>5: note is between the 3rd and 4th line from below</li>
	 * </ul> */
	public final int linePosition;
	/** The horizontal offset of the accidental in interline spaces. */
	public final float offset;
	/** The type of the accidental, e.g. flat or sharp. */
	public final Accidental type;

}
