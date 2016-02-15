package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.util.Interval.Before;

import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * Computes a {@link TraditionalKeyNotation} from a {@link TraditionalKey}.
 * 
 * @author Andreas Wenger
 */
public class TraditionalKeyNotator
	implements ElementNotator {
	
	public static final TraditionalKeyNotator traditionalKeyNotator = new TraditionalKeyNotator();
	

	@Override public TraditionalKeyNotation compute(MPElement element, Context context, Notations notations) {
		return compute((TraditionalKey) element, context);
	}
	
	public TraditionalKeyNotation compute(TraditionalKey key, Context context) {
		//compute width
		float width = 0;
		int fifth = key.getFifths();
		if (fifth > 0)
			width = fifth * context.settings.spacings.widthSharp;
		else
			width = -fifth * context.settings.spacings.widthFlat;
		ElementWidth keyWidth = new ElementWidth(0, width, 1);
		
		//compute LPs
		ClefType contextClef = context.score.getClef(context.mp, Before);
		int c4Lp = contextClef.getLp(pi(0, 0, 4));
		int minLp = contextClef.getKeySignatureLowestLp(fifth);
		
		return new TraditionalKeyNotation(key, keyWidth, c4Lp, minLp);
	}
	
}
