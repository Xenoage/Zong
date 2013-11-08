package com.xenoage.zong.core.music.util;

import lombok.Data;
import lombok.Getter;

import com.xenoage.utils.annotations.NonNull;


/**
 * Element at an index.
 * 
 * This is a wrapper class to combine any object with
 * its index it belongs to.
 * 
 * @author Andreas Wenger
 */
@Data public final class IndexE<T> {

	/** The element at the beat */
	@Getter @NonNull public final T element;
	/** The index where the element can be found */
	@Getter public final int index;

	
	public static <T> IndexE<T> indexE(T element, int index) {
		return new IndexE<T>(element, index);
	}

	@Override public String toString() {
		return "[" + element + " at index " + index + "]";
	}

}
