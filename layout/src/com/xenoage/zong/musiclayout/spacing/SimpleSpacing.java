package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.musiclayout.notation.Notation;

import lombok.Getter;

/**
 * An {@link ElementSpacing} with a {@link Notation}.
 * 
 * @author Andreas Wenger
 */
public class SimpleSpacing //TIDY: better name
	extends ElementSpacing {
	
	@Getter public Notation notation;

	public SimpleSpacing(Notation notation, Fraction beat, float xIs) {
		super(beat, xIs);
		this.notation = notation;
	}

}
