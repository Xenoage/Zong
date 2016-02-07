package com.xenoage.zong.musiclayout.notation;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
	
	public ChordNotation(Chord element) {
		this.element = element;
	}
	
	public ChordNotation(Chord element, ElementWidth width) {
		this.element = element;
		this.width = width;
	}

	public float getStemOffsetXIs() {
		return accidentals.widthIs + notes.stemOffsetIs; //TODO: right?
	}
	
	/**
	 * Gets the outermost note at the stem side.
	 * For upward stems, this is the highest note, and for
	 * downward stems or if there is no stem, this is the lowest note.
	 */
	public int getStemSideNoteLp() {
		if (stemDirection == Up)
			return getLast(notes.notes).lp;
		else
			return getFirst(notes.notes).lp;
	}
}
