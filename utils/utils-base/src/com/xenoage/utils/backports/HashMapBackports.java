package com.xenoage.utils.backports;

import com.xenoage.utils.kernel.functional.Function;

import java.util.HashMap;

/**
 * As long as not the whole projects support the Java 8 API,
 * this class provides workarounds for using at least Java-8-style API.
 *
 * @author Andreas Wenger
 */
public class HashMapBackports {

	public static <K, V> V computeIfAbsent(HashMap<K, V> map, K key, Function<K, V> mappingFunction) {
		V ret = map.get(key);
		if (ret == null) {
			ret = mappingFunction.apply(key);
			map.put(key, ret);
		}
		return ret;
	}
}
