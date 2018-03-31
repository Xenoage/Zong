package com.xenoage.utils.pdlib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.pcollections.HashPMap;
import org.pcollections.HashTreePMap;

import com.xenoage.utils.collections.IMap;

/**
 * Persistent hash map.
 * 
 * This map contains efficient producers to create modified
 * versions of this one.
 * 
 * Deprecated annotations are used to warn the programmer of calling
 * unsupported methods.
 * 
 * @author Andreas Wenger
 */
public final class PMap<K, V>
	implements IMap<K, V> {

	private final HashPMap<K, V> data;

	//cache for sorted keys
	private PList<K> keysSorted = null;


	public PMap(IMap<? extends K, ? extends V> map) {
		this.data = HashTreePMap.from(map);
	}

	public PMap() {
		this.data = HashTreePMap.empty();
	}

	private PMap(HashPMap<K, V> data) {
		this.data = data;
	}

	public static <K, V> PMap<K, V> pmap() {
		return new PMap<K, V>();
	}

	@Deprecated @Override public V put(K key, V value) {
		throw new UnsupportedOperationException("Use plus method instead");
	}

	public PMap<K, V> plus(K key, V value) {
		return new PMap<K, V>(data.plus(key, value));
	}

	@Deprecated @Override public void putAll(java.util.Map<? extends K, ? extends V> map) {
		throw new UnsupportedOperationException("Use plusAll method instead");
	}

	public PMap<K, V> plusAll(IMap<? extends K, ? extends V> map) {
		return new PMap<K, V>(data.plusAll(map));
	}

	@Deprecated @Override public void clear() {
		throw new UnsupportedOperationException("Create an empty instance instead");
	}

	@Override public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	@Override public boolean containsValue(Object value) {
		for (V v : data.values()) {
			if (v.equals(value))
				return true;
		}
		return false;
	}

	@Override public V get(Object key) {
		return data.get(key);
	}

	/**
	 * Gets the first key which belongs to the given value, or null
	 * if not found.
	 * Linear runtime complexity.
	 */
	public K getKeyByValue(V value) {
		for (K key : data.keySet()) {
			if (data.get(key).equals(value))
				return key;
		}
		return null;
	}

	@Override public boolean isEmpty() {
		return data.isEmpty();
	}

	@Deprecated @Override public V remove(Object key) {
		throw new UnsupportedOperationException("Use minus method instead");
	}

	public PMap<K, V> minus(Object key) {
		return new PMap<K, V>(data.minus(key));
	}

	public PMap<K, V> minusAll(Collection<?> keys) {
		return new PMap<K, V>(data.minusAll(keys));
	}

	/**
	 * Removes all pairs with the given value.
	 * Linear runtime complexity.
	 */
	public PMap<K, V> minusValue(V value) {
		HashPMap<K, V> ret = data;
		for (K key : ret.keySet()) {
			if (ret.get(key).equals(value))
				ret = ret.minus(key);
		}
		return new PMap<K, V>(ret);
	}

	/**
	 * Replaces the value of all pairs with the
	 * given value by the given new one.
	 * Linear runtime complexity.
	 */
	public PMap<K, V> replaceValue(V oldValue, V newValue) {
		HashPMap<K, V> ret = data;
		for (K key : ret.keySet()) {
			if (ret.get(key).equals(oldValue))
				ret = ret.plus(key, newValue);
		}
		return new PMap<K, V>(ret);
	}

	@Override public int size() {
		return data.size();
	}

	@Override public Set<java.util.Map.Entry<K, V>> entrySet() {
		return data.entrySet();
	}

	@Override public Set<K> keySet() {
		return data.keySet();
	}

	/**
	 * Gets a sorted list of all keys.
	 * This may be slow when called the first time. From the second time on,
	 * it is very fast since the list is internally cached.
	 */
	public PList<K> keySortedList(Comparator<K> comparator) {
		if (keysSorted == null) {
			ArrayList<K> keys = new ArrayList<K>(data.keySet());
			Collections.sort(keys, comparator);
			keysSorted = new PList<K>(keys);
		}
		return keysSorted;
	}

	@Override public Collection<V> values() {
		return data.values();
	}

	/**
	 * Returns true, if the given collection has the same values as this one,
	 * otherwise false.
	 */
	@Override public boolean equals(Object o) {
		return data.equals(o);
	}

	@Override public int hashCode() {
		return data.hashCode();
	}

	@Override public String toString() {
		return data.toString();
	}

}
