package com.xenoage.utils.collections;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.kernel.Tuple3;

import java.util.Map;
import java.util.Set;

import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Tuple3.t3;

/**
 * A three-directional map, with constant lookup time in all three directions.
 * A default value for non-existent entries may be provided, otherwise it is null.
 * 
 * @author Andreas Wenger
 */
public class TriMap<T1, T2, T3> {
	
	private Map<T1, Tuple3<T1, T2, T3>> map1 = map();
	private Map<T2, Tuple3<T1, T2, T3>> map2 = map();
	private Map<T3, Tuple3<T1, T2, T3>> map3 = map();
	private Tuple3<T1, T2, T3> defaultValue = null;
	
	public static <T1, T2, T3> TriMap<T1, T2, T3> triMap() {
		return new TriMap<>();
	}
	
	public int size() {
		return map1.size();
	}

	public boolean isEmpty() {
		return map1.isEmpty();
	}
	
	@MaybeNull public Tuple3<T1, T2, T3> getBy1(@MaybeNull T1 value1) {
		if (value1 == null)
			return defaultValue;
		Tuple3<T1, T2, T3> ret = map1.get(value1);
		return (ret != null ? ret : defaultValue);
	}

	@MaybeNull public Tuple3<T1, T2, T3> getBy2(@MaybeNull T2 value2) {
		if (value2 == null)
			return defaultValue;
		Tuple3<T1, T2, T3> ret = map2.get(value2);
		return (ret != null ? ret : defaultValue);
	}

	@MaybeNull public Tuple3<T1, T2, T3> getBy3(@MaybeNull T3 value3) {
		if (value3 == null)
			return defaultValue;
		Tuple3<T1, T2, T3> ret = map3.get(value3);
		return (ret != null ? ret : defaultValue);
	}

	public void put(@NonNull T1 value1, @NonNull T2 value2, @NonNull T3 value3) {
		removeOldValues(value1, value2, value3);
		Tuple3<T1, T2, T3> value = t3(value1, value2, value3);
		map1.put(value1, value);
		map2.put(value2, value);
		map3.put(value3, value);
	}

	private void removeOldValues(T1 value1, T2 value2, T3 value3) {
		remove(map1.get(value1));
		remove(map2.get(value2));
		remove(map3.get(value3));
	}

	private void remove(Tuple3<T1, T2, T3> value) {
		if (value != null) {
			map1.remove(value.get1());
			map2.remove(value.get2());
			map3.remove(value.get3());
		}
	}
	
	public Tuple3<T1, T2, T3> getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(T1 defaultValue1, T2 defaultValue2, T3 defaultValue3) {
		this.defaultValue = t3(defaultValue1, defaultValue2, defaultValue3);
	}

	public void removeDefaultValue() {
		this.defaultValue = null;
	}

	public Set<T1> getKeys1() {
		return map1.keySet();
	}

	public Set<T2> getKeys2() {
		return map2.keySet();
	}

	public Set<T3> getKeys3() {
		return map3.keySet();
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TriMap<?, ?, ?> triMap = (TriMap<?, ?, ?>) o;
		if (!map1.equals(triMap.map1)) return false;
		if (!map2.equals(triMap.map2)) return false;
		if (!map3.equals(triMap.map3)) return false;
		return defaultValue != null ? defaultValue.equals(triMap.defaultValue) : triMap.defaultValue == null;
	}

	@Override public int hashCode() {
		int result = map1.hashCode();
		result = 31 * result + map2.hashCode();
		result = 31 * result + map3.hashCode();
		result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
		return result;
	}
}
