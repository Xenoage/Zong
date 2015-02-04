package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.util.Interval.Before;

import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;

/**
 * Computes a {@link TraditionalKeyNotation} from a {@link TraditionalKey}.
 * 
 * @author Andreas Wenger
 */
public class TraditionalKeyNotator
	implements ElementNotator {
	
	public static final TraditionalKeyNotator traditionalKeyNotator = new TraditionalKeyNotator();
	

	@Override public TraditionalKeyNotation notate(MPElement element, Context context) {
		return notate((TraditionalKey) element, context);
	}
	
	public TraditionalKeyNotation notate(TraditionalKey key, Context context) {
		//compute width
		float width = 0;
		int fifth = key.getFifths();
		if (fifth > 0)
			width = fifth * context.settings.spacings.widthSharp;
		else
			width = -fifth * context.settings.spacings.widthFlat;
		ElementWidth keyWidth = new ElementWidth(0, width, 1);
		
		//compute LPs
		ClefType contextClef = context.score.getClef(context.mp, Before).getType();
		int c4Lp = contextClef.getLp(pi(0, 0, 4));
		int minLp = contextClef.getKeySignatureLowestLp(fifth);
		
		return new TraditionalKeyNotation(key, keyWidth, c4Lp, minLp);
	}
	
}
