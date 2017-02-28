package com.xenoage.zong.musiclayout.notation.chord;

import com.xenoage.zong.musiclayout.SLP;
import lombok.AllArgsConstructor;


/**
 * Vertical position of a chord stem.
 *
 * The stem may begin and and on different staffStampings. This is for example
 * needed for a cross-staff beam, where the note side of a stem belongs
 * to one staff and the end side belongs to the beam which belongs to
 * a different staff.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class StemNotation {

	/** The {@link SLP} where the stem stars (note side). */
	public SLP startSlp;
	/** The {@link SLP} where the stem ends (flag/beam side). */
	public SLP endSlp;
	
}
