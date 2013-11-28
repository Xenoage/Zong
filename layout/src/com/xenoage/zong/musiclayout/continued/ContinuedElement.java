package com.xenoage.zong.musiclayout.continued;

import com.xenoage.zong.core.music.MusicElement;

/**
 * Interface for a continued element.
 * 
 * A continued element connects stampings on different
 * staves and frames like a slur that spans over multiple staves
 * or even frames.
 * 
 * It is used for example in the layouting process, so that
 * the layouter of a staff or frame knows which slurs or voltas are
 * still unclosed and have to be painted.
 * 
 * A more sophisticated usage is the following situation:
 * the layouter knows if a following frame has to be re-layouted
 * when the current one changes. This allows re-layouting of single
 * frames instead of the whole score, which is very important
 * for the speed of the layouting process.
 * 
 * @author Andreas Wenger
 */
public interface ContinuedElement {

	/**
	 * Gets the MusicElement which is continued.
	 */
	public MusicElement getMusicElement();

	/**
	 * Gets the staff index of the continued element.
	 */
	public int getStaffIndex();

}
