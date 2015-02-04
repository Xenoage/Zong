package com.xenoage.zong.musiclayout.notator;

import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;

/**
 * Computes a {@link RestNotation} from a {@link Rest}.
 * 
 * @author Andreas Wenger
 */
public class RestNotator
	implements ElementNotator {
	
	public static final RestNotator restNotator = new RestNotator();
	

	@Override public RestNotation notate(MPElement element, Context context) {
		return notate((Rest) element, context);
	}
	
	public RestNotation notate(Rest rest, Context context) {
		float width = context.settings.spacings.normalChordSpacings.getWidth(rest.getDuration());
		return new RestNotation(rest, new ElementWidth(width));
	}
	
}
