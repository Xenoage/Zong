package com.xenoage.zong.musiclayout.notations.chord;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;

/**
 * This class stores the alignment of the accidentals of a chord.
 * 
 * Some rules are adepted from
 * "Ross: The Art of Music Engraving", page 130 ff, and
 * "Chlapik: Die Praxis des Notengraphikers", page 48 ff.
 *
 * @author Andreas Wenger
 */
@Const @Getter public final class AccidentalsAlignment {

	/** The accidentals of this chord. */
	public final AccidentalAlignment[] accidentals;
	/** The width of the accidentals of this chord. This is the distance between the left side of
	 * the leftmost accidental and the beginning of the notes. */
	public final float width;


	public AccidentalsAlignment(AccidentalAlignment[] accidentals, float width) {
		//must be sorted upwards
		for (int i = 0; i < accidentals.length - 1; i++) {
			if (accidentals[i].linePosition > accidentals[i + 1].linePosition)
				throw new IllegalArgumentException("Accidentals must be sorted upwards");
		}
		this.accidentals = accidentals;
		this.width = width;
	}

}
