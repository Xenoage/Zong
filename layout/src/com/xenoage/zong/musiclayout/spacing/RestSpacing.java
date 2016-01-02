package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musiclayout.notation.RestNotation;

import lombok.Getter;

/**
 * Spacing of a {@link Rest}.
 * 
 * @author Andreas Wenger
 */
public class RestSpacing
	extends ElementSpacing {
	
	@Getter public RestNotation notation;
	public int lp;
	
	
	public RestSpacing(RestNotation notation, Fraction beat, float xIs, int lp) {
		super(beat, xIs);
		this.notation = notation;
		this.lp = lp;
	}

	public Rest getElement() {
		return (Rest) notation.getElement();
	}

}
