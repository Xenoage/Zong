package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
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

	public Chord element;
	public ElementWidth width;
	public NotesAlignment notesAlignment;
	public StemDirection stemDirection;
	public StemAlignment stemAlignment;
	public AccidentalsAlignment accidentalsAlignment;
	public ArticulationsAlignment articulationsAlignment;

}
