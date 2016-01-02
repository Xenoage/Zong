package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.notation.ChordNotation;

import lombok.Getter;

/**
 * Spacing of a {@link Chord}.
 * 
 * @author Andreas Wenger
 */
public class ChordSpacing
	extends ElementSpacing {
	
	@Getter public ChordNotation notation;
	public int lp;
	
	
	public ChordSpacing(ChordNotation notation, Fraction beat, float xIs) {
		super(beat, xIs);
		this.notation = notation;
	}

	public Chord getElement() {
		return (Chord) notation.getElement();
	}
	
	public float getStemXIs() {
		return xIs + notation.notes.stemOffsetIs;
	}
	
}
