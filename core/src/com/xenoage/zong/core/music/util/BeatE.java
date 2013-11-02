package com.xenoage.zong.core.music.util;

import lombok.Data;
import lombok.NonNull;

import com.xenoage.utils.math.Fraction;


/**
 * Element at a beat.
 * 
 * This is a wrapper class to combine any object with
 * the beat (represented as a {@link Fraction}) it belongs to.
 * 
 * @author Andreas Wenger
 */
@Data public final class BeatE<T> {

	/** The element at the beat */
	@NonNull public final T element;
	/** The beat where the element can be found */
	@NonNull public final Fraction beat;
	
	
	public static <T> BeatE<T> beatE(T element, Fraction beat) {
		return new BeatE<T>(element, beat);
	}


	/**
	 * Returns the latest of the given elements. If none are given or
	 * no items with beats are given, null is returned.
	 */
	@SafeVarargs public static <T2> BeatE<T2> selectLatest(BeatE<T2>... elements) {
		BeatE<T2> ret = null;
		for (BeatE<T2> element : elements) {
			if (ret == null || (element != null && element.beat.compareTo(ret.beat) > 0))
				ret = element;
		}
		return ret;
	}


	@Override public String toString() {
		return element + " at beat " + beat;
	}


}
