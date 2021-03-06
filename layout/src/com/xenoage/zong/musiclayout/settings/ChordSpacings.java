package com.xenoage.zong.musiclayout.settings;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.collections.CollectionUtils.getMax;
import static com.xenoage.utils.collections.CollectionUtils.getMin;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;

import java.util.Map;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;

/**
 * Settings for space (in IS) chords need.
 * 
 * @author Andreas Wenger
 */
public class ChordSpacings {

	//duration-to-width mapping
	private Map<Fraction, Float> durationWidths;
	//cache
	private Fraction durationWidthsLowestDuration = Companion.get_0(), durationWidthsHighestDuration = Companion.get_0();
	private Map<Fraction, Float> durationWidthsCache = map();
	
	public static final ChordSpacings defaultChordSpacingsNormal;
	public static final ChordSpacings defaultChordSpacingsGrace;
	
	static {
		Map<Fraction, Float> n = map();
		n.put(Companion.fr(1,64), 1+1/2f);
		n.put(Companion.fr(1,32), 1+3/2f);
		n.put(Companion.fr(1,16), 2f);
		n.put(Companion.fr(1,8), 2+1/2f);
		n.put(Companion.fr(1,4), 3+1/2f);
		n.put(Companion.fr(3,8), 4+1/4f);
		n.put(Companion.fr(1,2), 4+3/4f);
		n.put(Companion.fr(1,1), 7+1/4f);
		defaultChordSpacingsNormal = new ChordSpacings(n);
		Map<Fraction, Float> g = map();
		g.put(Companion.fr(1,16), 1+1/4f);
		g.put(Companion.fr(1,8), 1+1/2f);
		g.put(Companion.fr(1,4), 1+3/4f);
		defaultChordSpacingsGrace = new ChordSpacings(g);
	}


	public ChordSpacings(Map<Fraction, Float> durationWidths) {
		this.durationWidths = durationWidths;

		//init cache
		//find lowest and highest duration
		durationWidthsLowestDuration = getMin(durationWidths.keySet());
		durationWidthsHighestDuration = getMax(durationWidths.keySet());
	}

	/**
	 * Computes and returns the width that fits to the given duration.
	 */
	public float getWidth(@NonNull Fraction duration) {
		checkArgsNotNull(duration);
		//if available, use defined width
		Float width = durationWidths.get(duration);
		if (width != null)
			return width;
		//if available, use cached computed width
		width = durationWidthsCache.get(duration);
		if (width != null)
			return width;
		//not found. find the greatest lesser duration and the lowest
		//greater duration and interpolate linearly. remember the result
		//to avoid this computation for the future.
		Fraction lowerDur = durationWidthsLowestDuration;
		Fraction higherDur = durationWidthsHighestDuration;
		for (Fraction d : durationWidths.keySet()) {
			if (d.compareTo(duration) <= 0 && d.compareTo(lowerDur) > 0) {
				lowerDur = d;
			}
			if (d.compareTo(duration) >= 0 && d.compareTo(higherDur) < 0) {
				higherDur = d;
			}
		}
		float lowerWidth = durationWidths.get(lowerDur);
		float higherWidth = durationWidths.get(higherDur);
		float durationWidth = (lowerWidth + higherWidth) * duration.toFloat() /
			(lowerDur.toFloat() + higherDur.toFloat());
		durationWidthsCache.put(duration, durationWidth);
		return durationWidth;
	}

}
