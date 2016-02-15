package com.xenoage.zong.musiclayout.spacer.measure;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.Interval.At;
import static com.xenoage.zong.musiclayout.notator.TraditionalKeyNotator.traditionalKeyNotator;

import java.util.List;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.SimpleSpacing;

/**
 * Computes the {@link LeadingSpacing} for a measure.
 * 
 * This spacing contains the current clef and key signature.
 * Additional notations are created and added to the given notations pool.
 * 
 * @author Andreas Wenger
 */
public class LeadingSpacer {
	
	public static final LeadingSpacer leadingSpacer = new LeadingSpacer();

	
	/**
	 * Computes the {@link LeadingSpacing} for the current measure.
	 */
	public LeadingSpacing compute(Context context, Notations notations) {
		float xOffset = context.settings.offsetMeasureStart;
		
		boolean useKey = false;
		MusicContext musicContext = context.getMusicContext(At, null);
		Key key = musicContext.getKey();
		if (key instanceof TraditionalKey) {
			useKey = true;
		}

		List<ElementSpacing> elements = alist(useKey ? 2 : 1);

		Clef clef = new Clef(musicContext.getClef()); //it is not the same element instance, but has the same meaning
		ClefNotation clefNotation = new ClefNotation(clef, new ElementWidth(0,
			context.settings.spacings.widthClef, 0), musicContext.getClef().getLp(), 1);
		notations.add(clefNotation);
		xOffset += context.settings.spacings.widthClef / 2;
		elements.add(new SimpleSpacing(clefNotation, fr(0), xOffset));
		xOffset += context.settings.spacings.widthClef / 2;

		if (useKey) {
			TraditionalKey tkey = (TraditionalKey) key;
			xOffset += context.settings.spacings.widthDistanceMin;
			TraditionalKey tradKey = new TraditionalKey(tkey.getFifths(), tkey.getMode()); //it is not the same element instance, but has the same meaning
			TraditionalKeyNotation keyNotation = traditionalKeyNotator.compute(tradKey, context);
			notations.add(keyNotation);
			elements.add(new SimpleSpacing(keyNotation, fr(0), xOffset));
			xOffset += keyNotation.getWidth().getWidth();
		}

		return new LeadingSpacing(elements, xOffset);
	}

}
