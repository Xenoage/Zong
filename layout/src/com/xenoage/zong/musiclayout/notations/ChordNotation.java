package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import com.xenoage.utils.annotations.Const;
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
@Const @AllArgsConstructor @Getter @Wither public class ChordNotation
	implements Notation {

	public final Chord element;
	public final ElementWidth width;
	public final NotesAlignment notesAlignment;
	public final StemDirection stemDirection;
	public final StemAlignment stemAlignment;
	public final AccidentalsAlignment accidentalsAlignment;
	public final ArticulationsAlignment articulationsAlignment;


	@Override public ElementWidth getWidth() {
		return width;
	}

	@Override public Chord getMusicElement() {
		return element;
	}

}
