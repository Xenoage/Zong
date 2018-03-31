package com.xenoage.utils.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Immutable closeable map.
 * 
 * This is just a wrapper around an {@link HashMap}. At the beginning,
 * the map is unclosed and can be written like a normal {@link HashMap}.
 * After the {@link #close()} method is called, all calls to write methods
 * will throw an {@link IllegalStateException}.
 * 
 * An {@link CMap} is "branched" when it is created based on an existing {@link IMap}.
 * In this case it shares its data memory, until the first write
 * operation is performed. At this point, a full copy of the data memory is made.
 * Thus, if the map is not modified later, branching the map is very fast.
 * 
 * As long as the class is used as an {@link CMap}, there are no compiler
 * warnings for the write methods. As soon as it is used as a {@link IMap},
 * compiler warnings for the write methods will show up.
 * 
 * This class is a pragmatic combination of the idea of functional data structures
 * like PMap from utils-pdlib and an efficient implementation like {@link HashMap}.
 * 
 * @author Andreas Wenger
 */
public final class CMap<K, V>
	implements IMap<K, V> {

	private Map<K, V> map;
	private boolean closed = false;
	private boolean sharedMemory = false; //true, when map is shared with another instance


	/**
	 * Creates an empty and unclosed {@link CMap}.
	 */
	public CMap() {
		this(true);
	}

	/**
	 * Creates an unclosed {@link CMap} based on the given mutable
	 * map. A shallow copy of the given map is created.
	 */
	public CMap(java.util.Map<K, V> mutableMap) {
		map = new HashMap<>(mutableMap);
	}

	private CMap(boolean init) {
		if (init)
			map = new HashMap<>();
		else
			map = null;
	}

	/**
	 * Creates an empty and unclosed {@link CMap}.
	 */
	public static <K2, V2> CMap<K2, V2> cmap() {
		return new CMap<>();
	}

	/**
	 * Creates an unclosed {@link CMap} based on the given mutable
	 * map. A shallow copy of the given map is created.
	 */
	public static <K2, V2> CMap<K2, V2> cmap(java.util.Map<K2, V2> mutableMap) {
		return new CMap<>(mutableMap);
	}

	/**
	 * Creates a new {@link CMap} based on the given {@link IMap} as a branch.
	 * This means, that the new map shares the data of the given map instance.
	 * The memory is shared until the new map receives the first write operation.
	 */
	public static <K2, V2> CMap<K2, V2> cmap(IMap<K2, V2> m) {
		CMap<K2, V2> ret = new CMap<>(false);
		ret.sharedMemory = true;
		if (m instanceof CMap)
			ret.map = ((CMap<K2, V2>) m).map; //avoid a stack of redirections. use map directly
		else
			ret.map = m; //no choice, we must use the public interface
		return ret;
	}

	/**
	 * Closes the map. All future calls to write methods will fail.
	 * Returns this map for convenience.
	 */
	public CMap<K, V> close() {
		closed = true;
		return this;
	}

	/**
	 * Closes the map, like {@link #close()}, but also looks recursively in the values
	 * for {@link CList}s and {@link CMap}s and also closes them.
	 */
	public IMap<K, V> closeDeep() {
		for (V item : values()) {
			if (item instanceof CList)
				((CList)item).closeDeep();
			else if (item instanceof CMap)
				((CMap)item).closeDeep();
		}
		return close();
	}

	private void requestWrite() {
		//if closed, further write operations are forbidden
		if (closed)
			throw new IllegalStateException("map is closed");
		//if shared memory is used, create full copy instead
		if (sharedMemory) {
			map = new HashMap<>(map);
			sharedMemory = false;
		}
	}

	@Override public int size() {
		return map.size();
	}

	@Override public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override public V get(Object key) {
		return map.get(key);
	}

	@Override public Set<K> keySet() {
		return map.keySet();
	}

	@Override public Collection<V> values() {
		return map.values();
	}

	@Override public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override public V put(K key, V value) {
		requestWrite();
		return map.put(key, value);
	}

	@Override public V remove(Object key) {
		requestWrite();
		return map.remove(key);
	}

	@Override public void putAll(java.util.Map<? extends K, ? extends V> m) {
		requestWrite();
		map.putAll(m);
	}

	@Override public void clear() {
		requestWrite();
		map.clear();
	}

	@Override public String toString() {
		return map.toString();
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CMap<?, ?> cMap = (CMap<?, ?>) o;
		return map != null ? map.equals(cMap.map) : cMap.map == null;
	}

	@Override public int hashCode() {
		return map != null ? map.hashCode() : 0;
	}

}
