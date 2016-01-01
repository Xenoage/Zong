package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.zong.core.music.format.SP.sp;

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
	/** The chords of the beam. */
	public List<ElementSpacing> chords;
	
	/**
	 * Gets the direction of the stem at the given chord.
	 */
	public StemDirection getStemDirection(int chordIndex) {
		return notation.chords.get(chordIndex).stemDirection;
	}
	
	/**
	 * Gets the SP of the end of the stem of the given chord.
	 */
	public SP getStemEndSp(int chordIndex) {
		ElementSpacing chord = chords.get(chordIndex);
		float xMm = chord.getVoiceXMm() + ChordSpacing.getStemXIs(chord) * chord.parent.interlineSpace;
		float lp = notation.chords.get(chordIndex).stem.endLp;
		return sp(xMm, lp);
	}
	
}
