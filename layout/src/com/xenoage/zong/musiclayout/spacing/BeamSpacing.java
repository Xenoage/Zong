package com.xenoage.zong.musiclayout.spacing;

import java.util.List;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.BeamNotation;

import lombok.AllArgsConstructor;

/**
 * Horizontal and vertical spacing of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamSpacing {
	
	/** The beam notation. */
	public BeamNotation notation;
	/** The end positions (far from the note) of the stems. */
	public List<SP> stemsEndSp;
	
	/**
	 * Gets the direction of the stem at the given chord.
	 */
	public StemDirection getStemDirection(int chordIndex) {
		return notation.chords.get(chordIndex).stemDirection;
	}
	
}
