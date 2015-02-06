package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.notator.ChordNotator.chordNotator;
import static com.xenoage.zong.musiclayout.notator.ClefNotator.clefNotator;
import static com.xenoage.zong.musiclayout.notator.RestNotator.restNotator;
import static com.xenoage.zong.musiclayout.notator.TimeNotator.timeNotator;
import static com.xenoage.zong.musiclayout.notator.TraditionalKeyNotator.traditionalKeyNotator;

import java.util.Map;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.core.util.ColumnElementIterator;
import com.xenoage.zong.core.util.MeasureElementIterator;
import com.xenoage.zong.core.util.VoiceElementIterator;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.notations.Notations;

/**
 * Creates layout information about the layout of {@link MusicElement}s,
 * so called {@link Notation}s.
 * 
 * For example, the detailled positioning for musical elements like chords,
 * rests, clefs and so on is computed.
 *
 * @author Andreas Wenger
 */
public final class Notator {
	
	public static final Notator notator = new Notator();
	
	private static final Map<MusicElementType, ElementNotator> notators = map();
	
	static {
		notators.put(MusicElementType.Chord, chordNotator);
		notators.put(MusicElementType.Clef, clefNotator);
		notators.put(MusicElementType.Rest, restNotator);
		notators.put(MusicElementType.Time, timeNotator);
		notators.put(MusicElementType.TraditionalKey, traditionalKeyNotator);
	}
	
	
	/**
	 * Computes the {@link Notation}s of all elements and stores
	 * the results in the {@link Notations}.
	 */
	public void computeAll(Context context) {
		context.saveMp();
		
		Score score = context.score;
		Notations notations = new Notations();
		
		//iterate over all column elements, measure elements and voice elements
		ColumnElementIterator itC = new ColumnElementIterator(score);
		for (ColumnElement e : itC) {
			//column elements: one notation for each staff
			for (int iStaff : range(context.score.getStavesCount())) {
				context.mp = itC.getMp().withStaff(iStaff);
				notations.add(compute(e, context, notations), iStaff);
			}
		}
		MeasureElementIterator itM = new MeasureElementIterator(score);
		for (MeasureElement e : itM) {
			context.mp = itM.getMp();
			notations.add(compute(e, context, notations));
		}
		VoiceElementIterator itV = new VoiceElementIterator(score);
		for (VoiceElement e : itV) {
			context.mp = itV.getMp();
			notations.add(compute(e, context, notations));
		}
		
		context.restoreMp();
	}

	/**
	 * Computes the {@link Notation} of the given element.
	 */
	private Notation compute(MPElement element, Context context, Notations notations) {
		ElementNotator notator = notators.get(element.getMusicElementType());
		if (notator == null) //element needs no notation
			return null;
		Notation notation = notator.compute(element, context, notations);
		return notation;
	}

}
