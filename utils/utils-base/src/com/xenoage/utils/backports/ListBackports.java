package com.xenoage.utils.backports;

import com.xenoage.utils.kernel.functional.Function;

import java.util.Collections;
import java.util.List;

/**
 * As long as not the whole projects support the Java 8 API,
 * this class provides workarounds for using at least Java-8-style API.
 *
 * @author Andreas Wenger
 */
public class ListBackports {

	public static <T> void sort(List<T> list, Function<T, Comparable> comparedValue) {
		Collections.sort(list, (v1, v2) -> comparedValue.apply(v2).compareTo(comparedValue.apply(v2)));
	}


}
