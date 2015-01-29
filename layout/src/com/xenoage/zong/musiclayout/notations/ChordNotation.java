package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.ChordDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;
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
	@NonNull public ChordDisplacement notesAlignment;
	@NonNull public StemDirection stemDirection;
	@NonNull public StemAlignment stemAlignment;
	@NonNull public AccidentalsDisplacement accidentalsAlignment;
	@NonNull public ArticulationsAlignment articulationsAlignment;

}
