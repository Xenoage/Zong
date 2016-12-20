package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import lombok.AllArgsConstructor;

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
		ChordSpacing chord = chords.get(chordIndex);
		float xMm = chord.getVoiceXMm() + chord.getStemXIs() * chord.voice.interlineSpace;
		StemNotation stem = notation.chords.get(chordIndex).stem;
		float lp;
		if (stem != null)
			lp = stem.endLp;
		else //it could be possible that there is no stem
			lp = notation.chords.get(chordIndex).getStemSideNoteLp() + getStemDirection(chordIndex).getSign() * 3 * 2;
		return sp(xMm, lp);
	}
	
}
