package com.xenoage.utils.collections;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.kernel.Tuple2;

import java.util.Map;
import java.util.Set;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Tuple2.t;

/**
 * A bidirectional map, with constant lookup time in both directions.
 * A default value for non-existent entries may be provided, otherwise it is null.
 * 
 * @author Andreas Wenger
 */
public class BiMap<T1, T2> {

	private Map<T1, Tuple2<T1, T2>> map1 = map();
	private Map<T2, Tuple2<T1, T2>> map2 = map();
	private Tuple2<T1, T2> defaultValue = t(null, null);

	public static <T1, T2> BiMap<T1, T2> biMap() {
		return new BiMap<>();
	}

	public int size() {
		return map1.size();
	}

	public boolean isEmpty() {
		return map1.isEmpty();
	}

	@MaybeNull public T2 getBy1(@MaybeNull T1 value1) {
		if (value1 == null)
			return defaultValue.get2();
		Tuple2<T1, T2> ret = map1.get(value1);
		return (ret != null ? ret : defaultValue).get2();
	}

	@MaybeNull public T1 getBy2(@MaybeNull T2 value2) {
		if (value2 == null)
			return defaultValue.get1();
		Tuple2<T1, T2> ret = map2.get(value2);
		return (ret != null ? ret : defaultValue).get1();
	}

	public void put(@NonNull T1 value1, @NonNull T2 value2) {
		checkArgsNotNull(value1, value2);
		removeOldValues(value1, value2);
		Tuple2<T1, T2> value = t(value1, value2);
		map1.put(value1, value);
		map2.put(value2, value);
	}

	private void removeOldValues(T1 value1, T2 value2) {
		remove(map1.get(value1));
		remove(map2.get(value2));
	}

	private void remove(Tuple2<T1, T2> value) {
		if (value != null) {
			map1.remove(value.get1());
			map2.remove(value.get2());
		}
	}

	public Tuple2<T1, T2> getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(T1 defaultValue1, T2 defaultValue2) {
		this.defaultValue = t(defaultValue1, defaultValue2);
	}

	public Set<T1> getKeys1() {
		return map1.keySet();
	}

	public Set<T2> getKeys2() {
		return map2.keySet();
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BiMap<?, ?> biMap = (BiMap<?, ?>) o;
		if (!map1.equals(biMap.map1)) return false;
		if (!map2.equals(biMap.map2)) return false;
		return defaultValue != null ? defaultValue.equals(biMap.defaultValue) : biMap.defaultValue == null;
	}

	@Override public int hashCode() {
		int result = map1.hashCode();
		result = 31 * result + map2.hashCode();
		result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
		return result;
	}
}
