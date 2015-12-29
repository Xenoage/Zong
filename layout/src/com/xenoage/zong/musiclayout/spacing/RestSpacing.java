package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.RestNotation;

/**
 * Spacing of a {@link Rest}.
 * 
 * @author Andreas Wenger
 */
public class RestSpacing
	extends ElementSpacing {
	
	/** LP of the rest symbol. */
	public int lp;
	
	
	public RestSpacing(Notation notation, Fraction beat, float offsetIs, int lp) {
		super(notation, beat, offsetIs);
		this.lp = lp;
	}

	@Override public Rest getElement() {
		return (Rest) notation.getElement();
	}
	
	public RestNotation getNotation() {
		return (RestNotation) notation;
	}

}
