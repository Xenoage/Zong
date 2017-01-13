package com.xenoage.zong.musiclayout.notator;

import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.TimeNotation;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * Computes a {@link TimeNotation} from a {@link TimeSignature}.
 * 
 * @author Andreas Wenger
 */
public class TimeNotator
	implements ElementNotator {
	
	public static final TimeNotator timeNotator = new TimeNotator();
	

	@Override public TimeNotation compute(MPElement element, Context context, Notations notations) {
		return compute((TimeSignature) element, context);
	}
	
	public TimeNotation compute(TimeSignature time, Context context) {
		//front and rear gap: 1 space
		float gap = 1f;
		//gap between digits: 0.1 space
		float digitGap = 0.1f;
		//the numbers of a normal time signature are centered.
		//the width of the time signature is the width of the wider number
		float numeratorWidth = context.symbols.computeNumberWidth(time.getType().getNumerator(), digitGap);
		float denominatorWidth = context.symbols.computeNumberWidth(time.getType().getDenominator(), digitGap);
		float width = Math.max(numeratorWidth, denominatorWidth);
		//create element layout
		float numeratorOffset = (width - numeratorWidth) / 2;
		float denominatorOffset = (width - denominatorWidth) / 2;
		return new TimeNotation(time, new ElementWidth(gap, width, gap), numeratorOffset,
			denominatorOffset, digitGap);
	}
	
}
