package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.Interval.At;
import static com.xenoage.zong.musiclayout.notator.TraditionalKeyNotator.traditionalKeyNotator;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.horizontal.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementSpacing;

/**
 * Strategy to compute the {@link LeadingSpacing} for
 * the given measure.
 * 
 * This spacing contains the current clef and
 * key signature.
 * 
 * The additionally created notations are returned, too.
 * 
 * @author Andreas Wenger
 */
public class LeadingSpacingStrategy
	implements ScoreLayouterStrategy {

	
	/**
	 * Computes and returns the leading spacing for the given context.
	 * This spacing contains the current clef and key signature.
	 * The additionally created notations are returned, too.
	 */
	public Tuple2<LeadingSpacing, NotationsCache> computeLeadingSpacing(Context context) {
		float xOffset = context.settings.offsetMeasureStart;
		
		boolean useKey = false;
		MusicContext musicContext = context.getMusicContext(At, null);
		Key key = musicContext.getKey();
		if (key instanceof TraditionalKey) {
			useKey = true;
		}

		CList<ElementSpacing> elements = clist(useKey ? 2 : 1);
		NotationsCache notations = new NotationsCache();

		Clef clef = new Clef(musicContext.getClef().getType()); //it is not the same element instance, but has the same meaning
		ClefNotation clefNotation = new ClefNotation(clef, new ElementWidth(0,
			context.settings.spacings.widthClef, 0), musicContext.getClef().getType().getLp(), 1);
		notations.add(clefNotation);
		xOffset += context.settings.spacings.widthClef / 2;
		elements.add(new ElementSpacing(clef, fr(0), xOffset));
		xOffset += context.settings.spacings.widthClef / 2;

		if (useKey) {
			TraditionalKey tkey = (TraditionalKey) key;
			xOffset += context.settings.spacings.widthDistanceMin;
			TraditionalKey tradKey = new TraditionalKey(tkey.getFifths(), tkey.getMode()); //it is not the same element instance, but has the same meaning
			TraditionalKeyNotation keyNotation = traditionalKeyNotator.notate(tradKey, context);
			notations.add(keyNotation, context.mp.staff);
			elements.add(new ElementSpacing(tradKey, fr(0), xOffset));
			xOffset += keyNotation.getWidth().getWidth();
		}

		return new Tuple2<LeadingSpacing, NotationsCache>(
			new LeadingSpacing(elements.close(), xOffset), notations);
	}

}
