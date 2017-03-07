package com.xenoage.zong.android.util;

/**
 * Functional interface.
 * Needed, since Java's Supplier is only supported from API level 24 on.
 *
 * @author Andreas Wenger
 */
public interface Supplier<T> {
	T run();
}
