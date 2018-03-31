package com.xenoage.utils.kernel.functional;

/**
 * See the same class in JDK 8.
 *
 * @author Andreas Wenger
 */
@FunctionalInterface
public interface Function<T, R> {
	R apply(T t);
}
