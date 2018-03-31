package com.xenoage.utils.collections;

import java.util.Map;

/**
 * Interface for immutable maps.
 * 
 * Deprecated annotations are used to warn the programmer of calling
 * unsupported methods.
 * 
 * @author Andreas Wenger
 */
public interface IMap<K, V>
	extends Map<K, V> {

	@Deprecated @Override public V put(K key, V value);

	@Deprecated @Override public V remove(Object key);

	@Deprecated @Override public void putAll(java.util.Map<? extends K, ? extends V> m);

	@Deprecated @Override public void clear();

}
