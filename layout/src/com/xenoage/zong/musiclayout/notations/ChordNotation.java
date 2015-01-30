package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;

/**
 * This class contains layout information about a chord, like its width and the
 * alignment of the notes, dots, accidentals and articulations.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class ChordNotation
	implements Notation {

	@NonNull public Chord element;
	@NonNull public ElementWidth width;
	@NonNull public NotesNotation notes;
	@NonNull public StemDirection stemDirection;
	@NonNull public StemNotation stem;
	@NonNull public AccidentalsNotation accidentals;
	@NonNull public ArticulationsNotation articulations;

}
