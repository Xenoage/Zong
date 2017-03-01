package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.List;

import static com.xenoage.zong.core.music.format.SP.sp;

/**
 * Horizontal and vertical spacing of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamSpacing {
	
	/** The beam notation. */
	public BeamNotation notation;
	/** The LP at the left side of the beam. This value may be different from the end LP
	 * of the left stem, when we have a cross-staff beam, since the beam lines are drawn
	 * on the other side then. */
	public float leftLp;
	/** The LP at the right side of the beam. See {@link #leftLp}. */
	public float rightLp;
	/** The chords of the beam. */
	public List<ChordSpacing> chords;
	
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
		float xMm = getStemXMm(chordIndex);
		StemNotation stem = notation.chords.get(chordIndex).stem;
		float lp;
		if (stem != null)
			lp = stem.endSlp.lp;
		else //it could be possible that there is no stem
			lp = notation.chords.get(chordIndex).getStemSideNoteLp() + getStemDirection(chordIndex).getSign() * 3 * 2;
		return sp(xMm, lp);
	}

	/**
	 * Gets the horizontal position of the given stem in mm.
	 */
	public float getStemXMm(int chordIndex) {
		val chord = chords.get(chordIndex);
		return chord.getVoiceXMm() + chord.getStemXIs() * chord.voice.interlineSpace;
	}

	/**
	 * Gets the {@link SP} of the left end of the beam.
	 */
	public SP getLeftSp() {
		return sp(getStemXMm(0), leftLp);
	}

	/**
	 * Gets the {@link SP} of the right end of the beam.
	 */
	public SP getRightSp() {
		return sp(getStemXMm(chords.size() - 1), rightLp);
	}
	
}
