package com.xenoage.zong.musiclayout.notation;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * This is the interface for {@link MusicElement} layout
 * information classes.
 * 
 * It is called <code>Notation</code>, because it contains the positioned representation
 * of the by then ambiguous raw musical data.
 * 
 * It contains the width of an element and additional layout information.
 * 
 * For example, the class {@link ChordNotation} contains the
 * alignment of its notes and accidentals.
 *
 * For performance reasons, also the {@link MP} of the element can be stored,
 * instead of being retrieved each time again (some values could be unknown though).
 *
 * @author Andreas Wenger
 */
public interface Notation {

	/** Gets the notated musical element. */
	MusicElement getElement();

	/** Gets the {@link MP} of the element, as far as possible. */
	MP getMp();

	/** Gets the required horizontal space of this element. */
	ElementWidth getWidth();

}
