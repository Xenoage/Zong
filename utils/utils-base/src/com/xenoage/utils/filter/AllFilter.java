package com.xenoage.utils.filter;

import java.util.List;

/**
 * Filter which selects all values.
 * 
 * @author Andreas Wenger
 */
public class AllFilter<T>
	implements Filter<T> {

	@Override public List<T> filter(List<T> values) {
		return values;
	}

}
