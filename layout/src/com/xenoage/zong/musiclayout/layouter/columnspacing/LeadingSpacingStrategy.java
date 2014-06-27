package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.horizontal.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;

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

	//used strategies
	private final NotationStrategy notationStrategy;


	/**
	 * Creates a new {@link LeadingSpacingStrategy}.
	 */
	public LeadingSpacingStrategy(NotationStrategy notationStrategy) {
		this.notationStrategy = notationStrategy;
	}

	/**
	 * Computes and returns the leading spacing for the given context.
	 * This spacing contains the current clef and key signature.
	 * The additionally created notations are returned, too.
	 */
	public Tuple2<LeadingSpacing, NotationsCache> computeLeadingSpacing(MusicContext musicContext,
		int iStaff, LayoutSettings layoutSettings) {
		float xOffset = layoutSettings.offsetMeasureStart;

		boolean useKey = false;
		Key key = musicContext.getKey();
		if (key instanceof TraditionalKey) {
			useKey = true;
		}

		CList<SpacingElement> elements = clist();
		NotationsCache notations = new NotationsCache();

		Clef clef = new Clef(musicContext.getClef().getType()); //it is not the same element instance, but has the same meaning
		ClefNotation clefNotation = new ClefNotation(clef, new ElementWidth(0,
			layoutSettings.spacings.widthClef, 0), musicContext.getClef().getType().getLp(), 1);
		notations.add(clefNotation);
		xOffset += layoutSettings.spacings.widthClef / 2;
		elements.add(new SpacingElement(clef, fr(0), xOffset));
		xOffset += layoutSettings.spacings.widthClef / 2;

		if (useKey) {
			TraditionalKey tkey = (TraditionalKey) key;
			xOffset += layoutSettings.spacings.widthDistanceMin;
			TraditionalKey tradKey = new TraditionalKey(tkey.getFifths(), tkey.getMode()); //it is not the same element instance, but has the same meaning
			TraditionalKeyNotation keyNotation = notationStrategy.computeTraditionalKey(tradKey, clef,
				layoutSettings);
			notations.add(keyNotation, iStaff);
			elements.add(new SpacingElement(tradKey, fr(0), xOffset));
			xOffset += keyNotation.getWidth().getWidth();
		}

		return new Tuple2<LeadingSpacing, NotationsCache>(
			new LeadingSpacing(elements.close(), xOffset), notations);
	}

}
