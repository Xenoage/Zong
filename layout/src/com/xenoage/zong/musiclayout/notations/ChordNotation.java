package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * This class contains layout information about a chord, like its width and the
 * alignment of the notes, dots, accidentals and articulations.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class ChordNotation
	implements Notation {

	@Getter public Chord element;
	@Getter public ElementWidth width;
	public NotesNotation notes;
	public StemDirection stemDirection = StemDirection.Default;
	public StemNotation stem;
	public AccidentalsNotation accidentals;
	public ArticulationsNotation articulations;
	public BeamNotation beam;
	
	public ChordNotation(Chord element) {
		this.element = element;
	}
	
	public ChordNotation(Chord element, ElementWidth width) {
		this.element = element;
		this.width = width;
	}

	public float getStemOffsetIs() {
		return accidentals.widthIs + notes.stemOffsetIs; //TODO: right?
	}
	
}
