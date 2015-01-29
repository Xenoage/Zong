package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.cache.util.OpenBeamMiddleStem;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * Creates a {@link StemStamping} from a {@link ChordNotation}.
 * 
 * @author Andreas Wenger
 */
@SuppressWarnings("Test needed")
public class StemStamper {
	
	public static final StemStamper stemStamper = new StemStamper();
	
	public enum Type {
		NoStem,
		Stem,
		MiddleBeamStem
	}
	
	
	public Type getStemType(ChordNotation chordNotation) {
		StemDirection stemDir = chordNotation.getStemDirection();
		if (stemDir == StemDirection.None)
			return Type.NoStem;
		//stamp all stems except middle-beam stems
		Chord chord = chordNotation.element;
		Beam beam = chord.getBeam();
		if (beam == null)
			return Type.Stem;
		else if (beam.getStart().getChord() == chord || beam.getStop().getChord() == chord)
			return Type.Stem;
		return Type.MiddleBeamStem;
	}

	public StemStamping stamp(ChordNotation chordNotation, float leftNoteXMm, StaffStamping staffStamping) {
		float stemXMm = getXMm(leftNoteXMm, chordNotation, staffStamping);
		StemAlignment sa = chordNotation.stemAlignment;
		return new StemStamping(staffStamping, chordNotation.element, stemXMm,
			sa.startLp, sa.endLp, chordNotation.stemDirection);
	}
	
	public OpenBeamMiddleStem openBeamMiddleStem(ChordNotation chordNotation, float leftNoteXMm,
		StaffStamping staffStamping) {
		NotesAlignment na = chordNotation.notesAlignment;
		float stemXMm = getXMm(leftNoteXMm, chordNotation, staffStamping);
		//for beamed chords in the middle of a beam only: we do not know the end position yet,
		//but remember it as an open stem
		return new OpenBeamMiddleStem(staffStamping, chordNotation.element, chordNotation.stemDirection,
			stemXMm, na.getBottomNoteAlignment().lp, na.getTopNoteAlignment().lp);
	}

	float getXMm(float leftNoteXMm, ChordNotation chordNotation, StaffStamping staffStamping) {
		return leftNoteXMm + chordNotation.notesAlignment.stemOffsetIs * staffStamping.is;
	}
	
}
