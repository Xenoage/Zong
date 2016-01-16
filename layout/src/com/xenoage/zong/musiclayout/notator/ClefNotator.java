package com.xenoage.zong.musiclayout.notator;

import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * Computes a {@link ClefNotation} from a {@link Clef}, which is not in a leading spacing.
 * These clefs are usually drawn smaller, dependent on the layout settings.
 * 
 * @author Andreas Wenger
 */
public class ClefNotator
	implements ElementNotator {
	
	public static final ClefNotator clefNotator = new ClefNotator();
	
	
	@Override public Notation compute(MPElement element, Context context, Notations notations) {
		return compute((Clef) element, context);
	}
	
	public ClefNotation compute(Clef clef, Context context) {
		LayoutSettings ls = context.settings;
		return new ClefNotation(clef, new ElementWidth(0, ls.spacings.widthClef * ls.scalingClefInner,
			0), clef.getType().getLp(), ls.scalingClefInner);
	}

}
