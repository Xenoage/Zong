package com.xenoage.zong.musiclayout.notator;

import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.Duration;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.RestNotation;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * Computes a {@link RestNotation} from a {@link Rest}.
 * 
 * @author Andreas Wenger
 */
public class RestNotator
	implements ElementNotator {
	
	public static final RestNotator restNotator = new RestNotator();
	

	@Override public RestNotation compute(MPElement element, Context context, Notations notations) {
		return compute((Rest) element, context);
	}
	
	public RestNotation compute(Rest rest, Context context) {
		float width = context.settings.spacings.normalChordSpacings.getWidth(rest.getDuration());
		Duration.Type duration = Duration.INSTANCE.getRestType(rest.getDuration());
		return new RestNotation(rest, new ElementWidth(width), duration);
	}
	
}
