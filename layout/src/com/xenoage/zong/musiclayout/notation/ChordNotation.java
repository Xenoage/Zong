package com.xenoage.zong.musiclayout.notation;

import com.xenoage.utils.annotations.Optimized;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.SLP;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.SLP.slp;

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
	/** The {@link MP}, stored for performance reasons. */
	@Getter @Optimized(Optimized.Reason.Performance) public MP mp;
	@Getter public ElementWidth width;
	public int staff;
	public NotesNotation notes;
	public StemDirection stemDirection = StemDirection.Default;
	public StemNotation stem;
	public AccidentalsNotation accidentals;
	public ArticulationsNotation articulations;
	
	public ChordNotation(Chord element) {
		this.element = element;
		this.mp = element.getMP();
	}
	
	public ChordNotation(Chord element, ElementWidth width) {
		this.element = element;
		this.mp = element.getMP();
		this.width = width;
	}

	public float getStemOffsetXIs() {
		return accidentals.widthIs + notes.stemOffsetIs; //TODO: right?
	}
	
	/**
	 * Gets the {@link SLP} of the outermost note at the stem side.
	 * For upward stems, this is the highest note, and for
	 * downward stems or if there is no stem, this is the lowest note.
	 */
	public SLP getStemSideNoteSlp() {
		return slp(staff, getStemSideNoteLp());
	}

	/**
	 * Gets the LP of the outermost note at the stem side.
	 * For upward stems, this is the highest note, and for
	 * downward stems or if there is no stem, this is the lowest note.
	 */
	public int getStemSideNoteLp() {
		return (stemDirection == Up ? getLast(notes.notes) : getFirst(notes.notes)).lp;
	}

	/**
	 * Gets the pitch of the outermost note at the stem side.
	 * For upward stems, this is the highest note, and for
	 * downward stems or if there is no stem, this is the lowest note.
	 */
	public Pitch getStemSideNotePitch() {
		return (stemDirection == Up ? getLast(element.getNotes()) : getFirst(element.getNotes())).getPitch();
	}

}
