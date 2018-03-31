package com.xenoage.utils.filter;

import java.util.List;

/**
 * Strategy to filter a given list of values.
 * 
 * For example, a dialog may be shown where the user can
 * select a single value. Or all values that are greater
 * than 5 could be selected.
 * 
 * @author Andreas Wenger
 */
public interface Filter<T> {

	public List<T> filter(List<T> values);

}
